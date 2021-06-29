package com.xdcplus.xdcweb.basics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdcplus.netty.common.model.Request;
import com.xdcplus.netty.common.model.StationInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fish
 * @since 2021-05-08
 */
public interface StationInfoMapper extends BaseMapper<StationInfo> {

    Integer insertReturnId(StationInfo stationInfo);

}
