package zj.health.health_v1.IM.model;

import android.content.Context;
import android.content.Intent;

import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;


import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.FriendshipManageMessageActivity;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.R;

/**
 * 新朋友会话
 */
public class FriendshipConversation extends Conversation {

    private TIMFriendFutureItem lastMessage;

    private long unreadCount;

    public FriendshipConversation(TIMFriendFutureItem message){
        lastMessage = message;
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        if (lastMessage == null) return 0;
        return lastMessage.getAddTime();
    }

    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum() {
        return unreadCount;
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage() {

    }

    /**
     * 获取头像
     */
    @Override
    public int getAvatar() {
        return R.drawable.ic_news;
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

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        Intent intent = new Intent(context, FriendshipManageMessageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary() {
        if (lastMessage == null) return "";
        String name = lastMessage.getProfile().getNickName();
        if (name.equals("")) name = lastMessage.getIdentifier();
        switch (lastMessage.getType()){
            case TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE://我收到的好友申请的未决消息
                return name + Health_AppLocation.instance.getString(R.string.summary_friend_add);
            case TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE://我发出的好友申请的未决消息
                return Health_AppLocation.instance.getString(R.string.summary_me) + Health_AppLocation.instance.getString(R.string.summary_friend_add_me) + name;
            case TIM_FUTURE_FRIEND_DECIDE_TYPE://已决消息
                return Health_AppLocation.instance.getString(R.string.summary_friend_added) + name;
            case TIM_FUTURE_FRIEND_RECOMMEND_TYPE://好友推荐
                return Health_AppLocation.instance.getString(R.string.summary_friend_recommend) + name;
            default:
                return "";
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        return Health_AppLocation.instance.getString(R.string.conversation_system_friend);

    }

    @Override
    public void setConsultListModel(ConsultListModel consultListModel) {

    }

    @Override
    public void setChatCount(int ChatCount) {

    }

    @Override
    public void setConsultID(String ConsultID) {

    }

    @Override
    public ConsultListModel getConsultListModel() {
        return null;
    }

    @Override
    public void setIsDoctor(boolean isDoctor) {

    }

    @Override
    public boolean getIsDoctor() {
        return false;
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

    }

    @Override
    public String getDoctorId() {
        return null;
    }


    /**
     * 设置最后一条消息
     */
    public void setLastMessage(TIMFriendFutureItem message){
        lastMessage = message;
    }


    /**
     * 设置未读数量
     *
     * @param count 未读数量
     */
    public void setUnreadCount(long count){
        unreadCount = count;
    }


}
