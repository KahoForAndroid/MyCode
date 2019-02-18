package zj.health.health_v1.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Consultation_Room_Adapter;
import zj.health.health_v1.Adapter.DoctorSerive_Adapter;
import zj.health.health_v1.Adapter.My_Wallet_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.adapters.ConversationAdapter;
import zj.health.health_v1.IM.model.Conversation;
import zj.health.health_v1.IM.model.CustomMessage;
import zj.health.health_v1.IM.model.FriendshipConversation;
import zj.health.health_v1.IM.model.GroupManageConversation;
import zj.health.health_v1.IM.model.MessageFactory;
import zj.health.health_v1.IM.model.NomalConversation;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Consultation_Room_Activity extends BaseActivity implements View.OnClickListener,ConversationView,FriendshipMessageView,GroupManageMessageView {

    private ImageView wallet,back;
    private RecyclerView user_recycle;
    private RelativeLayout list_null_rela,history_im_rela;
    private Consultation_Room_Adapter consultation_room_adapter = null;
    private RelativeLayout Patient_rela,bbs_rela,prescription_rela;
    private TextView title;
    private Intent it = null;
    public static List<ConsultListModel> consultListModelList = new ArrayList<>();
    private StringBuffer stringBuffer = new StringBuffer();
    private SwipeRefreshLayout swipe_view;
    private int position;


    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private GroupManagerPresenter groupManagerPresenter;
    private List<String> groupList;
    private FriendshipConversation friendshipConversation;
    private GroupManageConversation groupManageConversation;
    private int ChatCount;
    private PushBroad pushBroad;


    private Handler consult_list_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("code").equals("4")){
                        CreateDialog dialog = new CreateDialog();
                        dialog.MessageDailog2(Consultation_Room_Activity.this,
                                View.inflate(Consultation_Room_Activity.this,R.layout.main_popwindow2,null),
                                getString(R.string.please_Advisory_setting));
                        return;
                    }
                     if(jsonObject.optString("msg").equals("success")){
                        consultListModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<ConsultListModel>>(){}.getType());
                        if(consultListModelList!=null && consultListModelList.size()>0){
                            list_null_rela.setVisibility(View.GONE);
                            swipe_view.setVisibility(View.VISIBLE);
                            for (int i =  0;i<consultListModelList.size();i++){
                                if(Integer.parseInt(consultListModelList.get(i).getStatus()) == 5 || Integer.parseInt(consultListModelList.get(i).getStatus()) == 6){
                                    consultListModelList.remove(i);
                                }
                            }
                            consultation_room_adapter.setConsultListModelList(consultListModelList);

                            friendshipManagerPresenter = new FriendshipManagerPresenter(Consultation_Room_Activity.this);
                            groupManagerPresenter = new GroupManagerPresenter(Consultation_Room_Activity.this);
                            presenter = new ConversationPresenter(Consultation_Room_Activity.this);
                            presenter.getConversation();
                            if(swipe_view.isRefreshing()){
                                swipe_view.setRefreshing(false);
                            }
                        }else{
                            swipe_view.setVisibility(View.GONE);
                            list_null_rela.setVisibility(View.VISIBLE);
                        }

                        //清空咨询列表(调试用)
//                        for (int i = 0;i<consultListModelList.size();i++){
//                            PostEnd_Consult(consultListModelList.get(i).getConsultId());
//                        }
//                         PostEnd_Consult(consultListModelList.get(0).getConsultId());
                    }else{
                        Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                        if(swipe_view.isRefreshing()){
                            swipe_view.setRefreshing(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(swipe_view.isRefreshing()){
                        swipe_view.setRefreshing(false);
                    }
                }
            }else if(msg.what == Constant.UserErrorCode){
                if(swipe_view.isRefreshing()){
                    swipe_view.setRefreshing(false);
                }
                Toast.makeText(Consultation_Room_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                if(swipe_view.isRefreshing()){
                    swipe_view.setRefreshing(false);
                }
                Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler invite_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject1 = new JSONObject(msg.obj.toString());
                     if(jsonObject1.optString("msg").equals("success")){
//                        consultListModelList = new Gson().fromJson(jsonObject1.optString("data"),new TypeToken<List<FriendModel>>(){}.getType());
//                        adapter.setConsultListModelList(consultListModelList);
                         getConsultList();
                        Toast.makeText(Consultation_Room_Activity.this, R.string.Invitation_to_user, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Consultation_Room_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler getChatCountHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        //获取当前会话的聊天次数
                        ChatCount = jsonObject1.optInt("count");
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).setIdentify(consultListModelList.get(position).getUserId());
                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).setConsultListModel(consultListModelList.get(position));
                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).setChatCount(ChatCount);
                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).setConsultID(consultListModelList.get(position).getConsultId());
                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).setIsDoctor(true);
                        if(!conversationList.get(consultListModelList.get(position).getConversationListPosition()).getName().contains("?")) {
                            conversationList.get(consultListModelList.get(position).getConversationListPosition())
                                    .setTitleStr(conversationList.get(consultListModelList.get(position).getConversationListPosition()).getName());
                        }else{
                            conversationList.get(consultListModelList.get(position).getConversationListPosition())
                                    .setTitleStr(consultListModelList.get(position).getNickname());
                        }
