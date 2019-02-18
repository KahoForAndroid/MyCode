package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.NewBodyReportAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/9/12.
 */

public class IM_Check_Report_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;
    private RecyclerView recyclerView = null;
    private NewBodyReportAdapter adapter = null;
    private Gson gson = new Gson();
    private List<NewReportModel> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_check_report_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title) ;
        recyclerView = (RecyclerView)findViewById(R.id.report_recycle);
    }
    private void BindListener(){
        back.setOnClickListener(this);
    }

    private void setData(){
        title.setText(getString(R.string.Medical_Examinatoin_Report));
        String json = getIntent().getStringExtra("data");
        list = gson.fromJson(json,new TypeToken<List<NewReportModel>>(){}.getType());
        if(list == null){
            list = new ArrayList<>();
        }
        adapter = new NewBodyReportAdapter(this,list,false);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent it = new Intent(IM_Check_Report_Activity.this, IM_Report_defult_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("model",list.get(position));
                it.putExtra("bundle",bundle);
                startActivityForResult(it,0x11);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
