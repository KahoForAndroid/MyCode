package zj.health.health_v1.IM.ui.customview;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.huawei.android.pushagent.PushManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
//import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import zj.health.health_v1.Activity.Setting_Activity;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.model.FriendshipInfo;
import zj.health.health_v1.IM.model.GroupInfo;
import zj.health.health_v1.IM.model.UserInfo;
import zj.health.health_v1.IM.ui.SplashActivity;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DbUtils;
import zj.health.health_v1.Utils.SharedPreferencesUtils;

public class DialogActivity extends Activity implements View.OnClickListener {

    private RelativeLayout ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        setFinishOnTouchOutside(false);
        ok = (RelativeLayout)findViewById(R.id.ok);
        ok.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ok) {
            logout();
        }
    }

    private void logout(){
//        TlsBusiness.logout(UserInfo.getInstance().getId());
//        UserInfo.getInstance().setId(null);
//        FriendshipInfo.getInstance().clear();
//        GroupInfo.getInstance().clear();
//        Intent intent = new Intent(this,SplashActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);

        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        UserInfo.getInstance().setUserSig(null);
        FriendshipInfo.getInstance().clear();
        Health_AppLocation.instance.Token = null;
        GroupInfo.getInstance().clear();
//        SharedPreferences.Editor editor = SharedPreferencesUtils.
//                getEditor(this,"loginToken");
//        editor.clear();
//        editor.commit();
        new DbUtils().DelUsersData();
        Intent intent = new Intent(this,SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    /**
     * 判断小米推送是否已经初始化
     */
    private boolean shouldMiInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
