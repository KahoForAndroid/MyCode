package zj.health.health_v1.Utils.BlueToothUtil;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import java.util.Calendar;

import zj.health.health_v1.Base.BlueToothConstant;
import zj.health.health_v1.Model.DateModel;

/**
 * Created by Administrator on 2018/7/2.
 */

public class WearfitUtil {

    private static final String TAG = "WearfitUtil";
    private static Context mContext;
    private static WearfitUtil instance;

    private WearfitUtil() {
    }

    public static synchronized WearfitUtil getInstance(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (instance == null) {
            instance = new WearfitUtil();
        }
        return instance;
    }


    /**
     * 单次、实时测量
     * @param status  心率：0X09(单次) 0X0A(实时)
     *                血氧：0X11(单次) 0X12(实时)
     *                血压：0X21(单次) 0X22(实时)
     *
     * @param control 0关  1开
     */
    public byte[] setOnceOrRealTimeMeasure(int status, int control) {
        byte[] bytes = new byte[7];
        bytes[0] = (byte) 0xAB;
        bytes[1] = (byte) 0;
        bytes[2] = (byte) 4;
        bytes[3] = (byte) 0xFF;
        bytes[4] = (byte) 0x31;
        bytes[5] = (byte) status;
        bytes[6] = (byte) control;
//        broadcastData(bytes);
        return bytes;
    }


    /**
     * sync data from band
     *
     * @param timeInMillis
     */
    public static byte [] syncData(long timeInMillis) {
        DateModel dateModel = new DateModel(timeInMillis);
        byte[] data = new byte[17];
        data[0] = (byte) 0xAB;
        data[1] = (byte) 0;
        data[2] = (byte) 14;
        data[3] = (byte) 0xff;
        data[4] = (byte) 0x51;
        data[5] = (byte) 0x80;
//        data[6] = (byte)0;
        data[7] = (byte) ((dateModel.year - 2000));
        data[8] = (byte) (dateModel.month);
        data[9] = (byte) (dateModel.day);
        data[10] = (byte) (dateModel.hour);
        data[11] = (byte) (dateModel.minute);
        data[12] = (byte) ((dateModel.year - 2000));
        data[13] = (byte) (dateModel.month);
        data[14] = (byte) (dateModel.day);
        data[15] = (byte) (dateModel.hour);
        data[16] = (byte) (dateModel.minute);
        return data;
    }


    /**
     * 马达测试
     */
    public byte[] motorText(int control){
        byte[] bytes = new byte[7];
        bytes[0] = (byte) 0xAB;
        bytes[1] = (byte) 0;
        bytes[2] = (byte) 4;
        bytes[3] = (byte) 0xFF;
        bytes[4] = (byte) 0xB1;
        bytes[5] = (byte)0x80;
        bytes[6] = (byte)control;
        return bytes;
    }


    /**
     * 摇摇拍照指令
     * @param control 0关  1开
     */
    public byte[]  setSharkTakePhoto(int control){
        byte[] bytes = new byte[7];
        bytes[0] = (byte) 0xAB;
        bytes[1] = (byte) 0;
        bytes[2] = (byte) 4;
        bytes[3] = (byte) 0xFF;
        bytes[4] = (byte) 0x79;
        bytes[5] = (byte) 0x80;
        bytes[6] = (byte) control;
        return bytes;
    }

    /**
     * 同步时间  ab 00 0b ff 93 80 00 07 e0 0a 0e 09 33 12
     */
    public static byte[] setTimeSync() {
        //当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        byte[] data = new byte[14];
        data[0] = (byte) 0xAB;
        data[1] = (byte) 0;
        data[2] = (byte) 11;
        data[3] = (byte) 0xff;
        data[4] = (byte) 0x93;
        data[5] = (byte) 0x80;
//        data[6] = (byte)0;//占位符
        data[7] = (byte) ((year & 0xff00) >> 8);
        data[8] = (byte) (year & 0xff);
        data[9] = (byte) (month & 0xff);
        data[10] = (byte) (day & 0xff);
        data[11] = (byte) (hour & 0xff);
        data[12] = (byte) (minute & 0xff);
        data[13] = (byte) (second & 0xff);
        return data;
    }


    /**
     * @brief writeRXCharacteristic
     */
    public boolean writeRXCharacteristic(byte[] value,BluetoothGatt mBluetoothGatt) {
        BluetoothGattService RxService = mBluetoothGatt
                .getService(BlueToothConstant.RX_SERVICE_UUID);
        if (RxService == null) {
            return false;
        }

        BluetoothGattCharacteristic RxChar = RxService
                .getCharacteristic(BlueToothConstant.RX_CHAR_UUID);
        if (RxChar == null) {
            return false;
        }
        RxChar.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(RxChar);
//        Log.d(TAG, "Send command：status：" + status + "-->" + DataHandlerUtils.bytesToHexStr(value));
        return status;
    }

    /**
     * @brief enableTXNotification
     */
    @SuppressLint("InlinedApi")
    public void enableTXNotification(BluetoothGatt mBluetoothGatt) {
        BluetoothGattService RxService = mBluetoothGatt
                .getService(BlueToothConstant.RX_SERVICE_UUID);
        if (RxService == null) {
            return;
        }

        BluetoothGattCharacteristic TxChar = RxService
                .getCharacteristic(BlueToothConstant.TX_CHAR_UUID);
        if (TxChar == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(TxChar, true);
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(BlueToothConstant.CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }

}
