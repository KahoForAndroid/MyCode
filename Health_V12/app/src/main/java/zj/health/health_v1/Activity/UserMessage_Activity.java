package zj.health.health_v1.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.UserMessage;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.SharedPreferencesUtils;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * 用户个人信息页
 * Created by Administrator on 2018/9/3.
 */

public class UserMessage_Activity extends BaseActivity implements View.OnClickListener{

    private TextView title,sex_text,height_text,weight_text,update_text;
    private TextView name_text;
    private ImageView back,icon_head_img;
    private RelativeLayout icon_rela,name_rela,sex_rela,height_rela,weight_rela;
    private List<LocalMedia> selectList = null;
    private List<String> sexList = new ArrayList<>();
    private List<String> heightList = new ArrayList<>();
    private List<String> weightList = new ArrayList<>();
    private boolean updateData = false;//标识是否需要更新用户信息
    private OptionsPickerView optionsPickerView;
    private UserMessage userMessage;
    private boolean updateIcon = false;//标识是否更换了头像
    private DbUtils dbUtils = new DbUtils();
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
    private Handler IconHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        Health_AppLocation.instance.users.setIconUrl(jsonObject1.optString("iconUrl"));
                        dbUtils.updateUsersData(UserMessage_Activity.this,Health_AppLocation.instance.users);
                        Health_AppLocation.instance.Icon = Constant.photo_IP+jsonObject1.optString("iconUrl");
                        Glide.with(UserMessage_Activity.this).
                                load(Health_AppLocation.instance.Icon).apply(requestOptions).
                                into(icon_head_img);
                        updateIcon = true;
                        setIM_Icon(Health_AppLocation.instance.Icon);
                    }else{
                        Toast.makeText(UserMessage_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 100){
                Toast.makeText(UserMessage_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(UserMessage_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };



    private Handler uploadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        updateIcon(jsonObject1.optString("url"));
                    }else{
                        Toast.makeText(UserMessage_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == 100){
                Toast.makeText(UserMessage_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(UserMessage_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        title = (TextView)findViewById(R.id.title);
        name_text = (TextView)findViewById(R.id.name_text);
        sex_text = (TextView)findViewById(R.id.sex_text);
        height_text = (TextView)findViewById(R.id.height_text);
        weight_text = (TextView)findViewById(R.id.weight_text);
        back = (ImageView)findViewById(R.id.back);
        icon_head_img = (ImageView)findViewById(R.id.icon_head_img);
        icon_rela = (RelativeLayout)findViewById(R.id.icon_rela);
        name_rela = (RelativeLayout)findViewById(R.id.name_rela);
        sex_rela = (RelativeLayout)findViewById(R.id.sex_rela);
        height_rela = (RelativeLayout)findViewById(R.id.height_rela);
        weight_rela = (RelativeLayout)findViewById(R.id.weight_rela);
        update_text = (TextView)findViewById(R.id.update_text);

    }

    private void BindListener(){
        back.setOnClickListener(this);
        icon_rela.setOnClickListener(this);
        name_rela.setOnClickListener(this);
        sex_rela.setOnClickListener(this);
        height_rela.setOnClickListener(this);
        weight_rela.setOnClickListener(this);
        update_text.setOnClickListener(this);
    }

    private void setData(){
        title.setText(R.string.user_Message);
        name_text.setText(Health_AppLocation.instance.users.getNickname()+"");
        Glide.with(this).
                load(Health_AppLocation.instance.Icon).apply(requestOptions).
                into(icon_head_img);

        sexList.add("男");
        sexList.add("女");

        for (int j = 30;j<130;j++){
            weightList.add(j+"kg");
        }
        for (int i = 150;i<190;i++){
            heightList.add(i+"cm");
        }
        userMessage = (UserMessage) getIntent().getBundleExtra("bundle").getSerializable("userMessgae");
        height_text.setText(userMessage.getHeight()+"cm");
        weight_text.setText(userMessage.getWeight()+"kg");
        sex_text.setText(sexList.get(Integer.parseInt(userMessage.getSex())-1));


    }



    private void setIM_Icon(String url){
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setFaceUrl(url);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
            }

            @Override
            public void onSuccess() {
                Log.e("mainActivity", "设置个人IM头像成功");
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent it = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("userMessage",userMessage);
                it.putExtra("updateData",updateData);
                it.putExtra("updateIcon",updateIcon);
                it.putExtra("bundle",bundle);
                setResult(RESULT_OK,it);
                finish();
                break;
            case R.id.icon_rela:
                PictureSelectorCreate();
                break;
            case R.id.name_rela:
                break;
            case R.id.sex_rela:
                ShowDataPickView(sexList,sex_text);
                break;
            case R.id.height_rela:
                ShowDataPickView(heightList,height_text);
                break;
            case R.id.weight_rela:
                ShowDataPickView(weightList,weight_text);
                break;
            case R.id.update_text:
                name_text.setFocusableInTouchMode(true);
                break;
        }
    }
    private void UpdateData(){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("sex",);
//        jsonObject.put("height",);
//        jsonObject.put("weight",);
//        jsonObject.put("liveMode",);
//        jsonObject.put("sportMode",);
//        new PostUtils().JsonPost(Constant.updateData,);
    }

    private void updateIcon(String icon_url){
        Map<String,String> map = new HashMap<>();
        map.put("iconUrl",icon_url);
        new PostUtils().getNewPost(Constant.update_icon,map,IconHandler,this);
    }
    private void uploadIcon(String icon_url){
        new PostUtils().NewUploadImage(Constant.usericon,icon_url,uploadHandler,this);
    }

    private void ShowDataPickView(final List<String> stringList, final View view){
            optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if(view instanceof TextView){
                        updateData = true;
                        ((TextView) view).setText(stringList.get(options1));
                        if(view.getId() == R.id.sex_text){
                            userMessage.setSex(options1+"");
                        }else if(view.getId() == R.id.height_text){
                            userMessage.setHeight(height_text.getText().toString().replace("cm",""));
                        }else {
                            userMessage.setWeight(weight_text.getText().toString().replace("kg",""));
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



    private void  PictureSelectorCreate(){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(150,150)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(3,2)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(Environment.getExternalStorageDirectory().getPath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(80)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(600)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH(16,9)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(false)// 裁剪是否可放大缩小图片 true or false
                //.videoQuality()// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList.size()>0){
                        LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
                        uploadIcon(selectList.get(0).getCompressPath());
                    }

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userMessage",userMessage);
        it.putExtra("updateData",updateData);
        it.putExtra("updateIcon",updateIcon);
        it.putExtra("bundle",bundle);
        setResult(RESULT_OK,it);
        finish();
    }
}
