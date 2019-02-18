package zj.health.health_v1.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.AllCheckFilterModel;
import zj.health.health_v1.Model.SecurityModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/3.
 */

public class Register_Security_Activity extends BaseActivity implements View.OnClickListener{

    private TextView title,select_question1,select_question2;
    private ImageView back;
    private Button next_btn;
    private RelativeLayout question1_rl,question1_rl2;
    private List<SecurityModel> securityModelList;
    private List<String> Stringlist = new ArrayList<>();
    private EditText input_Answer,input_Answer2;
    private int OnClickSelectItem = 0;
    private int question1Item = 0;
    private int question2Item = 0;
    private boolean selectQuestion1 = false;
    private boolean selectQuestion2 = false;
    private OptionsPickerView optionsPickerView;
    private String phone = null;
    private String referral = null;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_security_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        next_btn = (Button)findViewById(R.id.next_btn);
        question1_rl = (RelativeLayout)findViewById(R.id.question1_rl);
        question1_rl2 = (RelativeLayout)findViewById(R.id.question1_rl2);
        select_question1 = (TextView)findViewById(R.id.select_question1);
        select_question2 = (TextView)findViewById(R.id.select_question2);
        input_Answer = (EditText)findViewById(R.id.input_Answer);
        input_Answer2 = (EditText)findViewById(R.id.input_Answer2);

    }

    private void BindListener(){
        back.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        question1_rl.setOnClickListener(this);
        question1_rl2.setOnClickListener(this);
    }
    private void setData(){
        title.setText(R.string.Security_setting);
        phone = getIntent().getStringExtra("phone");
        referral = getIntent().getStringExtra("referral");
        securityModelList = new Gson().fromJson(SecurityJson,new TypeToken<List<SecurityModel>>(){}.getType());
        for (int i = 0;i<securityModelList.size();i++){
            Stringlist.add(securityModelList.get(i).getQuestion());
        }

    }

    private void ShowPickView(){
        if(optionsPickerView == null) {
            optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                     if(OnClickSelectItem == 0){
                         selectQuestion1 = true;
                         question1Item = options1;
                         select_question1.setText(Stringlist.get(options1));
                     }else{
                         selectQuestion2 = true;
                         question2Item = options1;
                         select_question2.setText(Stringlist.get(options1));
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
                    .setLinkage(false)//设置是否联动，默认true
//                .setLabels("", "","")//设置选择的三级单位
                    .setCyclic(false, false, false)//循环与否
                    .setSelectOptions(1)//设置默认选中项
                    .setOutSideCancelable(false)//点击外部dismiss default true
                    .build();
            optionsPickerView.setPicker(Stringlist);
            optionsPickerView.show();
        }else{
            optionsPickerView.show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.next_btn:

                if(selectQuestion1 && selectQuestion2){
                    if(select_question1.getText().toString().equals(select_question2.getText().toString())){
                        Toast.makeText(this, R.string.choose_the_same_problem_error, Toast.LENGTH_SHORT).show();
                    }else{
                        if(input_Answer.getText().toString().length()>0 &&
                                input_Answer2.getText().toString().length()>0){

                            Intent it = new Intent(this,Register_SettingData_Activity.class);
                            it.putExtra("phone",phone);
                            it.putExtra("question1",Stringlist.get(question1Item));
                            it.putExtra("answer1",input_Answer.getText().toString());
                            it.putExtra("question2",Stringlist.get(question2Item));
                            it.putExtra("answer2",input_Answer.getText().toString());
                            it.putExtra("referral",referral);
                            startActivity(it);
                        }else{
                            Toast.makeText(this, R.string.input_answer, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(!selectQuestion1 && !selectQuestion2){
                    Intent it = new Intent(this,Register_SettingData_Activity.class);
                    it.putExtra("phone",phone);
                    it.putExtra("referral",referral);
                    startActivity(it);
                }else if(selectQuestion1 || selectQuestion2){
                    Toast.makeText(this, R.string.setting_question_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.question1_rl:
                OnClickSelectItem = 0;
                ShowPickView();
                break;
            case R.id.question1_rl2:
                OnClickSelectItem = 1;
                ShowPickView();
                break;

        }
    }
}

