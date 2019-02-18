package zj.health.health_v1.Fragment;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zj.health.health_v1.Activity.Health_Log_Activity;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/10.
 */

public class Body_blood_sugar_Fragment extends Fragment implements View.OnClickListener{

    private View view = null;
    private TimePickerView timePickerView = null;
    private EditText Blood_sugar_edit;
    private RelativeLayout date_rela,meal_rela,Postprandial_rela;
    private TextView dateText,meal_text,Postprandial_text;
    private Button commit_Button;
    private ImageView add_bluetooth;
    private long timesLong;
    private Handler submitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if (msg.obj!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){

                                Health_Log_Activity.isUploadGraphic = true;
                                Toast.makeText(Health_AppLocation.instance, getString(R.string.commit_success), Toast.LENGTH_SHORT).show();
                                getActivity().setResult(5);
                                getActivity().finish();

                        }else{
                            Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.body_blood_sugar_fragment,container,false);
        }
        initView();
        BindListener();
        setData();
        return view;
    }

    private void initView(){
        date_rela = (RelativeLayout)view.findViewById(R.id.date_rela);
        dateText = (TextView)view.findViewById(R.id.dateText);
        Blood_sugar_edit = (EditText)view.findViewById(R.id.Blood_sugar_edit);
        commit_Button = (Button)view.findViewById(R.id.commit_Button);
        add_bluetooth = (ImageView)view.findViewById(R.id.add_bluetooth);
        meal_rela = (RelativeLayout)view.findViewById(R.id.meal_rela);
        meal_text = (TextView)view.findViewById(R.id.meal_text);
        Postprandial_rela = (RelativeLayout)view.findViewById(R.id.Postprandial_rela);
        Postprandial_text = (TextView)view.findViewById(R.id.Postprandial_text);

    }
    private void BindListener(){
        commit_Button.setOnClickListener(this);
        add_bluetooth.setOnClickListener(this);
        date_rela.setOnClickListener(this);
        Postprandial_rela.setOnClickListener(this);
        meal_rela.setOnClickListener(this);
    }
    private void setData(){
        dateText.setText(DateUtil.getNowDay());
        timesLong = System.currentTimeMillis();
        TimePickerView.OnTimeSelectListener listener = new TimePickerView.OnTimeSelectListener(){

            @Override
            public void onTimeSelect(Date date, View v) {
                timesLong = date.getTime();
                dateText.setText(DateUtil.getNowdayymd()+" "+DateUtil.getDaysStrhm(date));
            }
        };
        timePickerView = new DateUtil().ShowTimePickerView(getActivity(),listener, Calendar.getInstance(),
                true,true,true,true,true,false);
    }

    /**
     * 提交测量数据
     */
    private void Submit(JSONObject jsonObject){

        new PostUtils().JsonPost(Constant.post_blood_glucose,jsonObject,submitHandler, (BaseActivity) getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.commit_Button:
                if(Blood_sugar_edit.getText().toString().length()<=0){
                    Toast.makeText(getActivity(), getString(R.string.all_input_tips), Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("bloodGlucose",Blood_sugar_edit.getText().toString());
                        jsonObject.put("recordTime",timesLong);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Submit(jsonObject);
                }
                break;
            case R.id.add_bluetooth:
                Toast.makeText(getActivity(), getString(R.string.connect_device_null), Toast.LENGTH_SHORT).show();
                break;
            case R.id.date_rela:
                timePickerView.show();
                break;
            case R.id.Postprandial_rela:

                meal_rela.setBackground(getResources().getDrawable(R.drawable.radius_black));
                Postprandial_rela.setBackground(getResources().getDrawable(R.drawable.radius_blue));
                Postprandial_text.setTextColor(getResources().getColor(R.color.white));
                meal_text.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.meal_rela:
                meal_rela.setBackground(getResources().getDrawable(R.drawable.radius_blue));
                Postprandial_rela.setBackground(getResources().getDrawable(R.drawable.radius_black));
                Postprandial_text.setTextColor(getResources().getColor(R.color.black));
                meal_text.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
}


