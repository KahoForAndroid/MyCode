package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Free_Doctor_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Free_doctor_List_Activity extends BaseActivity implements View.OnClickListener{

    private RecyclerView free_doctor_recy;
    private ImageView back;
    private TextView title;
    private Free_Doctor_Adapter adapter = null;
    private List<String> list = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.free_doctor_list_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        free_doctor_recy = (RecyclerView)findViewById(R.id.free_doctor_recy);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);

    }
    private void BindListener(){
        back.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.free_doctor));
        list = new ArrayList<>();
        adapter = new Free_Doctor_Adapter(this,list);
        free_doctor_recy.setLayoutManager(new LinearLayoutManager(this));
        free_doctor_recy.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
