package zj.health.health_v1.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Administrator on 2018/6/18.
 */

public class PermissionUtils {

    /**
     *判断位置信息是否开启
     * @param context
     * @return
     */
    public static boolean isLocationOpen(final Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //gps定位
        boolean isGpsProvider = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //网络定位
        boolean isNetWorkProvider = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGpsProvider|| isNetWorkProvider;
    }


    public static boolean isReceptionService(){

        return true;
    }

    public static boolean isMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    //是否已经授权改权限
    public static boolean isGranted(Context context,String permissicon){
        int checkPermission = ActivityCompat.checkSelfPermission(context,permissicon);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    //对权限和版本上是否都满足要求
    public static boolean Permission_Meet_the_requirements(Context context,String permission){
        return !isMarshmallow() || isGranted(context,permission);
    }


}
