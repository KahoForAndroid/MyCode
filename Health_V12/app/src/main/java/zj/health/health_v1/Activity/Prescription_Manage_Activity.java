package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Consultation_Room_Adapter;
import zj.health.health_v1.Adapter.New_Fragment_PagerAdapter;
import zj.health.health_v1.Adapter.ViewPager_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Fragment.Consultation_Setting_Fragment;
import zj.health.health_v1.Fragment.Prescription_Fragment;
import zj.health.health_v1.Fragment.Prescription_Manage_Fragment;
import zj.health.health_v1.Fragment.Question_Fragment;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Prescription_Manage_Activity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ImageView back;
    private TextView title;
    private RelativeLayout Prescription_Manage_rela,Quick_question_rela,Consultation_setting_rela;
    private View right_bottom_view,center_bottom_view,all_bottom_view;
    private ViewPager viewPager;
    private New_Fragment_PagerAdapter adapter;
    private List<Fragment> fragmentList;
    private Prescription_Manage_Fragment prescription_manage_fragment = new Prescription_Manage_Fragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_manager_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        Prescription_Manage_rela = (RelativeLayout)findViewById(R.id.Prescription_Manage_rela);
        Quick_question_rela = (RelativeLayout)findViewById(R.id.Quick_question_rela);
        Consultation_setting_rela = (RelativeLayout)findViewById(R.id.Consultation_setting_rela);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        right_bottom_view = (View)findViewById(R.id.right_bottom_view);
        center_bottom_view = (View)findViewById(R.id.center_bottom_view);
        all_bottom_view = (View)findViewById(R.id.all_bottom_view);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        Prescription_Manage_rela.setOnClickListener(this);
        Quick_question_rela.setOnClickListener(this);
        Consultation_setting_rela.setOnClickListener(this);

    }
    private void setData(){
        title.setText( R.string.archives_Manage);
        fragmentList = new ArrayList<>();
        fragmentList.add(prescription_manage_fragment);
//        fragmentList.add(new Question_Fragment());
        fragmentList.add(new Consultation_Setting_Fragment());
        adapter = new New_Fragment_PagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.Prescription_Manage_rela:


                all_bottom_view.setVisibility(View.VISIBLE);
                center_bottom_view.setVisibility(View.GONE);
                right_bottom_view.setVisibility(View.GONE);

                viewPager.setCurrentItem(0);

                break;
            case R.id.Quick_question_rela:



                all_bottom_view.setVisibility(View.GONE);
                center_bottom_view.setVisibility(View.VISIBLE);
                right_bottom_view.setVisibility(View.GONE);

                viewPager.setCurrentItem(1);

                break;
            case R.id.Consultation_setting_rela:


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
                viewPager.setCurrentItem(0);
                break;
//            case 1:
//                all_bottom_view.setVisibility(View.GONE);
//                center_bottom_view.setVisibility(View.VISIBLE);
//                right_bottom_view.setVisibility(View.GONE);
//                viewPager.setCurrentItem(1);
//                break;
            case 1:
                all_bottom_view.setVisibility(View.GONE);
                center_bottom_view.setVisibility(View.GONE);
                right_bottom_view.setVisibility(View.VISIBLE);

                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0x1){
            prescription_manage_fragment.onActivityResult(1,0x1,new Intent());
        }
    }
}
