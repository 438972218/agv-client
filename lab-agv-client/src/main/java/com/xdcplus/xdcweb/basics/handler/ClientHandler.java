package com.xdcplus.xdcweb.basics.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdcplus.netty.common.dto.RequestDTO;
import com.xdcplus.netty.common.dto.TaskDTO;
import com.xdcplus.netty.common.model.*;
import com.xdcplus.xdcweb.basics.service.*;
import com.xdcplus.xdcweb.global.utils.PropertyUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author : Fish Fei
 */
@Slf4j
@Component
public class ClientHandler extends SimpleChannelInboundHandler<AgvMessage> {

    public static volatile Integer heart = 0;

    public static volatile Integer packNr = 0;

    public static ClientHandler clientHandler;

    private Integer ACTION_TYPE_NEW = 10;
    //以响应
    private Integer ACTION_TYPE_RESOPNSE = 16;
    private Integer ACTION_TYPE_FINISH = 100;
    private Integer ACTION_TYPE_CONFIRM_FINISH = 102;
    private Integer PACK_ACK_ST_OK = 99;
    private Integer PACK_TYPE_XINTIAO = 257;
    private Integer PACK_TYPE_ACK = 258;
    private Integer PACK_TYPE_INTERACTION = 259;
    private Integer PACK_TYPE_STATE = 260;
    private Integer REQUEST_STATUS_2 = 2;
    private Integer REQUEST_STATUS_FINISH = 3;

    @Autowired
    IAgvMessageService agvMessageService;

    @Autowired
    AckService ackService;

    @Autowired
    IAgvStateService agvStateService;

    @Autowired
    IAxisStateService axisStateService;

    @Autowired
    IAlarmStateService alarmStateService;

    @Autowired
    IRequestService requestService;

    @Autowired
    IRequestFlowService requestFlowService;

    @PostConstruct
    public void init() {
        clientHandler = this;
    }

    public static Integer getPackNr() {
        return packNr;
    }

    public static void setPackNr(Integer packNr) {
        ClientHandler.packNr = packNr;
    }

    public static Integer getHeart() {
        return heart;
    }

