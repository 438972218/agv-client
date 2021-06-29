package com.xdcplus.netty.common.encode;

import com.xdcplus.netty.common.model.AgvMessage;
import com.xdcplus.netty.common.tool.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 解码器
 *
 * @author fish.fei
 * @date 2021/06/28
 */
@Slf4j
public class HeartbeatDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String log1=Converter.bytesToHexString(bytes);
        log.info(log1);
        if (!log1.substring(log1.length()-8).equals("ffffffff")) {
            byteBuf.writeBytes(bytes);
            return;
        }

//        System.out.println(Converter.bytesToHexString(bytes));
        AgvMessage megHeader = Converter.decodeObject(bytes);
        list.add(megHeader);
    }
}
