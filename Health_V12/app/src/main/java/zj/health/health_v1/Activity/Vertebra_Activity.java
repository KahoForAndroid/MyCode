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

public class Vertebra_Activity extends BaseActivity implements View.OnClickListener{

    private MyTextView Thoracic_vertebra_textview,Lumbar_vertebra_textview,Caudal_vertebra_textview,cervical_vertebra_textview,
            nerve_textview,skin_textview;
    private ImageView back;
    private Button commit_btn;
    private MyTextView[] myTextViews = null;
    private int update = 0; private BodyModel bodyModel = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertebra_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        Thoracic_vertebra_textview = (MyTextView)findViewById(R.id.Thoracic_vertebra_textview);
        Lumbar_vertebra_textview = (MyTextView)findViewById(R.id.Lumbar_vertebra_textview);
        Caudal_vertebra_textview = (MyTextView)findViewById(R.id.Caudal_vertebra_textview);
        cervical_vertebra_textview = (MyTextView)findViewById(R.id.cervical_vertebra_textview);
        nerve_textview = (MyTextView)findViewById(R.id.nerve_textview);
        skin_textview = (MyTextView)findViewById(R.id.skin_textview);
        commit_btn = (Button)findViewById(R.id.commit_btn);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        Thoracic_vertebra_textview.setOnClickListener(this);
        Lumbar_vertebra_textview.setOnClickListener(this);
        Caudal_vertebra_textview.setOnClickListener(this);
        cervical_vertebra_textview.setOnClickListener(this);
        nerve_textview.setOnClickListener(this);
        skin_textview.setOnClickListener(this);
        commit_btn.setOnClickListener(this);
    }
    private void setData(){

        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("bundle");
        bodyModel = (BodyModel) bundle.getSerializable("body");

        for (int i = 0;i<bodyModel.getSubItem().size();i++){
            if(bodyModel.getSubItem().get(i).getName().equals("颈椎")){
                cervical_vertebra_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("胸椎")){
                Thoracic_vertebra_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("腰椎")){
                Lumbar_vertebra_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("尾椎")){
                Caudal_vertebra_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("神经")){
                nerve_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }else if(bodyModel.getSubItem().get(i).getName().equals("皮肤")){
                skin_textview.subId = bodyModel.getSubItem().get(i).getSid();
            }
        }


        myTextViews = new MyTextView[]{
                cervical_vertebra_textview,Thoracic_vertebra_textview,
                Lumbar_vertebra_textview,Caudal_vertebra_textview,
                nerve_textview,skin_textview
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
