package zj.health.health_v1.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import zj.health.health_v1.Activity.Report_defult_Activity;
import zj.health.health_v1.Base.BaseActivity;

/**
 * Created by Administrator on 2019/1/28.
 */

public class JudgeUtils {

    public static void startArticleDetailActivity(Context context, Intent intent, ActivityOptions options){
        if(options!=null && !Build.MANUFACTURER.contains("samsung") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            context.startActivity(intent,options.toBundle());
        }else{
            context.startActivity(intent);
        }
    }
}
