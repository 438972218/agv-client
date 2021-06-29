package com.xdcplus.xdcweb.basics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdcplus.netty.common.dto.RequestDTO;
import com.xdcplus.netty.common.dto.StationInfoDTO;
import com.xdcplus.netty.common.dto.StationTaskInfoDTO;
import com.xdcplus.netty.common.dto.TaskDTO;
import com.xdcplus.netty.common.model.Request;
import com.xdcplus.netty.common.model.RequestFlow;
import com.xdcplus.netty.common.model.StationInfo;
import com.xdcplus.netty.common.model.StationTaskInfo;
import com.xdcplus.xdcweb.basics.mapper.RequestFlowMapper;
import com.xdcplus.xdcweb.basics.mapper.RequestMapper;
import com.xdcplus.xdcweb.basics.mapper.StationInfoMapper;
import com.xdcplus.xdcweb.basics.mapper.StationTaskInfoMapper;
import com.xdcplus.xdcweb.basics.service.IRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fish
 * @since 2021-05-08
 */
@Service
public class RequestServiceImpl extends ServiceImpl<RequestMapper, Request> implements IRequestService {
    @Autowired
    RequestMapper requestMapper;
    @Autowired
    StationInfoMapper stationInfoMapper;
    @Autowired
    StationTaskInfoMapper stationTaskInfoMapper;
    @Autowired
    RequestFlowMapper requestFlowMapper;

    @Override
    public String create(Request request) {
        request.setStatus(1);
        Integer result = requestMapper.insertReturnId(request);
        if(result==1){
            List<StationInfo> stationInfos=request.getStationInfos();
            stationInfos.stream().forEach(stationInfo -> {
                stationInfo.setRequestId(request.getId());
                stationInfoMapper.insertReturnId(stationInfo);
                List<StationTaskInfo> stationTaskInfos=stationInfo.getStationTaskInfos();
                stationTaskInfos.stream().forEach(stationTaskInfo -> {
                    stationTaskInfo.setStationInfoId(stationInfo.getId());
                    stationTaskInfoMapper.insert(stationTaskInfo);
                });
            });
        }

        List<StationInfo> stationInfos=request.getStationInfos();
        List<StationInfoDTO> stationInfoDTOS=new ArrayList<>();
        final int[] i = {1};
        stationInfos.stream().forEach(stationInfo -> {

            List<StationTaskInfo> stationTaskInfos=stationInfo.getStationTaskInfos();
            List<StationTaskInfoDTO> stationTaskInfoDTOS=new ArrayList<>();

            final int[] i1 = {1};
            stationTaskInfos.stream().forEach(stationTaskInfo -> {
                StationTaskInfoDTO stationTaskInfoDTO=StationTaskInfoDTO
                        .builder()
                        .actionAxis(stationTaskInfo.getActionAxis()).taskType(stationTaskInfo.getTaskType())
                        .actionIndex(i1[0])
                        .build();
                i1[0]++;
                stationTaskInfoDTOS.add(stationTaskInfoDTO);
            });

            StationInfoDTO stationInfoDTO=StationInfoDTO.builder()
                    .stationAsyn(1).stationIndex(i[0]).stationId(stationInfo.getStationId())
                    .stationTaskInfo(stationTaskInfoDTOS)
                    .build();
            i[0]++;
            stationInfoDTOS.add(stationInfoDTO);
        });

        TaskDTO taskDTO= TaskDTO.builder()
                .agvId(request.getAgvId()).actionType(10)
                .stationInfo(stationInfoDTOS)
                .build();

        RequestDTO requestDTO=RequestDTO.builder()
                .taskNum(request.getTaskNum())
                .Task(taskDTO)
                .build();

        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(requestDTO);
        String resJson=JSONObject.toJSONString(jsonObject);

        return resJson;
    }

