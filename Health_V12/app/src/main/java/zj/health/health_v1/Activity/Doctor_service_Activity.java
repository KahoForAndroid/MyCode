package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.DoctorSerive_Adapter;
import zj.health.health_v1.Adapter.DoctorServiceAdapter;
import zj.health.health_v1.Adapter.DoctorServiceConversationAdapter;
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
import zj.health.health_v1.IM.ui.HomeActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.Model.ConsultModel;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/4/11.
 */

public class Doctor_service_Activity extends BaseActivity implements View.OnClickListener,ConversationView,FriendshipMessageView,GroupManageMessageView {

    private ImageView back;
    private TextView title;
    private RecyclerView doctor_recycle;
    private RelativeLayout list_null_rela;
    private Intent it = null;
    private RelativeLayout family_doctor_rela,prescription_rela,Health_counseling_rela,history_im_rela;
    private List<ConsultListModel> consultListModelList = new ArrayList<>();
    private List<Conversation> conversationList = new LinkedList<>();
    public static List<ConsultModel> consultModelList = new ArrayList<>();
    private DoctorServiceConversationAdapter adapter;
    private DoctorServiceAdapter doctorSerive_adapter;
    private ListView listView;
    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private GroupManagerPresenter groupManagerPresenter;
    private List<String> groupList;
    private FriendshipConversation friendshipConversation;
    private GroupManageConversation groupManageConversation;
    private int ChatCount = 0;
    private int Position = 0;
    private String consultId = null;
    private int position = 0;
    private boolean updateList = false;
    private String titleStr = null;

    private Handler consult_list_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        consultListModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<ConsultListModel>>(){}.getType());
