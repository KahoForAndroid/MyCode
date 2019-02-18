package zj.health.health_v1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/8/23.
 */

public class SharedPreferencesUtils {


    /**
     * 存数据到SharedPreferences
     * @param context 上下文
     * @param name 文件名
     * @return
     */
    public static SharedPreferences.Editor getEditor(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }


    /**
     * 获取SharedPreferences
     * @param context 上下文
     * @param name 文件名
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp;
    }
}
