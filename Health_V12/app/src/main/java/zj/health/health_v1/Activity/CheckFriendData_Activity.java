package zj.health.health_v1.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/12/27.
 */

public class CheckFriendData_Activity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout check_healthReport_rela,check_particularReport_rela,check_medicineReminder_rela;
    private TextView title;
    private ImageView back;
    private FriendModel friendModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_friend_data_activity);
        initView();
        BindListener();
        setData();
    }



    private void initView(){
        check_healthReport_rela = (RelativeLayout)findViewById(R.id.check_healthReport_rela);
        check_particularReport_rela = (RelativeLayout)findViewById(R.id.check_particularReport_rela);
        check_medicineReminder_rela = (RelativeLayout)findViewById(R.id.check_medicineReminder_rela);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        check_healthReport_rela.setOnClickListener(this);
        check_particularReport_rela.setOnClickListener(this);
        check_medicineReminder_rela.setOnClickListener(this);
    }
    private void setData(){
        title.setText(R.string.data_list);
        friendModel = (FriendModel) getIntent().getBundleExtra("bundle").getSerializable("friendmodel");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.check_healthReport_rela:

                //如果对方有没开放任何一项体征的查看权限,那么不可跳转页面
                if(!friendModel.getFriendPermission().getPermission().isBloodGlucose() && !friendModel.getFriendPermission().getPermission().isBloodPressure()
                        && !friendModel.getFriendPermission().getPermission().isHeartRate() && !friendModel.getFriendPermission().getPermission().isTemp()
                        && !friendModel.getFriendPermission().getPermission().isWeight()){
                    Toast.makeText(this, getString(R.string.friend_data_not_permission), Toast.LENGTH_SHORT).show();
                }else{
                    Intent it = new Intent(CheckFriendData_Activity.this,Friend_Graph_Activity.class);
                    it.putExtra("friendId",friendModel.getId());
                    startActivity(it);
                }

                break;
            case R.id.check_particularReport_rela:
                //如果对方有没开放任何一项体征的查看权限,那么不可跳转页面
                if(!friendModel.getFriendPermission().getPermission().isHealthReport() && !friendModel.getFriendPermission().getPermission().isParticularReport()){
                    Toast.makeText(this, getString(R.string.friend_data_not_permission), Toast.LENGTH_SHORT).show();
                }else{
                    Intent it = new Intent(CheckFriendData_Activity.this,Medical_Examination_Report_Activity.class);
                    it.putExtra("friendid",friendModel.getId());
                    startActivity(it);
                }
                break;
            case R.id.check_medicineReminder_rela:

                if(!friendModel.getFriendPermission().getPermission().isMedicineReminder()){
                    Toast.makeText(this, getString(R.string.friend_data_not_permission), Toast.LENGTH_SHORT).show();
                }else{
                    Intent it = new Intent(CheckFriendData_Activity.this,Remind_Calendar_Activity.class);
                    it.putExtra("friendid",friendModel.getId());

                    startActivity(it);
                }
                break;
                default:
                    break;
        }
    }
}
