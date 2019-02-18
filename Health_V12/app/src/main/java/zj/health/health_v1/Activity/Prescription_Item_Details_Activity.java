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
import zj.health.health_v1.Adapter.Prescription_Item_Details_Adapter;
import zj.health.health_v1.Adapter.Take_medicine_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.RemindCalendarModel;
import zj.health.health_v1.Model.ReminderListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Prescription_Item_Details_Activity extends BaseActivity implements View.OnClickListener{

    private RecyclerView layout_recy;
    private Prescr_Details_Adapter adapter;
    private EditText applyIllness_edit,Remarks_edit;
    private ImageView back;
    private TextView title;
    private Button add_Button,save_Button;
    private List<ReminderListModel> list = new ArrayList<>();
    private final int DRUGNAME_RESUTL = 0x112;
    private final int MEDICINE_REPEAT = 0x113;
    private Gson gson = new Gson();
    private String prescId;
    private String remark;
    private Handler CommitreminderHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        Toast.makeText(Prescription_Item_Details_Activity.this, R.string.commit_success, Toast.LENGTH_SHORT).show();
                        setResult(0x1);
                        finish();
                    }else{
                        Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Prescription_Item_Details_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
                        applyIllness_edit.setText(jsonObject1.optString("applyIllness"));
//                        Rea.setText(jsonObject1.optString("applyIllness"));
                        list = gson.fromJson(jsonObject1.optString("medicines"),new TypeToken<List<ReminderListModel>>(){}.getType());
                        for (int i = 0;i<list.size();i++){
                            list.get(i).setPrescId(prescId);
                        }
                        adapter.setViewList(list);
                    }else{
                        Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Prescription_Item_Details_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler DeletePrescriptionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        getPrescription(prescId);
                    }else{
                        Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Prescription_Item_Details_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler NewPrescriptionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        String id = jsonObject1.optString("id");
                        for (int i = 0;i<adapter.getlist().size();i++){
                            adapter.getlist().get(i).setPrescId(id);
                        }
                        String json = gson.toJson(adapter.getlist());
                        Commit(json);
                    }else{
                        Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Prescription_Item_Details_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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

                    }else{
                        Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Prescription_Item_Details_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Prescription_Item_Details_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
    }
    private void BindListener(){
        back.setOnClickListener(this);
        add_Button.setOnClickListener(this);
        save_Button.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.Prescription_Item_Details));
        prescId = getIntent().getStringExtra("id");
        remark = getIntent().getStringExtra("remark");

        if(!StringUtil.isEmpty(prescId)){
            getPrescription(prescId);
        }else{
            ReminderListModel reminderListModel = new ReminderListModel();
            reminderListModel.setPrescId(prescId);
            list.add(reminderListModel);
        }
        if(!StringUtil.isEmpty(remark)){
            Remarks_edit.setText(remark);
        }
        adapter = new Prescr_Details_Adapter(this,list);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                DeletePrescription(list.get(position).getId());
            }
        });
        layout_recy.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        layout_recy.setAdapter(adapter);
//        SendPresc();
    }

    /**
     * 添加或更新处方药物
     * @param json 参数
     */
    private void Commit(String json){
        new PostUtils().JsonPost(Constant.doctor_post_presc,json,CommitreminderHandler,this);
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
     * 医生删除处方建议下的某一药物
     * @param medicineId 药物ID
     */
    private void DeletePrescription(String medicineId){
        String json = "medicineId="+medicineId;
        new PostUtils().Delete(Constant.doctor_post_presc+json,true,DeletePrescriptionHandler,this);
    }

    /**
     * 新建处方建议
     * @param applyIllness 适用病
     * @param remark 备注
     */
    private void newPrescription(String applyIllness,String remark){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("applyIllness",applyIllness);
            jsonObject.put("remark",remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostUtils().JsonPost(Constant.getPrescription,jsonObject,NewPrescriptionHandler,this);
    }

    /**
     * 发送药方给用户
     */
    private void SendPresc(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",Health_AppLocation.instance.users.getId());
        map.put("prescId",prescId);
        new PostUtils().getNewPost(Constant.send_presc,map,SendHandler,this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add_Button:
                ReminderListModel reminderListModel = new ReminderListModel();
                reminderListModel.setPrescId(prescId);
                list.add(reminderListModel);
                adapter.setViewList(list);
                adapter.notifyDataSetChanged();
                break;
            case R.id.save_Button:
                boolean isCommit = false;
                for (int i = 0;i<list.size();i++){
                    if(StringUtil.isEmpty(list.get(i).getMedicineName())){
                        isCommit = false;
                        Toast.makeText(this, R.string.medicineName_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(StringUtil.isEmpty(list.get(i).getTimesOneDay())){
                        isCommit = false;
                        Toast.makeText(this, R.string.timesOneDay_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(StringUtil.isEmpty(list.get(i).getDosage())){
                        isCommit = false;
                        Toast.makeText(this, R.string.dosage_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(StringUtil.isEmpty(list.get(i).getTreatment())){
                        isCommit = false;
                        Toast.makeText(this, R.string.treatment_null_error, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(applyIllness_edit.getText().toString().length()<=0){
                        isCommit = false;
                        Toast.makeText(this, R.string.ed_applyIllness, Toast.LENGTH_SHORT).show();
                        break;
                    }else if(Remarks_edit.getText().toString().length()<=0){
                        isCommit = false;
                        Toast.makeText(this, R.string.input_Remarks, Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        isCommit = true;
                    }
                }

                if(isCommit){
                    if(StringUtil.isEmpty(prescId)){
                        newPrescription(applyIllness_edit.getText().toString(),Remarks_edit.getText().toString());
                    }else{
                        String json = gson.toJson(adapter.getlist());
                        Commit(json);
                    }

                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //填写药物名返回
        if(resultCode == DRUGNAME_RESUTL){
            int position = data.getIntExtra("position",0);
            String name = data.getStringExtra("name");
            list.get(position).setMedicineName(name);
            adapter.setViewList(list);
        }else if(resultCode == MEDICINE_REPEAT){
            Calendar calendar = Calendar.getInstance();

            int position = data.getIntExtra("position",0);
            String name = data.getStringExtra("name");
            list.get(position).setTreatment(name);
            list.get(position).setIntervalMode(1+"");
            list.get(position).setIntervalTemplate(1+"");
//            list.get(position).setStartTime(calendar.getTimeInMillis()+"");
//            if(name.equals("1")){
//                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+1,23,59);
//            }else if(name.equals("2")){
//                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+3,23,59);
//            }else if(name.equals("3")){
//                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)+7,23,59);
//            }else{
//                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+2,calendar.get(Calendar.DAY_OF_MONTH),23,59);
//            }
//            list.get(position).setEndTime(calendar.getTimeInMillis()+"");
            adapter.setViewList(list);
        }
    }
}
