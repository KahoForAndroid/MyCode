package zj.health.health_v1.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMManagerExt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import GreenDaoDB.ConsultTimeModelDao;
import zj.health.health_v1.Adapter.Counseling_photo_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.Picture_OnItemClick;
import zj.health.health_v1.Model.ConsultModel;
import zj.health.health_v1.Model.ConsultTimeModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.MyView.KahoLabelLayout;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/11.
 */

public class Health_counseling_Activity extends BaseActivity implements View.OnClickListener{

    private ViewGroup.MarginLayoutParams marginLayoutParams;
    public KahoLabelLayout dorctor_leavel_layout,Selection_section_layout,Communication_mode_layout,area_layout;
    private EditText Description_edit;
    private AppCompatCheckBox checkbox;
    private Button next_btn;
    private ImageView back;
    private TextView history_text,edit_money_text,money_text;
    private String Selection_section_array [];
    private String dorctor_leavel_array [];
    private String Communication_mode_array [];
    private String area_array [];
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> IntentselectList = new ArrayList<>();
    private List<String> UploadSuccessPhotoName = new ArrayList<>();
    private int IndexSize = 0;//记录图片提交的次数
    private Counseling_photo_Adapter adapter;
    private RecyclerView counseling_recy;
    private ConsultModel consultModel = new ConsultModel();
    private CreateDialog dialog = new CreateDialog();
    private int newPhotoSize = 0 ;//记录新提交的图片
    private DbUtils dbUtils = new DbUtils();
    private PopupWindow popupWindow = null;
    private ConsultTimeModel consultTimeModel = null;
    private String tips = null;//弹出框提示文字
    private int place[];
    public static Health_counseling_Activity activity;


    private Handler UploadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            synchronized (this){
                                JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                                UploadSuccessPhotoName.add(jsonObject1.optString("url"));
                                IndexSize ++;
                                if(IndexSize == newPhotoSize){
                                    postCounseling(Description_edit.getText().toString(),UploadSuccessPhotoName);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Health_counseling_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Health_counseling_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Health_counseling_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler consultHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        JSONObject jsonObject = new JSONObject(jsonObject1.optString("data"));
                        Toast.makeText(Health_AppLocation.instance, R.string.release_succ,  Toast.LENGTH_SHORT).show();
                        setTimeToDb(jsonObject.optString("consultId"));
                        Intent it = new Intent(Health_counseling_Activity.this,Counselling_list_Activity.class);
                        it.putExtra("consultId",jsonObject.optString("consultId"));
                        startActivity(it);
                        finish();
                    }else if(jsonObject1.optString("code").equals("1001")){
                        Toast.makeText(Health_counseling_Activity.this, jsonObject1.optString("msg"), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Health_counseling_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Health_counseling_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Health_counseling_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler editHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            consultModel.setCost(Float.parseFloat(msg.obj.toString()));
            money_text.setText(getString(R.string.Estimate_money)+msg.obj.toString()+"元/次");
        }
    };
    private Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MessageDailog(View.inflate(Health_counseling_Activity.this,R.layout.main_popwindow,null),
                    tips,
                    Health_counseling_Activity.this, (Boolean) msg.obj);
        }
    };

    private Handler health_item_configHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_counseling_activity);
        activity = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        registerReceiver(broadcastReceiver,intentFilter);
        initView();
        BindListener();
        setData();

    }
    private void initView(){
        dorctor_leavel_layout = (KahoLabelLayout)findViewById(R.id.dorctor_leavel_layout);
        Selection_section_layout = (KahoLabelLayout)findViewById(R.id.Selection_section_layout);
        Communication_mode_layout = (KahoLabelLayout)findViewById(R.id.Communication_mode_layout);
        counseling_recy = (RecyclerView)findViewById(R.id.counseling_recy);
        area_layout = (KahoLabelLayout)findViewById(R.id.area_layout);
        Description_edit = (EditText)findViewById(R.id.Description_edit);
        edit_money_text = (TextView)findViewById(R.id.edit_money_text);
        money_text = (TextView)findViewById(R.id.money_text);
        checkbox = (AppCompatCheckBox)findViewById(R.id.checkbox);
        next_btn = (Button)findViewById(R.id.next_btn);
        back = (ImageView)findViewById(R.id.back);
        history_text = (TextView)findViewById(R.id.history_text);
    }
    private void BindListener(){
        next_btn.setOnClickListener(this);
        back.setOnClickListener(this);
        history_text.setOnClickListener(this);
        edit_money_text.setOnClickListener(this);
        dorctor_leavel_layout.setOnitemOnClickListener(new KahoLabelLayout.OnitemOnClickListener() {
            @Override
            public void OnitemClick(View view, int position) {
                dorctor_leavel_layout.requestOnClickView(position);
                consultModel.setLevel(String.valueOf(position));
                money_text.setText(getString(R.string.Estimate_money)+place[position]+"元/次");
            }
        });
        Selection_section_layout.setOnitemOnClickListener(new KahoLabelLayout.OnitemOnClickListener() {
            @Override
            public void OnitemClick(View view, int position) {
                Selection_section_layout.requestOnClickView(position);
                consultModel.setOffice(String.valueOf(position));
            }
        });
        Communication_mode_layout.setOnitemOnClickListener(new KahoLabelLayout.OnitemOnClickListener() {
            @Override
            public void OnitemClick(View view, int position) {
                Communication_mode_layout.requestOnClickView(position);
                consultModel.setCommunicateType(String.valueOf(position));
            }
        });
        area_layout.setOnitemOnClickListener(new KahoLabelLayout.OnitemOnClickListener() {
            @Override
            public void OnitemClick(View view, int position) {
                area_layout.requestOnClickView(position);
                consultModel.setRegion(area_array[position]);
            }
        });
    }

    private void setData(){
        money_text.setText(getString(R.string.Estimate_money)+"15 元/次");
        place = getResources().getIntArray(R.array.place);
        tips = getString(R.string.consult_tips);
        marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(10, 10, 10, 10);
        counseling_recy.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new Counseling_photo_Adapter(this,selectList);
        counseling_recy.setAdapter(adapter);
        Selection_section_array = getResources().getStringArray(R.array.Selection_section);
        dorctor_leavel_array = getResources().getStringArray(R.array.dorctor_leavel);
        Communication_mode_array = getResources().getStringArray(R.array.Communication_mode);
        area_array = getResources().getStringArray(R.array.address);
        for (int i = 0;i<Selection_section_array.length;i++){
            AddTextView(i,Selection_section_array[i],Selection_section_layout);
        }
        for (int i = 0;i<dorctor_leavel_array.length;i++){
            AddTextView(i,dorctor_leavel_array[i],dorctor_leavel_layout);
        }
        for (int i = 0;i<Communication_mode_array.length;i++){
            AddTextView(i,Communication_mode_array[i],Communication_mode_layout);
        }
//        for (int i = 0;i<area_array.length;i++){
//            AddTextView(i,area_array[i],area_layout);
//        }

        adapter.setOnItemClick(new Picture_OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position, boolean isDelete) {
                if(isDelete){
                    selectList.remove(position);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                }else{
                    PictureSelectorCreate();
                }
            }
        });
