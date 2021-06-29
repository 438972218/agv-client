package com.xdcplus.xdcweb.basics.service;

import com.xdcplus.netty.common.model.AgvMessage;
import org.springframework.stereotype.Service;

/**
 * @author : Fish Fei
 */
public interface AckService {

    AgvMessage sendOkAck(AgvMessage agvMessage);

    void acceptAck(AgvMessage agvMessage);
}
