package zj.health.health_v1.IM.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.qcloud.presentation.presenter.ChatPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ChatView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Activity.Counselling_list_Activity;
import zj.health.health_v1.Activity.Doctor_service_Activity;
import zj.health.health_v1.Activity.Health_counseling_Activity;
import zj.health.health_v1.Activity.IM_Doctor_introduce_Activity;
import zj.health.health_v1.Activity.IM_Select_Report_Activity;
import zj.health.health_v1.Activity.IM_Send_DrugtoUsers_Activity;
import zj.health.health_v1.Activity.Patient_information_Activity;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.AuxiliaryUtil.ChatInput;
import zj.health.health_v1.IM.AuxiliaryUtil.TemplateTitle;
import zj.health.health_v1.IM.AuxiliaryUtil.VoiceSendingView;
import zj.health.health_v1.IM.adapters.ChatAdapter;
import zj.health.health_v1.IM.model.CustomMessage;
import zj.health.health_v1.IM.model.FileMessage;
import zj.health.health_v1.IM.model.FriendProfile;
import zj.health.health_v1.IM.model.FriendshipInfo;
import zj.health.health_v1.IM.model.GroupInfo;
import zj.health.health_v1.IM.model.ImageMessage;
import zj.health.health_v1.IM.model.Message;
import zj.health.health_v1.IM.model.MessageFactory;
import zj.health.health_v1.IM.model.TextMessage;
import zj.health.health_v1.IM.model.UGCMessage;
import zj.health.health_v1.IM.model.VideoMessage;
import zj.health.health_v1.IM.model.VoiceMessage;
import zj.health.health_v1.IM.utils.FileUtil;
import zj.health.health_v1.IM.utils.MediaUtil;
import zj.health.health_v1.IM.utils.RecorderUtil;
import zj.health.health_v1.Implements.IM_OnItemClick;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.Model.QuestionModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.MyView.LoadingDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

public class ChatActivity extends FragmentActivity implements ChatView {

    private static final String TAG = "ChatActivity";

    private List<Message> messageList = new ArrayList<>();
    private ChatAdapter adapter;
    private ListView listView;
    private TemplateTitle title;
    private ChatPresenter presenter;
    private ChatInput input;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;
    private static final int VIDEO_RECORD = 500;
    private static final int SEND_DRUG = 600;
    private Uri fileUri;
    private VoiceSendingView voiceSendingView;
    private String identify,doctorId;
    private RecorderUtil recorder = new RecorderUtil();
    private TIMConversationType type;
    private String titleStr;
    private Handler handler = new Handler();
//    private MessageEntity messageEntity;//记录聊天次数实体类
    private ConsultListModel consultListModel;
    private String friendIcon;//对方头像
    private String myIcon;//自己的头像
    private int chatType;//聊天类型
    private int ChatCount;//当前聊天次数
    private String consultId;//对应的咨询ID
    private boolean isDoctor;//是否作为医生去会话患者
    private boolean isDelete = false;//是否需要删除会话(聊天次数达到上限时需要删除)
    private final int SEND_TO_HEALTH = 1;//发送健康日志类型
    private final int SEND_TO_REPORT = 2;//发送体检报告类型
    private final int SEND_TO_DRUG = 3;//发送药物建议类型
    private final int END_THE_CHAT = 4;//发起结束聊天
    private final int AGREE_END_THE_CHAT = 5;//同意结束聊天
    private final int REFUSE_END_THE_CHAT = 6;//拒绝结束聊天