//        getHealthConfig();

    }

    private void AddTextView(int id,String text,KahoLabelLayout labelLayout){
        MyTextView textView = new MyTextView(this);
        textView.setText(text);
        textView.setTextSize(14);
        textView.setTypeId(id);
        textView.setBackground(getResources().getDrawable(R.drawable.radius_kaho));
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setPadding(20, 20, 20, 20);
        labelLayout.addView(textView, marginLayoutParams);
    }

    private void UploadImage(String path){
        new PostUtils().NewUploadImage(Constant.report_img_Upload,path,UploadHandler,this);
    }


    /**
     * 把发布时间设置进数据库
     * @param consultId
     */
    private void setTimeToDb(String consultId){
        ConsultTimeModel consultTimeModel = new ConsultTimeModel();
        consultTimeModel.setConsultId(consultId);
        consultTimeModel.setConsultTime(System.currentTimeMillis()+"");
        List<ConsultTimeModel> consultTimeModelList = new ArrayList<>();
        consultTimeModelList.add(consultTimeModel);
        dbUtils.insertConsultTimeModelData(Health_counseling_Activity.this,consultTimeModelList);
    }

    /**
     * 检查最新的发布时间是否已经超过30分钟
     * @param timeHandler 从数据库获取最新的发布时间以及发布id
     * @param state 检查状态 false 非重新发布 true重新发布
     */
    private void checkTime(final Handler timeHandler, final boolean state){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    List<ConsultTimeModel> consultTimeModelList = dbUtils.getConsultTimeModelList();
                    if(consultTimeModelList !=null && consultTimeModelList.size()>0){
                        long time = DateUtil.getDistanceTimeMinute(DateUtil.setTimeToDate(Long.parseLong(consultTimeModelList.get(0).getConsultTime() )),
                                DateUtil.setTimeToDate(System.currentTimeMillis()));
                        consultTimeModel = consultTimeModelList.get(0);
                        if(time<30){
                            if(state){
                                tips = getString(R.string.consult_again_tips);
                                Message message = new Message();
                                message.obj = state;
                                timeHandler.sendMessage(message);
                            }else{
                                tips = getString(R.string.consult_tips);
                                Message message = new Message();
                                message.obj = state;
                                timeHandler.sendMessage(message);
                            }

                        }else{
                            if(state){
                                Release();
                            }
                        }
                    }else{
                        if(state){
                            Release();
                        }
                    }
            }
        }).start();
    }

    /**
     * 提交咨询
     */
    private void postCounseling(String desc,List<String> stringList){
        if(!StringUtil.isEmpty(desc)){
            consultModel.setDesc(desc);
        }
        for (int i = 0;i<selectList.size();i++){
            if(selectList.get(i).getCompressPath().contains("/image/")){
                stringList.add(selectList.get(i).getCompressPath());
            }
        }
        if(stringList.size()>0){
            consultModel.setImages(stringList);
        }
        if(consultModel.getCost() <= 0){
            consultModel.setCost(15);
        }
        consultModel.setRegion(area_array[0]);
        String json = new Gson().toJson(consultModel);
        new PostUtils().JsonPost(Constant.consult,json,consultHandler,this);
    }

    private void  PictureSelectorCreate(){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(3-selectList.size())// 最大图片选择数量 int
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
//                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
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

    /**
     * 从历史发布中返回的参数设置
     * @param consultModel
     */
    private void setHistory_data(ConsultModel consultModel){
        for (int i = 0;i<Selection_section_array.length;i++){
            if(Selection_section_array[i].equals(consultModel.getOffice())){
                Selection_section_layout.requestOnClickView(i);
                this.consultModel.setOffice(String.valueOf(i));
            }
        }
        for (int i = 0;i<dorctor_leavel_array.length;i++){
            if(dorctor_leavel_array[i].equals(consultModel.getLevel())){
                dorctor_leavel_layout.requestOnClickView(i);
                consultModel.setLevel(String.valueOf(i));
            }
        }
        for (int i = 0;i<Communication_mode_array.length;i++){
            if( Communication_mode_array[i].equals(consultModel.getCommType())){
                Communication_mode_layout.requestOnClickView(i);
                consultModel.setCommunicateType(String.valueOf(i));
            }
        }
//        for (int i = 0;i<area_array.length;i++){
//            if(area_array[i].equals(consultModel.getRegion())){
//                area_layout.requestOnClickView(i);
//                consultModel.setRegion(area_array[i]);
//            }
//        }
        Description_edit.setText(StringUtil.trimNull(consultModel.getDesc()));
        money_text.setText(getString(R.string.Estimate_money)+consultModel.getCost()+"元/次");
        this.consultModel = consultModel;
        selectList.clear();
        if(this.consultModel.getImages()!=null && this.consultModel.getImages().size()>0){
            for (int i =0 ;i<this.consultModel.getImages().size();i++){
                LocalMedia localMedia = new LocalMedia();
                localMedia.setCompressPath(consultModel.getImages().get(i));
                selectList.add(localMedia);
            }
        }
        adapter.setList(selectList);
        adapter.notifyDataSetChanged();

    }

    private void Release(){
        if(checkbox.isChecked()){
            if(selectList.size()>0){
                for (int i = 0;i<selectList.size();i++){
                    if(!selectList.get(i).getCompressPath().contains("/image/")){
                        newPhotoSize++;
                    }
                }
                for (int i = 0;i<selectList.size();i++){
                    if(!selectList.get(i).getCompressPath().contains("/image/")){
                        UploadImage(selectList.get(i).getCompressPath());
                    }
                }
            }else{
                postCounseling(Description_edit.getText().toString(),UploadSuccessPhotoName);
            }
        }
    }

    //带有确定取消的弹出框
    public void MessageDailog(final View view, String Text, View.OnClickListener onClickListener, final boolean state){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        TextView textView = (TextView)view.findViewById(R.id.popwindow_content_text);
        RelativeLayout ok = (RelativeLayout)view.findViewById(R.id.ok);
        RelativeLayout cancel = (RelativeLayout)view.findViewById(R.id.cancel);
        textView.setText(Text);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                needShowing = false;
                if(state){
                    Release();
                }else{
                    Intent it = new Intent(Health_counseling_Activity.this,Counselling_list_Activity.class);
                    it.putExtra("consultId",consultTimeModel.getConsultId());
                    startActivity(it);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needShowing = false;
                popupWindow.dismiss();
            }
        });

        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next_btn:
