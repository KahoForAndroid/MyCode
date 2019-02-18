package zj.health.health_v1.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Friend_log_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/18.
 */

public class Add_Friends_Activity extends BaseActivity implements View.OnClickListener{

    private ImageView back,search_img;
    private TextView title;
    private EditText search_edit;
    private LinearLayout add_address_list_layout;
    private FriendModel friendModel;
    private Handler Search_friendHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(msg.obj.toString());
                    if(jsonObject1.optString("code").equals("3")){
                        Toast.makeText(Add_Friends_Activity.this, jsonObject1.optString("msg"), Toast.LENGTH_SHORT).show();
                    }else {
                        if (jsonObject1.optString("msg").equals("success")) {
                            friendModel = new Gson().fromJson(jsonObject1.optString("data"), FriendModel.class);
                            if (friendModel != null) {
                                Intent it = new Intent(Add_Friends_Activity.this, Friends_Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("model", friendModel);
                                it.putExtra("bundle", bundle);
                                startActivity(it);
                            } else {
                                Toast.makeText(Add_Friends_Activity.this, R.string.friend_null, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Add_Friends_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Add_Friends_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Add_Friends_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        search_img = (ImageView)findViewById(R.id.search_img);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        search_edit = (EditText) findViewById(R.id.search_edit);
        add_address_list_layout = (LinearLayout)findViewById(R.id.add_address_list_layout);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        search_img.setOnClickListener(this);
        add_address_list_layout.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.add).toString()+getString(R.string.friend).toString());
    }
    private void searchFriend(String phone){
        String phones = "phone"+"="+phone;
        new PostUtils().Get(Constant.Search_friend+phones,true,Search_friendHandler,this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.search_img:
                if(search_edit.getText().toString().length() == 11){
                    searchFriend(search_edit.getText().toString());
                }else {
                    Toast.makeText(this, R.string.input_phoneNumber_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_address_list_layout:
                Intent it = new Intent(this,Commit_Contacts_Activity.class);
                startActivity(it);
                break;
        }
    }



    }
