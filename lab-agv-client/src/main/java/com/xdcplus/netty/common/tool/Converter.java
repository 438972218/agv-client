package com.xdcplus.netty.common.tool;

import cn.hutool.core.collection.CollUtil;
import com.xdcplus.netty.common.model.AgvMessage;
import com.xdcplus.netty.common.model.AxisState;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 转换类
 * @author : Fish Fei
 */
public class Converter {

    public static String Encoding = "GBK";

    /**
     * 整型转换为4位字节数组
     *
     * @param intValue
     * @return
     */
    public static byte[] int2Byte(int intValue) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
//            System.out.print(Integer.toBinaryString(b[i])+" ");
//            System.out.print((b[i] & 0xFF) + " ");
        }
        return b;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }


    /**
     * 十六进制字符串转换成普通字符串
     *
     * @param hex
     * @return
     */
    public String hex2String(String hex) {
        byte[] bytes = Converter.hexToBytes(hex);
        return Converter.toString(bytes);
    }

    /**
     * String字符串转换16进制byte[].
     *
     * @param s
     * @return byte[]
     */
    public static byte[] stringToBytes(String s) {
        s = s.replace(" ", "");
        s = s.replace("#", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baKeyword;
    }

    /**
     * 十六进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    private static byte[] hexToBytes(String hex) {
        hex = hex.replaceAll("0x", "");
        hex = hex.replaceAll(" ", "");

        String[] tmps = new String[hex.length() / 2];

        for (int i = 0; i < tmps.length; i++) {
            tmps[i] = hex.substring(i * 2, i * 2 + 2);
        }
        byte[] bytes = new byte[tmps.length];

        for (int j = 0; j < tmps.length; j++) {
            if (tmps[j].trim().length() == 2) {
                bytes[j] = Integer.valueOf(tmps[j], 16).byteValue();
            }
        }
        return bytes;
    }

    /**
     * 字节数组转换成字符串
     *
     * @param bytes
     * @return
     */
    private static String toString(byte[] bytes) {
        try {
            if (bytes == null) {
                return null;
            }
            return new String(bytes, Encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param bytes
     * @param separator
     * @return
     */
    public static String toHexString(byte[] bytes, String separator) {
        return toHexString(bytes, 0, bytes.length, separator);
    }


    /**
     * 字节数组转换为十六进制字符串
     *
     * @param bytes
     * @param startIndex
     * @param length
     * @param separator
     * @return
     */
    private static String toHexString(byte[] bytes, int startIndex, int length, String separator) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String tmp = "";
        for (int i = 0; i < length; i++) {
            tmp = Integer.toHexString(bytes[i + startIndex]).toUpperCase(Locale.getDefault());
            if (tmp.length() > 2) {
                tmp = tmp.substring(tmp.length() - 2, tmp.length());
            } else if (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            sb.append(separator + tmp);
        }
        return sb.toString();
    }


    /**
     * 左边补字节0
     *
     * @param buffer
     * @param size
     * @param b
     * @return
     */
    private static byte[] leftPad(byte[] buffer, int size, byte b) {
        int len = buffer.length;
        if (len > size) {
            len = size;
        }
        byte[] bytes = new byte[size];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = b;
        }
        System.arraycopy(buffer, 0, bytes, size - len, len);
        return bytes;
    }


    /**
     * 右边补字节0
     *
     * @param buffer
     * @param size
     * @param b
     * @return
     */
    private static byte[] rightPad(byte[] buffer, int size, byte b) {
        int len = buffer.length;
        if (len > size) {
            len = size;
        }
        byte[] bytes = new byte[size];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = b;
        }
        System.arraycopy(buffer, 0, bytes, 0, len);
        return bytes;
    }

    /**
     * @param input
     * @return
     */
    public static String ToSBC(String input) {
        return null;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2()配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param ary
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset]&0xFF)
                | ((ary[offset+1]<<8) & 0xFF00)
                | ((ary[offset+2]<<16)& 0xFF0000)
                | ((ary[offset+3]<<24) & 0xFF000000));
        return value;
    }

    //截取byte数组
    public static byte[] subByte(byte[] b, int off, int length) {
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }

    //合并多个数组
    public static byte[] splicingArrays(byte[]... bytes) {
        int length = 0;
        for (byte[] b : bytes) {
            length += b.length;
        }
        int interimLength = 0;
        byte[] result = new byte[length];
        for (byte[] b : bytes) {
            System.arraycopy(b, 0, result, interimLength, b.length);
            interimLength += b.length;
        }
        return result;
    }

    //int2转int4,后转int
    public static int int2Toint(byte[] heart){
        byte[] heart0=new byte[2];
        heart0[0]=(byte) 0x00;
        heart0[1]=(byte) 0x00;
        byte[] heart2 = splicingArrays(heart0,heart);
        int heart1 = bytesToInt2(heart2,0);
        return heart1;
    }

    //对象转byte[]
    public static byte[] compileObject(AgvMessage megHeader) throws UnsupportedEncodingException {
        //起始符
        byte[] packHead = new byte[4];
        packHead[0] = (byte) 0xFE;
        packHead[1] = (byte) 0xFE;
        packHead[2] = (byte) 0xFE;
        packHead[3] = (byte) 0xFE;

        byte[] packNr = new byte[4];
        //数据包序号
        if (megHeader.getPackNr() != null) {
            packNr = intToBytes2(megHeader.getPackNr());
        }
        //ACK确认数据包序号
        byte[] packAckNr = new byte[4];
        if (megHeader.getPackAckNr() != null) {
            //ACK确认数据包序号
            packAckNr = intToBytes2(megHeader.getPackAckNr());
        }
        //ACK状态
        byte[] packAckSt = new byte[2];
        if (megHeader.getPackAckSt() != null) {
            packAckSt = intToBytes2(megHeader.getPackAckSt());
            packAckSt = subByte(packAckSt, 2, 2);
        }
        byte[] senderCode = new byte[6];
        if (megHeader.getSenderCode() != null) {
            senderCode = megHeader.getSenderCode().getBytes("GBK");
        }
        byte[] receiverCode = new byte[6];
        if (megHeader.getReceiverCode() != null) {
            receiverCode = megHeader.getReceiverCode().getBytes("GBK");
        }
        //心跳
        byte[] heart = new byte[2];
        if (megHeader.getHeart() != null) {
            heart = intToBytes2(megHeader.getHeart());
            heart = subByte(heart, 2, 2);
        }
        //报文类型
        byte[] packType = new byte[2];
        if (megHeader.getPackType() != null) {
            packType = intToBytes2(megHeader.getPackType());
            packType = subByte(packType, 2, 2);
        }

        byte[] spare = new byte[2];
        spare[0] = (byte) 0x00;
        spare[1] = (byte) 0x00;

        //报文信息
        byte[] data = new byte[0];
        if (megHeader.getData() != null) {
            data = megHeader.getData().getBytes("GBK");
        }
//        //报文内容字节数
//        byte[] dataSize = intToBytes(data.length);

        //结尾符
        byte[] packEnd = new byte[4];
        packEnd[0] = (byte) 0xFF;
        packEnd[1] = (byte) 0xFF;
        packEnd[2] = (byte) 0xFF;
        packEnd[3] = (byte) 0xFF;

        //数据包长度
        byte[] packLength = intToBytes2(48 + data.length);

        byte[] message = splicingArrays(packHead, packNr, packLength, packAckNr, packAckSt, senderCode, receiverCode, heart,
                packType, spare, spare, spare, spare, data, spare, packEnd);

        return message;
    }

    //byte[]转对象
    public static AgvMessage decodeObject(byte[] bytes) throws Exception {
        if(bytes.length<48){
            throw new Exception("报文异常"+bytesToHexString(bytes));
        }
        AgvMessage megHeader=new AgvMessage();
        byte[] packNr= Arrays.copyOfRange(bytes,4,8);
        int packNr1 = bytesToInt2(packNr,0);
        byte[] packLength= Arrays.copyOfRange(bytes,8,12);
        int packLength1 = bytesToInt2(packLength,0);
        if(packLength1==0 ||packLength1<48){
            throw new Exception("报文异常");
        }
        byte[] senderCode= Arrays.copyOfRange(bytes,18,24);
        String senderCode1 = new String(senderCode, "GBK");
        byte[] heart= Arrays.copyOfRange(bytes,30,32);
        int heart1 = int2Toint(heart);
        byte[] packType= Arrays.copyOfRange(bytes,32,34);
        //报文类型
        int packType1 = int2Toint(packType);
        //状态报文
        if(packType1==260){
            //小车编号
            byte[] agvId= Arrays.copyOfRange(bytes,44,46);
            int agvId1 = int2Toint(agvId);
            //任务编号
            byte[] taskNum= Arrays.copyOfRange(bytes,46,50);
            int taskNum1 = bytesToInt2(taskNum,0);
            //小车电量
            byte[] agvEnergy= Arrays.copyOfRange(bytes,50,52);
            int agvEnergy1 = int2Toint(agvEnergy);
            //小车状态
            byte[] agvState= Arrays.copyOfRange(bytes,52,54);
            int agvState1 = int2Toint(agvState);
            //当前站台
            byte[] curStationNum= Arrays.copyOfRange(bytes,54,56);
            int curStationNum1 = int2Toint(curStationNum);
            //小车角度
            byte[] agvAngle= Arrays.copyOfRange(bytes,56,58);
            int agvAngle1 = int2Toint(agvAngle);
            //轴数量
            byte[] axleQty= Arrays.copyOfRange(bytes,58,60);
            int axleQty1 = int2Toint(axleQty);
            //轴状态
            List<AxisState> axisStates =new ArrayList<>();
            int location = 0;
            for(int i=0;i<axleQty1;i++){
                AxisState axisState=new AxisState();
                //轴号
                byte[] actionAxis= Arrays.copyOfRange(bytes,58+(i*8),60+(i*8));
                int actionAxis1 = int2Toint(actionAxis);
                //花篮数量
                byte[] materialCount= Arrays.copyOfRange(bytes,60+(i*8),62+(i*8));
                int materialCount1 = int2Toint(materialCount);
                //物料类型
                byte[] materialType= Arrays.copyOfRange(bytes,62+(i*8),66+(i*8));
                int materialType1 = bytesToInt2(materialType,0);
                axisState.setActionAxis(actionAxis1);
                axisState.setMaterialCount(materialCount1);
                axisState.setMaterialType(materialType1);
                axisStates.add(axisState);
                location=66+(i*8);
            }
            if(CollUtil.isNotEmpty(axisStates)){
                megHeader.setAxisStates(axisStates);
            }
            //坐标X
            byte[] x= Arrays.copyOfRange(bytes,location,location+4);
            int x1 = bytesToInt2(x,0);
            //坐标y
            byte[] y= Arrays.copyOfRange(bytes,location+4,location+8);
            int y1 = bytesToInt2(y,0);
            //异常码
            byte[] alarmCode= Arrays.copyOfRange(bytes,location+8,location+12);
            int alarmCode1 = bytesToInt2(alarmCode,0);
            megHeader.setAgvId(agvId1);
            megHeader.setTaskNum(taskNum1);
            megHeader.setAgvEnergy(agvEnergy1);
            megHeader.setAgvState(agvState1);
            megHeader.setCurStationNum(curStationNum1);
            megHeader.setX(x1);
            megHeader.setY(y1);
            megHeader.setAgvAngle(String.valueOf(agvAngle1/100));
            megHeader.setAxleQty(axleQty1);
            megHeader.setAlarmCode(alarmCode1);
        }else{
            byte[] data= Arrays.copyOfRange(bytes,42,42+packLength1-48);
            String data1 = new String(data, "GBK");
            megHeader.setData(data1);
        }
//        byte[] dataSize= Arrays.copyOfRange(bytes,42,46);
//        int dataSize1 = bytesToInt(dataSize,0);
//        byte[] data= Arrays.copyOfRange(bytes,42,42+packLength1-48);
//        String data1 = new String(data, "GBK");

        megHeader.setPackNr(packNr1);
        megHeader.setPackLength(packLength1);
        megHeader.setSenderCode(senderCode1);
        megHeader.setHeart(heart1);
        megHeader.setPackType(packType1);
//        megHeader.setData(data1);
//        megHeader.setDataSize(dataSize1);
        return megHeader;
    }

