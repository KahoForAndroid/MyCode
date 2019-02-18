package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Prescription_DoctorFragment_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.PrescModel;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/10/12.
 */

public class IM_Send_DrugtoUsers_Activity extends BaseActivity {

    private RecyclerView rescription_recy;
    private ImageView back;
    private TextView title;
    private EditText search_edit;
    private ImageView search_img;
    private String identify,json,prescID;
    private Prescription_DoctorFragment_Adapter adapter;
    private List<PrescModel> prescModelList = new ArrayList<>();
    private Handler prescHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (jsonObject.optString("msg").equals("success")) {
                        prescModelList = new Gson().fromJson(jsonObject.optString("data"), new TypeToken<List<PrescModel>>() {
                        }.getType());
                        prescModelList.add(new PrescModel());
                        adapter.setPrescModelList(prescModelList);
                    } else {
                        Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == Constant.UserErrorCode) {
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler DeleteprescHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (jsonObject.optString("msg").equals("success")) {
                        getPrescList();
                    } else {
                        Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == Constant.UserErrorCode) {
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler SendHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        Intent it = new Intent();
                        it.putExtra("json",json);
                        setResult(600,it);
                        finish();
                    }else{
                        Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler getPrescriptionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        json = jsonObject1.toString();
                        SendPresc(prescID);
                    }else{
                        Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(IM_Send_DrugtoUsers_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rescription_anage_fragment);
        initView();
        BindListener();
        setData();
    }

    private void initView() {
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        search_edit = (EditText) findViewById(R.id.search_edit);
        search_img = (ImageView) findViewById(R.id.search_img);
        rescription_recy = (RecyclerView) findViewById(R.id.rescription_recy);
    }

    private void BindListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        title.setText(getString(R.string.Prescription_management));
        identify = getIntent().getStringExtra("userid");
        adapter = new Prescription_DoctorFragment_Adapter(this, prescModelList,true);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                switch (view.getId()){
                    case R.id.delete_img:
                        String json = "prescId=" + prescModelList.get(position).getId();
                        DeletePresc(json);
                        break;
                    case R.id.send_to_user_button:
                        prescID = prescModelList.get(position).getId();
                        getPrescription(prescID);
                        break;
                        default:
                            break;
                }

            }
        });
        rescription_recy.setLayoutManager(new LinearLayoutManager(this));
        rescription_recy.setAdapter(adapter);
        getPrescList();
    }

    private void getPrescList() {
        new PostUtils().Get(Constant.doctor_presc_list, true, prescHandler, IM_Send_DrugtoUsers_Activity.this);
    }

    private void DeletePresc(String json) {
        new PostUtils().Delete(Constant.getPrescription + json, true, DeleteprescHandler, IM_Send_DrugtoUsers_Activity.this);
    }
    /**
     * 医生获取指定处方建议下的所有药物
     * @param prescid 处方ID
     */
    private void getPrescription(String prescid){
        String json = "prescId="+prescid;
        new PostUtils().Get(Constant.getPrescription+json,true,getPrescriptionHandler,this);
    }
    /**
     * 发送药方给用户
     */
    private void SendPresc(String id){
        Map<String,String> map = new HashMap<>();
        map.put("userId", identify);
        map.put("prescId",id);
        new PostUtils().getNewPost(Constant.send_presc,map,SendHandler,this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x1) {
            getPrescList();
        }

    }


}
