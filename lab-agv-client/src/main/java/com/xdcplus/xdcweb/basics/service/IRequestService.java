package com.xdcplus.xdcweb.basics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xdcplus.netty.common.model.Request;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fish
 * @since 2021-05-08
 */
public interface IRequestService extends IService<Request> {

    String create(Request request);

    void createToSubmit(Request request) throws Exception;

    String submit(Integer taskNum) throws Exception;

    List<Request> listAll(Request request);

    void delete(Integer taskNum) throws Exception;

    Request listAllByTaskNum(Integer taskNum);

    Integer getTaskNum();

}
