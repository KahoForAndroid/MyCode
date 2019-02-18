package zj.health.health_v1.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Prescription_DoctorFragment_Adapter;
import zj.health.health_v1.Adapter.Prescription_Manage_List_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.PrescModel;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Prescription_Manage_Fragment extends Fragment{

    private View view;
    private RecyclerView rescription_recy;
    private EditText search_edit;
    private ImageView search_img;
    private RelativeLayout topView;
    private Prescription_DoctorFragment_Adapter adapter;
    private List<PrescModel> prescModelList = new ArrayList<>();
    private PopupWindow popupWindow;
    private Handler prescHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        prescModelList.clear();
                        prescModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<PrescModel>>(){}.getType());
                        prescModelList.add(new PrescModel());
                        adapter.setPrescModelList(prescModelList);
                    }else{
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private Handler DeleteprescHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        getPrescList();
                    }else{
                        Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(getActivity(), R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), getString(R.string.http_error), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.rescription_anage_fragment,container,false);
        }else{
            initView();
            return inflater.inflate(R.layout.rescription_anage_fragment,container,false);
        }

        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BindListener();
        setData();
    }

    private void initView(){
        search_edit = (EditText)view.findViewById(R.id.search_edit);
        search_img = (ImageView)view.findViewById(R.id.search_img);
        rescription_recy = (RecyclerView)view.findViewById(R.id.rescription_recy);
        topView = (RelativeLayout) view.findViewById(R.id.top);
        topView.setVisibility(View.GONE);
    }
    private void BindListener(){

    }
    private void setData(){

        adapter = new Prescription_DoctorFragment_Adapter((BaseActivity) getActivity(),prescModelList);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                View popwindowView = View.inflate(getActivity(),R.layout.main_popwindow,null);
                MessageDailog(getActivity(),popwindowView,position);
            }
        });
        rescription_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        rescription_recy.setAdapter(adapter);
        getPrescList();
    }

    private void getPrescList(){
        new PostUtils().Get(Constant.doctor_presc_list,true,prescHandler,getActivity());
    }
    private void DeletePresc(String json){
        new PostUtils().Delete(Constant.getPrescription+json,true,DeleteprescHandler,getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0x1){
            getPrescList();
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
                    String json = "prescId="+prescModelList.get(position).getId();
                    DeletePresc(json);
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
