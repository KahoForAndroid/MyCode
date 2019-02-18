package zj.health.health_v1.IM.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Handler;

import zj.health.health_v1.Activity.IM_Check_Report_Activity;
import zj.health.health_v1.Activity.IM_Test_Activity;
import zj.health.health_v1.Activity.PrescriptionUsers_Item_Details_Activity;
import zj.health.health_v1.IM.adapters.ChatAdapter;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {


    private String TAG = getClass().getSimpleName();

    private final int TYPE_TYPING = 14;

    private Type type;
    private String desc;
    private String data;

    public CustomMessage(TIMMessage message){
        this.message = message;
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
        parse(elem.getData(),elem.getDesc());

    }
    public String getData(){
        return data;
    }

    public CustomMessage(Type type){
        message = new TIMMessage();
        String data = "";
        JSONObject dataJson = new JSONObject();
        try{
            switch (type){
                case TYPING:
                    dataJson.put("userAction",TYPE_TYPING);
                    dataJson.put("actionParam","EIMAMSG_InputStatus_Ing");
                    data = dataJson.toString();
            }
        }catch (JSONException e){
            Log.e(TAG, "generate json error");
        }
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(data.getBytes());
        message.addElement(elem);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void parse(byte[] data,String desc){
        type = Type.INVALID;
        try{
            String str = new String(data, "UTF-8");
//            if (str != null && str.startsWith("\ufeff")) {
//                str = str.substring(1);
//            }
            JSONObject jsonObj = new JSONObject(str);
            this.data = str;
            this.desc = desc;
            int action = jsonObj.getInt("userAction");
            switch (action){
                case TYPE_TYPING:
                    type = Type.TYPING;
//                    this.data = jsonObj.getString("actionParam");
                    if (jsonObj.getString("actionParam").equals("EIMAMSG_InputStatus_End")){
                        type = Type.INVALID;
                    }
                    break;
            }

        }catch (IOException | JSONException e){
            Log.e(TAG, "parse json error");

        }
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, final Context context) {
        clearView(viewHolder);
        RelativeLayout relativeLayout = getBubbleView(viewHolder);
        relativeLayout.addView(View.inflate(context, R.layout.custom_im_health_laytou,null));
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "sss", Toast.LENGTH_SHORT).show();
            }
        });
        showStatus(viewHolder);
    }

    public void  showMessageCustom(ChatAdapter.ViewHolder viewHolder, final Context context, final CustomMessage message, final android.os.Handler handler) {
        clearView(viewHolder);
        RelativeLayout relativeLayout = getBubbleView(viewHolder);

        JSONObject jsonObject = null;
        try {
            if(message.getData() != null){
                jsonObject = new JSONObject(message.getData());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int types = 0;
        if(message.getData() != null){
            types = jsonObject.optInt("type");
        }
            final int type = types;

        if(type == 1){

        }else if(type == 2){
            relativeLayout.addView(View.inflate(context, R.layout.custom_im_health_laytou,null));
            ImageView type_img = (ImageView)relativeLayout.findViewById(R.id.type_img);
            TextView type_text = (TextView)relativeLayout.findViewById(R.id.type_text);
            type_img.setImageResource(R.drawable.send_report_img);
            type_text.setText(context.getString(R.string.health_report));
        }else if(type == 3){
            relativeLayout.addView(View.inflate(context, R.layout.custom_im_health_laytou,null));
            ImageView type_img = (ImageView)relativeLayout.findViewById(R.id.type_img);
            TextView type_text = (TextView)relativeLayout.findViewById(R.id.type_text);
            type_img.setImageResource(R.drawable.send_drug_img);
            type_text.setText(context.getString(R.string.my_prescription));
        }else if(type == 4){
            relativeLayout.addView(View.inflate(context, R.layout.custom_im_end_the_chat_laytou,null));
            ImageView type_img = (ImageView)relativeLayout.findViewById(R.id.type_img);
            TextView type_text = (TextView)relativeLayout.findViewById(R.id.type_text);
            type_img.setImageResource(R.drawable.end_the_chat_icon);
            type_text.setText(context.getString(R.string.send_end_the_chat_text));
        }else if(type == 5){
            relativeLayout.addView(View.inflate(context, R.layout.custom_im_end_the_chat_laytou,null));
            ImageView type_img = (ImageView)relativeLayout.findViewById(R.id.type_img);
            TextView type_text = (TextView)relativeLayout.findViewById(R.id.type_text);
            type_img.setImageResource(R.drawable.end_the_chat_icon);

            if(!message.isSelf()){
                type_text.setText(context.getString(R.string.Other_agree_end_the_chat_send_success));
            }else{
                type_text.setText(context.getString(R.string.agree_end_the_chat_send_success));
            }
            android.os.Message message3 = new android.os.Message();
            message3.what = 3;
            message3.obj = message.desc;
            handler.sendMessage(message3);
        }else if(type == 6){
            relativeLayout.addView(View.inflate(context, R.layout.custom_im_end_the_chat_laytou,null));
            ImageView type_img = (ImageView)relativeLayout.findViewById(R.id.type_img);
            TextView type_text = (TextView)relativeLayout.findViewById(R.id.type_text);
            type_img.setImageResource(R.drawable.refuse_end_the_chat_icon);
            if(!message.isSelf()){
                type_text.setText(context.getString(R.string.Other_refuse_end_the_chat_send_success));
            }else{
                type_text.setText(context.getString(R.string.refuse_end_the_chat_send_success));
            }
        }else {
            relativeLayout.addView(View.inflate(context, R.layout.custom_im_end_the_chat_laytou,null));
            ImageView type_img = (ImageView)relativeLayout.findViewById(R.id.type_img);
            TextView type_text = (TextView)relativeLayout.findViewById(R.id.type_text);
            type_img.setImageResource(R.drawable.end_the_chat_icon);
            type_text.setText(context.getString(R.string.null_message_item));
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(message.data!=null){
                        JSONObject jsonObject = new JSONObject(message.getData());
//                        int type = jsonObject.optInt("type");
                        if(type == 1){

                        }else if(type == 2){
                            Intent it = new Intent(context, IM_Check_Report_Activity.class);
                            String a = message.data;
                            it.putExtra("data",jsonObject.optString("data"));
                            context.startActivity(it);
                        }else if(type == 3){
                            Intent it = new Intent(context, PrescriptionUsers_Item_Details_Activity.class);
                            String a = message.data;
                            it.putExtra("json",jsonObject.optString("data"));
                            context.startActivity(it);
                        }else if(type == 4){
                            if (!message.isSelf()) {
                                final CreateDialog createDialog = new CreateDialog();

                                View.OnClickListener onClickListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        switch (v.getId()) {
                                            case R.id.ok:
                                                android.os.Message message1 = new android.os.Message();
                                                message1.what = 1;
                                                message1.obj = message.desc;
                                                handler.sendMessage(message1);
                                                createDialog.getPopupWindow().dismiss();
                                                break;
                                            case R.id.cancel:
                                                android.os.Message message2 = new android.os.Message();
                                                message2.what = 2;
                                                message2.obj = message.desc;
                                                handler.sendMessage(message2);
                                                createDialog.getPopupWindow().dismiss();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                };
                                createDialog.MessageDailog_EndChat(context, View.inflate(context, R.layout.end_chat_popwindow, null), onClickListener);
                            }
                        }else if(type == 5){

                        }
                    }else if(type == 6){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        showStatus(viewHolder);
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return null;
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    public enum Type{
        TYPING,
        INVALID,
    }
}
