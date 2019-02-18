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

import zj.health.health_v1.Adapter.My_Patient_Adapter;
import zj.health.health_v1.Adapter.My_Wallet_Adapter;
import zj.health.health_v1.Adapter.SecondaryListAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/12.
 */

public class My_Patient_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;
    private List<SecondaryListAdapter.DataTree<String, String>> datas = new ArrayList<>();
    private RecyclerView patient_recy;
    private My_Patient_Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_patient_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        patient_recy = (RecyclerView)findViewById(R.id.patient_recy);
    }
    private void BindListener(){
        back.setOnClickListener(this);

    }
    private void setData(){
        title.setText(R.string.my_Patient);
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("1");
        list1.add("1");
        List<String> list2 = new ArrayList<>();
        list2.add("1");
        list2.add("2");
        list2.add("1");
        list2.add("2");
        list2.add("2");
        datas.add(new SecondaryListAdapter.DataTree<String, String>(getString(R.string.Sign_patient).toString(),
                list1));
        datas.add(new SecondaryListAdapter.DataTree<String, String>(getString(R.string.Health_counseling_patient).toString(),
                list2));
        adapter = new My_Patient_Adapter(this);
        adapter.setData(datas);
        patient_recy.setLayoutManager(new LinearLayoutManager(this));
        patient_recy.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