//                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).setMyIcon(Health_AppLocation.instance.Icon);
//                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).setFriendIcon(consultListModelList.get(position).getIconUrl());
                        conversationList.get(consultListModelList.get(position).getConversationListPosition()).navToDetail(Consultation_Room_Activity.this);

                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Consultation_Room_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onsultation_oom_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        user_recycle = (RecyclerView)findViewById(R.id.user_recycle);
        Patient_rela = (RelativeLayout)findViewById(R.id.Patient_rela);
        prescription_rela = (RelativeLayout)findViewById(R.id.prescription_rela);
        bbs_rela = (RelativeLayout)findViewById(R.id.bbs_rela);
        wallet = (ImageView)findViewById(R.id.wallet);
        swipe_view = (SwipeRefreshLayout)findViewById(R.id.swipe_view);
        list_null_rela = (RelativeLayout)findViewById(R.id.list_null_rela);
        history_im_rela = (RelativeLayout)findViewById(R.id.history_im_rela);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        Patient_rela.setOnClickListener(this);
        prescription_rela.setOnClickListener(this);
        bbs_rela.setOnClickListener(this);
        wallet.setOnClickListener(this);
        history_im_rela.setOnClickListener(this);
        swipe_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getConsultList();
            }
        });

    }
    private void setData(){
        consultation_room_adapter = new Consultation_Room_Adapter(this,consultListModelList,conversationList);
        user_recycle.setLayoutManager(new LinearLayoutManager(this));
        user_recycle.setAdapter(consultation_room_adapter);
        pushBroad = new PushBroad();
        consultation_room_adapter.setOnItemClick(new Consultation_Room_Adapter.ConsultItemClick() {
            @Override
            public void onConsultItemClick(View view, int Position, int Type) {
                switch (Integer.parseInt(consultListModelList.get(Position).getStatus())){
                    case 1:
                        if(Type == 1){
                            Intent it = new Intent(Consultation_Room_Activity.this,Patient_information_Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("model",consultListModelList.get(Position));
                            it.putExtra("bundle",bundle);
                            startActivity(it);
                        }else{
//                            boolean Isequals = false;
//                            for (int i = 0;i<conversationList.size();i++){
//                                if(conversationList.get(i).getIdentify().equals(consultListModelList.get(Position).getUserId())){
//                                    Isequals = true;
//                                   b    Toast.makeText(Consultation_Room_Activity.this, R.string.doctorInvite_toUser_Error, Toast.LENGTH_SHORT).show();
//                                    break;
//                                }
//                            }
//                            if(!Isequals){
//                                getConsultInvite(consultListModelList.get(Position).getConsultId());
//                            }
                            getConsultInvite(consultListModelList.get(Position).getConsultId());
                        }
                        break;
                    case 2:
                        if(Type == 1){
                            Intent it = new Intent(Consultation_Room_Activity.this,Patient_information_Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("model",consultListModelList.get(Position));
                            it.putExtra("bundle",bundle);
                            startActivityForResult(it,0x1);
                        }
                        break;
                    case 3:
                        if(Type == 1){
                            Intent it = new Intent(Consultation_Room_Activity.this,Patient_information_Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("model",consultListModelList.get(Position));
                            it.putExtra("bundle",bundle);
                            startActivityForResult(it,0x1);
                        }
                        break;
                    case 4:
                        if(Type == 1){
                            position = Position;
                            LoadingDialog.getLoadingDialog().StartLoadingDialog(Consultation_Room_Activity.this);
                            getChatCount(consultListModelList.get(Position).getConsultId());

                        }
                        break;
                    default:
                        break;
                }
            }
        });

        getConsultList();

    }

    private void getConsultList(){
        new PostUtils().Get(Constant.getConsultList,true,consult_list_handler,this);
    }

    /**
     * 获取当前聊天次数
     */
    private void getChatCount(String consultId){
        String data = "consultId"+"="+consultId;
        new PostUtils().Get(Constant.getChatCount+data,true,getChatCountHandler,this);
    }

    private void getConsultInvite(String consultId){
        Map<String,String> map = new HashMap<>();
        map.put("consultId",consultId);
        new PostUtils().getNewPost(Constant.getConsultInvite,map,invite_Handler,this);
    }




    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */
    @Override
    public void initView(final List<TIMConversation> conversationList) {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item:conversationList){
            switch (item.getType()){
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    break;
            }
        }
        //由于没有头像参数 需要先调用api循环获取每个用户的头像
        List<String> users = new ArrayList<String>();
        for (int i = 0 ;i<this.conversationList.size();i++){
            users.clear();
            users.add(this.conversationList.get(i).getIdentify());
            //获取好友资料
            final int finalI = i;
            TIMFriendshipManagerExt.getInstance().getFriendsProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
                @Override
                public void onError(int code, String desc){
                    //错误码 code 和错误描述 desc，可用于定位请求失败原因
                    //错误码 code 列表请参见错误码表
                    friendshipManagerPresenter.getFriendshipLastMessage();
                    groupManagerPresenter.getGroupManageLastMessage();
                }

                @Override
                public void onSuccess(List<TIMUserProfile> result){
//                    for (int i = 0 ;i<Doctor_service_Activity.this.conversationList.size();i++){
//                        Doctor_service_Activity.this.conversationList.get(i).setFriendIcon(result.get(0).getFaceUrl());
//                    }
                    Consultation_Room_Activity.this.conversationList.get(finalI).setFriendIcon(result.get(0).getFaceUrl());
                    Consultation_Room_Activity.this.conversationList.get(finalI).setMyIcon(Health_AppLocation.instance.Icon);
//                    friendshipManagerPresenter.getFriendshipLastMessage();
//                    groupManagerPresenter.getGroupManageLastMessage();
                }
            });
        }
        friendshipManagerPresenter.getFriendshipLastMessage();


    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {

        if (message == null){
            return;
        }

        if (message.getConversation().getType() == TIMConversationType.System){
            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator =conversationList.iterator();
        while (iterator.hasNext()){
            Conversation c = iterator.next();
            if (conversation.equals(c)){
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while(iterator.hasNext()){
            Conversation conversation = iterator.next();
            if (conversation.getIdentify()!=null&&conversation.getIdentify().equals(identify)){
                iterator.remove();
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {

    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
        Collections.sort(conversationList);
        consultation_room_adapter.setConversationList(conversationList);
        if (this instanceof BaseActivity){
//            ((HomeActivity) getActivity()).setMsgUnread(getTotalUnreadNum() == 0);
        }
    }



    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message 最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {
        if (groupManageConversation == null){
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        }else{
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Conversation conversation = conversationList.get(info.position);
        if (conversation instanceof NomalConversation){
            menu.add(0, 1, Menu.NONE, getString(R.string.conversation_del));
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NomalConversation conversation = (NomalConversation) conversationList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                if (conversation != null){
                    if (presenter.delConversation(conversation.getType(), conversation.getIdentify())){
                        conversationList.remove(conversation);
//                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private long getTotalUnreadNum(){
        long num = 0;
        for (Conversation conversation : conversationList){
            num += conversation.getUnreadNum();
        }
        return num;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.Patient_rela:
                it = new Intent(Consultation_Room_Activity.this,My_Patient_Activity.class);
                startActivity(it);
                break;
            case R.id.prescription_rela:
                it = new Intent(Consultation_Room_Activity.this,Prescription_Manage_Activity.class);
                startActivity(it);
//                Toast.makeText(this, getString(R.string.coming_opening), Toast.LENGTH_SHORT).show();
                break;
            case R.id.bbs_rela:
//                it = new Intent(Consultation_Room_Activity.this,Doctor_bbs_Activity.class);
//                startActivity(it);
                Toast.makeText(this, getString(R.string.coming_opening), Toast.LENGTH_SHORT).show();
                break;
            case R.id.wallet:
                it = new Intent(Consultation_Room_Activity.this,My_Wallet_ForDoctor_Activity.class);
                startActivity(it);
                break;
            case R.id.history_im_rela:
                it = new Intent(this,History_IM_Activity.class);
                startActivity(it);
                break;
            default:
                break;
        }
    }


    private void PostEnd_Consult(String consultId){
        Map<String,String> map = new HashMap<>();
        map.put("consultId",consultId);
        new PostUtils().getNewPost(Constant.end_consult,map,End_Consult_Handler,this);
    }

    private Handler End_Consult_Handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){

                    }else{
                        Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(Consultation_Room_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Consultation_Room_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0x111){
            getConsultList();
        }else if(resultCode == 0x3){
//            LoadingDialog.getLoadingDialog().StartLoadingDialog(this);
//            String identify = data.getStringExtra("identify");
//            boolean delete = TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, identify);
//            LoadingDialog.getLoadingDialog().StopLoadingDialog();
//            if(delete){
//                getConsultList();
//            }
            getConsultList();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateList");
        if(pushBroad!=null){
            registerReceiver(pushBroad,intentFilter);
        }else{
            pushBroad = new PushBroad();
            registerReceiver(pushBroad,intentFilter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(pushBroad!=null){
            unregisterReceiver(pushBroad);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter!=null){
            presenter.removeObserver();
        }
    }

     class PushBroad extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            getConsultList();
        }
    }
}
