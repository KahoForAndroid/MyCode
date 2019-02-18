package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Model.BodyModel;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/14.
 */

public class Body_Fluid_Activity extends BaseActivity implements View.OnClickListener{

    private MyTextView trachea_textview,lung_textview,Liver_gallbladder_and_spleen_textview,Liver_sausage_textview,
            Gynaecology_textview,Blood_blood_vessel_textview,skin_textview,heart_textview,stomach_textview,
            kidney_textview,andrology_textview,fluid_textview;
    private MyTextView[] myTextViews = null;
    private ImageView back;
    private Button commit_btn;
    private int update = 0;
    private BodyModel bodyModel = null;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_fluid_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        trachea_textview = (MyTextView)findViewById(R.id.trachea_textview);
        lung_textview = (MyTextView)findViewById(R.id.lung_textview);
        Liver_gallbladder_and_spleen_textview = (MyTextView)findViewById(R.id.Liver_gallbladder_and_spleen_textview);
        Liver_sausage_textview = (MyTextView)findViewById(R.id.Liver_sausage_textview);
        Gynaecology_textview = (MyTextView)findViewById(R.id.Gynaecology_textview);
        Blood_blood_vessel_textview = (MyTextView)findViewById(R.id.Blood_blood_vessel_textview);
        skin_textview = (MyTextView)findViewById(R.id.skin_textview);
        heart_textview = (MyTextView)findViewById(R.id.heart_textview);
        stomach_textview = (MyTextView)findViewById(R.id.stomach_textview);
        kidney_textview = (MyTextView)findViewById(R.id.kidney_textview);
        andrology_textview = (MyTextView)findViewById(R.id.andrology_textview);
        fluid_textview = (MyTextView)findViewById(R.id.fluid_textview);
        commit_btn = (Button)findViewById(R.id.commit_btn);
        title = (TextView)findViewById(R.id.title);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        trachea_textview.setOnClickListener(this);
        lung_textview.setOnClickListener(this);
        Liver_gallbladder_and_spleen_textview.setOnClickListener(this);
        Liver_sausage_textview.setOnClickListener(this);
        Gynaecology_textview.setOnClickListener(this);
        Blood_blood_vessel_textview.setOnClickListener(this);
        skin_textview.setOnClickListener(this);
        heart_textview.setOnClickListener(this);
        stomach_textview.setOnClickListener(this);
        kidney_textview.setOnClickListener(this);
        andrology_textview.setOnClickListener(this);
        fluid_textview.setOnClickListener(this);
        commit_btn.setOnClickListener(this);
    }
    private void setData(){

        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("bundle");
        bodyModel = (BodyModel) bundle.getSerializable("body");
        title.setText(bodyModel.getName());
        for (int i = 0;i<bodyModel.getSubItem().size();i++){
            if(bodyModel.getSubItem().get(i).getName().equals("气管")){
                trachea_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("心")){
                heart_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("肺")){
                lung_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("肝胆脾")){
                Liver_gallbladder_and_spleen_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("胃")){
                stomach_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("肾")){
                kidney_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("妇科")){
                Gynaecology_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("男科")){
                andrology_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("血液/血管")){
                Blood_blood_vessel_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("体液")){
                fluid_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("皮肤")){
                skin_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("肝肠")){
                Liver_sausage_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }
        }


        myTextViews = new MyTextView[]{
                trachea_textview,lung_textview,Liver_gallbladder_and_spleen_textview,Liver_sausage_textview,
                Gynaecology_textview,Blood_blood_vessel_textview,skin_textview,heart_textview,stomach_textview,
                kidney_textview,andrology_textview,fluid_textview
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