    @Override
    public void createToSubmit(Request request) throws Exception {
        Request request1 =requestMapper.listAllByTaskNum(request.getTaskNum());
        if(ObjectUtil.isNotNull(request1)){
            throw new Exception(String.format("任务:%s已存在",request.getTaskNum()));
        }
        request.setStatus(9);
        Integer result = requestMapper.insertReturnId(request);
        if(result==1){
            List<StationInfo> stationInfos=request.getStationInfos();
            stationInfos.stream().forEach(stationInfo -> {
                stationInfo.setRequestId(request.getId());
                stationInfoMapper.insertReturnId(stationInfo);
                List<StationTaskInfo> stationTaskInfos=stationInfo.getStationTaskInfos();
                stationTaskInfos.stream().forEach(stationTaskInfo -> {
                    stationTaskInfo.setStationInfoId(stationInfo.getId());
                    stationTaskInfoMapper.insert(stationTaskInfo);
                });
            });
        }

//        List<StationInfo> stationInfos=request.getStationInfos();
//        List<StationInfoDTO> stationInfoDTOS=new ArrayList<>();
//        final int[] i = {1};
//        stationInfos.stream().forEach(stationInfo -> {
//
//            List<StationTaskInfo> stationTaskInfos=stationInfo.getStationTaskInfos();
//            List<StationTaskInfoDTO> stationTaskInfoDTOS=new ArrayList<>();
//
//            final int[] i1 = {1};
//            stationTaskInfos.stream().forEach(stationTaskInfo -> {
//                StationTaskInfoDTO stationTaskInfoDTO=StationTaskInfoDTO
//                        .builder()
//                        .actionAxis(stationTaskInfo.getActionAxis()).taskType(stationTaskInfo.getTaskType())
//                        .actionIndex(i1[0])
//                        .build();
//                i1[0]++;
//                stationTaskInfoDTOS.add(stationTaskInfoDTO);
//            });
//
//            StationInfoDTO stationInfoDTO=StationInfoDTO.builder()
//                    .stationAsyn(1).stationIndex(i[0]).stationId(stationInfo.getStationId())
//                    .stationTaskInfo(stationTaskInfoDTOS)
//                    .build();
//            i[0]++;
//            stationInfoDTOS.add(stationInfoDTO);
//        });
//
//        TaskDTO taskDTO= TaskDTO.builder()
//                .agvId(request.getAgvId()).actionType(10)
//                .stationInfo(stationInfoDTOS)
//                .build();
//
//        RequestDTO requestDTO=RequestDTO.builder()
//                .taskNum(request.getTaskNum())
//                .Task(taskDTO)
//                .build();
//
//        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(requestDTO);
//        String resJson=JSONObject.toJSONString(jsonObject);
//
//        return resJson;
    }

    @Override
    public String submit(Integer taskNum) throws Exception {
        Request request =requestMapper.listAllByTaskNum(taskNum);
        if(ObjectUtil.isNull(request)){
            throw new Exception(String.format("无法找到任务:%s",taskNum));
        }

        List<StationInfo> stationInfos=request.getStationInfos();
        List<StationInfoDTO> stationInfoDTOS=new ArrayList<>();
        final int[] i = {1};
        stationInfos.stream().forEach(stationInfo -> {

            List<StationTaskInfo> stationTaskInfos=stationInfo.getStationTaskInfos();
            List<StationTaskInfoDTO> stationTaskInfoDTOS=new ArrayList<>();

            final int[] i1 = {1};
            stationTaskInfos.stream().forEach(stationTaskInfo -> {
                StationTaskInfoDTO stationTaskInfoDTO=StationTaskInfoDTO
                        .builder()
                        .actionAxis(stationTaskInfo.getActionAxis()).taskType(stationTaskInfo.getTaskType())
                        .actionIndex(i1[0])
                        .build();
                i1[0]++;
                stationTaskInfoDTOS.add(stationTaskInfoDTO);
            });

            StationInfoDTO stationInfoDTO=StationInfoDTO.builder()
                    .stationAsyn(1).stationIndex(i[0]).stationId(stationInfo.getStationId())
                    .stationTaskInfo(stationTaskInfoDTOS)
                    .build();
            i[0]++;
            stationInfoDTOS.add(stationInfoDTO);
        });

        TaskDTO taskDTO= TaskDTO.builder()
                .agvId(request.getAgvId()).actionType(10)
                .stationInfo(stationInfoDTOS)
                .build();

        RequestDTO requestDTO=RequestDTO.builder()
                .taskNum(request.getTaskNum())
                .Task(taskDTO)
                .build();

        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(requestDTO);
        String resJson=JSONObject.toJSONString(jsonObject);

        //修改request状态
        request.setStatus(1);
        requestMapper.updateById(request);
        RequestFlow requestFlow=new RequestFlow();
        requestFlow.setRequestId(request.getId());
        requestFlow.setFromStatus(9);
        requestFlow.setToStatus(1);
        requestFlowMapper.insert(requestFlow);

        return resJson;
    }

    @Override
    public List<Request> listAll(Request request) {
        return requestMapper.listAll(request);
    }

    @Override
    public Request listAllByTaskNum(Integer taskNum) {
        return requestMapper.listAllByTaskNum(taskNum);
    }

    @Override
    public Integer getTaskNum() {
        QueryWrapper<Request> requestQueryWrapper=new QueryWrapper<>();
        requestQueryWrapper.orderByDesc("id");
        List<Request> employeeList=requestMapper.selectList(requestQueryWrapper);
        if(CollUtil.isEmpty(employeeList)){
            return 1;
        }else{
            Integer id = employeeList.get(0).getTaskNum()+1;
            return id;
        }
    }

    @Override
    public void delete(Integer taskNum) throws Exception {
        Request request =requestMapper.listAllByTaskNum(taskNum);
        Integer oldStatus=request.getStatus();
        if(ObjectUtil.isNull(request)){
            throw new Exception(String.format("无法找到任务:%s",taskNum));
        }
        request.setStatus(4);
        requestMapper.updateById(request);
        RequestFlow requestFlow=new RequestFlow();
        requestFlow.setRequestId(request.getId());
        requestFlow.setFromStatus(oldStatus);
        requestFlow.setToStatus(4);
        requestFlowMapper.insert(requestFlow);
    }
}
