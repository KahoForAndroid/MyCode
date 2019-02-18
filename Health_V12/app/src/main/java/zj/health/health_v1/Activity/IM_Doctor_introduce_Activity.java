package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Model.DoctorModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/11/27.
 */

public class IM_Doctor_introduce_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back,user_icon_img;
    private TextView Job_Title_text,username_text,work_address_text,Department_text,Be_good_at_text,introduction_text;
    private RequestOptions requestOptions ;
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
                        Glide.with(IM_Doctor_introduce_Activity.this).
                                load(Constant.photo_IP+doctorModel.getIconUrl()).
                                apply(requestOptions).
                                into(user_icon_img);
                        Job_Title_text.setText(doctorModel.getTitle());
                        Department_text.setText(doctorModel.getOffice());
                        work_address_text.setText(doctorModel.getHospital());
                        introduction_text.setText(StringUtil.trimNull(doctorModel.getIntroduction()));
                        Be_good_at_text.setText(StringUtil.trimNull(doctorModel.getSpeciality()));
                    }else{
                        Toast.makeText(IM_Doctor_introduce_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(IM_Doctor_introduce_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(IM_Doctor_introduce_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_doctor_introduce_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        user_icon_img = (ImageView) findViewById(R.id.user_icon_img);
        username_text = (TextView) findViewById(R.id.username_text);
        Job_Title_text = (TextView) findViewById(R.id.Job_Title_text);
        work_address_text = (TextView) findViewById(R.id.work_address_text);
        Department_text = (TextView) findViewById(R.id.Department_text);
        Be_good_at_text = (TextView) findViewById(R.id.Be_good_at_text);
        introduction_text = (TextView) findViewById(R.id.introduction_text);
    }
    private void BindListener(){
        back.setOnClickListener(this);
    }
    private void setData(){

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

        if(getIntent().getStringExtra("doctorId")!=null){
            getDoctorData(getIntent().getStringExtra("doctorId"));
        }
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
    private void getDoctorData(String doctorId){
        String json = "doctorId="+doctorId;
        new PostUtils().Get(Constant.getDoctorData+json,true,getDoctorData_Handler,this);
    }
}
