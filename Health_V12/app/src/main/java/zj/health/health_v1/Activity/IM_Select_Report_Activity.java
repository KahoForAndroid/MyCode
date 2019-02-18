package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.IM_select_report_Adapter;
import zj.health.health_v1.Adapter.NewBodyReportAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/9/13.
 */

public class IM_Select_Report_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title,send_text;
    private RecyclerView recyclerView = null;
    private IM_select_report_Adapter adapter = null;
    private Gson gson = new Gson();
    private List<NewReportModel> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_select_report_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        send_text = (TextView)findViewById(R.id.send_text);
        recyclerView = (RecyclerView)findViewById(R.id.report_recycle);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        send_text.setOnClickListener(this);
    }

    private void setData(){
        title.setText(getString(R.string.Medical_Examinatoin_Report));
        String json = getIntent().getStringExtra("data");
        list = gson.fromJson(json,new TypeToken<List<NewReportModel>>(){}.getType());
        if(list == null){
            list = new ArrayList<>();
        }
        adapter = new IM_select_report_Adapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send_text:
                List<NewReportModel> newReportModelList = new ArrayList<>();
                for (int i = 0;i<list.size();i++){
                    if(list.get(i).isChecked()){
                        newReportModelList.add(list.get(i));
                    }
                }
                if(newReportModelList.size() == 0){
                    Toast.makeText(this, R.string.at_least_one_report, Toast.LENGTH_SHORT).show();
                }else{
                    String json = new Gson().toJson(newReportModelList);
                    Intent it = new Intent();
                    it.putExtra("json",json);
                    setResult(0x4,it);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}

