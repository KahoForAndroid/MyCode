package zj.health.health_v1.Fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Activity.Health_Log_Activity;
import zj.health.health_v1.Activity.Medical_Examination_Report_Activity;
import zj.health.health_v1.Activity.Remind_Calendar_Activity;
import zj.health.health_v1.Activity.Report_defult_Activity;
import zj.health.health_v1.Adapter.Body_Report_Adapter;
import zj.health.health_v1.Adapter.NewBodyReportAdapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.Contacts;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.Model.ReportModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.JudgeUtils;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/5/3.
 */

public class Report_Fragment extends Fragment{

    private View view = null;
    private RecyclerView recyclerView = null;
    private NewBodyReportAdapter adapter = null;
    private Gson gson = new Gson();
    public boolean isUpload = false;
    private RelativeLayout list_null_rela;
    private List<NewReportModel> list = new ArrayList<>();
    private PopupWindow popupWindow;
    private boolean haveFriendId = false;//如果为真 就说明从亲友日志进入
    private Handler mineHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if (msg.obj!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                        reportModels = gson.fromJson(jsonObject.optString("info"),new TypeToken<List<ReportModel>>(){}.getType());
//                        adapter.setList(reportModels);
//                        adapter.notifyDataSetChanged();
//                        isUpload = false;
                        list = gson.fromJson(jsonObject.optString("data"),new TypeToken<List<NewReportModel>>(){}.getType());
                        if(list!=null && list.size()>0){
                            Collections.reverse(list);
                            recyclerView.setVisibility(View.VISIBLE);
                            list_null_rela.setVisibility(View.GONE);
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                            isUpload = false;
                        }else{
                            recyclerView.setVisibility(View.GONE);
                            list_null_rela.setVisibility(View.VISIBLE);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler DeleteHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if (msg.obj!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.optString("msg").equals("success")){
                            Message message = new Message();
                            message.what = 0;
                            ((Medical_Examination_Report_Activity)getActivity()).uploadHandler.sendMessage(message);
//                            uploadList();
                        }else{
                            Toast.makeText(getActivity(), R.string.http_error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), R.string.http_error, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(), R.string.http_error, Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }
        }
    };




    @Override
    public void onStart() {
        super.onStart();
        if(isUpload){
            uploadList();
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.from(getActivity()).inflate(R.layout.report_fragment,container,false);
        initView();
        setData();
        return view;
    }

    private void initView(){
        recyclerView = (RecyclerView)view.findViewById(R.id.report_recycle);
        list_null_rela = (RelativeLayout)view.findViewById(R.id.list_null_rela);
    }

    private void setData(){
        if(getArguments()!=null && getArguments().getString("friendid")!=null){
            haveFriendId = true;
        }
        adapter = new NewBodyReportAdapter(getActivity(),list,haveFriendId);
        adapter.setOnItemClick(new OnItemClick() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void OnItemClickListener(View view, int position) {
                switch (view.getId()){
                    case R.id.item_rela:
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),view,"");
                        Intent it = new Intent(getActivity(), Report_defult_Activity.class);
                        Bundle bundle = options.toBundle();
                        bundle.putSerializable("model",list.get(position));
                        it.putExtra("bundle",bundle);
                        it.putExtra("type",Integer.parseInt(list.get(position).getType()));
                        JudgeUtils.startArticleDetailActivity(getActivity(),it,options);
                        break;
                    case R.id.delete_img:
                        View popwindowView = View.inflate(getActivity(),R.layout.main_popwindow,null);
                        MessageDailog(getActivity(),popwindowView,position);
                        break;
                        default:
                            break;
                }

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        uploadList();
    }

    public void uploadList(){
        if(getArguments()!=null && getArguments().getString("friendid")!=null){
            String config = "friendId="+getArguments().getString("friendid");
            new PostUtils().Get(Constant.friend_report_list+config,true,mineHandler, (BaseActivity) getActivity());
        }else{
            new PostUtils().Get(Constant.report_list,true,mineHandler, (BaseActivity) getActivity());
        }
    }

    public void DeleteReport(String parm){
        new PostUtils().Delete(Constant.report_Upload+parm,true,DeleteHandler,getActivity());
    }

//    public void getImages(String postname){
//        new PostUtils().Get(postname,true,ImageHandler, (BaseActivity) getActivity());
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x11){
            if(resultCode == getActivity().RESULT_OK){
                uploadList();
            }
        }
    }

    //带有确定取消的弹出框
    public void MessageDailog(Context context, final View view, final int position){
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
        textView.setText(getString(R.string.delete_tips));
        if(ok != null){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param = "reportId="+list.get(position).getId();
                    DeleteReport(param);
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
}
