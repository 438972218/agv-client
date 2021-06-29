package com.xdcplus.netty.common.encode;

import com.xdcplus.netty.common.model.AgvMessage;
import com.xdcplus.netty.common.tool.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳编码器
 *
 * @author fish.fei
 * @date 2021/06/28
 */
@Slf4j
public class HeartbeatEncoder extends MessageToByteEncoder<AgvMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AgvMessage megHeader, ByteBuf byteBuf) throws Exception {
        byte[] a = Converter.compileObject(megHeader);
        ByteBuf b= Unpooled.copiedBuffer(a);
        if(a.length!=48){
            log.info(Converter.bytesToHexString(a));
        }
//        System.out.println(Converter.bytesToHexString(a));
        byteBuf.writeBytes(b);
    }
}