//                        if(consultListModelList!=null && consultListModelList.size()>0){
//
//                            getConversation(consultListModelList);
//
//                        }else{
//                            presenter.getConversation();
//                        }
                        if(consultListModelList == null){
                            consultListModelList = new ArrayList<>();
                        }
                        friendshipManagerPresenter = new FriendshipManagerPresenter(Doctor_service_Activity.this);
                        groupManagerPresenter = new GroupManagerPresenter(Doctor_service_Activity.this);
                        presenter = new ConversationPresenter(Doctor_service_Activity.this);
                        registerForContextMenu(listView);
                        presenter.getConversation();
                    }else{
                        Toast.makeText(Doctor_service_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }else if(msg.what == Constant.UserErrorCode){

                Toast.makeText(Doctor_service_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{

                Toast.makeText(Doctor_service_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler ChatSelectConsultIDHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        //获取当前会话的聊天次数
                        List<String> stringList = new Gson().fromJson(jsonObject1.optString("consultId"),new TypeToken<List<String>>(){}.getType());
                        consultId = stringList.get(0);
//                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        getChatCount(consultId);

                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(Doctor_service_Activity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Doctor_service_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Doctor_service_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }};


    private Handler getCommunicatingHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
             if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        consultModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<ConsultModel>>(){}.getType());
                        if(consultModelList.size()>0){
                            list_null_rela.setVisibility(View.GONE);
                            doctor_recycle.setVisibility(View.VISIBLE);
                            doctorSerive_adapter.setConsultListModelList(consultModelList);
                            if(conversationList.size()==0){
                                friendshipManagerPresenter = new FriendshipManagerPresenter(Doctor_service_Activity.this);
                                groupManagerPresenter = new GroupManagerPresenter(Doctor_service_Activity.this);
                                presenter = new ConversationPresenter(Doctor_service_Activity.this);
                                presenter.getConversation();
                            }
                        }else{
                            doctorSerive_adapter.setConsultListModelList(consultModelList);
                            doctor_recycle.setVisibility(View.GONE);
                            list_null_rela.setVisibility(View.VISIBLE);
                        }
                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(Doctor_service_Activity.this, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Doctor_service_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Doctor_service_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }};


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
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        ChatCount = jsonObject1.optInt("count");
//                        if(conversationList.size() == 0){
//                            conversationList.add(new NomalConversation(new TIMConversation()));
//                        }
                        conversationList.get(Position).setIdentify(consultModelList.get(Position).getDoctorId());
                        conversationList.get(Position).setChatCount(ChatCount);
                        conversationList.get(Position).setConsultID(consultId);
                        conversationList.get(Position).setIsDoctor(false);
                        conversationList.get(Position).setTitleStr(consultModelList.get(Position).getDoctorName());
                        conversationList.get(Position).setFriendIcon(consultModelList.get(Position).getDoctorIcon());
                        conversationList.get(Position).setDoctorId(consultModelList.get(Position).getDoctorId());
                        conversationList.get(Position).navToDetail(Doctor_service_Activity.this);
                        if (conversationList.get(Position) instanceof GroupManageConversation) {
                            groupManagerPresenter.getGroupManageLastMessage();
                        }
                    }else{
                        LoadingDialog.getLoadingDialog().StopLoadingDialog();
                        Toast.makeText(Doctor_service_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingDialog.getLoadingDialog().StopLoadingDialog();
                }
            }else if(msg.what == Constant.UserErrorCode){
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Doctor_service_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(Doctor_service_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }};
    private Handler setIconHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (int i = 0;i<consultListModelList.size();i++){
                if(Integer.parseInt(consultListModelList.get(i).getStatus()) == 3 || Integer.parseInt(consultListModelList.get(i).getStatus()) == 4){
                    for (int k = 0;k<conversationList.size();k++) {
                        if (consultListModelList.get(i).getUserId().equals(conversationList.get(k).getIdentify())) {
                            conversationList.remove(k);
                        }
                    }
                }
            }
                friendshipManagerPresenter.getFriendshipLastMessage();
                groupManagerPresenter.getGroupManageLastMessage();

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_service_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        doctor_recycle = (RecyclerView)findViewById(R.id.doctor_recycle);
        family_doctor_rela = (RelativeLayout)findViewById(R.id.family_doctor_rela);
        prescription_rela = (RelativeLayout)findViewById(R.id.prescription_rela);
        Health_counseling_rela = (RelativeLayout)findViewById(R.id.Health_counseling_rela);
        listView = (ListView)findViewById(R.id.list);
        list_null_rela = (RelativeLayout)findViewById(R.id.list_null_rela);
        history_im_rela = (RelativeLayout)findViewById(R.id.history_im_rela);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        family_doctor_rela.setOnClickListener(this);
        prescription_rela.setOnClickListener(this);
        Health_counseling_rela.setOnClickListener(this);
        history_im_rela.setOnClickListener(this);

    }
    private void setData(){
        title.setText(R.string.Doctor_service);
        doctorSerive_adapter = new DoctorServiceAdapter(this,consultModelList,conversationList);
        doctorSerive_adapter.setConsultItemClick(new DoctorServiceAdapter.ConsultItemClick() {
            @Override
            public void onConsultItemClick(View view, int Position, int Type) {
                position = Position;
                LoadingDialog.getLoadingDialog().StartLoadingDialog(Doctor_service_Activity.this);
                consultId = consultModelList.get(Position).getId();
                getChatCount(consultId);

            }
        });
        doctor_recycle.setLayoutManager(new LinearLayoutManager(this));
        doctor_recycle.setAdapter(doctorSerive_adapter);
//        getCommunicating();
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

    /**
     * 获取当前会话对应的咨询ID
     */
    private void ChatSelectConsultID(String conversationId){
        String data = "conversationId"+"="+conversationId;
        new PostUtils().Get(Constant.ChatSelectConsultID+data,true,ChatSelectConsultIDHandler,this);
    }

    /**
     * 获取正在沟通的健康咨询列表
     */
    private void getCommunicating(){
        new PostUtils().Get(Constant.get_communicating,true,getCommunicatingHandler,this);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.family_doctor_rela:
                it = new Intent(Doctor_service_Activity.this,IM_Test_Activity.class);
                startActivity(it);
                break;
            case R.id.prescription_rela:
                it = new Intent(Doctor_service_Activity.this,Prescription_Activity.class);
                startActivity(it);
//                Toast.makeText(this, getString(R.string.coming_opening), Toast.LENGTH_SHORT).show();
                break;
            case R.id.Health_counseling_rela:
                it = new Intent(Doctor_service_Activity.this,Health_counseling_Activity.class);
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


//        //清除所有会话列表 (调试用)
//        for (int i = 0;i<this.conversationList.size();i++){
//            boolean delete = TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, this.conversationList.get(i).getIdentify());
//        }

//            for (int i = 0; i < consultListModelList.size(); i++) {
//                if (Integer.parseInt(consultListModelList.get(i).getStatus()) == 3 || Integer.parseInt(consultListModelList.get(i).getStatus())==4) {
//                    for (int j = 0;j<this.conversationList.size();j++) {
//                        if (consultListModelList.get(i).getUserId().equals(this.conversationList.get(j).getIdentify())) {
//                            this.conversationList.remove(j);
//                            break;
//                        }
//                    }
//                }
//            }

        //由于没有头像参数 需要先调用api循环获取每个用户的头像
        List<String> users = new ArrayList<String>();
        for (int i = 0 ;i<this.conversationList.size();i++) {
            users.add(this.conversationList.get(i).getIdentify());
        }

        //获取好友资料
        TIMFriendshipManagerExt.getInstance().getFriendsProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                friendshipManagerPresenter.getFriendshipLastMessage();
                groupManagerPresenter.getGroupManageLastMessage();
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result) {
                synchronized (this) {
                    for (int j = 0; j < result.size(); j++) {
                        for (int i = 0; i < Doctor_service_Activity.this.conversationList.size(); i++) {
                            if (Doctor_service_Activity.this.conversationList.get(i).getIdentify().equals(result.get(j).getIdentifier())) {
                                String name = null;

                                Doctor_service_Activity.this.conversationList.get(i).setFriendIcon(result.get(0).getFaceUrl());
                                Doctor_service_Activity.this.conversationList.get(i).setMyIcon(Health_AppLocation.instance.Icon);
                                Doctor_service_Activity.this.conversationList.get(i).setTitleStr(result.get(0).getNickName());
                            }
                        }
                    }
                }
                setIconHandler.sendEmptyMessage(200);
            }
        });



    }




    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {

        if (message == null){
            adapter.notifyDataSetChanged();
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


//        //如果用户本身是医生角色,那么需要通过循环筛选掉不需要的会话
//        for (int i = 0;i<consultListModelList.size();i++){
//            //筛选条件是跟用户已经进行咨询或者已经结束咨询的筛选掉
//            if(Integer.parseInt(consultListModelList.get(i).getStatus()) == 3 || Integer.parseInt(consultListModelList.get(i).getStatus()) == 4){
//                for (int k = 0;k<conversationList.size();k++) {
//                    if (consultListModelList.get(i).getUserId().equals(conversationList.get(k).getIdentify())) {
//                        conversationList.remove(k);
//                    }
//                }
//            }
//        }

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
                adapter.notifyDataSetChanged();
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
        for (Conversation conversation : conversationList){
            if (conversation.getIdentify()!=null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())){
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
//        if(conversationList.size()>0){
//            listView.setVisibility(View.VISIBLE);
//            list_null_rela.setVisibility(View.GONE);
//            Collections.sort(conversationList);
//        }else {
//            listView.setVisibility(View.GONE);
//            list_null_rela.setVisibility(View.VISIBLE);
//        }
        Collections.sort(conversationList);
        doctorSerive_adapter.setConversationList(conversationList);
//        adapter.notifyDataSetChanged();
//        adapter.NeedToUpdate(updateList);
//        if (this instanceof BaseActivity){
////            ((HomeActivity) getActivity()).setMsgUnread(getTotalUnreadNum() == 0);
//    }
    }



    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message 最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
//        if (friendshipConversation == null){
//            friendshipConversation = new FriendshipConversation(message);
//            conversationList.add(friendshipConversation);
//        }else{
//            friendshipConversation.setLastMessage(message);
//        }
//        friendshipConversation.setUnreadCount(unreadCount);
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
//                    if (presenter.delConversation(conversation.getType(), conversation.getIdentify())){
//                        conversationList.remove(conversation);
//                        adapter.notifyDataSetChanged();
//                    }
                    boolean delete = TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, conversation.getIdentify());
                    if (delete) {
                        presenter.getConversation();
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


    public void getConversation(List<ConsultListModel> consultListModelList){
        boolean isDoctorConversation = false;
        List<TIMConversation> list = TIMManagerExt.getInstance().getConversationList();
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : list){
            if (conversation.getType() == TIMConversationType.System){
                continue;
            };
            if(consultListModelList != null || consultListModelList.size() > 0){
                for (ConsultListModel consultListModel: consultListModelList ){
                    if(Integer.parseInt(consultListModel.getStatus()) == 3){
                        if(conversation.getPeer().equals(consultListModel.getUserId())){
                            isDoctorConversation = true;
                            break;
                        }else{
                            isDoctorConversation = false;
                        }
                    }else{
                        isDoctorConversation = false;
                    }
                }
            }

            if(!isDoctorConversation){
                result.add(conversation);
            }

            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            conversationExt.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e("doctor_service", "get message error" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        updateMessage(timMessages.get(0));
                    }

                }
            });

        }
        initView(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == 0x3) {
//            String identify = data.getStringExtra("identify");
//            boolean delete = TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, identify);
//            if (delete) {
//                presenter.getConversation();
//                getCommunicating();
//            }
//        }
        getCommunicating();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommunicating();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter!=null){
            presenter.removeObserver();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}
