package zj.health.health_v1.IM.model;

import android.content.Context;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMConversationExt;

import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.ChatActivity;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.R;

/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation {


    private TIMConversation conversation;


    //最后一条消息
    private Message lastMessage;



    public NomalConversation(TIMConversation conversation) {
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();

    }

    public void setIcon(String friendIcon) {
        icon = friendIcon;
    }


    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public int getAvatar() {
        switch (type) {
            case C2C:
                return R.drawable.head_other;
            case Group:
                return R.drawable.head_group;
        }
        return 0;
    }

    @Override
    public void setFriendIcon(String friendIcon) {
        icon = friendIcon;
    }

    @Override
    public String getMyIcon() {
        return MyIcon;
    }

    @Override
    public void setMyIcon(String myIcon) {
        MyIcon = myIcon;
    }

    @Override
    public String getFriendIcon() {
        return icon;
    }

    @Override
    public void setConsultListModel(ConsultListModel consultListModel) {
        this.consultListModel = consultListModel;
    }

    @Override
    public void setChatCount(int ChatCount) {
        this.ChatCount = ChatCount;
    }

    @Override
    public void setConsultID(String ConsultID) {
        this.ConsultID = ConsultID
        ;
    }

    @Override
    public ConsultListModel getConsultListModel() {
        return this.consultListModel;
    }

    @Override
    public void setIsDoctor(boolean isDoctor) {
        this.isDoctor = isDoctor;
    }

    @Override
    public boolean getIsDoctor() {
        return isDoctor;
    }

    @Override
    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    @Override
    public String getTitleStr() {
        return titleStr;
    }

    @Override
    public void setDoctorId(String id) {
        DoctorId = id;
    }

    @Override
    public String getDoctorId() {
        return DoctorId;
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        ChatActivity.navToChat(context,identify,icon,MyIcon,ChatCount,ConsultID,titleStr,isDoctor,type,consultListModel,DoctorId);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary(){
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            TextMessage textMessage = new TextMessage(ext.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return Health_AppLocation.instance.getString(R.string.conversation_draft) + textMessage.getSummary();
            }else{
                return lastMessage.getSummary();
            }
        }else{
            if (lastMessage == null) return Health_AppLocation.instance.getString(R.string.newMessage);
            return lastMessage.getSummary();
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        if (type == TIMConversationType.Group){
            name=GroupInfo.getInstance().getGroupName(identify);
            if (name.equals("")) name = identify;
        }else{
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            name=profile == null?identify:profile.getName();
        }
        return name;
    }



    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum(){
        if (conversation == null) return 0;
        TIMConversationExt ext = new TIMConversationExt(conversation);
        return ext.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage(){
        if (conversation != null){
            TIMConversationExt ext = new TIMConversationExt(conversation);
            ext.setReadMessage(null, null);
        }
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return ext.getDraft().getTimestamp();
            }else{
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType(){
        return conversation.getType();
    }
}
