package zj.health.health_v1.Activity;

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
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Friend_log_Adapter;
import zj.health.health_v1.Adapter.new_Friend_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/18.
 */

public class New_Friend_Message_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;
    private RecyclerView new_Friend_recy;
    private List<FriendModel> friendModelList = new ArrayList<>();
    private new_Friend_Adapter adapter;
    private RelativeLayout list_null_rela;
    private int Position;
    private String userid,name;
    private String tag = "new friend activity";
    private Handler newFriendHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        friendModelList = new Gson().fromJson(jsonObject1.optString("data"),new TypeToken<List<FriendModel>>(){}.getType());
                        if(friendModelList.size()>0){
                            new_Friend_recy.setVisibility(View.VISIBLE);
                            list_null_rela.setVisibility(View.GONE);
                        }else{
                            new_Friend_recy.setVisibility(View.GONE);
                            list_null_rela.setVisibility(View.VISIBLE);
                        }
                        adapter.setFriendModelList(friendModelList);
                    }else{
                        Toast.makeText(New_Friend_Message_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(New_Friend_Message_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(New_Friend_Message_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler newFriendAcceptHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("msg").equals("success")){
                        userid = friendModelList.get(Position).getId();
                        name = friendModelList.get(Position).getNickname();
                        friendModelList.remove(Position);
                        adapter.setFriendModelList(friendModelList);
                        AddIMFriend(userid,name);
                    }else{
                        Toast.makeText(New_Friend_Message_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(New_Friend_Message_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(New_Friend_Message_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friend_message_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        new_Friend_recy = (RecyclerView)findViewById(R.id.new_Friend_recy);
        list_null_rela = (RelativeLayout)findViewById(R.id.list_null_rela);
    }
    private void BindListener(){
        back.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.new_friends));
        adapter = new new_Friend_Adapter(this,friendModelList);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                LoadingDialog.getLoadingDialog().StartLoadingDialog(New_Friend_Message_Activity.this);
                Position = position;
                String params = "friendId"+"="+friendModelList.get(position).getUserId();
                postNewFriendAccept(friendModelList.get(position).getUserId());
            }
        });
        new_Friend_recy.setLayoutManager(new LinearLayoutManager(this));
        new_Friend_recy.setAdapter(adapter);
        postNewFriendList();
    }

    /**
     * 添加为腾讯云IM好友
     * @param Identifier
     * @param name
     */
    private void AddIMFriend(String Identifier,String name){
        //创建请求列表
        List<TIMAddFriendRequest> reqList = new ArrayList<TIMAddFriendRequest>();

        //添加好友请求
        TIMAddFriendRequest req = new TIMAddFriendRequest(Identifier);
//        req.setAddrSource("DemoApp");
//        req.setAddWording("add me");
//        req.setRemark("Cat");
        req.setIdentifier(Identifier);
        reqList.add(req);

        //申请添加好友
        TIMFriendshipManagerExt.getInstance().addFriend(reqList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int code, String desc){
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Log.e(tag, "addFriend failed: " + code + " desc");
            }

            @Override
            public void onSuccess(List<TIMFriendResult> result){
                Log.e(tag, "addFriend succ");
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(New_Friend_Message_Activity.this, R.string.add_success, Toast.LENGTH_SHORT).show();
//                for(TIMFriendResult res : result){
//                    Log.e(tag, "identifier: " + res.getIdentifer() + " status: " + res.getStatus());
//                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                setResult(0x1);
                finish();
                break;

        }
    }
    private void postNewFriendList(){
        new PostUtils().Get(Constant.new_friend_list,true,newFriendHandler,this);
    }
    private void postNewFriendAccept(String id){
        Map<String,String> map = new HashMap<>();
        map.put("friendId",id);
        new PostUtils().getNewPost(Constant.accept_friend,map,newFriendAcceptHandler,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(0x1);
    }
}
