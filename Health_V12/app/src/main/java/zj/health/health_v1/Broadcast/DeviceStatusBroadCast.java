package zj.health.health_v1.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.DeviceService;

/**
 * Created by Administrator on 2018/7/6.
 */

public class DeviceStatusBroadCast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.bluetooth.le.ACTION_GATT_CONNECTED")){
            Toast.makeText(Health_AppLocation.instance, Health_AppLocation.instance.getString(R.string.bluetooth_connect), Toast.LENGTH_SHORT).show();
            Intent it = new Intent(context, DeviceService.class);
            context.startService(it);
        }else if(intent.getAction().equals("com.example.bluetooth.le.ACTION_GATT_DISCONNECTED")){
//            Toast.makeText(Health_AppLocation.instance, Health_AppLocation.instance.getString(R.string.bluetooth_disconnect), Toast.LENGTH_SHORT).show();
//            if(MainsActivity.mBluetoothLeService!=null){
//                MainsActivity.mBluetoothLeService.disconnect();
//            }
//            Intent intent1 = new Intent();
//            intent.setAction("disconnected_device");
//            context.sendBroadcast(intent1);
//            Intent it = new Intent(context, DeviceService.class);
//            context.stopService(it);
        }else if(intent.getAction().equals("com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED")){

        }

    }
}
