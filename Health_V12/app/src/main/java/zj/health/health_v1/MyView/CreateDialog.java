package zj.health.health_v1.MyView;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

import zj.health.health_v1.Activity.My_Activity;
import zj.health.health_v1.Adapter.Binding_SelectedQuestion_Adapter;
import zj.health.health_v1.Adapter.Share_Adapter;
import zj.health.health_v1.IM.model.Message;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/24.
 */

public class CreateDialog {

    private PopupWindow popupWindow;


    //带有确定取消的弹出框
    public void MessageDailog(Context context,final View view,View.OnClickListener onClickListener){
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
        if(ok != null){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
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


    //带有确定取消的弹出框（用于发起结束对话）
    public void MessageDailog_EndChat(Context context,final View view,View.OnClickListener onClickListener){
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
        cancel.setVisibility(View.VISIBLE);
        if(ok != null){
            ok.setOnClickListener(onClickListener);
        }
        if(cancel!=null){
            cancel.setOnClickListener(onClickListener);
        }

        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }


    }


    //只带确定按钮的弹出框
    public void MessageDailog2(Context context,final View view, String Text){
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
        textView.setText(Text);
        cancel.setVisibility(View.GONE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }


    }





    public void EditDailog(final Context context, final View view, final android.os.Handler handler){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

        RelativeLayout ok = (RelativeLayout)view.findViewById(R.id.ok);
        RelativeLayout cancel = (RelativeLayout)view.findViewById(R.id.cancel);
        final EditText dialog_edit = (EditText)view.findViewById(R.id.dialog_edit);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog_edit.getText().toString().length()>0 && Float.parseFloat(dialog_edit.getText().toString())>=0){
                    android.os.Message message = new android.os.Message();
                    message.obj = dialog_edit.getText().toString();
                    handler.sendMessage(message);
                    popupWindow.dismiss();
                }else{
                    Toast.makeText(context, context.getString(R.string.edit_money_tips), Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


    }



    public void Medicine_much_Dailog(Context context, View.OnClickListener onClickListener,int style,List<String> much_list,int position){
        View view = View.inflate(context,R.layout.bottom_dialog,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(style);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        RelativeLayout day_one_rela,day_two_rela,day_three_rela,day_four_rela,cancel_rela;
        TextView day_one_text,day_two_text,day_three_text,day_four_text;

        day_one_rela = (RelativeLayout)view.findViewById(R.id.day_one_rela);
        day_two_rela = (RelativeLayout)view.findViewById(R.id.day_two_rela);
        day_three_rela = (RelativeLayout)view.findViewById(R.id.day_three_rela);
        day_four_rela = (RelativeLayout)view.findViewById(R.id.day_four_rela);
        cancel_rela = (RelativeLayout)view.findViewById(R.id.cancel_rela);
        day_one_text = (TextView)view.findViewById(R.id.day_one_text);
        day_two_text = (TextView)view.findViewById(R.id.day_two_text);
        day_three_text = (TextView)view.findViewById(R.id.day_three_text);
        day_four_text = (TextView)view.findViewById(R.id.day_four_text);

        day_one_rela.setOnClickListener(onClickListener);
        day_two_rela.setOnClickListener(onClickListener);
        day_three_rela.setOnClickListener(onClickListener);
        day_four_rela.setOnClickListener(onClickListener);
        cancel_rela.setOnClickListener(onClickListener);

        if(much_list!=null && much_list.size()>0){
            day_one_text.setText(much_list.get(0));
            day_two_text.setText(much_list.get(1));
            day_three_text.setText(much_list.get(2));
            day_four_text.setText(much_list.get(3));
        }else{
            String muchString [] = context.getResources().getStringArray(R.array.much_forday);
            day_one_text.setText(muchString[0]);
            day_two_text.setText(muchString[1]);
            day_three_text.setText(muchString[2]);
            day_four_text.setText(muchString[3]);
        }


        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

    }




    public void Measurement_much_Dailog(Context context, View.OnClickListener onClickListener,int style,List<String> much_list,int position){
        View view = View.inflate(context,R.layout.bottom_dialog,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(style);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        RelativeLayout day_one_rela,day_two_rela,day_three_rela,day_four_rela,cancel_rela;
        TextView day_one_text,day_two_text,day_three_text,day_four_text;

        day_one_rela = (RelativeLayout)view.findViewById(R.id.day_one_rela);
        day_two_rela = (RelativeLayout)view.findViewById(R.id.day_two_rela);
        day_three_rela = (RelativeLayout)view.findViewById(R.id.day_three_rela);
        day_four_rela = (RelativeLayout)view.findViewById(R.id.day_four_rela);
        cancel_rela = (RelativeLayout)view.findViewById(R.id.cancel_rela);
        day_one_text = (TextView)view.findViewById(R.id.day_one_text);
        day_two_text = (TextView)view.findViewById(R.id.day_two_text);
        day_three_text = (TextView)view.findViewById(R.id.day_three_text);
        day_four_text = (TextView)view.findViewById(R.id.day_four_text);
        day_four_rela.setVisibility(View.GONE);
        day_one_rela.setOnClickListener(onClickListener);
        day_two_rela.setOnClickListener(onClickListener);
        day_three_rela.setOnClickListener(onClickListener);
        day_four_rela.setOnClickListener(onClickListener);
        cancel_rela.setOnClickListener(onClickListener);

        if(much_list!=null && much_list.size()>0){
            day_one_text.setText(much_list.get(0));
            day_two_text.setText(much_list.get(1));
            day_three_text.setText(much_list.get(2));
        }else{
            String muchString [] = context.getResources().getStringArray(R.array.much_forday_toMeasurement);
            day_one_text.setText(muchString[0]);
            day_two_text.setText(muchString[1]);
            day_three_text.setText(muchString[2]);
        }


        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

    }





    public void Share_Dailog(final My_Activity my_activity, int style){
        View view = View.inflate(my_activity,R.layout.share_bottom_dialog,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(style);
        popupWindow.setBackgroundDrawable(my_activity.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        List<String> stringList = Arrays.asList(my_activity.getResources().getStringArray(R.array.share));
        RelativeLayout cancel_rela;
        RecyclerView share_recy;
        Share_Adapter share_adapter = new Share_Adapter(my_activity,stringList);
        share_adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                switch (position){
                    case 0:
                        my_activity.Share(SendMessageToWX.Req.WXSceneSession);
                        break;
                    case 1:
                        my_activity.Share(SendMessageToWX.Req.WXSceneTimeline);
                        break;
                        default:
                            break;
                }
            }
        });

        cancel_rela = (RelativeLayout)view.findViewById(R.id.cancel_rela);
        share_recy = (RecyclerView)view.findViewById(R.id.share_recy);
        cancel_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        share_recy.setLayoutManager(new GridLayoutManager(my_activity,4));
        share_recy.setAdapter(share_adapter);

        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

    }




    public void Measurement_Type_Dailog(Context context, View.OnClickListener onClickListener,int style,List<String> much_list,int position){
        View view = View.inflate(context,R.layout.bottom_dialog,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(style);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        RelativeLayout day_one_rela,day_two_rela,day_three_rela,day_four_rela,cancel_rela;
        TextView day_one_text,day_two_text,day_three_text,day_four_text;

        day_one_rela = (RelativeLayout)view.findViewById(R.id.day_one_rela);
        day_two_rela = (RelativeLayout)view.findViewById(R.id.day_two_rela);
        day_three_rela = (RelativeLayout)view.findViewById(R.id.day_three_rela);
        day_four_rela = (RelativeLayout)view.findViewById(R.id.day_four_rela);
        cancel_rela = (RelativeLayout)view.findViewById(R.id.cancel_rela);
        day_one_text = (TextView)view.findViewById(R.id.day_one_text);
        day_two_text = (TextView)view.findViewById(R.id.day_two_text);
        day_three_text = (TextView)view.findViewById(R.id.day_three_text);
        day_four_text = (TextView)view.findViewById(R.id.day_four_text);
        day_one_rela.setOnClickListener(onClickListener);
        day_two_rela.setOnClickListener(onClickListener);
        day_three_rela.setOnClickListener(onClickListener);
        day_four_rela.setOnClickListener(onClickListener);
        cancel_rela.setOnClickListener(onClickListener);

        if(much_list!=null && much_list.size()>0){
            day_one_text.setText(much_list.get(0));
            day_two_text.setText(much_list.get(1));
            day_three_text.setText(much_list.get(2));
            day_four_text.setText(much_list.get(3));
        }else{
            String muchString [] = context.getResources().getStringArray(R.array.Measurement_type_array);
            day_one_text.setText(muchString[0]);
            day_two_text.setText(muchString[1]);
            day_three_text.setText(muchString[2]);
            day_four_text.setText(muchString[3]);
        }


        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

    }


    public void Sport_Type_Dailog(Context context, View.OnClickListener onClickListener,int style,List<String> sport_list,int position){
        View view = View.inflate(context,R.layout.bottom_dialog,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(style);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        RelativeLayout day_one_rela,day_two_rela,day_three_rela,day_four_rela,cancel_rela;
        TextView day_one_text,day_two_text,day_three_text,day_four_text;

        day_one_rela = (RelativeLayout)view.findViewById(R.id.day_one_rela);
        day_two_rela = (RelativeLayout)view.findViewById(R.id.day_two_rela);
        day_three_rela = (RelativeLayout)view.findViewById(R.id.day_three_rela);
        day_four_rela = (RelativeLayout)view.findViewById(R.id.day_four_rela);
        cancel_rela = (RelativeLayout)view.findViewById(R.id.cancel_rela);
        day_one_text = (TextView)view.findViewById(R.id.day_one_text);
        day_two_text = (TextView)view.findViewById(R.id.day_two_text);
        day_three_text = (TextView)view.findViewById(R.id.day_three_text);
        day_one_rela.setOnClickListener(onClickListener);
        day_two_rela.setOnClickListener(onClickListener);
        day_three_rela.setOnClickListener(onClickListener);
        cancel_rela.setOnClickListener(onClickListener);
        day_four_rela.setVisibility(View.GONE);

        if(sport_list!=null && sport_list.size()>0){
            day_one_text.setText(sport_list.get(0));
            day_two_text.setText(sport_list.get(1));
            day_three_text.setText(sport_list.get(2));
        }else{
            String sportlist [] = context.getResources().getStringArray(R.array.sport_array);
            day_one_text.setText(sportlist[0]);
            day_two_text.setText(sportlist[1]);
            day_three_text.setText(sportlist[2]);
        }


        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

    }


    public void SportMode_Dailog(Context context, View.OnClickListener onClickListener,int style,List<String> mode_list,int position){
        View view = View.inflate(context,R.layout.sport_mode_dialog,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(style);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);

        RelativeLayout day_one_rela,day_two_rela,day_three_rela,day_four_rela,cancel_rela,day_five_rela;
        TextView day_one_text,day_two_text,day_three_text,day_four_text,day_five_text;

        day_one_rela = (RelativeLayout)view.findViewById(R.id.day_one_rela);
        day_two_rela = (RelativeLayout)view.findViewById(R.id.day_two_rela);
        day_three_rela = (RelativeLayout)view.findViewById(R.id.day_three_rela);
        day_four_rela = (RelativeLayout)view.findViewById(R.id.day_four_rela);
        day_five_rela = (RelativeLayout) view.findViewById(R.id.day_five_rela);
        cancel_rela = (RelativeLayout)view.findViewById(R.id.cancel_rela);
        day_one_text = (TextView)view.findViewById(R.id.day_one_text);
        day_two_text = (TextView)view.findViewById(R.id.day_two_text);
        day_three_text = (TextView)view.findViewById(R.id.day_three_text);
        day_four_text = (TextView)view.findViewById(R.id.day_four_text);
        day_five_text = (TextView)view.findViewById(R.id.day_five_text);
        day_one_rela.setOnClickListener(onClickListener);
        day_two_rela.setOnClickListener(onClickListener);
        day_three_rela.setOnClickListener(onClickListener);
        day_four_rela.setOnClickListener(onClickListener);
        day_five_rela.setOnClickListener(onClickListener);
        cancel_rela.setOnClickListener(onClickListener);

        if(mode_list!=null && mode_list.size()>0){
            day_one_text.setText(mode_list.get(0));
            day_two_text.setText(mode_list.get(1));
            day_three_text.setText(mode_list.get(2));
            day_four_text.setText(mode_list.get(3));
            day_five_text.setText(mode_list.get(4));
        }else{
            String muchString [] = context.getResources().getStringArray(R.array.sport_frequency);
            day_one_text.setText(muchString[0]);
            day_two_text.setText(muchString[1]);
            day_three_text.setText(muchString[2]);
            day_four_text.setText(muchString[3]);
            day_five_text.setText(muchString[4]);
        }


        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }

    }








    public void     QuestionDailog(Context context, final View view, View.OnClickListener onClickListener,Binding_SelectedQuestion_Adapter adapter){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popwindow_bg));
        popupWindow.setOutsideTouchable(true);


        RecyclerView question_recy = (RecyclerView)view.findViewById(R.id.question_recy);
        question_recy.setLayoutManager(new LinearLayoutManager(context));
        question_recy.setAdapter(adapter);
        if (popupWindow != null) {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        }
//        view.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = view.findViewById(R.id.popwindow_lin).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        popupWindow.dismiss();
//                    }
//                }
//                return true;
//            }
//        });

    }

    public PopupWindow getPopupWindow(){
        return popupWindow;
    }


}
