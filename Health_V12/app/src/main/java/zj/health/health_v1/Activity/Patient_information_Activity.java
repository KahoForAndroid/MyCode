package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zj.health.health_v1.Adapter.ImgAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.Model.DoctorModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/9/6.
 */

public class Patient_information_Activity extends BaseActivity implements View.OnClickListener{

    private ConsultListModel consultListModel = new ConsultListModel();
    private ImageView back,user_icon_img;
    private TextView username_text,age_text,height_text,weight_text,
            life_mode_text,sport_mode_text,Illness_text,Description_text;
    private ImgAdapter adapter;
    private Button next_btn;
    private boolean update = false;
    private boolean isDoctor = false;
    private RequestOptions requestOptions ;
    private RecyclerView img_recy;
    private Handler invite_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        next_btn.setBackground(getResources().getDrawable(R.drawable.gary_button));
                        update = true;
                        Toast.makeText(Patient_information_Activity.this, R.string.Invitation_to_user, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Patient_information_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Patient_information_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Patient_information_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler getDoctorData_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        DoctorModel doctorModel = new Gson().fromJson(jsonObject1.optString("data"),DoctorModel.class);
                        username_text.setText(doctorModel.getNickname());
                        Glide.with(Patient_information_Activity.this).
                                load(Constant.photo_IP+doctorModel.getIconUrl()).
                                apply(requestOptions).
                                into(user_icon_img);
                    }else{
                        Toast.makeText(Patient_information_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Patient_information_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Patient_information_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_information_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        user_icon_img = (ImageView) findViewById(R.id.user_icon_img);
        username_text = (TextView) findViewById(R.id.username_text);
        age_text = (TextView) findViewById(R.id.age_text);
        height_text = (TextView) findViewById(R.id.height_text);
        weight_text = (TextView) findViewById(R.id.weight_text);
        life_mode_text = (TextView) findViewById(R.id.life_mode_text);
        sport_mode_text = (TextView) findViewById(R.id.sport_mode_text);
        Illness_text = (TextView) findViewById(R.id.Illness_text);
        Description_text = (TextView) findViewById(R.id.Description_text);
        next_btn = (Button) findViewById(R.id.next_btn);
        img_recy = (RecyclerView)findViewById(R.id.img_recy);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        next_btn.setOnClickListener(this);
    }
    private void setData(){


          if(getIntent().getBundleExtra("bundle")!= null) {
            consultListModel = (ConsultListModel) getIntent().
                    getBundleExtra("bundle").getSerializable("model");

            if(getIntent().getBooleanExtra("isChat",false)){
                next_btn.setVisibility(View.GONE);
            }
            username_text.setText(consultListModel.getNickname());
            age_text.setText(consultListModel.getAge() + "岁");
            height_text.setText(consultListModel.getHeight() + "cm");
            weight_text.setText(consultListModel.getWeight() + "kg");
            life_mode_text.setText(StringUtil.trimNull(consultListModel.getLiveMode()));
            sport_mode_text.setText(StringUtil.trimNull(consultListModel.getSportMode()));
            Description_text.setText(StringUtil.trimNull(consultListModel.getIllnessDesc()));

            if (consultListModel.getIllness().size() > 0) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < consultListModel.getIllness().size(); i++) {
                    stringBuffer.append(consultListModel.getIllness().get(i).getDesc() + ",");
                }
                Illness_text.setText(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
            } else {
                Illness_text.setText(getString(R.string.user_Illness_null));
            }

            switch (Integer.parseInt(consultListModel.getStatus())) {
                case 1:
                    next_btn.setBackground(getResources().getDrawable(R.drawable.main_button));
                    break;
                case 2:
                    next_btn.setBackground(getResources().getDrawable(R.drawable.gary_button));
                    break;
                case 3:
                    next_btn.setVisibility(View.GONE);
                    break;

                }
            }

        img_recy.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new ImgAdapter(this,consultListModel.getImages());
        img_recy.setAdapter(adapter);

        requestOptions = new RequestOptions()
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
        Glide.with(this).
                load(Constant.photo_IP+consultListModel.getIconUrl()).
                apply(requestOptions).
                into(user_icon_img);


    }
    private void getConsultInvite(String consultId){
        Map<String,String> map = new HashMap<>();
        map.put("consultId",consultId);
        new PostUtils().getNewPost(Constant.getConsultInvite,map,invite_Handler,this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                if(update){
                    setResult(0x111);
                }
                finish();
                break;
            case R.id.next_btn:
                if(Integer.parseInt(consultListModel.getStatus())!=2){
                    getConsultInvite(consultListModel.getConsultId());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(update){
            setResult(0x111);
        }
        finish();
    }
}