    public static void setHeart(Integer heart) {
        ClientHandler.heart = heart;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state().equals(IdleState.READER_IDLE)) {
//                System.out.println("Client READER_IDLE");
//            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
//                System.out.println("Client WRITER_IDLE");
//            } else
            if (event.state().equals(IdleState.WRITER_IDLE)) {
//                System.out.println("Client ALL_IDLE");
                if (TcpClient.restrict) {
                    AgvMessage agvMessage = combineAgvMessage();
                    ctx.writeAndFlush(agvMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("hell world \n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AgvMessage agvMessage) throws Exception {
//        log.info("Server say : " + agvMessage);
//        System.out.println("Server say : " + agvMessage);
        agvMessage.setType("server");
        //ACK确认包
        if (agvMessage.getPackType().equals(PACK_TYPE_ACK)) {
//            clientHandler.agvMessageService.save(agvMessage);
            //状态报文
        } else if (agvMessage.getPackType().equals(PACK_TYPE_STATE)) {
            packTypeState(agvMessage);
            //任务交互，修改request状态
        } else if (agvMessage.getPackType().equals(PACK_TYPE_INTERACTION)) {
            packTypeInteraction(agvMessage, ctx);
        }
    }

    //任务交互
    private void packTypeInteraction(AgvMessage agvMessage, ChannelHandlerContext ctx) {
        clientHandler.agvMessageService.save(agvMessage);
        String data = agvMessage.getData();
        if (data.equals("{}") || !data.substring(data.length() - 1).equals("}")) {
            return;
        }
        RequestDTO requestDTO = JSON.parseObject(data, RequestDTO.class);
        Optional.ofNullable(requestDTO).ifPresent(requestDTO1 -> {
            Integer taskNum = requestDTO1.getTaskNum();
            TaskDTO taskDTO = requestDTO1.getTask();
            Integer actionType = taskDTO.getActionType();

            if (actionType.equals(ACTION_TYPE_RESOPNSE)) {
                //调度已响应任务
                //修改request对应状态
                Request request = new Request();
                request.setStatus(REQUEST_STATUS_2);
                UpdateWrapper<Request> requestWrapper = new UpdateWrapper<>();
                requestWrapper.eq("task_num", taskNum);
                clientHandler.requestService.update(request, requestWrapper);
                insertFlow(taskNum, REQUEST_STATUS_2);

//                    agvMessage.setPackType(259);
                //ACK确认数据包序号
                agvMessage.setPackAckNr(agvMessage.getPackNr());
                //ACK状态
                agvMessage.setPackAckSt(PACK_ACK_ST_OK);
                ctx.channel().writeAndFlush(agvMessage);
            } else if (actionType.equals(ACTION_TYPE_FINISH)) {
                //调度反馈任务完成
                Request request = new Request();
                request.setStatus(REQUEST_STATUS_FINISH);
                UpdateWrapper<Request> requestWrapper = new UpdateWrapper<>();
                requestWrapper.eq("task_num", taskNum);
                clientHandler.requestService.update(request, requestWrapper);
                insertFlow(taskNum, REQUEST_STATUS_FINISH);

                //ACK确认数据包序号
                agvMessage.setPackAckNr(agvMessage.getPackNr());
                //ACK状态
                agvMessage.setPackAckSt(PACK_ACK_ST_OK);
                taskDTO.setActionType(ACTION_TYPE_CONFIRM_FINISH);
                requestDTO1.setTask(taskDTO);
                String requestStr = JSON.toJSONString(requestDTO1);
                agvMessage.setData(requestStr);

                ctx.channel().writeAndFlush(agvMessage);
            }
        });
    }

    //状态报文
    private void packTypeState(AgvMessage agvMessage) {
//        log.info("Server say : " + agvMessage);
        clientHandler.agvMessageService.save(agvMessage);

        AgvState agvState=new AgvState();
        BeanUtils.copyProperties(agvMessage, agvState);
//        QueryWrapper<AgvState> agvStateQueryWrapper=new QueryWrapper<>();
//        agvStateQueryWrapper.eq("agv_id",agvState.getAgvId());
//        int count = agvStateService.count(agvStateQueryWrapper);
//        if(count!=0){
            UpdateWrapper<AgvState> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("agv_id",agvState.getAgvId());
//            agvStateService.update(agvState,updateWrapper);
//        }else{
        agvState.setId(null);
        agvState.setUpdateTime(DateUtil.toLocalDateTime(new Date()));
        clientHandler.agvStateService.saveOrUpdate(agvState,updateWrapper);
//        }
//        List<AxisState> axisStates=agvMessage.getAxisStates();
//        if(CollUtil.isEmpty(axisStates)){
//            return;
//        }
//        axisStates.forEach(axisState -> {
//            axisState.setAgvStateId(agvState.getId());
//            UpdateWrapper<AxisState> axisStateUpdateWrapper=new UpdateWrapper<>();
//            axisStateUpdateWrapper.eq("agv_state_id",agvMessage.getAgvId());
//            clientHandler.axisStateService.saveOrUpdate(axisState, axisStateUpdateWrapper);
//        });

//        String data = agvMessage.getData();
//        AgvPad agvPad = JSON.parseObject(data, AgvPad.class);
//        Optional.ofNullable(agvPad).ifPresent(agvPad1 -> {
//            List<AgvState> agvStates = agvPad1.getAgvStates();
//            for (AgvState agvState : agvStates) {
//                Integer id = clientHandler.agvStateService.insertReturnId(agvState);
//                if (agvState.getAxisStates() != null) {
//                    for (AxisState axisState : agvState.getAxisStates()) {
//                        axisState.setAgvStateId(id);
//                        clientHandler.axisStateService.save(axisState);
//                    }
//                }
//            }
//            List<AlarmState> alarmStates = agvPad1.getAlarmStates();
//            for (AlarmState alarmState : alarmStates) {
//                clientHandler.alarmStateService.save(alarmState);
//            }
//        });
    }

    public AgvMessage combineAgvMessage() {
        AgvMessage agvMessage = new AgvMessage();
        heart = heart + 1;
        if (heart == 257) {
            heart = 1;
        }
        packNr = packNr + 1;
        if (packNr == 60001) {
            packNr = 1;
        }
        agvMessage.setHeart(heart);
        agvMessage.setPackNr(packNr);
        agvMessage.setPackType(257);
        return agvMessage;
    }

    private void insertFlow(Integer taskNum, Integer status) {
        Request request1 = clientHandler.requestService.listAllByTaskNum(taskNum);
        if (ObjectUtil.isNull(request1)) {
            try {
                throw new Exception(String.format("任务:%s不存在"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestFlow requestFlow = new RequestFlow();
        requestFlow.setRequestId(request1.getId());
        if (status.equals(REQUEST_STATUS_2)) {
            requestFlow.setFromStatus(1);
            requestFlow.setToStatus(REQUEST_STATUS_2);
        } else if (status.equals(REQUEST_STATUS_FINISH)) {
            requestFlow.setFromStatus(REQUEST_STATUS_2);
            requestFlow.setToStatus(REQUEST_STATUS_FINISH);
        }
        clientHandler.requestFlowService.save(requestFlow);
    }

}
