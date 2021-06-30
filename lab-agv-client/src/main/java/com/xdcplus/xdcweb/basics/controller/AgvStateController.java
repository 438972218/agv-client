package com.xdcplus.xdcweb.basics.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdcplus.netty.common.model.AgvState;
import com.xdcplus.xdcweb.basics.service.IAgvStateService;
import com.xdcplus.xdcweb.global.pojo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/agv-state")
public class AgvStateController {
    @Autowired
    IAgvStateService agvStateService;

    @PostMapping("/list")
    public ResponseVO<AgvState> list(@RequestBody AgvState agvState) {

        QueryWrapper<AgvState> queryWrapper=new QueryWrapper<>();
        if(agvState.getAgvId()!=null){
            queryWrapper.eq("agv_id",agvState.getAgvId());
        }
        List<AgvState> agvStates = agvStateService.list(queryWrapper);
        if(CollUtil.isNotEmpty(agvStates)){
            return ResponseVO.success(agvStates.get(0));
        }else{
            return ResponseVO.success();
        }
    }

}
