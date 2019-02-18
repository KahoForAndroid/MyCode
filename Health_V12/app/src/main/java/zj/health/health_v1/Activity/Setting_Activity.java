package zj.health.health_v1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
//import com.xiaomi.mipush.sdk.PushMessageHelper;

import okhttp3.OkHttpClient;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.model.FriendshipInfo;
import zj.health.health_v1.IM.model.GroupInfo;
import zj.health.health_v1.IM.model.UserInfo;
import zj.health.health_v1.IM.ui.SplashActivity;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2018/4/10.
 */

public class Setting_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private RelativeLayout logout_rela,replacement_rela,message_notice_rela,Legal_declaration_rela;
    private DbUtils dbUtils = new DbUtils();
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        logout_rela = (RelativeLayout)findViewById(R.id.logout_rela);
        replacement_rela = (RelativeLayout)findViewById(R.id.replacement_rela);
        message_notice_rela = (RelativeLayout)findViewById(R.id.message_notice_rela);
        Legal_declaration_rela = (RelativeLayout)findViewById(R.id.Legal_declaration_rela);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        logout_rela.setOnClickListener(this);
        replacement_rela.setOnClickListener(this);
        message_notice_rela.setOnClickListener(this);
        Legal_declaration_rela.setOnClickListener(this);
    }
    private void setData(){

    }


    private void logout(){
//        TlsBusiness.logout(UserInfo.getInstance().getId());
        LoginBusiness.logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(Setting_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                UserInfo.getInstance().setId(null);
                UserInfo.getInstance().setUserSig(null);
                FriendshipInfo.getInstance().clear();
                Health_AppLocation.instance.Token = null;
                GroupInfo.getInstance().clear();
//                SharedPreferences.Editor editor = SharedPreferencesUtils.
//                         getEditor(Setting_Activity.this,"loginToken");
//                editor.clear();
//                editor.commit();
                dbUtils.DelUsersData();
                Intent intent = new Intent(Setting_Activity.this,SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.logout_rela:
                View popwindow = View.inflate(this,R.layout.main_popwindow,null);
                MessageDailog(this,popwindow);
                break;
            case R.id.replacement_rela:
                Intent it = new Intent(this,Replacement_binding_Frist_Activity.class);
                startActivity(it);
                break;
            case R.id.message_notice_rela:
                Intent ita = new Intent(this,Message_Notice_Setting_Activity.class);
                startActivity(ita);
                break;
            case R.id.Legal_declaration_rela:
                Intent itb = new Intent(this,PayActivity.class);
                startActivity(itb);
                break;
                default:
                    break;
        }
    }

    //带有确定取消的弹出框
    public void MessageDailog(Context context, final View view){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        TextView textView = (TextView)view.findViewById(R.id.popwindow_content_text);
        RelativeLayout ok = (RelativeLayout)view.findViewById(R.id.ok);
        RelativeLayout cancel = (RelativeLayout)view.findViewById(R.id.cancel);
        textView.setText(getString(R.string.login_tips));
        if(ok != null){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    logout();
                }
            });
        }
        if(cancel!=null){
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }

        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }


    }
}