//    //byte[]转对象s
//    public static List<AgvMessage> decodeObjects(byte[] bytes) throws UnsupportedEncodingException {
//        AgvMessage megHeader=new AgvMessage();
//        byte[] packNr= Arrays.copyOfRange(bytes,4,8);
//        int packNr1 = bytesToInt2(packNr,0);
//        byte[] packLength= Arrays.copyOfRange(bytes,8,12);
//        int packLength1 = bytesToInt2(packLength,0);
//        byte[] senderCode= Arrays.copyOfRange(bytes,18,24);
//        String senderCode1 = new String(senderCode, "GBK");
//        byte[] heart= Arrays.copyOfRange(bytes,30,32);
//        int heart1 = int2Toint(heart);
//        byte[] packType= Arrays.copyOfRange(bytes,32,34);
//        int packType1 = int2Toint(packType);
////        byte[] dataSize= Arrays.copyOfRange(bytes,42,46);
////        int dataSize1 = bytesToInt(dataSize,0);
//        byte[] data= Arrays.copyOfRange(bytes,42,42+packLength1-48);
//
//        String data1 = new String(data, "GBK");
//
//        megHeader.setPackNr(packNr1);
//        megHeader.setPackLength(packLength1);
//        megHeader.setSenderCode(senderCode1);
//        megHeader.setHeart(heart1);
//        megHeader.setPackType(packType1);
//        megHeader.setData(data1);
//        return megHeader;
//    }

    //byte[]转string
    public static String bytesToHexString(byte[] src){

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();

    }

//    //对象转json
//    public static String agvInteractionToJson(AgvInteraction agvInteraction){
//        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(agvInteraction);
//        //去policyQueryRequestDto对象中的id属性
//        jsonObject.remove("id");
//        //将json对象转成字符串
//        String resJson=JSONObject.toJSONString(jsonObject);
//
//        return resJson;
//    }
}
