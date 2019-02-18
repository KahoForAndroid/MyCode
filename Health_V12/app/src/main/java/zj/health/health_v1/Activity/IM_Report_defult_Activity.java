package zj.health.health_v1.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import zj.health.health_v1.Adapter.Health_Report_Adapter;
import zj.health.health_v1.Adapter.IM_Health_Report_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.Picture_OnItemClick;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.MyView.KahoLabelLayout;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/9/12.
 */

public class IM_Report_defult_Activity extends BaseActivity implements View.OnClickListener{

    private ViewGroup.MarginLayoutParams marginLayoutParams;
    private ImageView back,img_add;
    private TextView title,data_text;
    private EditText edit_name,remark_text;
    private Button add_btn;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> IntentselectList = new ArrayList<>();
    private RecyclerView img_recycle = null;
    private IM_Health_Report_Adapter adapter = null;
    private KahoLabelLayout labellayout;
    private List<String> stringList = new ArrayList<>();
    private NewReportModel reportModel = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_report_commit_activity);
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

        adapter = new IM_Health_Report_Adapter(this,selectList);
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
        title.setText(getString(R.string.Medical_Examinatoin_Report));
        add_btn.setVisibility(View.GONE);
        reportModel = (NewReportModel) getIntent().getBundleExtra("bundle").getSerializable("model");//获取报告对象
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
        adapter.setOnItemClick(new Picture_OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position, boolean isDelete) {

            }
        });

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


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.back:
                finish();
                break;
                default:
                    break;
        }
    }

}
