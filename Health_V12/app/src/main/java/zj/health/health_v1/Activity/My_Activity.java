package zj.health.health_v1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Main_bbs_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.LiveMode;
import zj.health.health_v1.Model.SportModes;
import zj.health.health_v1.Model.UserInfo;
import zj.health.health_v1.Model.UserMessage;
import zj.health.health_v1.Model.Users;
import zj.health.health_v1.Model.illnessModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.GreenDaoManager;
import zj.health.health_v1.Utils.HttpUtil;
import zj.health.health_v1.Utils.ImageUtils;
import zj.health.health_v1.Utils.SharedPreferencesUtils;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * 个人信息页面
 * Created by Administrator on 2018/4/10.
 */

public class My_Activity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout certification_rela,wallet_rela,setting_rela,user_message_rela,life_rela,sport_rela,drug_rela,history_im_rela;
    private Intent it = null;
    private ImageView back,user_logo;
    private TextView title,username_text,life_mode_text,sport_mode_text,sex_text,height_text,weight_text,drug_text;
    private Gson gson = new Gson();
    private DbUtils dbUtils = new DbUtils();
    private List<LiveMode> liveModeList;
    private List<SportModes> sportModesList;
    private OptionsPickerView optionsPickerView;
    private List<String> sportStringList = new ArrayList<>();
    private List<String> liveStringList = new ArrayList<>();
    private List<illnessModel> model = new ArrayList<>();
    private List<illnessModel> model_for_users = new ArrayList<>();
    private List<Integer> idList;//用户疾病ID
    private boolean updateMessageData;
    private boolean updateData;
    private RelativeLayout share_rela;
    private RequestOptions requestOptions = new RequestOptions()
            .placeholder(R.drawable.icon_head) //加载中图片
            .error(R.drawable.icon_head) //加载失败图片
            .fallback(R.drawable.icon_head) //url为空图片
            .centerCrop()
            .transform(new GlideCircleTransform(this))
            // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略

    private Handler SportHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            sportModesList = gson.fromJson(jsonObject.optString("data"),
                                    new TypeToken<List<SportModes>>(){}.getType());
                            dbUtils.insertSportListData(My_Activity.this,sportModesList);
                            for (int i = 0;i<sportModesList.size();i++){
                                sportStringList.add(sportModesList.get(i).getDesc());
                            }
                            if(Health_AppLocation.instance.userMessage.getSportMode().equals("0")){
                                Health_AppLocation.instance.userMessage.setSportMode("1");
                                sport_mode_text.setText(sportStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getSportMode())-1));
                            }else{
                                sport_mode_text.setText(sportStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getSportMode())-1));
                            }
                        }else {
                            Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }}
    };

    private Handler LiveHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            liveModeList = gson.fromJson(jsonObject.optString("data"),
                                    new TypeToken<List<LiveMode>>(){}.getType());
                            dbUtils.insertLiveListData(My_Activity.this,liveModeList);

                            for (int i = 0;i<liveModeList.size();i++){
                                liveStringList.add(liveModeList.get(i).getDesc());
                            }
                            if(Health_AppLocation.instance.userMessage.getLiveMode().equals("0")){
                                Health_AppLocation.instance.userMessage.setLiveMode("2");
                                life_mode_text.setText(liveStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getLiveMode())-1));
                            }else{
                                life_mode_text.setText(liveStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getLiveMode())-1));
                            }
                        }else {
                            Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }}
    };
    private Handler dbSportHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sportModesList = (List<SportModes>) msg.obj;
            if(sportModesList == null || sportModesList.size() == 0){
                getSportMode();
            }else{
                for (int i = 0;i<sportModesList.size();i++){
                    sportStringList.add(sportModesList.get(i).getDesc());
                }
                if(Health_AppLocation.instance.userMessage.getSportMode().equals("0")){
                    Health_AppLocation.instance.userMessage.setSportMode("1");
                    sport_mode_text.setText(sportStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getSportMode())-1));
                }else{
                    sport_mode_text.setText(sportStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getSportMode())-1));
                }
            }
        }
    };
    private Handler dbliveHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            liveModeList = (List<LiveMode>) msg.obj;
            if(liveModeList == null || liveModeList.size() == 0){
                getLiveMode();
            }else{
                for (int i = 0;i<liveModeList.size();i++){
                    liveStringList.add(liveModeList.get(i).getDesc());
                }
                if(Health_AppLocation.instance.userMessage.getLiveMode().equals("0")){
                    Health_AppLocation.instance.userMessage.setLiveMode("2");
                    life_mode_text.setText(liveStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getLiveMode())-1));
                }else{
                    life_mode_text.setText(liveStringList.get(Integer.parseInt(Health_AppLocation.instance.userMessage.getLiveMode())-1));
                }
            }
        }
    };
    private Handler userHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            Health_AppLocation.instance.userMessage = gson.fromJson(jsonObject.optString("data"), UserMessage.class);
                            if(Health_AppLocation.instance.userMessage.getSex().equals("1")){
                                sex_text.setText(R.string.man);
                            }else{
                                sex_text.setText(R.string.woman);
                            }
                            height_text.setText(Health_AppLocation.instance.userMessage.getHeight()+"cm");
                            weight_text.setText(Health_AppLocation.instance.userMessage.getWeight()+"kg");

                            dbUtils.getLiveModeDataList(dbliveHandler);
                            dbUtils.getSportModeDataList(dbSportHandler);
                        }else {
                            Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }}
    };


    private Handler UpdateUserMessage_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            LoadingDialog.getLoadingDialog().StopLoadingDialog();
                            finish();
                        }else {
                            Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                            LoadingDialog.getLoadingDialog().StopLoadingDialog();
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{

                    Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                    finish();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                finish();
            }else{
                Toast.makeText(My_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                finish();
            }}
    };


    private Handler illnessHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        model = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<illnessModel>>(){}.getType());
