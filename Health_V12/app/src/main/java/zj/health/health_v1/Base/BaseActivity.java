package zj.health.health_v1.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

/**
 * 创建者 Kaho
 * 公共Activity类
 * Created by Administrator on 2018/4/2.
 */

public class BaseActivity extends AppCompatActivity{


    protected float mDensity;//获取屏幕密度
    protected int mDensityDpi;//获取手机分辨率
    protected int mWidth;//屏幕宽度(以PX为单位)
    protected int mHeight;//屏幕高度(以PX为单位)
    protected boolean needShowing = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindowManager().getDefaultDisplay().getMetrics(Health_AppLocation.instance.displayMetrics);
        mDensity = Health_AppLocation.instance.displayMetrics.density;
        mDensityDpi = Health_AppLocation.instance.displayMetrics.densityDpi;
        mWidth = Health_AppLocation.instance.displayMetrics.widthPixels;
        mHeight = Health_AppLocation.instance.displayMetrics.heightPixels;
//        Health_AppLocation.instance.ActivityList.add(this);
    }





}
