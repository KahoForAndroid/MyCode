package zj.health.health_v1.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Model.DrugNameModel;
import zj.health.health_v1.MyView.KahoLabelLayout;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.GreenDaoManager;

/**
 * Created by Administrator on 2018/6/20.
 */

public class Add_drug_for_calendar_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;
    private KahoLabelLayout labe_layout;
    private Button cleanButton,addButton;
    private EditText input_edit;
    private ViewGroup.MarginLayoutParams marginLayoutParams;
    private DbUtils dbUtils = new DbUtils();
    private List<DrugNameModel> drugNameModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_drug_for_calendar_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        labe_layout = (KahoLabelLayout)findViewById(R.id.labe_layout);
        cleanButton = (Button)findViewById(R.id.cleanButton);
        addButton = (Button)findViewById(R.id.addButton);
        input_edit = (EditText)findViewById(R.id.input_edit);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        cleanButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        labe_layout.setOnitemOnClickListener(new KahoLabelLayout.OnitemOnClickListener() {
            @Override
            public void OnitemClick(View view, int position) {
                input_edit.setText(((MyTextView)view).getText());
                labe_layout.requestOnClickView(position);
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.N)
    private void setData(){
        title.setText(getString(R.string.medicineName_null_error));
        marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(10, 10, 10, 10);

        drugNameModelList = dbUtils.getDrugNameDataList();
        Set<String> nameModelSet = new HashSet<>();
//        nameModelSet.addAll(drugNameModelList);
//        drugNameModelList.addAll(nameModelSet);
        if(drugNameModelList!=null && drugNameModelList.size()>0){
            for (int i = 0 ;i<drugNameModelList.size();i++){
                String name = drugNameModelList.get(i).getDrugName();
                nameModelSet.add(name);
            }
        }
        Iterator iterator = nameModelSet.iterator();
        while (iterator.hasNext()){
            AddTextView((String) iterator.next(),labe_layout);
        }
    }



    private void AddTextView(String text,KahoLabelLayout labelLayout){
        MyTextView textView = new MyTextView(this);
        textView.setText(text);
        textView.setTextSize(14);
        textView.setBackground(getResources().getDrawable(R.drawable.radius_kaho));
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setPadding(20, 20, 20, 20);
        labelLayout.addView(textView, marginLayoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.cleanButton:
                GreenDaoManager.getInstance().getSession().getDrugNameModelDao().deleteAll();
                drugNameModelList = dbUtils.getDrugNameDataList();
                Set<String> nameModelSet = new HashSet<>();
                if(drugNameModelList!=null && drugNameModelList.size()>0){
                    for (int i = 0 ;i<drugNameModelList.size();i++){
                        String name = drugNameModelList.get(i).getDrugName();
                        nameModelSet.add(name);
                    }
                }
                Iterator iterator = nameModelSet.iterator();
                while (iterator.hasNext()){
                    AddTextView((String) iterator.next(),labe_layout);
                }
                break;
            case R.id.addButton:
                DrugNameModel drugNameModel = new DrugNameModel();
                drugNameModel.setDrugName(input_edit.getText().toString());
                if(drugNameModelList!=null && drugNameModelList.size()>0){
                    drugNameModel.setId(drugNameModelList.size());
                }else{
                    drugNameModel.setId(0);
                }
                dbUtils.insertDrugNameModelData(this,drugNameModel);
                int position = getIntent().getIntExtra("position",0);
                Intent it = new Intent();
                it.putExtra("position",position);
                it.putExtra("name",input_edit.getText().toString());
                setResult(0x112,it);
                finish();
                break;
        }
    }
}
