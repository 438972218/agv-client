package com.xdcplus.xdcweb.basics.controller;


import com.xdcplus.netty.common.model.AgvMessage;
import com.xdcplus.netty.common.model.Request;
import com.xdcplus.netty.common.tool.Converter;
import com.xdcplus.xdcweb.basics.handler.ClientHandler;
import com.xdcplus.xdcweb.basics.handler.TcpClient;
import com.xdcplus.xdcweb.basics.service.IAgvMessageService;
import com.xdcplus.xdcweb.basics.service.IRequestService;
import com.xdcplus.xdcweb.basics.service.impl.AgvMessageServiceImpl;
import com.xdcplus.xdcweb.global.pojo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fish
 * @since 2021-05-08
 */
@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    IRequestService requestService;

    @Autowired
    IAgvMessageService iAgvMessageService;

//    @PostMapping("/send1")
//    public void send1(@RequestBody Request request) throws UnsupportedEncodingException {
//
//        AgvMessage agvMessage = new AgvMessage();
//        Integer packNr = ClientHandler.getPackNr()+1;
//
//        agvMessage.setPackNr(packNr);
//        ClientHandler.setPackNr(packNr);
//
//        //任务交互
//        agvMessage.setPackType(259);
//        agvMessage.setType("client");
//
//        String json = requestService.create(request);
//
//        agvMessage.setData(json);
//        byte[] a = Converter.compileObject(agvMessage);
//        agvMessage.setBytes(a);
//        iAgvMessageService.save(agvMessage);
//
//        TcpClient.send(agvMessage);
//        return ResponseVO.success();
//    }

    @PostMapping("/create")
    public ResponseVO createToSubmit(@RequestBody Request request) throws Exception {
        requestService.createToSubmit(request);
        return ResponseVO.success();
    }

    @GetMapping("/submit")
    public ResponseVO submit(@RequestParam(value = "taskNum", required = false, defaultValue = "0") Integer taskNum) throws Exception {

        String json = requestService.submit(taskNum);

        AgvMessage agvMessage = new AgvMessage();
        Integer packNr = ClientHandler.getPackNr()+1;

        agvMessage.setPackNr(packNr);
        ClientHandler.setPackNr(packNr);

        //任务交互
        agvMessage.setPackType(259);
        agvMessage.setType("client");

        agvMessage.setData(json);
        byte[] a = Converter.compileObject(agvMessage);
        agvMessage.setBytes(a);
        iAgvMessageService.save(agvMessage);

        TcpClient.send(agvMessage);
        return ResponseVO.success();
    }

    @PostMapping("/list")
    public ResponseVO<List<Request>> list(@RequestBody Request request) {
        List<Request> requests =requestService.listAll(request);
        return ResponseVO.success(requests);
    }

    @GetMapping("/delete")
    public ResponseVO delete(@RequestParam(value = "taskNum", required = false, defaultValue = "0") Integer taskNum) throws Exception {
        requestService.delete(taskNum);
        return ResponseVO.success();
    }

    @GetMapping("/gettasknum")
    public ResponseVO<Integer> getTaskNum() {
        return ResponseVO.success(requestService.getTaskNum());
    }

}
