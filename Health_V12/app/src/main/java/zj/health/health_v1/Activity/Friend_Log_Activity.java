package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Friend_log_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/18.
 */

public class Friend_Log_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView newMessgae_img,back;
    private TextView title;
    private RecyclerView friend_recy;
    private RelativeLayout add_rela;
    private RelativeLayout list_null_rela;
    private Friend_log_Adapter adapter;
    private Intent it = null;
    private List<FriendModel> friendModelList = new ArrayList<>();
    private FriendModel friendModel;
    private int Position;
    private Handler friend_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        friendModelList = new Gson().fromJson(jsonObject1.optString("data"),
                                new TypeToken<List<FriendModel>>(){}.getType());
                        if(friendModelList !=null && friendModelList.size()>0){
                            friend_recy.setVisibility(View.VISIBLE);
                            list_null_rela.setVisibility(View.GONE);
                            adapter.setFriendModelList(friendModelList);
                        }else{
                            friend_recy.setVisibility(View.GONE);
                            list_null_rela.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Toast.makeText(Friend_Log_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Friend_Log_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Friend_Log_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler checkHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){

                        FriendModel.FriendPermission.Permission permission = new Gson().fromJson(jsonObject1.optString("data"),
                                FriendModel.FriendPermission.Permission.class);

                        FriendModel.FriendPermission friendPermission = new FriendModel.FriendPermission();
                        friendPermission.setUserId(friendModelList.get(Position).getId());
                        friendPermission.setPermission(permission);
                        friendModelList.get(Position).setFriendPermission(friendPermission);

                        it = new Intent(Friend_Log_Activity.this,CheckFriendData_Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("friendmodel",friendModelList.get(Position));
                        it.putExtra("bundle",bundle);
                        startActivity(it);
                    }else{
                        Toast.makeText(Friend_Log_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Friend_Log_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Friend_Log_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_log_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        newMessgae_img = (ImageView)findViewById(R.id.newMessgae_img);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        friend_recy = (RecyclerView)findViewById(R.id.friend_recy);
        add_rela = (RelativeLayout)findViewById(R.id.add_rela);
        list_null_rela = (RelativeLayout)findViewById(R.id.list_null_rela);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        newMessgae_img.setOnClickListener(this);
        add_rela.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.friend_log));
        adapter = new Friend_log_Adapter(this,friendModelList);
        friend_recy.setLayoutManager(new LinearLayoutManager(this));
        friend_recy.setAdapter(adapter);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                switch (view.getId()){
                    case R.id.to_setting_img:
                        it = new Intent(Friend_Log_Activity.this,Friend_permissions_setting_Activity.class);
                        it.putExtra("userId",friendModelList.get(position).getId());
                        startActivity(it);
                        break;
                    case R.id.check_layout:
                        Position = position;
                        getFriendPermission(friendModelList.get(Position).getId());
                        break;
                        default:
                            break;
                }

            }
        });
        getFriendList();

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.newMessgae_img:
                 it = new Intent(this,New_Friend_Message_Activity.class);
                startActivityForResult(it,0x1);
                break;
            case R.id.add_rela:
                 it = new Intent(this,Add_Friends_Activity.class);
                startActivity(it);
                break;
        }
    }

    private void getFriendList(){
        new PostUtils().Get(Constant.list_friend,true,friend_Handler,this);
    }

    private void getFriendPermission(String userid){
        String json = "friendId="+userid;
        new PostUtils().Get(Constant.friend_permission4me+json,true,checkHandler,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0x1){
            getFriendList();
        }
    }
}
