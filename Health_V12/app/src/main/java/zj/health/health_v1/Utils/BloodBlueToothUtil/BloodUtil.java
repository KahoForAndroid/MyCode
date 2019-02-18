package zj.health.health_v1.Utils.BloodBlueToothUtil;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * 血压设备工具类
 * Created by Administrator on 2018/7/17.
 */

public class BloodUtil {

    //发送连接血压计指令：cc80020301010001
    //("cc80020301030003");停止测量
    //("cc80020304040001");  电量
    //("cc80020301020002"); 启动测试
    //("cc80020301040004"); 关机
    public final String connectDevice =  "cc80020301010001";
    public final String StopMeasure =  "cc80020301030003";
    public final String Power =  "cc80020304040001";
    public final String StartMeasure =  "cc80020301020002";
    public final String CloseDevice =  "cc80020301040004";

    /**
     *
     * @param bytes 从设备接收到的数据
     * @param handler 数据提取后通过handler传回界面刷新UI
     */
    public void backBytes(byte[] bytes, Handler handler) {
        byte codeByte = bytes[5];//区别码 04 是电量 5是过程中产生的数据  6是正式结果  7是错误结束
        switch (codeByte) {
            case 0x04:
                int power = getIntValue(bytes[6], bytes[7]);
                break;
            case 0x05:
                int shousuoya = getIntValue(bytes[11], bytes[12]);

                break;
            case 0x06:
                int maibo = getIntValue(bytes[17], bytes[18]);
                int shuzhangya = getIntValue(bytes[15], bytes[16]);
                int shousuoya2 = getIntValue(bytes[13], bytes[14]);
                Message message = new Message();
                message.what = 200;
                message.obj = new int[]{shousuoya2,shuzhangya,maibo};
                handler.sendMessage(message);

                break;
            case 0x07:
                Message messageError = new Message();
                messageError.what = 100;
                handler.sendMessage(messageError);
                break;
        }
    }

    public  int getIntValue(byte byte1, byte byte2) {
        return (getIntValue(byte1) << 8) + getIntValue(byte2);
    }
    public  int getIntValue(byte byt) {
        return byt < 0 ? Math.abs(byt) + (128 - Math.abs(byt)) * 2 : byt;
    }

    public byte[] hex2byte(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length % 2 != 0) {
            System.out.println("ERROR:  le= " + paramArrayOfByte.length + " b:" + paramArrayOfByte.toString());
            return null;
        }
        byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
        for (int i = 0; i < paramArrayOfByte.length; i += 2) {
            String str = new String(paramArrayOfByte, i, 2);
            arrayOfByte[(i / 2)] = ((byte) Integer.parseInt(str, 16));
        }
        return arrayOfByte;
    }


    public boolean isBPDevice(BluetoothDevice bluetoothDevice) {//设备过滤
        if (null == bluetoothDevice || TextUtils.isEmpty(bluetoothDevice.getName()))
            return false;
        if (bluetoothDevice.getName().toLowerCase().startsWith("bp")) {
            return true;
        }
        return false;
    }

}
