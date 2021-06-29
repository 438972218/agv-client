package com.xdcplus.xdcweb.basics.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xdcplus.netty.common.model.AgvMessage;
import com.xdcplus.netty.common.model.BisData;
import com.xdcplus.netty.common.model.Request;
import com.xdcplus.netty.common.tool.Converter;
import com.xdcplus.xdcweb.basics.handler.ClientHandler;
import com.xdcplus.xdcweb.basics.handler.TcpClient;
import com.xdcplus.xdcweb.basics.service.IAgvMessageService;
import com.xdcplus.xdcweb.basics.service.IBisDataService;
import com.xdcplus.xdcweb.global.pojo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author fish
 * @since 2021-04-30
 */
@RestController
@RequestMapping("/bisdata")
public class BisDataController {
    @Autowired
    IBisDataService bisDataService;

    @PostMapping("/list")
    public ResponseVO<List<BisData>> list(@RequestBody BisData bisData) {
        QueryWrapper<BisData> queryWrapper = new QueryWrapper<>();
        if(bisData.getDataCate()!=null){
            queryWrapper.eq("data_cate", bisData.getDataCate());
        }
        if(bisData.getDataExt()!=null){
            queryWrapper.eq("data_ext", bisData.getDataExt());
        }

        List<BisData> bisData1=bisDataService.list(queryWrapper);
        return ResponseVO.success(bisData1);
    }

    @PostMapping("/create")
    public ResponseVO createToSubmit(@RequestBody BisData bisData) {
        bisDataService.save(bisData);
        return ResponseVO.success();
    }

    @PostMapping("/update")
    public ResponseVO update(@RequestBody BisData bisData){
        bisDataService.updateById(bisData);
        return ResponseVO.success();
    }

    @GetMapping("/delete")
    public ResponseVO delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id){
        bisDataService.removeById(id);
        return ResponseVO.success();
    }

}
