package com.xdcplus.xdcweb.basics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdcplus.netty.common.model.StationInfo;
import com.xdcplus.xdcweb.basics.mapper.StationInfoMapper;
import com.xdcplus.xdcweb.basics.service.IStationInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fish
 * @since 2021-05-08
 */
@Service
public class StationInfoServiceImpl extends ServiceImpl<StationInfoMapper, StationInfo> implements IStationInfoService {

}
