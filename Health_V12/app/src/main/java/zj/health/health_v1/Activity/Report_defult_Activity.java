package zj.health.health_v1.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Health_Report_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.Picture_OnItemClick;
import zj.health.health_v1.Model.AllCheckFilterModel;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.Model.ReportModel;
import zj.health.health_v1.MyView.KahoLabelLayout;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/6/5.
 */

public class Report_defult_Activity extends BaseActivity implements View.OnClickListener{

    private ViewGroup.MarginLayoutParams marginLayoutParams;
    private ImageView back,img_add;
    private TextView title,data_text;
    private EditText edit_name,remark_text;
    private Button add_btn;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> IntentselectList = new ArrayList<>();
    private TimePickerView timePicker = null;
    private Calendar selectedDate,startDate,endDate;
    private RecyclerView img_recycle = null;
    private Health_Report_Adapter adapter = null;
    private KahoLabelLayout labellayout;
    private List<String> stringList = new ArrayList<>();
    private NewReportModel reportModel = null;
    private List<String> UploadSuccessPhotoName = new ArrayList<>();
    private int IndexSize = 0;//记录图片提交的次数
    private int ListSize = 0;//记录筛选网络图片后的真实长度
    private int submitType = 0;//提交是的类型0为健康报告 1为专项报告
    private Handler submitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            setResult(RESULT_OK);
                            Toast.makeText(Health_AppLocation.instance, R.string.save_succ, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Report_defult_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Report_defult_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };



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
                                IndexSize ++;
                                //获取提交上去的图片名
                                UploadSuccessPhotoName.add(jsonObject1.optString("url"));
                                if(IndexSize == ListSize){

                                    for (int i = 0 ;i<IntentselectList.size();i++){
                                        UploadSuccessPhotoName.add(IntentselectList.get(i).getCompressPath());
                                    }
                                    Submit(edit_name.getText().toString(),
                                            StringUtil.trimNull(remark_text.getText().toString()),
                                            UploadSuccessPhotoName);
                                }
                            }

                        }
                    } catch (JSONException e) {
                        ListSize = 0;
                        e.printStackTrace();
                    }
                }else{
                    ListSize = 0;
                    Toast.makeText(Report_defult_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Report_defult_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                ListSize = 0;
                Toast.makeText(Report_defult_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.special_report_commit_activity);
//        Transition explode = TransitionInflater.from(this).inflateTransition(android.R.transition.explode);
//        //退出时使用
//        getWindow().setExitTransition(explode);
////第一次进入时使用
//        getWindow().setEnterTransition(explode);
////再次进入时使用
//        getWindow().setReenterTransition(explode);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        img_add = (ImageView)findViewById(R.id.img_add);
        title = (TextView)findViewById(R.id.title);
        data_text = (TextView)findViewById(R.id.data_text);
        edit_name = (EditText)findViewById(R.id.edit_name);
        remark_text = (EditText)findViewById(R.id.remark_text);
        add_btn = (Button)findViewById(R.id.add_btn);
        img_recycle = (RecyclerView)findViewById(R.id.img_recycle);
        labellayout = (KahoLabelLayout)findViewById(R.id.labellayout);

        adapter = new Health_Report_Adapter(this,selectList);
        marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(10, 10, 10, 10);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        img_add.setOnClickListener(this);
        data_text.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.Special_report));
        reportModel = (NewReportModel) getIntent().getBundleExtra("bundle").getSerializable("model");//获取报告对象
        submitType = getIntent().getIntExtra("type",0);
        if(submitType == 1){
            title.setText(getString(R.string.health_report));
        }else{
            title.setText(getString(R.string.Special_report));
        }
        if(Integer.parseInt(reportModel.getType() )== 2){
            labellayout.setType(2);

            for (int k = 0;k<Health_AppLocation.instance.bodyModelList.size();k++){
                if(Health_AppLocation.instance.bodyModelList.get(k).getId().equals(reportModel.getpId())){
                  int position = k;
                    for (int i = 0;i<reportModel.getSubIds().size();i++){
                        String subid =reportModel.getSubIds().get(i).toString();
                        for (int j = 0;j<Health_AppLocation.instance.bodyModelList.get(position).getSubItem().size();j++){
                            if(Health_AppLocation.instance.bodyModelList.get(position).getSubItem().get(j).getSid().equals(subid)){
                                stringList.add(Health_AppLocation.instance.bodyModelList.get(position).getSubItem().get(j).getName());
                            }
                        }
                    }
                    break;
                }
            }

            for(int i = 0;i<stringList.size();i++){
                AddTextView(stringList.get(i),labellayout,i);
            }
        }

        data_text.setText(DateUtil.getNowdayymd());
        if(reportModel!=null){
            data_text.setText(reportModel.getCreateDate().substring(0,10));
            edit_name.setText(reportModel.getTitle());
            remark_text.setText(reportModel.getMark());

            for (int i = 0;i< reportModel.getImages().size();i++){
                LocalMedia localMedia = new LocalMedia();
                localMedia.setCompressPath(reportModel.getImages().get(i));
                IntentselectList.add(localMedia);
            }
            img_recycle.setLayoutManager(new GridLayoutManager(this,3));
            adapter.setList(IntentselectList);
            img_recycle.setAdapter(adapter);
        }
        labellayout.setOnitemOnClickListener(new KahoLabelLayout.OnitemOnClickListener() {
            @Override
            public void OnitemClick(View view, int position) {

            }
        });
        adapter.setOnItemClick(new Picture_OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position, boolean isDelete) {
                if(isDelete){
                    //这里假设一种情况：因为最初传递过来的图片是由IntentselectList保存并且展示
                    //如果用户进来第一时间就把传递过来的图片删掉，那么被remove的元素的所属集合应该是IntentselectList
                    //否则remove的元素所属集合应该是selectList
                     if(selectList.size() == 0){
                        IntentselectList.remove(position);
                        adapter.setList(IntentselectList);
                    }else{
                        selectList.remove(position);
                        adapter.setList(selectList);
                    }
                    adapter.notifyDataSetChanged();
                }else if(position>IntentselectList.size()-1){
                    PictureSelectorCreate();
                }else{
                    Intent it = new Intent(Report_defult_Activity.this,CheckPhotoActivity.class);
                    it.putExtra("url",IntentselectList.get(position).getCompressPath());
                    startActivity(it);
                }
            }
        });
        setPickView();

    }

    private void setPickView(){
        selectedDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        endDate.set(2100,11,30);
        timePicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                data_text.setText(DateUtil.getDaysStr(date).substring(0,10));
            }
        }).setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日",
                        "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setDividerColor(getResources().getColor(R.color.title_bottom_line))
                .setTextColorCenter(getResources().getColor(R.color.main_color))//设置选中项的颜色
                .setTextColorOut(getResources().getColor(R.color.background_gray3))//设置没有被选中项的颜色
                .setContentSize(21)
                .setDate(selectedDate)
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(-10, 0,10, 0,
                        0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }


    private void  PictureSelectorCreate(){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9-IntentselectList.size())// 最大图片选择数量 int
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

    private void AddTextView(String text,KahoLabelLayout labelLayout,int position){
        MyTextView textView = new MyTextView(this);
        textView.setText(text);
        textView.setTextSize(14);
        if(position > 0){
            textView.setBackground(getResources().getDrawable(R.drawable.radius_blue2));
            textView.setTextColor(getResources().getColor(R.color.white));
        }else {
            textView.setBackground(getResources().getDrawable(R.drawable.radius_white));
            textView.setTextColor(Color.parseColor("#333333"));
        }
        textView.setPadding(20, 20, 20, 20);
        labelLayout.addView(textView, marginLayoutParams);
    }

    private void Submit(String title,String mark,List<String> UploadSuccessPhotoName){

        reportModel.setImages(null);
        reportModel.setTitle(title);
        reportModel.setMark(mark);
        reportModel.setImageUrls(UploadSuccessPhotoName);
        String json = new Gson().toJson(reportModel);
        new PostUtils().JsonPost(Constant.report_update,json,submitHandler,this);

    }

    private void UploadImage(String path){
        new PostUtils().NewUploadImage(Constant.report_img_Upload,path,UploadHandler,this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.data_text:
                timePicker.show();
                break;
            case R.id.add_btn:
                if(adapter.getItemCount() == 0|| StringUtil.isNull(title.getText().toString())
                        || StringUtil.isNull(data_text.getText().toString())){
                    Toast.makeText(this, getString(R.string.health_report_null_tips), Toast.LENGTH_SHORT).show();
                }else{

                    //如果用户没有新增图片，则直接覆盖图片名提交
                    if(selectList.size() == 0){
                        //如果用户没有新增图片，并且删除了所有原有图片
                        if(IntentselectList.size()>0){

                            for (int i = 0;i<IntentselectList.size();i++){
                                UploadSuccessPhotoName.add(IntentselectList.get(i).getCompressPath());
                            }

                            Submit(edit_name.getText().toString(),
                                    StringUtil.trimNull(remark_text.getText().toString()),
                                    UploadSuccessPhotoName);
                        }else{
                            Toast.makeText(this, getString(R.string.health_report_null_tips), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        for (int i = 0;i<selectList.size();i++) {
                            //因为通过传递过来的网络图片对象只有getCompressPath是有值，
                            //网络图片不需要提交，所以要先筛选出来
                            if(selectList.get(i).getPath()!=null){
                                ListSize ++;
                                UploadImage(selectList.get(i).getCompressPath());
                            }
                        }
                    }

                }
                break;
            case R.id.img_add:
                PictureSelectorCreate();
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
                    if(selectList.size()>0){

                        for (int i = 0;i<IntentselectList.size();i++){
                            selectList.add(i,IntentselectList.get(i));
                        }
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    }
    

}
