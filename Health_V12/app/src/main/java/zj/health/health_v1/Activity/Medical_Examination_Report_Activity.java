package zj.health.health_v1.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.New_Fragment_PagerAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Fragment.Health_report_Fragment;
import zj.health.health_v1.Fragment.Report_Fragment;
import zj.health.health_v1.Fragment.Special_report_Fragment;
import zj.health.health_v1.Model.BodyModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/2.
 */

public class Medical_Examination_Report_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back,health_reprot_img,special_report_img,all_img,
            health_report_rectangle_img,special_report_rectangle_img;
    private TextView title,all_text,health_report_text,Special_report_text;
    private LinearLayout title_lin,report_lin;
    private ViewPager report_viewpager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private New_Fragment_PagerAdapter viewPager_adapter;
    private Report_Fragment report_fragment1 = new Report_Fragment();
    private Health_report_Fragment health_report_fragment = new Health_report_Fragment();
    private Special_report_Fragment special_report_fragment = new Special_report_Fragment();
    private DbUtils dbUtils = new DbUtils();
    private Gson gson = new Gson();
    private List<List<String>> sublists = new ArrayList<>();//体征子类list
    private List<String> parentList = new ArrayList<>();//体征父类list
    public String checkParent = "0";
    public String checkChild = "0";
    private OptionsPickerView optionsPickerView;
    private Handler config_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            Health_AppLocation.instance.bodyModelList = new Gson().
                                    fromJson(jsonObject.optString("data"),new TypeToken<List<BodyModel>>(){}.getType());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Medical_Examination_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Medical_Examination_Report_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Medical_Examination_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    public Handler uploadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    report_fragment1.uploadList();
                    health_report_fragment.uploadList();
                    special_report_fragment.uploadList();
                    break;
                case 1:
                    report_fragment1.uploadList();
                    health_report_fragment.uploadList();
                    break;
                case 2:
                    report_fragment1.uploadList();
                    special_report_fragment.uploadList();
                    break;
                    default:
                        break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_examination_report_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        health_reprot_img = (ImageView)findViewById(R.id.health_reprot_img);
        special_report_img = (ImageView)findViewById(R.id.special_report_img);
        all_img = (ImageView)findViewById(R.id.all_img);
        health_report_rectangle_img = (ImageView)findViewById(R.id.health_report_rectangle_img);
        special_report_rectangle_img = (ImageView)findViewById(R.id.special_report_rectangle_img);
        title = (TextView)findViewById(R.id.title);
        all_text = (TextView)findViewById(R.id.all_text);
        health_report_text = (TextView)findViewById(R.id.health_report_text);
        Special_report_text = (TextView)findViewById(R.id.Special_report_text);
        report_viewpager = (ViewPager)findViewById(R.id.report_viewpager);
        report_lin = (LinearLayout)findViewById(R.id.report_lin);
        title_lin = (LinearLayout)findViewById(R.id.title_lin);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        health_reprot_img.setOnClickListener(this);
        special_report_img.setOnClickListener(this);
        all_text.setOnClickListener(this);
        health_report_text.setOnClickListener(this);
        Special_report_text.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.Medical_Examinatoin_Report));
//        PostFilter();
        if(Health_AppLocation.instance.bodyModelList.size() == 0){
            GetConfig();
        }
        if(getIntent().getStringExtra("friendid")!=null){
            report_lin.setVisibility(View.GONE);
            title_lin.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putString("friendid",getIntent().getStringExtra("friendid"));
            report_fragment1.setArguments(bundle);
            health_report_fragment.setArguments(bundle);
            special_report_fragment.setArguments(bundle);
        }
        fragmentList.add(report_fragment1);
        fragmentList.add(health_report_fragment);
        fragmentList.add(special_report_fragment);
        viewPager_adapter = new New_Fragment_PagerAdapter(getSupportFragmentManager(),fragmentList);
        report_viewpager.setOffscreenPageLimit(3);
        report_viewpager.setAdapter(viewPager_adapter);
        report_viewpager.setCurrentItem(0);
        report_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        OnClickAllText();
                        break;
                    case 1:
                        OnClickhealth_report();
                        break;
                    case 2:
                        OnClickSpecial_report();
                        ShowPickView();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void ShowPickView(){
        if(optionsPickerView == null) {
            optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    checkParent = Health_AppLocation.instance.bodyModelList.get(options1).getId();
                    if(options2 == 0){
                       checkChild = "0";
                    }else{
                        checkChild = Health_AppLocation.instance.bodyModelList.get(options1).getSubItem().get(options2).getSid();
                    }

                    special_report_fragment.uploadList();
                    //OnClickSpecial_report();
                }
            }).setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setTitleText("类型选择")//标题
                    .setSubCalSize(18)//确定和取消文字大小
                    .setTitleSize(20)//标题文字大小
                    .setTitleColor(Color.BLACK)//标题文字颜色
                    .setSubmitColor(getResources().getColor(R.color.main_color))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.main_color))//取消按钮文字颜色
                    .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                    .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                    .setDividerColor(getResources().getColor(R.color.title_bottom_line))
                    .setTextColorCenter(getResources().getColor(R.color.main_color))//设置选中项的颜色
                    .setTextColorOut(getResources().getColor(R.color.line_btn))//设置没有被选中项的颜色
                    .setContentTextSize(18)//滚轮文字大小
                    .setLinkage(true)//设置是否联动，默认true
