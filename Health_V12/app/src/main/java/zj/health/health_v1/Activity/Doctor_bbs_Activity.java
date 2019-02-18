package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Main_bbs_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Model.bbsModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Doctor_bbs_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;
    private RecyclerView bbs_recy;
    private Main_bbs_Adapter adapter;
    private List<bbsModel> bbsModelList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_bbs_activity);
        initView();
        BindListener();
        setData();
    }



    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        bbs_recy = (RecyclerView)findViewById(R.id.bbs_recy);
    }
    private void BindListener(){
        back.setOnClickListener(this);

    }
    private void setData(){
        title.setText(R.string.Doctor_bbs);
        bbsModelList = new ArrayList<>();


        bbsModel model1 = new bbsModel();
        model1.setAuthor("Doctor.zhao");
        model1.setTitle("万方医学");
        model1.setContent("形成了文献资源整合发现服务");
        model1.setType(1);
        model1.setDate("2018年2月3日");

        bbsModelList.add(model1);

        bbsModel model2 = new bbsModel();
        model2.setAuthor("Doctor.Leung");
        model2.setTitle("对于感冒你知道多少？");
        model2.setContent("上呼吸道感染简称上感，又称普通感冒。是包括鼻腔、咽或喉部急性炎症的总称");
        model2.setPhoto("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527675917773&di=a5f3523c138a690ee24cbac5370ce99f&imgtype=0&src=http%3A%2F%2Fwww.kepu.net.cn%2Fgb%2Fydrhcz%2Fydrhcz_wap%2Fydrhcz_wap_zpzs%2F201707%2FW020170719514312431984.jpg"+";");
        model2.setType(2);
        model2.setDate("2018年3月6日");
        bbsModelList.add(model2);



        bbsModel model3 = new bbsModel();
        model3.setAuthor("梁医生");
        model3.setTitle("中药专业知识");
        model3.setContent("由于中药主要来源于天然药及其加工品，但以植物性药物居多，故有“诸药以草为本”的说法，且记述这些药物的书籍往往冠以“本草”之名");
        model3.setPhoto("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527683646970&di=7bda8394c5cb3f30d4f633d05a2a8abd&imgtype=0&src=http%3A%2F%2Fimgqn.koudaitong.com%2Fupload_files%2F2015%2F02%2F05%2FFhgQYbgl_N4LTQef0npAz_m0KAd3.jpg%2521730x0.jpg"+";"
        +"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527683732484&di=c7b78307c1b0bc5d884e0f52bc332fdb&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161209%2F37df77c740fc449cbacfed7b975da2f6_th.png"+";");
        model3.setType(3);
        model3.setDate("2018年3月6日");
        bbsModelList.add(model3);


        bbsModel model4 = new bbsModel();
        model4.setAuthor("陈医生");
        model4.setTitle("医学会");
        model4.setContent("4月25日下午，中华医学会2018年党的工作会议在学会办公楼五层会议室召开");
        model4.setPhoto("http://fcc.zzu.edu.cn/photo/pic/1507/1507031147109625432/1018043900.jpg"+";"
                +"http://img1.imgtn.bdimg.com/it/u=1595706631,786902820&fm=214&gp=0.jpg"+";"
                +"http://www.yaozui.com/ckeditor_assets/images/2017/04/12/c44453fe3e42d52a45f7d9c5e8045f78.jpeg"+";");
        model4.setType(4);
        model4.setDate("2018年4月25日");
        bbsModelList.add(model4);

        adapter = new Main_bbs_Adapter(this,bbsModelList);
        bbs_recy.setLayoutManager(new LinearLayoutManager(this));
        bbs_recy.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
