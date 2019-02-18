package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.BodyModel;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/13.
 */

public class Special_Report_Activity extends BaseActivity implements View.OnClickListener{

    private MyTextView head_textview,Vertebra_textview,body_fluid_textview,limb_textview;
    private ImageView back;
    private Intent it = null;
    private int ResultCode = 0;
    private Bundle bundle = null;
    private Handler config_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            Health_AppLocation.instance.bodyModelList = new Gson().
                                    fromJson(jsonObject.optString("data"),new TypeToken<List<BodyModel>>(){}.getType());
                            if(Health_AppLocation.instance.bodyModelList!=null){
                                for (int i = 0;i<Health_AppLocation.instance.bodyModelList.size();i++){
                                    if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("头部")){
                                        head_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                                    }else if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("身体/体液")){
                                        body_fluid_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                                    }else if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("脊椎")){
                                        Vertebra_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                                    }else if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("四肢")){
                                        limb_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Special_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Special_Report_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Special_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_report_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        head_textview = (MyTextView)findViewById(R.id.head_textview);
        Vertebra_textview = (MyTextView)findViewById(R.id.Vertebra_textview);
        body_fluid_textview = (MyTextView)findViewById(R.id.body_fluid_textview);
        limb_textview = (MyTextView)findViewById(R.id.limb_textview);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        head_textview.setOnClickListener(this);
        Vertebra_textview.setOnClickListener(this);
        body_fluid_textview.setOnClickListener(this);
        limb_textview.setOnClickListener(this);
    }
    private void setData(){
        if(Health_AppLocation.instance.bodyModelList.size()>0){
            for (int i = 0;i<Health_AppLocation.instance.bodyModelList.size();i++){
                if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("头部")){
                    head_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                }else if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("身体/体液")){
                    body_fluid_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                }else if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("脊椎")){
                    Vertebra_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                }else if(Health_AppLocation.instance.bodyModelList.get(i).getName().equals("四肢")){
                    limb_textview.ParentId = Health_AppLocation.instance.bodyModelList.get(i).getId();
                }
            }
        }else{
            GetConfig();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                if(ResultCode == 1){
                    setResult(ResultCode);
                }else{
                    setResult(0);
                }
                finish();
                break;
            case R.id.head_textview:

                it = new Intent(this,Head_Report_Activity.class);
                bundle = new Bundle();

                for (int i = 0 ;i<Health_AppLocation.instance.bodyModelList.size();i++){
                    if(Health_AppLocation.instance.bodyModelList.get(i).getId().equals(head_textview.ParentId)){
                        bundle.putSerializable("body",Health_AppLocation.instance.bodyModelList.get(i));
                        break;
                    }
                }
                it.putExtra("bundle",bundle);
                startActivityForResult(it,0x1);
                break;
            case R.id.Vertebra_textview:
                it = new Intent(this,Vertebra_Activity.class);
                bundle = new Bundle();

                for (int i = 0 ;i<Health_AppLocation.instance.bodyModelList.size();i++){
                    if(Health_AppLocation.instance.bodyModelList.get(i).getId().equals(Vertebra_textview.ParentId)){
                        bundle.putSerializable("body",Health_AppLocation.instance.bodyModelList.get(i));
                        break;
                    }
                }
                it.putExtra("bundle",bundle);
                startActivityForResult(it,0x1);
                break;
            case R.id.body_fluid_textview:
                it = new Intent(this,Body_Fluid_Activity.class);
                bundle = new Bundle();

                for (int i = 0 ;i<Health_AppLocation.instance.bodyModelList.size();i++){
                    if(Health_AppLocation.instance.bodyModelList.get(i).getId().equals(body_fluid_textview.ParentId)){
                        bundle.putSerializable("body",Health_AppLocation.instance.bodyModelList.get(i));
                        break;
                    }
                }
                it.putExtra("bundle",bundle);
                startActivityForResult(it,0x1);
                break;
            case R.id.limb_textview:
                it = new Intent(this,Limb_Report_Activity.class);
                bundle = new Bundle();

                for (int i = 0 ;i<Health_AppLocation.instance.bodyModelList.size();i++){
                    if(Health_AppLocation.instance.bodyModelList.get(i).getId().equals(limb_textview.ParentId)){
                        bundle.putSerializable("body",Health_AppLocation.instance.bodyModelList.get(i));
                        break;
                    }
                }
                it.putExtra("bundle",bundle);
                startActivityForResult(it,0x1);
                break;
        }
    }

    private void GetConfig(){
        new PostUtils().Get(Constant.get_report_config,true,config_handler,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            ResultCode = resultCode;
        }else{
            ResultCode = 0;
        }
    }

    @Override
    public void onBackPressed() {
        if(ResultCode == 1){
            setResult(ResultCode);
        }else{
            setResult(0);
        }
        finish();
    }
}
