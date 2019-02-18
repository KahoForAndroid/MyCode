package zj.health.health_v1.Model;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Administrator on 2018/7/9.
 */

public class BlueToothDeviceModel {

    private int FromType;
    private BluetoothDevice bluetoothDevice;

    public int getFromType() {
        return FromType;
    }

    public void setFromType(int fromType) {
        FromType = fromType;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
