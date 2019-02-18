package zj.health.health_v1.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zj.health.health_v1.Activity.My_Activity;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.ConsultSettingModel;
import zj.health.health_v1.Model.LiveMode;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Consultation_Setting_Fragment extends Fragment implements View.OnClickListener{

    private View view = null;
    private RelativeLayout Graphic_consulting_price_rela,Video_consulting_price_rela,consultingType_rela,region_rela;
    private TextView Graphic_consulting_price_text,Video_consulting_price_text,area_text,consulting_text;
    private EditText introduction_edit,speciality_edit;
    private Button next_btn;
    private String address[];
    private String Communication_mode[];
    private ConsultSettingModel consultSettingModel = new ConsultSettingModel();
    private OptionsPickerView optionsPickerView;
    private List<String> regionList = new ArrayList<>();//用于选择地区
    private List<String> Communication_modeList = new ArrayList<>();//用于选择咨询方式
    private CreateDialog dialog = new CreateDialog();
    private int PickViewType = 1;
    private Handler edit_chat_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            consultSettingModel.setChatPrice(Float.parseFloat(msg.obj.toString()));
            Graphic_consulting_price_text.setText(msg.obj.toString()+"元/次");
        }
    };
    private Handler edit_video_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            consultSettingModel.setVideoPrice(Float.parseFloat(msg.obj.toString()));
            Video_consulting_price_text.setText(msg.obj.toString()+"元/次");
        }
    };

    private Handler consult_settingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            consultSettingModel = new Gson().fromJson(jsonObject.optString("data"),ConsultSettingModel.class);
                            if(consultSettingModel!=null){
                                Graphic_consulting_price_text.setText(consultSettingModel.getChatPrice()+"元");
                                Video_consulting_price_text.setText(consultSettingModel.getVideoPrice()+"元");
                                area_text.setText(address[Integer.parseInt(consultSettingModel.getRegion())]);
                                consulting_text.setText(Communication_mode[Integer.parseInt(consultSettingModel.getCommunicationType())]);
                                if(consultSettingModel.getIntroduction()!=null && consultSettingModel.getIntroduction().length()>0){
                                    introduction_edit.setText(StringUtil.trimNull(consultSettingModel.getIntroduction()));
                                    introduction_edit.setSelection(consultSettingModel.getIntroduction().length());
                                }
                                if(consultSettingModel.getSpeciality()!=null && consultSettingModel.getSpeciality().length()>0){
                                    speciality_edit.setText(StringUtil.trimNull(consultSettingModel.getSpeciality()));
                                    speciality_edit.setSelection(consultSettingModel.getSpeciality().length());
                                }
                            }else{
                                consultSettingModel = new ConsultSettingModel();
                            }
                        }else {
                            Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }}
    };

    private Handler Post_consult_settingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(msg.obj!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            Toast.makeText(getActivity(), getString(R.string.save_succ), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }}
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.consultation_setting_fragment,container,false);
        initView();
        BindListener();
        setData();
        return view;
    }
    private void initView(){
        Graphic_consulting_price_rela = (RelativeLayout)view.findViewById(R.id.Graphic_consulting_price_rela);
        Video_consulting_price_rela = (RelativeLayout)view.findViewById(R.id.Video_consulting_price_rela);
        consultingType_rela = (RelativeLayout)view.findViewById(R.id.consultingType_rela);
        region_rela = (RelativeLayout)view.findViewById(R.id.region_rela);
        Graphic_consulting_price_text = (TextView)view.findViewById(R.id.Graphic_consulting_price_text);
        Video_consulting_price_text = (TextView)view.findViewById(R.id.Video_consulting_price_text);
        area_text = (TextView)view.findViewById(R.id.area_text);
        consulting_text = (TextView)view.findViewById(R.id.consulting_text);
        next_btn = (Button)view.findViewById(R.id.next_btn);
        introduction_edit = (EditText)view.findViewById(R.id.introduction_edit);
        speciality_edit = (EditText)view.findViewById(R.id.speciality_edit);
    }
    private void BindListener(){
        Graphic_consulting_price_rela.setOnClickListener(this);
        Video_consulting_price_rela.setOnClickListener(this);
        consultingType_rela.setOnClickListener(this);
        region_rela.setOnClickListener(this);
        next_btn.setOnClickListener(this);
    }
    private void setData(){
        address = getActivity().getResources().getStringArray(R.array.address);
        Communication_mode = getActivity().getResources().getStringArray(R.array.Communication_mode);
        getConsult_Setting();
    }

    private void getConsult_Setting(){
        new PostUtils().Get(Constant.consult_setting,true,consult_settingHandler,getActivity());
    }
    private void PostConsult_Setting(String json){
        new PostUtils().JsonPost(Constant.consult_setting,json,Post_consult_settingHandler, (BaseActivity) getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Graphic_consulting_price_rela:
                dialog.EditDailog(getActivity(),
                        View.inflate(getActivity(),R.layout.edit_dialog,null),
                        edit_chat_Handler);
                break;
            case R.id.Video_consulting_price_rela:
                dialog.EditDailog(getActivity(),
                        View.inflate(getActivity(),R.layout.edit_dialog,null),
                        edit_video_Handler );
                break;
            case R.id.consultingType_rela:
                PickViewType = 2;
                ShowRegionPickView();
                break;
            case R.id.region_rela:
                PickViewType = 1;
                ShowRegionPickView();
                break;
            case R.id.next_btn:
                if(StringUtil.isEmpty(consultSettingModel.getRegion())){
                    Toast.makeText(getActivity(), getString(R.string.select_region_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isEmpty(consultSettingModel.getCommunicationType())){
                    Toast.makeText(getActivity(), getString(R.string.CommunicationType_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(consultSettingModel.getChatPrice() == 0){
                    Toast.makeText(getActivity(), getString(R.string.select_region_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(consultSettingModel.getVideoPrice() == 0){
                    Toast.makeText(getActivity(), getString(R.string.VideoPrice_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(introduction_edit.getText().toString().length()>0){
                    consultSettingModel.setIntroduction(introduction_edit.getText().toString());
                }else{
                    Toast.makeText(getActivity(), getString(R.string.intput_introduction), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(speciality_edit.getText().toString().length()>0){
                    consultSettingModel.setSpeciality(speciality_edit.getText().toString());
                }else{
                    Toast.makeText(getActivity(), getString(R.string.intput_speciality), Toast.LENGTH_SHORT).show();
                    return;
                }
                String json = new Gson().toJson(consultSettingModel);
                PostConsult_Setting(json);
                break;
                default:
                    break;
        }
    }

    /**
     * 滚轮选择器
     */
    private void ShowRegionPickView(){
        if(optionsPickerView == null) {
            optionsPickerView = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if(PickViewType == 1){
                        area_text.setText(regionList.get(options1));
                        consultSettingModel.setRegion(options1+"");
                    }else{
                        consulting_text.setText(Communication_modeList.get(options1));
                        consultSettingModel.setCommunicationType(options1+"");
                    }
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

            if(PickViewType == 1){
                regionList = Arrays.asList(address);
                optionsPickerView.setPicker(regionList);
            }else{
                Communication_modeList = Arrays.asList(Communication_mode);
                optionsPickerView.setPicker(Communication_modeList);
            }
            optionsPickerView.show();
        }else{
            if(PickViewType == 1){
                regionList = Arrays.asList(address);
                optionsPickerView.setPicker(regionList);
            }else{
                Communication_modeList = Arrays.asList(Communication_mode);
                optionsPickerView.setPicker(Communication_modeList);
            }
            optionsPickerView.show();
        }
    }
}