//                .setLabels("", "","")//设置选择的三级单位
                    .setCyclic(false, false, false)//循环与否
                    .setSelectOptions(1, 1, 1)  //设置默认选中项
                    .setOutSideCancelable(false)//点击外部dismiss default true
                    .build();

           if(parentList.size()==0 && sublists.size()==0){
               for (int i = 0; i < Health_AppLocation.instance.bodyModelList.size(); i++) {

                   parentList.add(Health_AppLocation.instance.bodyModelList.get(i).getName());

                   if(!Health_AppLocation.instance.bodyModelList.get(i).getSubItem().get(0).getSid().equals("0")){
                       BodyModel.subItem subItem  = new BodyModel.subItem();
                       subItem.setSid("0");
                       subItem.setName(getString(R.string.all));
                       Health_AppLocation.instance.bodyModelList.get(i).getSubItem().add(0,subItem);
                       List<String> sublist = new ArrayList<>();
                       for (int j = 0;j<Health_AppLocation.instance.bodyModelList.get(i).getSubItem().size();j++){
                           sublist.add(Health_AppLocation.instance.bodyModelList.get(i).getSubItem().get(j).getName());
                       }
                       sublists.add(sublist);
                   }else{
                       List<String> sublist = new ArrayList<>();
                       for (int j = 0;j<Health_AppLocation.instance.bodyModelList.get(i).getSubItem().size();j++){
                           sublist.add(Health_AppLocation.instance.bodyModelList.get(i).getSubItem().get(j).getName());
                       }
                       sublists.add(sublist);
                   }
               }
           }
           if(parentList.size()==0 && sublists.size()==0){
               Toast.makeText(this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
           }else{
               optionsPickerView.setPicker(parentList, sublists);
               optionsPickerView.show();
           }

        }else{
            if(parentList.size()==0 && sublists.size()==0){
                Toast.makeText(this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                optionsPickerView.setPicker(parentList, sublists);
                optionsPickerView.show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.health_reprot_img:
                Intent it = new Intent(this,Health_Report_Activity.class);
                startActivityForResult(it,0x1);
                break;
            case R.id.special_report_img:
                Intent specIntent = new Intent(this,Special_Report_Activity.class);
                startActivityForResult(specIntent,0x2);
                break;
            case R.id.all_text:
                OnClickAllText();
                break;
            case R.id.health_report_text:
                OnClickhealth_report();
                break;
            case R.id.Special_report_text:
                ShowPickView();
                OnClickSpecial_report();
                break;
        }
    }
    private void OnClickAllText(){
        all_img.setVisibility(View.VISIBLE);
        health_report_rectangle_img.setVisibility(View.GONE);
        special_report_rectangle_img.setVisibility(View.GONE);
        all_text.setTextColor(getResources().getColor(R.color.white));
        health_report_text.setTextColor(getResources().getColor(R.color.black));
        Special_report_text.setTextColor(getResources().getColor(R.color.black));
        report_viewpager.setCurrentItem(0);
    }
    private void OnClickhealth_report(){
        all_img.setVisibility(View.GONE);
        health_report_rectangle_img.setVisibility(View.VISIBLE);
        special_report_rectangle_img.setVisibility(View.GONE);
        all_text.setTextColor(getResources().getColor(R.color.black));
        health_report_text.setTextColor(getResources().getColor(R.color.white));
        Special_report_text.setTextColor(getResources().getColor(R.color.black));
        report_viewpager.setCurrentItem(1);
    }
    private void OnClickSpecial_report(){
        all_img.setVisibility(View.GONE);
        health_report_rectangle_img.setVisibility(View.GONE);
        special_report_rectangle_img.setVisibility(View.VISIBLE);
        all_text.setTextColor(getResources().getColor(R.color.black));
        health_report_text.setTextColor(getResources().getColor(R.color.black));
        Special_report_text.setTextColor(getResources().getColor(R.color.white));
        report_viewpager.setCurrentItem(2);
    }

    private void GetConfig(){
        new PostUtils().Get(Constant.get_report_config,true,config_handler,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0x1:
                if(resultCode == RESULT_OK){
                    health_report_fragment.isUpload = true;
                    report_fragment1.isUpload = true;
                    //special_report_fragment.isUpload = true;
//                    OnClickAllText();
                    health_report_fragment.uploadList();
                    report_fragment1.uploadList();
                }
                break;
            case 0x2:
                if(resultCode == 1){
                    health_report_fragment.isUpload = true;
                    report_fragment1.isUpload = true;
//                    OnClickAllText();
                    health_report_fragment.uploadList();
                    report_fragment1.uploadList();
                }
                break;
                default:
                    break;
        }
    }

}
