package com.xdcplus.xdcweb.basics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xdcplus.netty.common.model.Request;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fish
 * @since 2021-05-08
 */
public interface RequestMapper extends BaseMapper<Request> {

    Integer insertReturnId(Request request);

    List<Request> listAll(Request request);

    Request listAllByTaskNum(Integer id);
}
