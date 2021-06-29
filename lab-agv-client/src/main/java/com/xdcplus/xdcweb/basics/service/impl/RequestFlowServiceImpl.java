package com.xdcplus.xdcweb.basics.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
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
import com.xdcplus.xdcweb.basics.service.IRequestFlowService;
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
public class RequestFlowServiceImpl extends ServiceImpl<RequestFlowMapper, RequestFlow> implements IRequestFlowService {
}
