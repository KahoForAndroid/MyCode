package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.History_IM_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/9/25.
 */

public class History_IM_Activity extends BaseActivity implements View.OnClickListener{

    private TextView title;
    private ImageView back;
    private RecyclerView im_recy;
    private History_IM_Adapter adapter;
    private List<TIMUserProfile> results = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_im_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        im_recy = (RecyclerView)findViewById(R.id.im_recy);
    }
    private void BindListener(){
        back.setOnClickListener(this);
    }
    private void setData(){
        title.setText(getString(R.string.history_im_text));
        adapter = new History_IM_Adapter(this,results);
        im_recy.setLayoutManager(new LinearLayoutManager(this));
        im_recy.setAdapter(adapter);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent it = new Intent(History_IM_Activity.this, ChatActivity.class);
                it.putExtra("identify", results.get(position).getIdentifier());
                it.putExtra("type", TIMConversationType.C2C);
                it.putExtra("myIcon", Health_AppLocation.instance.Icon);
                if(results.get(position).getFaceUrl().contains(Constant.photo_IP)){
                    it.putExtra("friendIcon",results.get(position).getFaceUrl());
                }else{
                    it.putExtra("friendIcon",Constant.photo_IP+results.get(position).getFaceUrl());
                }
                it.putExtra("chatType",0);
                it.putExtra("isHistory",true);
                it.putExtra("titleStr", results.get(position).getNickName());
                startActivity(it);

            }
        });
        //获取好友列表
        TIMFriendshipManagerExt.getInstance().getFriendList(new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
//                Log.e(tag, "getFriendList failed: " + code + " desc");
                Toast.makeText(History_IM_Activity.this, "获取失败,请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result){
//                for(TIMUserProfile res : result){
////                    Log.e(tag, "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
////                            + " remark: " + res.getRemark());
//                }
                results.addAll(result);
                adapter.setResult(results);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
                default:
                    break;
        }
    }
}
