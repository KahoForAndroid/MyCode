package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Model.BodyModel;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/13.
 */

public class Head_Report_Activity extends BaseActivity implements View.OnClickListener{

    private MyTextView brain_textview,eyes_textview,oral_cavity_textview,
            Otorhinolaryngology_textview,skin_textview;
    private ImageView back;
    private Button commit_btn;
    private MyTextView[] myTextViews = null;
    private int update = 0;
    private BodyModel bodyModel = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.head_report_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        brain_textview = (MyTextView)findViewById(R.id.brain_textview);
        eyes_textview = (MyTextView)findViewById(R.id.eyes_textview);
        oral_cavity_textview = (MyTextView)findViewById(R.id.oral_cavity_textview);
        Otorhinolaryngology_textview = (MyTextView)findViewById(R.id.Otorhinolaryngology_textview);
        skin_textview = (MyTextView)findViewById(R.id.skin_textview);
        commit_btn = (Button)findViewById(R.id.commit_btn);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        brain_textview.setOnClickListener(this);
        eyes_textview.setOnClickListener(this);
        oral_cavity_textview.setOnClickListener(this);
        Otorhinolaryngology_textview.setOnClickListener(this);
        skin_textview.setOnClickListener(this);
        commit_btn.setOnClickListener(this);
    }
    private void setData(){
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("bundle");
        bodyModel = (BodyModel) bundle.getSerializable("body");

        for (int i = 0;i<bodyModel.getSubItem().size();i++){
            if(bodyModel.getSubItem().get(i).getName().equals("脑")){
                brain_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("眼睛")){
                eyes_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("耳鼻喉")){
                Otorhinolaryngology_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("口腔")){
                oral_cavity_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("皮肤")){
                skin_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }
        }

        myTextViews = new MyTextView[]{
                brain_textview,eyes_textview,Otorhinolaryngology_textview,
                oral_cavity_textview,skin_textview
        };


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                setResult(update);
                finish();
                break;
            case R.id.commit_btn:
                List<String> list = new ArrayList<>();
                List<Integer> idlist = new ArrayList<>();
                list.add(getString(R.string.Selected_items).toString());
                for(int i = 0;i<myTextViews.length;i++){
                    if(myTextViews[i].isClick){
                        list.add(myTextViews[i].getText().toString());
                        idlist.add(Integer.valueOf(myTextViews[i].subId));
                    }
                }
                Intent it = new Intent(this,Special_Report_commit_Activity.class);
                it.putStringArrayListExtra("list", (ArrayList<String>) list);
                it.putIntegerArrayListExtra("idlist", (ArrayList<Integer>) idlist);
                it.putExtra("pid",bodyModel.getId());
                startActivityForResult(it,0x1);
                break;
        }
        if(view instanceof MyTextView){
            if(((MyTextView) view).isClick){
                ((MyTextView) view).setClick(false);
                view.setBackground(getResources().getDrawable(R.drawable.radius_from_body_gary));
            }else{
                ((MyTextView) view).setClick(true);
                view.setBackground(getResources().getDrawable(R.drawable.radius_from_body_blue));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            update = data.getIntExtra("update",0);
        }else{
            update = 0;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(update);
        finish();
    }
}
