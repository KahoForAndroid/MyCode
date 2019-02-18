package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.New_Fragment_PagerAdapter;
import zj.health.health_v1.Adapter.ViewPager_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Fragment.Health_Prescription_Fragment;
import zj.health.health_v1.Fragment.Prescription_Fragment;
import zj.health.health_v1.Fragment.Prescription_Manage_Fragment;
import zj.health.health_v1.Fragment.Question_Fragment;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/11.
 */

public class Prescription_Activity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ImageView back;
    private TextView title,all_text,Family_doctor_text,Health_counseling_text;
    private RelativeLayout all_rela,Family_doctor_rela,Health_counseling_rela;
    private View right_bottom_view,center_bottom_view,all_bottom_view;
    private ViewPager viewPager;
    private New_Fragment_PagerAdapter adapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        all_text = (TextView)findViewById(R.id.all_text);
        Family_doctor_text = (TextView)findViewById(R.id.Family_doctor_text);
        Health_counseling_text = (TextView)findViewById(R.id.Health_counseling_text);
        all_rela = (RelativeLayout)findViewById(R.id.all_rela);
        Family_doctor_rela = (RelativeLayout)findViewById(R.id.Family_doctor_rela);
        Health_counseling_rela = (RelativeLayout)findViewById(R.id.Health_counseling_rela);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        right_bottom_view = (View)findViewById(R.id.right_bottom_view);
        center_bottom_view = (View)findViewById(R.id.center_bottom_view);
        all_bottom_view = (View)findViewById(R.id.all_bottom_view);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        all_rela.setOnClickListener(this);
        Family_doctor_rela.setOnClickListener(this);
        Health_counseling_rela.setOnClickListener(this);

    }
    private void setData(){
        title.setText(R.string.my_prescription);
        fragmentList = new ArrayList<>();
        fragmentList.add(new Prescription_Fragment());
//        fragmentList.add(new Prescription_Fragment());
        fragmentList.add(new Health_Prescription_Fragment());
        adapter = new New_Fragment_PagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.all_rela:


                all_bottom_view.setVisibility(View.VISIBLE);
                center_bottom_view.setVisibility(View.GONE);
                right_bottom_view.setVisibility(View.GONE);

                viewPager.setCurrentItem(0);

                break;
//            case R.id.Family_doctor_rela:
//
//
//
//                all_bottom_view.setVisibility(View.GONE);
//                center_bottom_view.setVisibility(View.VISIBLE);
//                right_bottom_view.setVisibility(View.GONE);
//
//                viewPager.setCurrentItem(1);
//
//                break;
            case R.id.Health_counseling_rela:


                all_bottom_view.setVisibility(View.GONE);
                center_bottom_view.setVisibility(View.GONE);
                right_bottom_view.setVisibility(View.VISIBLE);

                viewPager.setCurrentItem(1);

                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:


                all_bottom_view.setVisibility(View.VISIBLE);
                center_bottom_view.setVisibility(View.GONE);
                right_bottom_view.setVisibility(View.GONE);
                break;
//            case 1:
//
//
//
//                all_bottom_view.setVisibility(View.GONE);
//                center_bottom_view.setVisibility(View.VISIBLE);
//                right_bottom_view.setVisibility(View.GONE);
//
//                break;
            case 1:



                all_bottom_view.setVisibility(View.GONE);
                center_bottom_view.setVisibility(View.GONE);
                right_bottom_view.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
