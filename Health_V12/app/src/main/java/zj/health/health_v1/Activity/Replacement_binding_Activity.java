package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zj.health.health_v1.Adapter.Binding_SelectedQuestion_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.SecurityModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Replacement_binding_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back,top_check_img,top_bottom_img;
    private TextView title,question_text,get_Verification_Code_text;
    private RelativeLayout question_rela,get_Verification_Code_rl;
    private EditText input_Answer,ed_phone,ed_Verification_Code;
    private LinearLayout gone_layout;
    private Button next_btn;
    private Binding_SelectedQuestion_Adapter adapter = null;
    private CreateDialog dialog = new CreateDialog();
    private boolean isClickQuestion = false;//标识是否选择了问题
    private boolean nextChecked = false;//是否满足下一步的条件
    private List<SecurityModel> securityModelList;
    private Timer timer;
    private int second = 60;//用于发送验证码倒数
    private Timertask timertask;
    private int selectType = 1;//标识选择方式 1是通过问题 2是通过亲友
    private String oldPhone = null;//旧手机
    private String oldPhoneCode = null;//旧手机验证码
    private String sqId = null;
    private String SecurityJson = "[\n" +
            "        {\n" +
            "            \"id\": \"5b4ef45d62433d3b25dfee65\",\n" +
            "            \"sid\": 1,\n" +
            "            \"question\": \"您的女儿的姓名\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"5b4ef4e08050fc3b25fc8066\",\n" +
            "            \"sid\": 2,\n" +
            "            \"question\": \"您的儿子的姓名\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"5b4ef4ed8050fc3b25fc8067\",\n" +
            "            \"sid\": 3,\n" +
            "            \"question\": \"您的父亲的姓名\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"5b4ef4fc8050fc3b25fc8068\",\n" +
            "            \"sid\": 4,\n" +
            "            \"question\": \"您的老伴的姓名\"\n" +
            "        }\n" +
            "    ]";

    private Handler getVerification_Code_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject jsonObject = new JSONObject(msg.obj.toString());
                if(jsonObject.opt("msg").equals("success")){
                    timer = new Timer();
                    timertask = new Timertask();
                    timer.schedule(timertask,1000,1000);
                    Toast.makeText(Replacement_binding_Activity.this, getString(R.string.send_verification_code_success), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Replacement_binding_Activity.this, jsonObject.opt("msg").toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repllacement_binding_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        top_check_img = (ImageView)findViewById(R.id.top_check_img);
        top_bottom_img = (ImageView)findViewById(R.id.top_bottom_img);
        question_text = (TextView)findViewById(R.id.question_text);
        get_Verification_Code_text = (TextView)findViewById(R.id.get_Verification_Code_text);
        title = (TextView)findViewById(R.id.title);
        question_rela = (RelativeLayout)findViewById(R.id.question_rela);
        get_Verification_Code_rl = (RelativeLayout)findViewById(R.id.get_Verification_Code_rl);
        input_Answer = (EditText)findViewById(R.id.input_Answer);
        ed_phone = (EditText)findViewById(R.id.ed_phone);
        ed_Verification_Code = (EditText)findViewById(R.id.ed_Verification_Code);
        gone_layout = (LinearLayout)findViewById(R.id.gone_layout);
        next_btn = (Button)findViewById(R.id.next_btn);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        top_check_img.setOnClickListener(this);
        top_bottom_img.setOnClickListener(this);
        get_Verification_Code_rl.setOnClickListener(this);
        question_rela.setOnClickListener(this);
        question_text.setOnClickListener(this);
        input_Answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(input_Answer.getText().length()>0 && isClickQuestion){
                    if(gone_layout.getVisibility() ==View.GONE){
                        nextChecked = true;
                        next_btn.setBackground(getResources().getDrawable(R.drawable.on_click_button));
                    }
                }else{
                    nextChecked = false;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.gary_button));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed_phone.getText().length()>0 && ed_Verification_Code.getText().length()>0){
                    nextChecked = true;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.on_click_button));
                }else{
                    nextChecked = false;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.gary_button));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_Verification_Code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed_phone.getText().length()>0 && ed_Verification_Code.getText().length()>0){
                    nextChecked = true;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.on_click_button));
                }else{
                    nextChecked = false;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.gary_button));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private void setData(){
        title.setText(getString(R.string.Replacement_binding_phone));
        oldPhone = getIntent().getStringExtra("phone");
        securityModelList = new Gson().fromJson(SecurityJson,new TypeToken<List<SecurityModel>>(){}.getType());
        adapter = new Binding_SelectedQuestion_Adapter(this,securityModelList);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                isClickQuestion = true;
                sqId = securityModelList.get(position).getId();
                question_text.setText(securityModelList.get(position).getQuestion());
                dialog.getPopupWindow().dismiss();
                if(input_Answer.getText().length()>0){
                    nextChecked = true;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.on_click_button));
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
            finish();
            break;
            case R.id.next_btn:
                if(nextChecked){
                    Intent it = new Intent(this,Replacement_binding_Last_Activity.class);

                    if(selectType == 1){
                        it.putExtra("selectType",selectType);
                        it.putExtra("sqId",sqId);
                        it.putExtra("oldphone",oldPhone);
                        it.putExtra("answer",input_Answer.getText().toString());
                        startActivity(it);
                    }else{
                        if(ed_phone.getText().toString().length() == 11 &&
                                ed_Verification_Code.getText().toString().length() == 6){
                            it.putExtra("selectType",selectType);
                            it.putExtra("oldphone",oldPhone);
                            it.putExtra("friendphone",ed_phone.getText().toString());
                            it.putExtra("code",ed_Verification_Code.getText().toString());
                            startActivity(it);
                        }else{
                            Toast.makeText(this, R.string.next_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(this, R.string.next_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.top_check_img:
                selectType = 1;
                top_check_img.setImageDrawable(getResources().getDrawable(R.drawable.mian_checkbox_checked));
                top_bottom_img.setImageDrawable(getResources().getDrawable(R.drawable.mian_checkbox_notchecked));
                gone_layout.setVisibility(View.GONE);
                if(input_Answer.getText().length()>0 && isClickQuestion){
                    nextChecked = true;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.on_click_button));
                }else{
                    nextChecked = false;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.gary_button));
                }
                break;
            case R.id.top_bottom_img:
                selectType = 2;
                top_check_img.setImageDrawable(getResources().getDrawable(R.drawable.mian_checkbox_notchecked));
                top_bottom_img.setImageDrawable(getResources().getDrawable(R.drawable.mian_checkbox_checked));
                gone_layout.setVisibility(View.VISIBLE);
                if(ed_phone.getText().length()>0 && ed_Verification_Code.getText().length()>0){
                    nextChecked = true;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.on_click_button));
                }else{
                    nextChecked = true;
                    next_btn.setBackground(getResources().getDrawable(R.drawable.gary_button));
                }
                break;
            case R.id.get_Verification_Code_rl:
                if(ed_phone.getText().toString().length() == 11){
                    getCode(ed_phone.getText().toString());
                }else{
                    Toast.makeText(this, getString(R.string.input_phoneNumber_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.question_rela:
                break;
            case R.id.question_text:
                dialog.QuestionDailog(this,
                        View.inflate(this,R.layout.binding_question_dialog_layout,null),
                        this,adapter);
                break;

        }
    }
    private class Timertask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(second > 0){
                        second --;
                        get_Verification_Code_rl.setEnabled(false);
                        get_Verification_Code_rl.setBackgroundColor(getResources().getColor(R.color.image_overlay_false));
                        get_Verification_Code_text.setText(getString(R.string.get_Verification_Code)+"("+second+")");
                    }else{
                        get_Verification_Code_rl.setEnabled(true);
                        get_Verification_Code_rl.setBackground(getResources().getDrawable(R.drawable.layou_onclick));
                        get_Verification_Code_text.setText(getString(R.string.get_Verification_Code));
                        timertask.cancel();
                        second = 60;//恢复60秒
                    }
                }
            });
        }
    }



    private void getCode(String phone){
        String get = "oldPhone"+"="+oldPhone+"&"+"friendPhone"+"="+phone;
        new PostUtils().Get(Constant.friend_to_send+get,false,
                getVerification_Code_Handler,this);
    }
}