    private Handler messageHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if(messageList.size()-1>0) {
                    if (!messageList.get(messageList.size() - 2).isSelf()) {
                        inc_ChatPost(consultId);
                    }
                }
            }

        }};
    private Handler End_the_chat_messageHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                Toast.makeText(ChatActivity.this, getString(R.string.end_the_chat_send_success), Toast.LENGTH_SHORT).show();
            }

        }};


    private Handler agree_or_refuse_messageHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                if(msg.obj.toString().equals(consultId)){
                    PostEnd_Consult(consultId);
                }
            }else if(msg.what == 2){
                if(msg.obj.toString().equals(consultId)){
                    send_CustomElem(REFUSE_END_THE_CHAT);
                }
            }else if(msg.what == 3){
                if(msg.obj.toString().equals(consultId)){
                    ChatCount = 41;
                    isDelete = true;
                    title.setDelete(isDelete);
                    input.setChatCount(41);
                    Intent it = new Intent();
                    it.putExtra("identify",identify);
                    setResult(0x3,it);
                }
            }
        }};
    private Handler End_Consult_Handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        isDelete = true;
                        title.setDelete(isDelete);
                        input.setChatCount(41);
                        Intent it = new Intent();
                        it.putExtra("identify",identify);
                        setResult(0x3,it);
                        send_CustomElem(AGREE_END_THE_CHAT);
                        finish();
                    }else{
                        Toast.makeText(ChatActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(ChatActivity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(ChatActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Handler addMessageHandler = new Handler(){
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
                        input.setChatCount(ChatCount);
                    }else{
                        isDelete = true;
                        title.setDelete(isDelete);
                        input.setChatCount(41);
                        Intent it = new Intent();
                        it.putExtra("identify",identify);
                        setResult(0x3,it);

                        CreateDialog dialog = new CreateDialog();
                        dialog.MessageDailog2(ChatActivity.this,
                                View.inflate(ChatActivity.this,R.layout.main_popwindow2,null),
                                getString(R.string.thisChatisEnd));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(ChatActivity.this, getString(R.string.user_error_message), Toast.LENGTH_SHORT).show();
            }else{
                LoadingDialog.getLoadingDialog().StopLoadingDialog();
                Toast.makeText(ChatActivity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }};




    public static void navToChat(Context context, String identify, TIMConversationType type){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        if(context instanceof BaseActivity){
            ((BaseActivity) context).startActivityForResult(intent,0x1);
        }else{
            context.startActivity(intent);
        }
    }


    /**
     * 跳转到聊天界面
     * @param context 上下文
     * @param identify IM用户的identify(即app用户的userId)
     * @param friendIcon 对方头像
     * @param MyIcon 自己的头像
     * @param ChatCount 剩余的聊天次数
     * @param consultId 该会话聊天对应的咨询ID
     * @param isDoctor 是否医生
     * @param type 聊天类型
     * @param consultListModel 带有患者基本信息的实体类
     */
    public static void navToChat(Context context, String identify, String friendIcon, String MyIcon,
                                 int ChatCount,String consultId,String titleStr,boolean isDoctor,TIMConversationType type,
                                 ConsultListModel consultListModel,String doctorId){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        intent.putExtra("friendIcon", friendIcon);
        intent.putExtra("myIcon", MyIcon);
        intent.putExtra("ChatCount", ChatCount);
        intent.putExtra("consultId", consultId);
        intent.putExtra("isDoctor",isDoctor);
        intent.putExtra("titleStr",titleStr);
        intent.putExtra("doctorId",doctorId);
        if(consultListModel != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("model",consultListModel);
            intent.putExtra("bundle",bundle);
        }
        if(context instanceof BaseActivity){
            ((BaseActivity) context).startActivityForResult(intent,0x1);
        }else{
            context.startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        identify = getIntent().getStringExtra("identify");
        type = (TIMConversationType) getIntent().getSerializableExtra("type");
        friendIcon = getIntent().getStringExtra("friendIcon");
        myIcon = getIntent().getStringExtra("myIcon");
        ChatCount = getIntent().getIntExtra("ChatCount",0);
        consultId = getIntent().getStringExtra("consultId");
        isDoctor = getIntent().getBooleanExtra("isDoctor",false);
        titleStr = getIntent().getStringExtra("titleStr");
        doctorId = getIntent().getStringExtra("doctorId");
        title = (TemplateTitle) findViewById(R.id.chat_title);
        title.setConsult(getIntent().getBooleanExtra("consult",false));

        if(getIntent().getBundleExtra("bundle")!=null){
            consultListModel = (ConsultListModel) getIntent().getBundleExtra("bundle").getSerializable("model");
        }
        if(ChatCount>=41){
            setIsDelete(true);
            title.setDelete(true);
        }else{
            setIsDelete(false);
            title.setDelete(false);
        }
        presenter = new ChatPresenter(this, identify, type);
        input = (ChatInput) findViewById(R.id.input_panel);
        input.setIsDoctor(isDoctor);
        input.initView();
        input.setChatView(this);
        input.setChatCount(ChatCount);
        if(getIntent().getBooleanExtra("isHistory",false)){
            input.setVisibility(View.GONE);
        }
        chatType = getIntent().getIntExtra("chatType",0);
        adapter = new ChatAdapter(this, R.layout.item_message, messageList,myIcon,friendIcon,agree_or_refuse_messageHandler);
        adapter.setIm_onItemClick(new IM_OnItemClick() {
            @Override
            public void OnIiemLeftClick(Message message) {
                if(message.getDesc()!=null){
                    Toast.makeText(ChatActivity.this, "get", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void OnIiemRightClick(Message message) {
                if(message instanceof CustomMessage){
                    if(((CustomMessage) message).getData()!=null){
                        Toast.makeText(ChatActivity.this, "get", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        input.setInputMode(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int firstItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItem == 0) {
                    //如果拉到顶端读取更多消息
                    presenter.getMessage(messageList.size() > 0 ? messageList.get(0).getMessage() : null);

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
            }
        });
        registerForContextMenu(listView);

        switch (type) {
            case C2C:
                title.setMoreImg(R.drawable.btn_person);
                if(getIntent().getBooleanExtra("isHistory",false)){
                    title.gone_btn_more();
                }
                if (FriendshipInfo.getInstance().isFriend(identify)){
                    title.setMoreImgAction(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!isDoctor){
                                Intent it = new Intent(ChatActivity.this,IM_Doctor_introduce_Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("model",consultListModel);
                                it.putExtra("bundle",bundle);
                                it.putExtra("doctorId",doctorId);
                                startActivity(it);
                            }else{
                                Intent it = new Intent(ChatActivity.this,Patient_information_Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("model",consultListModel);
                                it.putExtra("bundle",bundle);
                                it.putExtra("isChat",true);
                                startActivity(it);
                            }

                        }
                    });
                    FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
//                    title.setTitleText(titleStr = profile == null ? identify : profile.getName());
                    title.setTitleText(titleStr);
                }else{
                    title.setMoreImgAction(new View.OnClickListener() {
                         @Override
                        public void onClick(View v) {
                            Intent person = new Intent(ChatActivity.this,AddFriendActivity.class);
                            person.putExtra("id",identify);
                            person.putExtra("name",identify);
                            startActivity(person);
                        }
                    });
                    title.setTitleText(titleStr = identify);
                }
                break;
            case Group:
                title.setMoreImg(R.drawable.btn_group);
                title.setMoreImgAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatActivity.this, GroupProfileActivity.class);
                        intent.putExtra("identify", identify);
                        startActivity(intent);
                    }
                });
                title.setTitleText(GroupInfo.getInstance().getGroupName(identify));
                break;

        }
        voiceSendingView = (VoiceSendingView) findViewById(R.id.voice_sending);
        presenter.start();

        switch (chatType){
            case Constant.SELECTDOCTORSTATE:
                input.setText(getString(R.string.have_a_question));
                sendText();
                break;
                default:
                    break;
        }
    }




    @Override
    protected void onPause(){
        super.onPause();
        //退出聊天界面时输入框有内容，保存草稿
        if (input.getText().length() > 0){
            TextMessage message = new TextMessage(input.getText());
            presenter.saveDraft(message.getMessage());
        }else{
            presenter.saveDraft(null);
        }
//        RefreshEvent.getInstance().onRefresh();
        presenter.readMessages();
        MediaUtil.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
//        if(isDelete){
//            Intent it = new Intent();
//            it.putExtra("identify",identify);
//            setResult(0x3,it);
////            boolean delete = TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, identify);
//        }
        if (getIntent().getBooleanExtra("consult",false)){
            Health_counseling_Activity.activity.finish();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 显示消息
     *
     * @param message
     */
    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            adapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
                if (mMessage instanceof CustomMessage){
                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
                    switch (messageType){
                        case TYPING:
                            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
                            title.setTitleText(getString(R.string.chat_typing));
                            handler.removeCallbacks(resetTitle);
                            handler.postDelayed(resetTitle,3000);
                            break;
                        case INVALID:
                            if (messageList.size()==0){
                                mMessage.setHasTime(null);
                            }else{
                                mMessage.setHasTime(messageList.get(messageList.size()-1).getMessage());
                            }

//                            int Frequency = messageEntity.getFrequency();
//                            messageEntity.setFrequency(Frequency++);
//                            dbUtils.setMessageEntity(messageEntity);

                            messageList.add(mMessage);
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount()-1);
                            break;
                        default:
                            break;
                    }
                }else{
                    if (messageList.size()==0){
                        mMessage.setHasTime(null);
                    }else{
                        mMessage.setHasTime(messageList.get(messageList.size()-1).getMessage());
                    }
                    messageList.add(mMessage);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(adapter.getCount()-1);
                }

            }
        }

    }

    /**
     * 显示消息
     *
     * @param messages
     */
    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i){
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted) continue;
//            if (mMessage instanceof CustomMessage && (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING ||
//                    ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1){
                mMessage.setHasTime(messages.get(i+1));
                messageList.add(0, mMessage);
            }else{
                mMessage.setHasTime(null);
                messageList.add(0, mMessage);
            }
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(newMsgNum);
    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {
        for (Message msg : messageList) {
            TIMMessageExt ext = new TIMMessageExt(msg.getMessage());
            if (ext.checkEquals(timMessageLocator)) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 上传聊天次数自增
     * @param consultId 会话对应的咨询ID
     */
    private void inc_ChatPost(String consultId){
        Map<String,String> map = new HashMap<>();
        map.put("consultId",consultId);
        new PostUtils().getNewPostIM(Constant.inc_chat,map,addMessageHandler,this);
    }



    /**
     * 清除所有消息，等待刷新
     */
    @Override
    public void clearAllMessage() {
        messageList.clear();
    }

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */
    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        long id = message.getMsgUniqueId();
        for (Message msg : messageList){
            if (msg.getMessage().getMsgUniqueId() == id){
                switch (code){
                    case 80001:
                        //发送内容包含敏感词
                        msg.setDesc(getString(R.string.chat_content_bad));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        adapter.notifyDataSetChanged();

    }

    private void PostEnd_Consult(String consultId){
        Map<String,String> map = new HashMap<>();
        map.put("consultId",consultId);
        new PostUtils().getNewPostIM(Constant.end_consult,map,End_Consult_Handler,this);
    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    /**
     * 发送照片消息
     */
    @Override
    public void sendPhoto() {
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_photo.resolveActivity(getPackageManager()) != null) {
            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
            if (tempFile != null) {
                fileUri = Uri.fromFile(tempFile);
            }
            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent_photo, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText() {
        Message message = new TextMessage(input.getText());
        presenter.sendMessage(message.getMessage(),messageHandler);
        input.setText("");
    }

    /**
     * 发送文件
     */
    @Override
    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CODE);
    }


    /**
     * 开始发送语音消息
     */
    @Override
    public void startSendVoice() {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
        recorder.startRecording();

    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void endSendVoice() {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
        } else if (recorder.getTimeInterval() > 60) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_long), Toast.LENGTH_SHORT).show();
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            presenter.sendMessage(message.getMessage(),messageHandler);
        }
    }

    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    @Override
    public void sendVideo(String fileName) {
        Message message = new VideoMessage(fileName);
        presenter.sendMessage(message.getMessage(),messageHandler);
    }


    /**
     * 结束发送语音消息
     */
    @Override
    public void cancelSendVoice() {

    }

    /**
     * 正在发送
     */
    @Override
    public void sending() {
        if (type == TIMConversationType.C2C){
            Message message = new CustomMessage(CustomMessage.Type.TYPING);
            presenter.sendOnlineMessage(message.getMessage());
        }
    }

    /**
     * 显示草稿
     */
    @Override
    public void showDraft(TIMMessageDraft draft) {
        input.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    @Override
    public void videoAction() {
        Intent intent = new Intent (this, TCVideoRecordActivity.class);
        startActivityForResult(intent, VIDEO_RECORD);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void send_CustomElem(int Type) {
        if(Type == SEND_TO_HEALTH){
            showToast(getString(R.string.coming_opening));
        }else if(Type == SEND_TO_REPORT){
            getReport();
        }else if(Type == SEND_TO_DRUG){
//            showToast(getString(R.string.coming_opening));
            Intent it = new Intent(this, IM_Send_DrugtoUsers_Activity.class);
            it.putExtra("userid",identify);
            startActivityForResult(it,0x22);
        }else if(Type == END_THE_CHAT){
            String a = "{\"userAction\": 14,\"actionParam\":" +
                    " \"EIMAMSG_InputStatus_End\",\"type\": '"+END_THE_CHAT+"'}";
            TIMMessage timMessage = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(a.getBytes());
            elem.setDesc(consultId);
            if(timMessage.addElement(elem)!=0){
                return;
            }
            Message message = new CustomMessage(timMessage);
            presenter.sendMessage(message.getMessage(),End_the_chat_messageHandler);
        }else if(Type == AGREE_END_THE_CHAT){
            String a = "{\"userAction\": 14,\"actionParam\":" +
                    " \"EIMAMSG_InputStatus_End\",\"type\": '"+AGREE_END_THE_CHAT+"'}";
            TIMMessage timMessage = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(a.getBytes());
            elem.setDesc(consultId);
            if(timMessage.addElement(elem)!=0){
                return;
            }
            Message message = new CustomMessage(timMessage);
            presenter.sendMessage(message.getMessage(),agree_or_refuse_messageHandler);
        }else if(Type == REFUSE_END_THE_CHAT){
            String a = "{\"userAction\": 14,\"actionParam\":" +
                    " \"EIMAMSG_InputStatus_End\",\"type\": '"+REFUSE_END_THE_CHAT+"'}";
            TIMMessage timMessage = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(a.getBytes());
            elem.setDesc(consultId);
            if(timMessage.addElement(elem)!=0){
                return;
            }
            Message message = new CustomMessage(timMessage);
            presenter.sendMessage(message.getMessage(),agree_or_refuse_messageHandler);
        }

    }

    @Override
    public void setIsDelete(boolean IsDelete) {
        this.isDelete = IsDelete;
        title.setDelete(isDelete);
        if(isDelete){
            Intent it = new Intent();
            it.putExtra("identify",identify);
            setResult(0x3,it);
        }
    }

    @Override
    public boolean getIsDelete() {
        return isDelete;
    }

    private void getReport(){
        new PostUtils().Get(Constant.report_list,true,ReportHandler, this);
    }

    private Handler ReportHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                if (msg.obj!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        List<NewReportModel> list = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<NewReportModel>>(){}.getType());
                        if(list.size() == 0){
                            showToast(getString(R.string.report_list_null));
                            return;
                        }
                        Intent it = new Intent(ChatActivity.this, IM_Select_Report_Activity.class);
                        it.putExtra("data",jsonObject.optString("data"));
                        startActivityForResult(it,0x1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    showToast(getString(R.string.user_error_message));
                }
            }else if(msg.what == Constant.UserErrorCode){
                showToast(getString(R.string.user_error_message));
            }else{
                showToast(getString(R.string.user_error_message));
            }
        }
    };



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                   ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = messageList.get(info.position);
        menu.add(0, 1, Menu.NONE, getString(R.string.chat_del));
        if (message.isSendFail()){
            menu.add(0, 2, Menu.NONE, getString(R.string.chat_resend));
        }else if (message.getMessage().isSelf()){
            menu.add(0, 4, Menu.NONE, getString(R.string.chat_pullback));
        }
        if (message instanceof ImageMessage || message instanceof FileMessage){
            menu.add(0, 3, Menu.NONE, getString(R.string.chat_save));
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = messageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                messageList.remove(info.position);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                messageList.remove(message);
                presenter.sendMessage(message.getMessage(),messageHandler);
                break;
            case 3:
                message.save();
                break;
            case 4:
                presenter.revokeMessage(message.getMessage());
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && fileUri != null) {
                showImagePreview(fileUri.getPath());
            }
        } else if (requestCode == IMAGE_STORE) {
            if (resultCode == RESULT_OK && data != null) {
                showImagePreview(FileUtil.getFilePath(this, data.getData()));
            }

        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW){
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri",false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists()){
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, options);
                    if (file.length() == 0 && options.outWidth == 0) {
                        Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
                    }else {
                        if (file.length() > 1024 * 1024 * 10){
                            Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
                        }else{
                            Message message = new ImageMessage(path,isOri);
                            presenter.sendMessage(message.getMessage(),messageHandler);
                        }
                    }
                }else{
                    Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == VIDEO_RECORD) {
            if (resultCode == RESULT_OK) {
                String videoPath = data.getStringExtra("videoPath");
                String coverPath = data.getStringExtra("coverPath");
                long duration = data.getLongExtra("duration", 0);
                Message message = new UGCMessage(videoPath, coverPath, duration);
                presenter.sendMessage(message.getMessage(),messageHandler);
            }
        }else if(resultCode == 0x4){
            String json = data.getStringExtra("json");
            String a = "{\"userAction\": 14,\"actionParam\":" +
                    " \"EIMAMSG_InputStatus_End\",\"type\": '"+SEND_TO_REPORT+"',\"data\":"+json+"}";
            TIMMessage timMessage = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(a.getBytes());
            elem.setDesc("自定义信息");
            if(timMessage.addElement(elem)!=0){
                return;
            }
            Message message = new CustomMessage(timMessage);
            presenter.sendMessage(message.getMessage(),messageHandler);
        }else if(resultCode == SEND_DRUG){

            String json = data.getStringExtra("json");
            String a = "{\"userAction\": 14,\"actionParam\":" +
                    " \"EIMAMSG_InputStatus_End\",\"type\": '"+SEND_TO_DRUG+"',\"data\":"+json+"}";
            TIMMessage timMessage = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setData(a.getBytes());
            elem.setDesc("自定义信息");
            if(timMessage.addElement(elem)!=0){
                return;
            }
            Message message = new CustomMessage(timMessage);
            presenter.sendMessage(message.getMessage(),messageHandler);
            Toast.makeText(this, R.string.send_success, Toast.LENGTH_SHORT).show();
        }

    }


    private void showImagePreview(String path){
        if (path == null) return;
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

    private void sendFile(String path){
        if (path == null) return;
        File file = new File(path);
        if (file.exists()){
            if (file.length() > 1024 * 1024 * 10){
                Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
            }else{
                Message message = new FileMessage(path);
                presenter.sendMessage(message.getMessage(),messageHandler);
            }
        }else{
            Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 将标题设置为对象名称
     */
    private Runnable resetTitle = new Runnable() {
        @Override
        public void run() {
            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
            title.setTitleText(titleStr);
        }
    };

    @Override
    public void onBackPressed() {
//        if(getIntent().getBooleanExtra("consult",false)){
//            Intent it = new Intent(this, Doctor_service_Activity.class);
//            startActivity(it);
//        }
//        finish();
        if(Counselling_list_Activity.activity!=null){
            Counselling_list_Activity.activity.finish();
        }
        finish();
    }
}
