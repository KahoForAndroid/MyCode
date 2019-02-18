package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Implements.Picture_OnItemClick;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.Model.UserInfo;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.GreenDaoManager;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/4.
 */

public class Health_Report_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back,img_add;
    private TextView title,data_text;
    private EditText edit_name,remark_text;
    private Button add_btn;
    private List<LocalMedia> selectList = null;
    private TimePickerView timePicker = null;
    private Calendar selectedDate,startDate,endDate;
    private RecyclerView img_recycle = null;
    private Health_Report_Adapter adapter = null;
    private String postName = "check/submit?";
    private Gson gson = new Gson();
    private StringBuilder UploadSuccessPhotoName = new StringBuilder();
    private int IndexSize = 0;
    private String reportId = null;
    private List<String> images = new ArrayList<>();
    private Handler submitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
//                            JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
//                            reportId = jsonObject1.optString("reportId");
                            Toast.makeText(Health_AppLocation.instance, R.string.commit_success, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Health_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Health_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
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
                                String url = jsonObject1.optString("url");
                                IndexSize ++;
                                images.add(url);
                                if(IndexSize == selectList.size()){
                                    Submit("1",edit_name.getText().toString(),remark_text.getText().toString());
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Health_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Health_Report_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Health_Report_Activity.this, getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_report_activity);
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
    }
    private void BindListener(){
        back.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        img_add.setOnClickListener(this);
        data_text.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.health_report));
        data_text.setText(DateUtil.getNowdayymd());
        selectList = new ArrayList<>();
        adapter = new Health_Report_Adapter(this,selectList);
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
        img_recycle.setLayoutManager(new GridLayoutManager(this,3));
        img_recycle.setAdapter(adapter);
        selectedDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        endDate.set(2010,0,1);
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
                .setRangDate(endDate,startDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();

    }
    private void  PictureSelectorCreate(){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量 int
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

    private void UploadImage(String path){
        new PostUtils().NewUploadImage(Constant.report_img_Upload,path,UploadHandler,this);
    }

    private void Submit(String type,String title,String mark){

        NewReportModel newReportModel = new NewReportModel();
        newReportModel.setType(type);
        newReportModel.setTitle(title);
        newReportModel.setMark(mark);
        newReportModel.setImageUrls(images);
        String json = new Gson().toJson(newReportModel);
        new PostUtils().JsonPost(Constant.report_Upload,json,submitHandler,this);

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
                if(selectList.size()==0 || StringUtil.isNull(title.getText().toString())
                        || StringUtil.isNull(data_text.getText().toString())){
                    Toast.makeText(this, getString(R.string.health_report_null_tips), Toast.LENGTH_SHORT).show();
                }else{
                        for (int i = 0;i<selectList.size();i++){
                            UploadImage(selectList.get(i).getCompressPath());
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
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    }
}
