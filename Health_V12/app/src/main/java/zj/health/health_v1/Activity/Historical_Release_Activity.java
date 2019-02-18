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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Health_history_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ConsultModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/17.
 */

public class Historical_Release_Activity extends BaseActivity{

    private RecyclerView history_recy;
    private Health_history_Adapter adapter;
    private TextView title;
    private ImageView back;
    private List<ConsultModel> consultModelList = new ArrayList<>();
    private Handler hissory_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        consultModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<ConsultModel>>(){}.getType());
                        if(consultModelList!=null && consultModelList.size()>0){
                            adapter.setConsultModelList(consultModelList);
                        }else{
                            Toast.makeText(Historical_Release_Activity.this, R.string.history_null, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Historical_Release_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Historical_Release_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Historical_Release_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historical_release_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        history_recy = (RecyclerView)findViewById(R.id.history_recy);
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void setData(){
        title.setText(getString(R.string.history_release));
        adapter = new Health_history_Adapter(this,consultModelList);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent it = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",consultModelList.get(position));
                it.putExtra("bundle",bundle);
                setResult(0x1,it);
                finish();
            }
        });
        history_recy.setLayoutManager(new LinearLayoutManager(this));
        history_recy.setAdapter(adapter);

        getConsultList();
    }
    private void getConsultList(){
        new PostUtils().Get(Constant.consult_history,true,hissory_handler,this);
    }
}
