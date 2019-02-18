package zj.health.health_v1.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.ParameterizedTypeUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Certificate_Adapter;
import zj.health.health_v1.Adapter.Hospital_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Implements.Picture_OnItemClick;
import zj.health.health_v1.Model.HospitalModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.ImageUtils;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/9/13.
 */

public class RegisterDoctor_Supplement_Activity extends BaseActivity implements View.OnClickListener{

    private Button next_btn,select_workaddress_btn;
    private Intent it = null;
    private Map<String,String> map = new HashMap<>();
    private ImageView icon_head,id_img,back;
    private TextView Doctor_certificate_photo_count_text,Department_ed,Job_Title_ed;
    private EditText nick_name_ed;
    private int SelectPictureType = 0;//1.用户头像 2.身份证 3.执业证书
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> certificateList = new ArrayList<>();
    private List<String> certificatePathList = new ArrayList<>();
    private boolean isSelectPhoto = false;
    private ImageUtils imageUtils;
    private FileOutputStream outputStream = null;
    private final int compressIndex = 60;
    private Bitmap bitmap = null;
    private CreateDialog dialog = new CreateDialog();
    private View dialogView = null;
    private RecyclerView Hospital_Recy;
    private Hospital_Adapter adapter;
    private List<HospitalModel> hospitalModelList = new ArrayList<>();
    private String HospitalId = null;
    private String UserIconPath = null;
    private String IDCardPath = null;
    private Certificate_Adapter certificate_adapter;
    private RecyclerView Certificate_recy;
    private int certificateSize = 0;
    private OptionsPickerView optionsPickerView;
    private int DepartmentId,JobId;
    private Handler hospital_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        hospitalModelList = new Gson().
                                fromJson(jsonObject.optString("data"),new TypeToken<List<HospitalModel>>(){}.getType());

                        if(dialogView==null){
                            dialogView = View.inflate(RegisterDoctor_Supplement_Activity.this, R.layout.select_hostial,null);
                            Hospital_Recy = (RecyclerView)dialogView.findViewById(R.id.Hospital_Recy);
                            Hospital_Recy.setLayoutManager(new LinearLayoutManager(RegisterDoctor_Supplement_Activity.this));
                            Hospital_Recy.setAdapter(adapter);
                            dialog.MessageDailog(RegisterDoctor_Supplement_Activity.this,
                                    dialogView,
                                    RegisterDoctor_Supplement_Activity.this);
                            adapter.setList(hospitalModelList);
                        }else{
                            dialog.MessageDailog(RegisterDoctor_Supplement_Activity.this,
                                    dialogView,
                                    RegisterDoctor_Supplement_Activity.this);
                            adapter.setList(hospitalModelList);
                        }
                    }else{
                        Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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
                        UserIconPath = jsonObject1.optString("url");
                        PostPhoto(Constant.doctorId_img,IDCardPath,idcard_Handler);
                    }else{
                        Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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
                                    certificateList.get(i).getCompressPath(),certificate_Handler);
                        }
                    }else{
                        Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
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
                        JSONObject jsonObjects = new JSONObject(jsonObject.optString("data"));
                        certificatePathList.add(jsonObjects.optString("url"));
                        synchronized (this){
                            certificateSize ++;
                            if(certificateSize == certificateList.size()){
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("nickname",nick_name_ed.getText().toString());
                                jsonObject1.put("office",DepartmentId+"");
                                jsonObject1.put("title",JobId+"");
                                jsonObject1.put("hospitalId",HospitalId);
                                jsonObject1.put("iconUrl",UserIconPath);
                                jsonObject1.put("idUrl",IDCardPath);

                                JSONArray jsonArray = new JSONArray();
                                jsonArray.put(certificatePathList);
                                jsonObject1.put("certUrls",jsonArray);

                                RegisterPost(jsonObject1);
                            }
                        }
                    }else{
                        Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };



    private Handler register_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
