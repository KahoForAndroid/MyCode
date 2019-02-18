package zj.health.health_v1.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Manifest;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/6/17.
 */

public class BlueToothUtils {

    private Context context;
    private static final int REQUEST_BLUETOOTH_PERMISSION=10;

    public BlueToothUtils(Context context){
        this.context = context;
    }

    /**
     * 检查该设备是否支持BLE设备，谷歌在Android4.3才开始支持BLE设备
     */
    public void CheckBLE(){
        if(!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(context, context.getString(R.string.bluetooth_ble_error), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检测设备是否支持蓝牙
     * @param bluetoothManager 蓝牙管理对象
     */
    public BluetoothManager CheckBlueTooth(BluetoothManager bluetoothManager){
        BluetoothAdapter bluetoothAdapter = null;
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(context, context.getString(R.string.bluetooth__error), Toast.LENGTH_SHORT).show();
        }
        return bluetoothManager;
    }


    /**
     * 检测权限
     */
    public void RequestBluetoothPermission(){
        //判断系统版本
        if(Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions((BaseActivity) context,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_BLUETOOTH_PERMISSION);
                if (ActivityCompat.shouldShowRequestPermissionRationale((BaseActivity) context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //判断是否跟用户做一个说明
                }
            }
        }else{

        }
    }
}
