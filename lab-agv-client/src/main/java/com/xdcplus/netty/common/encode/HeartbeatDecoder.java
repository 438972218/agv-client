package com.xdcplus.netty.common.encode;

import com.xdcplus.netty.common.model.AgvMessage;
import com.xdcplus.netty.common.tool.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


/**
 * 解码器
 *
 * @author fish.fei
 * @date 2021/06/28
 */
@Slf4j
public class HeartbeatDecoder extends ByteToMessageDecoder {

    private String ending="ffffffff";

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String log1=Converter.bytesToHexString(bytes);
        if(bytes.length!=50){
            log.info(log1);
        }

        int location=log1.indexOf(ending);
        int length=log1.length();
        //1.结尾符在最后
        if(location==(log1.length()-8)){
            AgvMessage megHeader = Converter.decodeObject(bytes);
            list.add(megHeader);
        //2.没有结尾符
        }else if(location==-1){
            byteBuf.writeBytes(bytes);
            return;
        //3.结尾符在中间
        }else if(location<(log1.length()-8) && location>0){
            //拆分
            byte[] one =Arrays.copyOfRange(bytes,0,(location+8)/2);
            byte[] two =Arrays.copyOfRange(bytes,(location+8)/2,bytes.length);

            //解析one
            AgvMessage megHeader = Converter.decodeObject(one);
            list.add(megHeader);

            //two存入下次解析部分
            byteBuf.writeBytes(two);
            return;
        }

//        if(log1.substring(log1.length()-8).equals(ending)){
//            AgvMessage megHeader = Converter.decodeObject(bytes);
//            list.add(megHeader);
//        }else if(!log1.substring(log1.length()-8).equals(ending)){
//            byteBuf.writeBytes(bytes);
//            return;
//        }else if(location<log1.length()-8){
//
//        }

        if (!log1.substring(log1.length()-8).equals("ffffffff")) {
            byteBuf.writeBytes(bytes);
            return;
        }

//        System.out.println(Converter.bytesToHexString(bytes));
        AgvMessage megHeader = Converter.decodeObject(bytes);
        list.add(megHeader);
    }
}
