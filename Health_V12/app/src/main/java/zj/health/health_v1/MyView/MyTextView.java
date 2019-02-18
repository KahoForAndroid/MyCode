package zj.health.health_v1.MyView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/30.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView{

    public boolean isClick = false;
    public int typeId;
    public String ParentId ;
    public String subId;

    public MyTextView(Context context) {
        super(context);
//        if(isClick){
//            setClick(true);
//            setBackground(getResources().getDrawable(R.drawable.radius_blue));
//            setTextColor(getResources().getColor(R.color.white));
//        }else{
//            setClick(false);
//            setBackground(getResources().getDrawable(R.drawable.radius_kaho));
//            setTextColor(getResources().getColor(R.color.black));
//        }
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public boolean getClickStatus(){
        return isClick;
    }
    public void setClick(boolean isClick){
        this.isClick = isClick;
    }
    public void setTypeId(int typeId){
        this.typeId = typeId;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_UP:
//                if(getClickStatus()){
//                    setClick(false);
//                    setBackground(getResources().getDrawable(R.drawable.radius_kaho));
//                    setTextColor(getResources().getColor(R.color.black));
//                }else{
//                    setClick(true);
//                    setBackground(getResources().getDrawable(R.drawable.radius_blue));
//                    setTextColor(getResources().getColor(R.color.white));
//                }
//                break;
//        }
//        return false;
//    }
}
