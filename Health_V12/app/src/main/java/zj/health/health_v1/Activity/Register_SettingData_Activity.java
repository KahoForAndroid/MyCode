package zj.health.health_v1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.SplashActivity;
import zj.health.health_v1.Model.AllCheckFilterModel;
import zj.health.health_v1.Model.Contacts;
import zj.health.health_v1.Model.SecurityModel;
import zj.health.health_v1.Model.Users;
import zj.health.health_v1.MyView.SwitchView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.FileUtils;
import zj.health.health_v1.Utils.ImageUtils;
import zj.health.health_v1.Utils.PermisionUtils;
import zj.health.health_v1.Utils.SharedPreferencesUtils;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/3.
 */

public class Register_SettingData_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back,icon_head,select_sex_img;
    private LinearLayout switch_icon_head_lin;
    private EditText nick_name_ed;
    private TextView birth_ed,man_text,woman_text,region_Text;
    private SwitchView switch_view;
    private Button next_btn;
    private Intent it = null;
    private boolean IsswitchViewClick = false;

    private ImageUtils imageUtils;
    private FileOutputStream outputStream = null;
    private final int compressIndex = 60;
    private Bitmap bitmap = null;
    private List<LocalMedia> selectList;
    private TimePickerView timePicker = null;
    private Calendar startDate;
    private boolean isSelectPhoto = false;
    private String birthYear = null;
    private String phone = null;
    private String question1,answer1,question2,answer2;
    private int SexType = 1;//性别类型
    private OptionsPickerView optionsPickerView;
    private List<String> regionList = new ArrayList<>();//用于选择地区
    private boolean isSelectRegion = false;//是否已经选择了地区

    //接收医生注册信息变量

    //用于标识已经上传了多少张执业图片
    private int certificateSize = 0;
    //医生头像路径
    private String UserIconPath = null;
    //医生的身份证路径
    private String IDCardPath = null;
    //就职医院ID
    private String HospitalId = null;
    //医生名字
    private String DoctorName = null;
    //医生的诊室
    private String Doctor_department = null;
    //医生的职位
    private String Doctor_Job = null;
    //执业图片拼接
    private String certificatePhotos = null;
    //医生执业图片
    private List<String> certificateList;
    private String referral = null;//推荐码
    private Gson gson = new Gson();
    private String iconUrl = null;

    private Handler uploadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        iconUrl = jsonObject1.optString("url");
                        registerUsers(iconUrl);
                    }else{
                        Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Register_SettingData_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler registerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        Health_AppLocation.instance.Token = jsonObject1.optString("token");
                        //如果有医生资料
                       if(HospitalId!=null){
                           PostPhoto(Constant.doctoricon,UserIconPath,uploadHandler_Doctor);
                       }else{
                           SharedPreferences.Editor editor = SharedPreferencesUtils.
                                   getEditor(Register_SettingData_Activity.this,"loginToken");
                           editor.putString("phone",phone);
                           editor.commit();
                           it = new Intent(Register_SettingData_Activity.this,SplashActivity.class);
                           it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(it);
                       }
                    }else{
                        Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };




    private Handler uploadHandler_Doctor = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        UserIconPath = new JSONObject(jsonObject.optString("data")).optString("url");
                        PostPhoto(Constant.doctorId_img,IDCardPath,idcard_Handler);
                    }else{
                        Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Register_SettingData_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler idcard_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        IDCardPath = jsonObject1.optString("url");
                        for (int i = 0;i<certificateList.size();i++){
                            PostCertificate(Constant.doctor_certificate,
                                    certificateList.get(i),certificate_Handler);
                        }
                    }else{
                        Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler certificate_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        synchronized (this){
                            JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                            certificateList.set(certificateSize,jsonObject1.optString("url"));
                            certificateSize ++;
                            if(certificateSize == certificateList.size()){
                                JSONObject jsonObjects = new JSONObject();
                                jsonObjects.put("nickname",DoctorName);
                                jsonObjects.put("office",Doctor_department);
                                jsonObjects.put("title",Doctor_Job);
                                jsonObjects.put("hospitalId",HospitalId);
                                jsonObjects.put("iconUrl",UserIconPath);
                                jsonObjects.put("idUrl",IDCardPath);
                                JSONArray jsonArray = new JSONArray();
                                jsonArray.put(certificateList);
                                jsonObjects.put("certUrls",jsonArray);
                                RegisterPostDoctor(jsonObjects);
                            }
                        }
                    }else{
                        Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };



    private Handler register_Handler_Doctor = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        SharedPreferences.Editor editor = SharedPreferencesUtils.
                                getEditor(Register_SettingData_Activity.this,"loginToken");
                        editor.putString("phone",phone);
                        editor.commit();
                        it = new Intent(Register_SettingData_Activity.this,SplashActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Register_SettingData_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_settingdata_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        switch_icon_head_lin = (LinearLayout) findViewById(R.id.switch_icon_head_lin);
        nick_name_ed = (EditText)findViewById(R.id.nick_name_ed);
        birth_ed = (TextView)findViewById(R.id.birth_ed);
        back = (ImageView)findViewById(R.id.back);
        icon_head = (ImageView)findViewById(R.id.icon_head);
        next_btn = (Button)findViewById(R.id.next_btn);
        switch_view = (SwitchView)findViewById(R.id.switch_view);
        select_sex_img = (ImageView)findViewById(R.id.select_sex_img);
        man_text = (TextView) findViewById(R.id.man_text);
        woman_text = (TextView) findViewById(R.id.woman_text);
        region_Text = (TextView)findViewById(R.id.region_Text);
    }

    private void BindListener(){
        back.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        switch_icon_head_lin.setOnClickListener(this);
        birth_ed.setOnClickListener(this);
        select_sex_img.setOnClickListener(this);
        region_Text.setOnClickListener(this);

        switch_view.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                IsswitchViewClick = true;
                switch_view.setState(true);
                next_btn.setText(R.string.next);
            }

            @Override
            public void toggleToOff() {
                IsswitchViewClick = false;
                switch_view.setState(false);
                next_btn.setText(R.string.register_finish);
            }
        });

    }

    private void setData(){
        phone = getIntent().getStringExtra("phone");
        referral = getIntent().getStringExtra("referral");
        //只要一个为空,其他都为空，否则都有值
        if(getIntent().getStringExtra("question1")!=null){
            question1 = getIntent().getStringExtra("question1");
            answer1 = getIntent().getStringExtra("answer1");
            question2 = getIntent().getStringExtra("question2");
            answer2 = getIntent().getStringExtra("answer2");
        }
        imageUtils = new ImageUtils();
        startDate = Calendar.getInstance();
        startDate.set(1900,0,1);
        TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date, View v) {
                birth_ed.setText(DateUtil.getYearStr(date));
                birthYear = DateUtil.getYearStr2(date);
            }
        };
        timePicker = new DateUtil().ShowTimePickerView(this,listener,startDate,
                true,false,false,false,false,false);

    }

    private void  PictureSelectorCreate(){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(150,150)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(3,2)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                //.compressSavePath(Constant.User_Icon_Path)//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
                //.selectionMedia(true)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(60)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(600)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH(16,9)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                //.videoQuality()// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    //上传图片
    private void PostPhoto(String postName,String path,Handler handler){
        new PostUtils().NewUploadImage(postName,path,handler,this);
    }
    private void RegisterPostDoctor(JSONObject jsonObject){
        new PostUtils().RegisterPost(Constant.doctor_register,jsonObject,register_Handler_Doctor,this);
    }

    //上传执业照片
    private void PostCertificate(String postName,String path,Handler handler){
        new PostUtils().NewUploadImage(postName,path,handler,this);
    }

    //注册用户条件判断
    private void registerUsers(String userIcon){
        if(question1 == null){
            Users users = new Users();
            users.setPhone(phone);
            users.setNickname(nick_name_ed.getText().toString());
            users.setBirth(birthYear);
            users.setSex(SexType);
            users.setRegion(region_Text.getText().toString());
            if(referral!=null){
                users.setReferrerCode(referral);
            }
            if(userIcon == null){
                users.setIconUrl(null);
            }else{
                users.setIconUrl(iconUrl);
            }
            users.setIsDoctor(false);
            users.setPlatform("1");
            RegisterPost(gson.toJson(users));
        }else{
            Users users = new Users();
            users.setPhone(phone);
            users.setNickname(nick_name_ed.getText().toString());
            users.setBirth(birthYear);
            users.setRegion(region_Text.getText().toString());
            users.setIconUrl(iconUrl);
            users.setIsDoctor(false);
            users.setSex(SexType);
            users.setPlatform("1");
            if(referral!=null){
                users.setReferrerCode(referral);
            }

            Users.securityAnswer securityAnswer = new Users.securityAnswer();
            securityAnswer.setAnswer1(answer1);
            securityAnswer.setAnswer2(answer2);
            securityAnswer.setQuestion1(question1);
            securityAnswer.setQuestion2(question2);
            users.setSecurityAnswer(securityAnswer);
            RegisterPost(gson.toJson(users));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.birth_ed:
                timePicker.show();
                break;
            case R.id.region_Text:
                ShowRegionPickView();
                break;
            case R.id.select_sex_img:
                if(SexType == 1){
                    select_sex_img.setImageResource(R.drawable.select_woman);
                    man_text.setTextColor(getResources().getColor(R.color.black));
                    woman_text.setTextColor(getResources().getColor(R.color.white));
                    SexType = 2;
                }else{
                    select_sex_img.setImageResource(R.drawable.select_man);
                    man_text.setTextColor(getResources().getColor(R.color.white));
                    woman_text.setTextColor(getResources().getColor(R.color.black));
                    SexType = 1;
                }
                break;
            case R.id.switch_icon_head_lin:
                PictureSelectorCreate();
                break;
            case R.id.next_btn:
                if(IsswitchViewClick){
                    if(HospitalId == null){
                        Toast.makeText(this, R.string.doctor_data_null, Toast.LENGTH_SHORT).show();
                        it = new Intent(this,Commit_DoctorData_Activity.class);
                        startActivityForResult(it,0x1);
                    }else{
                        if(birthYear == null){
                            Toast.makeText(this, R.string.birthYear_null, Toast.LENGTH_SHORT).show();
                        }else{
                            if(nick_name_ed.getText().toString().length()>=2){
                                if(isSelectPhoto){
                                    PostUserIcon(Constant.User_Icon_Path+Constant.UserPhoto_Name);
                                }else {
                                    if(isSelectRegion){
                                        registerUsers(null);
                                    }else{
                                        Toast.makeText(this, R.string.select_region_null, Toast.LENGTH_SHORT).show();
                                        }
                                }
                            }else{
                                Toast.makeText(this, R.string.nick_name_null, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }else{
                    if(birthYear == null){
                        Toast.makeText(this, R.string.birthYear_null, Toast.LENGTH_SHORT).show();
                    }else{
                        if(nick_name_ed.getText().toString().length()>=2){
                            if(isSelectPhoto){
                                PostUserIcon(Constant.User_Icon_Path+Constant.UserPhoto_Name);
                            }else{
                                if(isSelectRegion){
                                    registerUsers(null);
                                }else{
                                    Toast.makeText(this, R.string.select_region_null, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }else{
                            Toast.makeText(this, R.string.nick_name_null, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
                    if(selectList.size()>0){
                        if(selectList.get(0).isCompressed()){
                            new ImageAsyncTask().execute();
                            //icon_head.setImageURI(Uri.fromFile(new File(selectList.get(0).getCompressPath())));
                        }
                    }

                    break;
            }
        }else if(resultCode == 111){
            HospitalId = data.getStringExtra("HospitalId");
            UserIconPath = data.getStringExtra("doctorIconPath");
            IDCardPath = data.getStringExtra("idCardPath");
            DoctorName = data.getStringExtra("name");
            Doctor_department = data.getStringExtra("department");
            Doctor_Job = data.getStringExtra("job");
            certificatePhotos = data.getStringExtra("certificatePhotos");

            String certificates [] = certificatePhotos.split(";");
            certificateList = new ArrayList<>();
            for (int i = 0 ;i<certificates.length;i++){
                certificateList.add(certificates[i]);
            }

            //医生注册资料填写获取完毕之后,再检验一次用户资料是否填写完整
            if(birthYear == null){
                Toast.makeText(this, R.string.birthYear_null, Toast.LENGTH_SHORT).show();
            }else{
                if(nick_name_ed.getText().toString().length()>2){
                    if(isSelectPhoto){
                        PostUserIcon(Constant.User_Icon_Path+Constant.UserPhoto_Name);
                    }else {
                        if(isSelectRegion){
                            registerUsers(null);
                        }else{
                            Toast.makeText(this, R.string.select_region_null, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(this, R.string.nick_name_null, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class ImageAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
             bitmap  = imageUtils.toRoundBitmap(BitmapFactory.decodeFile(selectList.get(0).getCompressPath()));
            try {
                File file = new File(Constant.User_Icon_Path,Constant.UserPhoto_Name);
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressIndex, outputStream);
                PictureFileUtils.deleteCacheDirFile(Register_SettingData_Activity.this);
                PictureFileUtils.deleteExternalCacheDirFile(Register_SettingData_Activity.this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(outputStream!=null){
                        outputStream.close();
                    }
                 } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            isSelectPhoto = true;
            Bitmap bitmaps = (Bitmap) o;
            icon_head.setImageBitmap(bitmaps);
        }
    }

    private void PostUserIcon(String path){
        new PostUtils().NewUploadImage(Constant.usericon,path,uploadHandler,this);
    }
    private void RegisterPost(String json){

        new PostUtils().JsonPost(Constant.register,json,registerHandler,this);
    }


    private void ShowRegionPickView(){
        if(optionsPickerView == null) {
            optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    region_Text.setText(regionList.get(options1));
                    isSelectRegion = true;
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

            regionList.add("广东");
            optionsPickerView.setPicker(regionList);
            optionsPickerView.show();
        }else{
            optionsPickerView.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        icon_head = null;
        if(bitmap!=null){
            bitmap.recycle();
            bitmap = null;
        }
        FileUtils.DelFile(Constant.User_Icon_Path+Constant.UserPhoto_Name);
    }


}