//                        adapter.setModel(model);
                        getUserillness();

                    }else{
                        Toast.makeText(My_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler UserillnessHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                         if(model!=null && model.size()>0){
//
                            StringBuffer stringBuffer = new StringBuffer();
//
                             model_for_users = new Gson().
                                    fromJson(jsonObject.optString("data"),new TypeToken<List<illnessModel>>(){}.getType());
                             for (int i  =0;i<model_for_users.size();i++){
                                 stringBuffer.append(model_for_users.get(i).getDesc()+",");
                                }
                             drug_text.setText(stringBuffer.toString()+"");
                            }


                    }else{
                        Toast.makeText(My_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        certification_rela = (RelativeLayout)findViewById(R.id.certification_rela);
        wallet_rela = (RelativeLayout)findViewById(R.id.wallet_rela);
        setting_rela = (RelativeLayout)findViewById(R.id.setting_rela);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        username_text = (TextView)findViewById(R.id.username_text);
        user_logo = (ImageView)findViewById(R.id.user_logo);
        user_message_rela = (RelativeLayout)findViewById(R.id.user_message_rela);
        life_mode_text = (TextView)findViewById(R.id.life_mode_text);
        sport_mode_text = (TextView)findViewById(R.id.sport_mode_text);
        life_rela = (RelativeLayout)findViewById(R.id.life_rela);
        sport_rela = (RelativeLayout)findViewById(R.id.sport_rela);
        sex_text = (TextView)findViewById(R.id.sex_text);
        height_text = (TextView)findViewById(R.id.height_text);
        weight_text = (TextView)findViewById(R.id.weight_text);
        drug_rela = (RelativeLayout)findViewById(R.id.drug_rela);
        drug_text = (TextView)findViewById(R.id.drug_text);
        share_rela = (RelativeLayout)findViewById(R.id.share_rela);
        history_im_rela = (RelativeLayout)findViewById(R.id.history_im_rela);
    }
    private void BindListener(){
        certification_rela.setOnClickListener(this);
        wallet_rela.setOnClickListener(this);
        setting_rela.setOnClickListener(this);
        back.setOnClickListener(this);
        user_message_rela.setOnClickListener(this);
        life_rela.setOnClickListener(this);
        sport_rela.setOnClickListener(this);
        drug_rela.setOnClickListener(this);
        share_rela.setOnClickListener(this);
        history_im_rela.setOnClickListener(this);
    }
    private void setData(){
        title.setText(R.string.peopleMessgae);
        username_text.setText(Health_AppLocation.instance.users.getNickname());
        Glide.with(this).
                load(Health_AppLocation.instance.Icon).apply(requestOptions).
                into(user_logo);

        getUserData();
        getillness_List();

    }
    private void getSportMode(){
        new PostUtils().Get(Constant.sport_mode,true,SportHandler,this);
    }
    private void getLiveMode(){
        new PostUtils().Get(Constant.live_mode,true,LiveHandler,this);
    }
    private void getUserData(){
        new PostUtils().Get(Constant.getUserData,true,userHandler,this);
    }
    private void getillness_List(){
        new PostUtils().Get(Constant.illness_List,true,illnessHandler,this);
    }
    private void getUserillness(){
        new PostUtils().Get(Constant.updateOrPost_illness,true,UserillnessHandler,this);
    }
    private void ShowModePickView(final List<String> stringList, final View view){
        optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(view instanceof TextView){
                    updateMessageData = true;
                    ((TextView) view).setText(stringList.get(options1));
                        if(view.getId() == R.id.life_mode_text){
                            Health_AppLocation.instance.userMessage.setLiveMode((options1+1)+"");
                        }else{
                            Health_AppLocation.instance.userMessage.setSportMode((options1+1)+"");
                        }
                }
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择")//标题
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

        optionsPickerView.setPicker(stringList);
        optionsPickerView.show();
    }

    private void updateUserMessgae(){
        LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
        String json = gson.toJson(Health_AppLocation.instance.userMessage);
        new PostUtils().JsonPost(Constant.updateData,json,UpdateUserMessage_Handler,this);
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.certification_rela:
                it = new Intent(this,RegisterDoctor_Supplement_Activity.class);
                startActivity(it);
                break;
            case R.id.wallet_rela:
                it = new Intent(this,My_Wallet_Activity.class);
                startActivity(it);
                break;
            case R.id.setting_rela:
                it = new Intent(this,Setting_Activity.class);
                startActivity(it);
                break;
            case R.id.back:
                if(updateData || updateMessageData){
                    updateUserMessgae();
                }else{
                    finish();
                }
                break;
            case R.id.user_message_rela:
                it = new Intent(this,UserMessage_Activity .class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userMessgae",Health_AppLocation.instance.userMessage);
                it.putExtra("bundle",bundle);
                startActivityForResult(it,0x1);
                break;
            case R.id.life_rela:
                ShowModePickView(liveStringList,life_mode_text);
                break;
            case R.id.sport_rela:
                ShowModePickView(sportStringList,sport_mode_text);
                break;
            case R.id.drug_rela:
                it = new Intent(this,Drug_List_Activity.class);
                it.putExtra("model_for_users", (Serializable) model_for_users);
                it.putExtra("model", (Serializable) model);
                startActivityForResult(it,0x2);
                break;
            case R.id.share_rela:
               new CreateDialog().Share_Dailog(this,
                       android.R.style.Animation_Toast);
                break;
            case R.id.history_im_rela:
                it = new Intent(this,History_IM_Activity.class);
                startActivity(it);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0x1:
                if(resultCode == RESULT_OK){
                    updateData = data.getBooleanExtra("updateData",false);
                    if(updateData){
                        Health_AppLocation.instance.userMessage = (UserMessage) data.getBundleExtra("bundle").
                                getSerializable("userMessage");
                        if(Health_AppLocation.instance.userMessage.getSex().equals("1")){
                            sex_text.setText(R.string.man);
                        }else{
                            sex_text.setText(R.string.woman);
                        }
                        height_text.setText(Health_AppLocation.instance.userMessage.getHeight()+"cm");
                        weight_text.setText(Health_AppLocation.instance.userMessage.getWeight()+"kg");
                    }
                    if(data.getBooleanExtra("updateIcon",false)){
                        Glide.with(this).
                                load(Health_AppLocation.instance.Icon).apply(requestOptions).
                                into(user_logo);
                    }
                }
                break;
            case 0x2:
                if(resultCode == RESULT_OK){
                    String text = data.getStringExtra("drug");
                    drug_text.setText(text+"");
                    model_for_users = (List<illnessModel>) data.getSerializableExtra("model_for_users");
                }
                break;
        }

    }

    public void Share(int scene){
        com.tencent.mm.sdk.modelmsg.WXWebpageObject wxWebpageObject = new com.tencent.mm.sdk.modelmsg.WXWebpageObject();
        wxWebpageObject.webpageUrl = "http://www.lzkzh.com/index.html";
        com.tencent.mm.sdk.modelmsg.WXMediaMessage mediaMessage = new com.tencent.mm.sdk.modelmsg.WXMediaMessage(wxWebpageObject);
        mediaMessage.description  = "推荐码"+Health_AppLocation.instance.users.getReferralCode();
        mediaMessage.title = "乐众康";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo_icon);
        mediaMessage.thumbData = new ImageUtils().bmpToByteArray(bitmap,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webUrl";
        req.message = mediaMessage;
        req.scene = scene;
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this,Constant.WeChat_ShareID,true);
        iwxapi.registerApp(Constant.WeChat_ShareID);
        iwxapi.sendReq(req);

    }

    @Override
    public void onBackPressed() {
        if(updateData || updateMessageData){
            updateUserMessgae();
        }else{
            finish();
        }
    }
}
