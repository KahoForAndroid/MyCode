package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Prescr_Details_Adapter;
import zj.health.health_v1.Adapter.Prescr_Users_Details_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/10/11.
 */

public class PrescriptionUsers_Item_Details_Activity extends BaseActivity implements View.OnClickListener{

    private RecyclerView layout_recy;
    private Prescr_Users_Details_Adapter adapter;
    private EditText applyIllness_edit,Remarks_edit;
    private ImageView back;
    private TextView title;
    private Button add_Button,save_Button;
    private List<ReminderListModel> list = new ArrayList<>();
    private Gson gson = new Gson();
    private String suggestionId;


    private Handler suggestionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        applyIllness_edit.setText(jsonObject1.optString("applyIllness"));
                        Remarks_edit.setText(jsonObject1.optString("remark"));
                        applyIllness_edit.setFocusable(false);
                        Remarks_edit.setFocusable(false);
                        list = gson.fromJson(jsonObject1.optString("medicines"),new TypeToken<List<ReminderListModel>>(){}.getType());
                        adapter.setViewList(list);
                    }else{
                        Toast.makeText(PrescriptionUsers_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PrescriptionUsers_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(PrescriptionUsers_Item_Details_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PrescriptionUsers_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_item_details_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        layout_recy = (RecyclerView)findViewById(R.id.layout_recy);
        add_Button = (Button)findViewById(R.id.add_Button);
        save_Button = (Button)findViewById(R.id.save_Button);
        applyIllness_edit = (EditText)findViewById(R.id.applyIllness_edit);
        Remarks_edit = (EditText)findViewById(R.id.Remarks_edit);
        add_Button.setVisibility(View.GONE);
        save_Button.setVisibility(View.GONE);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        add_Button.setOnClickListener(this);
        save_Button.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.Prescription_Item_Details));
        suggestionId = getIntent().getStringExtra("suggestionId");

        adapter = new Prescr_Users_Details_Adapter(this,list);

        layout_recy.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        layout_recy.setAdapter(adapter);
//        SendPresc();
        if(StringUtil.isEmpty(getIntent().getStringExtra("json"))){
            getPrescDetail();
        }else{
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = new JSONObject(getIntent().getStringExtra("json"));
                applyIllness_edit.setText(jsonObject1.optString("applyIllness"));
                Remarks_edit.setText(jsonObject1.optString("remark"));
                applyIllness_edit.setFocusable(false);
                Remarks_edit.setFocusable(false);
                list = gson.fromJson(jsonObject1.optString("medicines"),new TypeToken<List<ReminderListModel>>(){}.getType());
                adapter.setViewList(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void getPrescDetail(){
        String json = "suggestionId="+suggestionId;
        new PostUtils().Get(Constant.getUserSuggestionDetail+json,true,suggestionHandler,this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
         default:
             break;
    }
    }

}

