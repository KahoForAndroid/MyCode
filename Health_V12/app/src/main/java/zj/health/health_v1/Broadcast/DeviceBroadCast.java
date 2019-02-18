package zj.health.health_v1.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zj.health.health_v1.Activity.Health_Log_Activity;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.R;
import zj.health.health_v1.Service.DeviceService;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/7/6.
 */

public class DeviceBroadCast extends BroadcastReceiver{

    private Intent it ;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        it = new Intent(context, DeviceService.class);

        if(intent.getIntExtra("TYPE",0) == 4){
            if(intent.getIntExtra("Status",0) == 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(it);
                }else{
                    context.startService(it);
                }
            }else{
                String read = intent.getIntExtra("readData",0)+"";
                long timeInMillis = intent.getLongExtra("timeInMillis",0);
                if(timeInMillis>0){
                    synchronized (this){
                        Health_AppLocation.instance.UpdateHeartTime = timeInMillis;
                        Submit(read,timeInMillis);
                    }
                }
            }

        }else if(intent.getIntExtra("TYPE",0) == 1){

        }else if(intent.getIntExtra("TYPE",0) == 2){

        }else if(intent.getIntExtra("TYPE",0) == 3){

        }else if(intent.getIntExtra("TYPE",0) == 5){

        }
    }



    /**
     * 提交测量数据
     */
    private void Submit(String read,long timeInMillis){
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("heartRate",read+"");
            jsonObject1.put("type",1+"");
//            if(timeInMillis>0){
//                jsonObject1.put("recordTime",timeInMillis+"");
//            }else{
//                jsonObject1.put("recordTime",System.currentTimeMillis()+"");
//            }
            jsonObject1.put("recordTime",timeInMillis+"");
//            jsonObject1.put("recordTime",System.currentTimeMillis()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostUtils().JsonPost(Constant.post_heart_rate,jsonObject1,submitHandler, Health_AppLocation.instance);
    }


    private Handler submitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 200){
                if (msg.obj!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){

                            Health_Log_Activity.isUploadGraphic = true;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Health_AppLocation.instance, context.getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Health_AppLocation.instance, context.getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Health_AppLocation.instance, context.getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
