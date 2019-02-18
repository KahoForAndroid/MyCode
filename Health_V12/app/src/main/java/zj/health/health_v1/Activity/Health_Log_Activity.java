package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.New_Fragment_PagerAdapter;
import zj.health.health_v1.Adapter.Report_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Fragment.Blood_Sugar_ChartFragment;
import zj.health.health_v1.Fragment.Blood_pressure_ChartFragment;
import zj.health.health_v1.Fragment.ChartFragment;
import zj.health.health_v1.Fragment.Heart_Rate_ChartFragmrnt;
import zj.health.health_v1.Fragment.Temperature_ChartFragment;
import zj.health.health_v1.Fragment.Weight_ChartFragment;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.GraphicModel;
import zj.health.health_v1.MyView.CustomViewPager;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Health_Log_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;
    private final int UPLOADGRAPHIC = 0x3;
    private RelativeLayout friend_log_rela,data_rela,report_rela;
    private RecyclerView body_recycle;
    private CustomViewPager headlth_log_viewpager;
    private android.support.v4.app.FragmentManager fragmentManager;
    private Report_Adapter adapter;
    private List <String> typeList  = new ArrayList<>();
    private Intent it = null;
    private New_Fragment_PagerAdapter fragment_pagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private ChartFragment chartFragment = new ChartFragment();
    private Blood_pressure_ChartFragment blood_pressure_chartFragment = new Blood_pressure_ChartFragment();
    private Blood_Sugar_ChartFragment blood_sugar_chartFragment = new Blood_Sugar_ChartFragment();
    private Weight_ChartFragment weight_chartFragment = new Weight_ChartFragment();
    private Temperature_ChartFragment temperature_chartFragment = new Temperature_ChartFragment();
    private Heart_Rate_ChartFragmrnt heart_rate_chartFragmrnt = new Heart_Rate_ChartFragmrnt();
    public static boolean isUploadGraphic = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_log_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        friend_log_rela = (RelativeLayout) findViewById(R.id.friend_log_rela);
        data_rela = (RelativeLayout) findViewById(R.id.data_rela);
        report_rela = (RelativeLayout) findViewById(R.id.report_rela);
        body_recycle = (RecyclerView) findViewById(R.id.body_recycle);
        headlth_log_viewpager = (CustomViewPager) findViewById(R.id.headlth_log_viewpager);

    }

    private void BindListener(){
        back.setOnClickListener(this);
        data_rela.setOnClickListener(this);
        report_rela.setOnClickListener(this);
        friend_log_rela.setOnClickListener(this);

    }
    private void setData(){
        title.setText(R.string.health_log);
        headlth_log_viewpager.setScanScroll(false);
        gridLayoutManager = new GridLayoutManager(this,6);
        adapter = new Report_Adapter(this,typeList,mWidth/6);
        body_recycle.setLayoutManager(gridLayoutManager);
        body_recycle.setAdapter(adapter);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                headlth_log_viewpager.setCurrentItem(position);
                adapter.setclick(position);
                adapter.notifyDataSetChanged();
            }
        });

        typeList = Arrays.asList(getResources().getStringArray(R.array.body_type));
        fragmentList.add(blood_pressure_chartFragment);
        fragmentList.add(weight_chartFragment);
        fragmentList.add(temperature_chartFragment);
        fragmentList.add(heart_rate_chartFragmrnt);
        fragmentList.add(blood_sugar_chartFragment);
        gridLayoutManager.setSpanCount(typeList.size());
        fragmentManager = getSupportFragmentManager();
        fragment_pagerAdapter = new New_Fragment_PagerAdapter(fragmentManager,fragmentList);
        headlth_log_viewpager.setOffscreenPageLimit(2);
        headlth_log_viewpager.setAdapter(fragment_pagerAdapter);
        headlth_log_viewpager.setCurrentItem(0);
        adapter.setTypeList(typeList);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.data_rela:
                it = new Intent(this,BlueTooth_Data_Activity.class);
                startActivityForResult(it,0x112);
                break;
            case R.id.report_rela:
                it = new Intent(this,Medical_Examination_Report_Activity.class);
                startActivity(it);
                break;
            case R.id.friend_log_rela:
                it = new Intent(this,Friend_Log_Activity.class);
                startActivity(it);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                blood_pressure_chartFragment.onActivityResult(0x1,resultCode,new Intent());
                break;
            case 2:
                weight_chartFragment.onActivityResult(0x1,resultCode,new Intent());
                break;
            case 3:
                temperature_chartFragment.onActivityResult(0x1,resultCode,new Intent());
                break;
            case 4:
                heart_rate_chartFragmrnt.onActivityResult(0x1,resultCode,new Intent());
                break;
            case 5:
                blood_sugar_chartFragment.onActivityResult(0x1,resultCode,new Intent());
                break;
                default:
                    break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
