package zj.health.health_v1.Utils;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 常用字符串工具类
 * Created by Administrator on 2018/4/3.
 */

public class StringUtil {

    /**
     * 用于判断字符串是否为空或为空字符
     * @param s String变量
     * @return 不为空或不为空字符返回 true 否者返回false
     */
    public static boolean isEmpty(String s) {
        if (isNull(s))
            return true;
        else if (s.trim().equalsIgnoreCase(""))
            return true;
        else
            return false;
    }

    public static boolean isNull(String s) {
        if (s == null)
            return true;
        else if (s.equalsIgnoreCase("null"))
            return true;
        else
            return false;
    }

    /**
     * 处理String为空的情况 会直接返回空字符,以防止报空异常
     * @param s String 变量
     * @return
     */
    public static String trimNull(String s) {
        if (isNull(s))
            return "";
        else
            return s.trim();
    }

    /**
     * 判断是否是数字 如：123.4
     */
    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        int dom = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '.') {
                if (i == 0 && str.substring(0, 1).equalsIgnoreCase("-"))
                    continue;
                if (str.charAt(i) > '9' || str.charAt(i) < '0') {
                    return false;
                }
            } else {
                dom++;
                if (dom > 1)
                    return false;
            }
        }
        return true;
    }


    /**
     * dp转换成px
     */
    public static int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(Context context,float pxValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
    /**
     * sp转换成px
     */
    public static int sp2px(Context context, float spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }
    /**
     * px转换成sp
     */
    public static int px2sp(Context context,float pxValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue/fontScale+0.5f);
    }


    /**
     * 16进制转为String
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }


    /**
     * 字节数组转化成集合
     */
    public static List<Integer> bytesToArrayList(byte[] bytes){
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            datas.add(bytes[i] & 0xff);
        }
        return datas;
    }


//    /**
//     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
//     */
//    public static String decode(String bytes)
//    {
//        String hexString="0123456789ABCDEF";
//        ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
//        //将每2位16进制整数组装成一个字节
//        for(int i=0;i<bytes.length();i+=2)
//            baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
//        String bb = "";
//        try {
//            bb = new String(baos.toByteArray(), "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bb;
//    }


    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String decode(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

//    /**
//     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
//     */
//    public static String encode(String str){
//        String hexString="0123456789ABCDEF";
//        //根据默认编码获取字节数组
//        byte[] bytes=str.getBytes();
//        StringBuilder sb=new StringBuilder(bytes.length*2);
//        //将字节数组中每个字节拆解成2位16进制整数
//        for(int i=0;i<bytes.length;i++){
//            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
//            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
//        }
//        return sb.toString();
//    }

    private  int byteToInt(byte b, byte c) {//计算总包长，两个字节表示的
        short s = 0;
        int ret;
        short s0 = (short) (c & 0xff);// 最低位
        short s1 = (short) (b & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        ret = s;
        return ret;
    }

    private byte[] int2byte(int res) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (res & 0xff);// 最低位
        targets[0] = (byte) ((res >> 8) & 0xff);// 次低位
        return targets;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }


    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static String byte2hex(byte [] buffer){
        String h = "";
        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            h = h + temp;
        }
        return h;
    }


    /**
     * 传输数据
     *
     * @param byteArray
     * @return
     */
    public boolean write(BluetoothGattCharacteristic bleGattCharacteristic, BluetoothGatt bleGatt,
                         byte byteArray[]) {
        if (bleGattCharacteristic == null)
            return false;
        if (bleGatt == null)
            return false;
        bleGattCharacteristic.setValue(byteArray);
        return bleGatt.writeCharacteristic(bleGattCharacteristic);
    }

    /**
     * 传输数据
     *
     * @param
     * @return
     */
    public boolean write(BluetoothGattCharacteristic bleGattCharacteristic, BluetoothGatt bleGatt,
                         String str) {
        if (bleGattCharacteristic == null)
            return false;
        if (bleGatt == null)
            return false;
        bleGattCharacteristic.setValue(str);
        return bleGatt.writeCharacteristic(bleGattCharacteristic);
    }


    //将字符串转换成二进制字符串，以空格相隔
    public static String StrToBinstr(String str) {
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=Integer.toBinaryString(strChar[i])+ " ";
        }

        return result;
    }

    /**
     * 针对微信支付生成商户订单号，为了避免微信商户订单号重复（下单单位支付），
     *
     * @return
     */
    public static String generateOrderSN() {
        StringBuffer orderSNBuffer = new StringBuffer();
        orderSNBuffer.append(System.currentTimeMillis());
        orderSNBuffer.append(createRandom(false,7));
        return orderSNBuffer.toString();
    }

    /**
     * 创建指定数量的随机字符串
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length){
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }
}
