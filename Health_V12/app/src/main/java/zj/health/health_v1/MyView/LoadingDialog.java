package zj.health.health_v1.MyView;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;

import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/24.
 */

public class LoadingDialog {

    private static LoadingDialog loadingDialog = null;
    private PopupWindow LoadingWindow;
    private View view = null;
    private Animation operatingAnim = null;
    private ImageView loading_img = null;

    public static synchronized LoadingDialog getLoadingDialog(){
        if(loadingDialog == null){
            loadingDialog = new LoadingDialog();
        }else{
            return loadingDialog;
        }
        return loadingDialog;
    }

    public void StartLoadingDialog(Context context){
        view = View.inflate(context, R.layout.loading_layout,null);
        LoadingWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        LoadingWindow.setFocusable(true);
        LoadingWindow.update();
        LoadingWindow.setAnimationStyle(R.style.MyDialog);
        LoadingWindow.setOutsideTouchable(true);

        loading_img = (ImageView)view.findViewById(R.id.loading_img);
        operatingAnim = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            loading_img.startAnimation(operatingAnim);
        }

        if (LoadingWindow != null) {
            LoadingWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }
    }
    public void StopLoadingDialog(){
        if(LoadingWindow!=null){
            LoadingWindow.dismiss();
            operatingAnim = null;
            loading_img.clearAnimation();
            view = null;
        }
    }

}
