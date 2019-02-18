package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Drug_List_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Model.illnessModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/9/5.
 */

public class Drug_List_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title,save_Text;
    private RecyclerView drug_recy;
    private List<illnessModel> model = new ArrayList<>();//全部疾病列表
    private List<illnessModel> model_for_users = new ArrayList<>();//用户疾病列表
    private Drug_List_Adapter adapter;
    private StringBuffer stringBuffer = new StringBuffer();
    private Handler illnessHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                   if(jsonObject.optString("msg").equals("success")){
                       model = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<illnessModel>>(){}.getType());
                       adapter.setModel(model);

                   }else{
                       Toast.makeText(Drug_List_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Drug_List_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Drug_List_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler upDateillnessHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        Intent it = new Intent();
                        it.putExtra("drug",stringBuffer.toString());
                        it.putExtra("model_for_users", (Serializable) model_for_users);
                        setResult(RESULT_OK,it);
                        finish();

                    }else{
                        Toast.makeText(Drug_List_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Drug_List_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Drug_List_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_list_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        save_Text = (TextView)findViewById(R.id.save_Text);
        drug_recy = (RecyclerView)findViewById(R.id.drug_recy);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        save_Text.setOnClickListener(this);
    }
    private void setData(){
        title.setText(R.string.Illness_title);
        model = (List<illnessModel>) getIntent().getSerializableExtra("model");
        model_for_users = (List<illnessModel>) getIntent().getSerializableExtra("model_for_users");
        if(model == null ){
            model = new ArrayList<>();
            getList();
        }
        adapter = new Drug_List_Adapter(this,model,model_for_users);
        drug_recy.setLayoutManager(new LinearLayoutManager(this));
        drug_recy.setAdapter(adapter);

    }

    private void getList(){
        new PostUtils().Get(Constant.illness_List,true,illnessHandler,this);
    }
    private void updateIllness(List<illnessModel> model_for_users){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0;i<model_for_users.size();i++){
                jsonArray.put(Integer.parseInt(model_for_users.get(i).getId()));
            }
            jsonObject.put("illness",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new PostUtils().JsonPost(Constant.updateOrPost_illness,jsonObject,upDateillnessHandler,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save_Text:
                model_for_users.clear();
                stringBuffer = new StringBuffer();
                for (int i = 0;i<adapter.getModelList().size();i++){
                    if(adapter.getModelList().get(i).isChecked()){
                        stringBuffer.append(adapter.getModelList().get(i).getDesc()+",");
                        model_for_users.add(adapter.getModelList().get(i));
                    }
                }
                if(model_for_users.size() == 0){
                    updateIllness(model_for_users);
                }else{

                    updateIllness(model_for_users);
                }

                break;
        }
    }
}