//                List<TIMConversation> list = TIMManagerExt.getInstance().getConversationList();
//                List<TIMConversation> result = new ArrayList<>();
//                for (TIMConversation conversation : list) {
//                    if (conversation.getType() == TIMConversationType.System) {
//                        continue;
//                    }
//
//                    result.add(conversation);
//                }
                if(Doctor_service_Activity.consultModelList!=null && Doctor_service_Activity.consultModelList.size()>0){
                    Toast.makeText(this, getString(R.string.health_counseling_rep_error_tips), Toast.LENGTH_SHORT).show();
                }else{
                    checkTime(timeHandler,true);
                }
//                if(!Health_AppLocation.instance.users.getDoctorStatus().equals("2") && result.size()>0){
//                }else{
//                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.history_text:
                Intent it = new Intent(this,Historical_Release_Activity.class);
                startActivityForResult(it,1);
                break;
            case R.id.edit_money_text:

                dialog.EditDailog(this,
                        View.inflate(this,R.layout.edit_dialog,null),
                        editHandler);
                break;
                default:
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
//                    selectList = PictureSelector.obtainMultipleResult(data);
                    selectList.addAll(PictureSelector.obtainMultipleResult(data));
                    if(selectList.size()>0){
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }else if(resultCode == 0x1){
            consultModel = (ConsultModel) data.getBundleExtra("bundle").getSerializable("model");
            setHistory_data(consultModel);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && needShowing){
            checkTime(timeHandler,false);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void getHealthConfig(){
        new PostUtils().Get(Constant.health_item_config,true,health_item_configHandler,this);
    }
}