//                        it = new Intent(RegisterDoctor_Supplement_Activity.this,MainsActivity.class);
//                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(it);
                        Toast.makeText(Health_AppLocation.instance, R.string.data_commit_success_wait, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterDoctor_Supplement_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_doctor_data_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        next_btn = (Button)findViewById(R.id.next_btn);
        back = (ImageView)findViewById(R.id.back);
        icon_head = (ImageView)findViewById(R.id.icon_head);
        nick_name_ed = (EditText) findViewById(R.id.nick_name_ed);
        Department_ed = (TextView)findViewById(R.id.Department_ed);
        Job_Title_ed = (TextView)findViewById(R.id.Job_Title_ed);
        select_workaddress_btn = (Button)findViewById(R.id.select_workaddress_btn);
        id_img = (ImageView)findViewById(R.id.id_img);
        Certificate_recy = (RecyclerView)findViewById(R.id.Certificate_recy);
        Doctor_certificate_photo_count_text = (TextView)findViewById(R.id.Doctor_certificate_photo_count_text);
    }
    private void BindListener(){
        select_workaddress_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        icon_head.setOnClickListener(this);
        id_img.setOnClickListener(this);
        back.setOnClickListener(this);
        Department_ed.setOnClickListener(this);
        Job_Title_ed.setOnClickListener(this);


    }

    private void setData(){
        imageUtils = new ImageUtils();
        adapter = new Hospital_Adapter(RegisterDoctor_Supplement_Activity.this,hospitalModelList);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                HospitalId = hospitalModelList.get(position).getId();
                select_workaddress_btn.setText(hospitalModelList.get(position).getHospitalName());
                dialog.getPopupWindow().dismiss();
            }
        });

        Certificate_recy.setLayoutManager(new GridLayoutManager(this,3));
        certificate_adapter = new Certificate_Adapter(this,certificateList);
        Certificate_recy.setAdapter(certificate_adapter);
        certificate_adapter.setOnItemClick(new Picture_OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position, boolean isDelete) {
                if(isDelete){
                    selectList.remove(position);
                    certificate_adapter.setList(selectList);
                    certificate_adapter.notifyDataSetChanged();
                    Doctor_certificate_photo_count_text.setText(selectList.size()+"/3");
                }else{
                    SelectPictureType = 3;
                    PictureSelectorCreate(3);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_workaddress_btn:
                if(hospitalModelList.size() == 0){
                    GetHospitalList();
                }else{
                    dialog.MessageDailog(this,
                            dialogView,
                            this);
                    adapter.notifyDataSetChanged();

                }
                break;
            case R.id.next_btn:
                if(UserIconPath == null){
                    Toast.makeText(this, R.string.icon_head_null_error, Toast.LENGTH_SHORT).show();
                }else if(IDCardPath == null){
                    Toast.makeText(this, R.string.id_card_img_null, Toast.LENGTH_SHORT).show();
                }else if(selectList.size() == 0){
                    Toast.makeText(this, R.string.Certificate_null, Toast.LENGTH_SHORT).show();
                }else{
                    if(nick_name_ed.getText().toString().length()>0 &&
                            Department_ed.getText().toString().length()>0 &&
                            Job_Title_ed.getText().toString().length()>0){
                        PostPhoto(Constant.doctoricon,UserIconPath,uploadHandler);
//                        Intent it = new Intent();
//                        it.putExtra("doctorIconPath",UserIconPath);
//                        it.putExtra("idCardPath",IDCardPath);
//                        it.putExtra("HospitalId",HospitalId);
//                        it.putExtra("name",nick_name_ed.getText().toString());
//                        it.putExtra("department",Department_ed.getText().toString());
//                        it.putExtra("job",Job_Title_ed.getText().toString());
//
//                        StringBuffer stringBuffer = new StringBuffer();
//                        for (int i = 0;i<selectList.size();i++){
//                            stringBuffer.append(selectList.get(i).getCompressPath()+";");
//                        }
//                        it.putExtra("certificatePhotos",stringBuffer.toString());
//
//                        setResult(111,it);
//                        finish();
                    }else{
                        Toast.makeText(this, R.string.next_error, Toast.LENGTH_SHORT).show();
                    }
                }
//                it = new Intent(Commit_DoctorData_Activity.this,Wait_for_audit_Activity.class);
//                startActivity(it);
                break;
            case R.id.id_img:
                SelectPictureType = 2;
                PictureSelectorCreate(1);
                break;
//            case R.id.add_img:
//                SelectPictureType = 3;
//                PictureSelectorCreate(3);
//                break;
            case R.id.icon_head:
                SelectPictureType = 1;
                PictureSelectorCreate(1);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.Department_ed:
                List<String> DepartmentList = Arrays.asList(getResources().getStringArray(R.array.Selection_section));
                ShowModePickView(DepartmentList,Department_ed);
                break;
            case R.id.Job_Title_ed:
                List<String> JobList = Arrays.asList(getResources().getStringArray(R.array.dorctor_leavel));
                ShowModePickView(JobList,Job_Title_ed);
                break;
            default:
                break;

        }
    }



    private void  PictureSelectorCreate(int pictureNum){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(3)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
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





    //上传图片
    private void PostPhoto(String postName,String path,Handler handler){
        new PostUtils().NewUploadImage(postName,path,handler,this);
    }
    private void RegisterPost(JSONObject jsonObject){
        new PostUtils().RegisterPost(Constant.doctor_register,jsonObject,register_Handler,this);
    }

    //上传执业照片
    private void PostCertificate(String postName,String path,Handler handler){
        new PostUtils().NewUploadImage(postName,path,handler,this);
    }

    //获取医院列表
    private void GetHospitalList(){
        new PostUtils().Get(Constant.doctor_hospital_config,true,
                hospital_Handler,RegisterDoctor_Supplement_Activity.this);
    }



    //用于显示医生头像的异步处理 因为头像要转换成圆形
    public class ImageAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                bitmap  = imageUtils.toRoundBitmap(BitmapFactory.decodeFile(selectList.get(0).getCompressPath()));
                UserIconPath = Constant.User_Icon_Path+Constant.doctorIcon_Name;
                File file = new File(Constant.User_Icon_Path,Constant.doctorIcon_Name);
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressIndex, outputStream);
                PictureFileUtils.deleteCacheDirFile(RegisterDoctor_Supplement_Activity.this);
                PictureFileUtils.deleteExternalCacheDirFile(RegisterDoctor_Supplement_Activity.this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(outputStream!=null){
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
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



    private void ShowModePickView(final List<String> stringList, final View view){
        optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(view instanceof TextView){
                    ((TextView) view).setText(stringList.get(options1));
                    if(view.getId() == R.id.Department_ed){
                        DepartmentId = options1;
                    }else{
                        JobId = options1;
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
                        if(SelectPictureType == 1){
                            if(selectList.get(0).isCompressed()){
                                new ImageAsyncTask().execute();
                                //icon_head.setImageURI(Uri.fromFile(new File(selectList.get(0).getCompressPath())));
                            }
                        }else if(SelectPictureType == 2){
                            IDCardPath = selectList.get(0).getCompressPath();
                            Bitmap bitmap = BitmapFactory.decodeFile(selectList.get(0).getCompressPath());
                            id_img.setImageBitmap(bitmap);

                        }else if(SelectPictureType == 3){
                            certificateList = PictureSelector.obtainMultipleResult(data);
                            Doctor_certificate_photo_count_text.setText(certificateList.size()+"/3");
                            certificate_adapter.setList(certificateList);
                        }

                    }

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(dialog.getPopupWindow() == null){
            finish();
        }else{
            if(dialog.getPopupWindow().isShowing()){
                dialog.getPopupWindow().dismiss();
            }else{
                finish();
            }
        }

    }
}

