package zj.health.health_v1.IM.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;


import java.util.Calendar;

import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.ui.GroupManageMessageActivity;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.R;

/**
 * 群管理会话
 */
public class GroupManageConversation extends Conversation {

    private final String TAG = "GroupManageConversation";

    private TIMGroupPendencyItem lastMessage;

    private long unreadCount;


    public GroupManageConversation(TIMGroupPendencyItem message){
        lastMessage = message;
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
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
        //不能传入最后一条消息时间，由于消息时间戳的单位是秒
        GroupManagerPresenter.readGroupManageMessage(Calendar.getInstance().getTimeInMillis(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "read all message error,code " + i);
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "read all message succeed");
            }
        });
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
        readAllMessage();
        Intent intent = new Intent(context, GroupManageMessageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary() {
        if (lastMessage == null) return "";
        String from = lastMessage.getFromUser();
        String to = lastMessage.getToUser();

        boolean isSelf = from.equals(UserInfo.getInstance().getId());
        switch (lastMessage.getPendencyType()){
            case INVITED_BY_OTHER:
                if (isSelf){
                    return Health_AppLocation.instance.getResources().getString(R.string.summary_me)+
                            Health_AppLocation.instance.getResources().getString(R.string.summary_group_invite)+
                            to+
                            Health_AppLocation.instance.getResources().getString(R.string.summary_group_add);
                }else{
                    if (to.equals(UserInfo.getInstance().getId())){
                        return from+
                                Health_AppLocation.instance.getResources().getString(R.string.summary_group_invite)+
                                Health_AppLocation.instance.getResources().getString(R.string.summary_me)+
                                Health_AppLocation.instance.getResources().getString(R.string.summary_group_add);
                    }else{
                        return from+
                                Health_AppLocation.instance.getResources().getString(R.string.summary_group_invite)+
                                to+
                                Health_AppLocation.instance.getResources().getString(R.string.summary_group_add);
                    }

                }
            case APPLY_BY_SELF:
                if (isSelf){
                    return Health_AppLocation.instance.getResources().getString(R.string.summary_me)+
                            Health_AppLocation.instance.getResources().getString(R.string.summary_group_apply)+
                            GroupInfo.getInstance().getGroupName(lastMessage.getGroupId());
                }else{
                    return from+Health_AppLocation.instance.getResources().getString(R.string.summary_group_apply)+ GroupInfo.getInstance().getGroupName(lastMessage.getGroupId());
                }

            default:
                return "";
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        return Health_AppLocation.instance.getString(R.string.conversation_system_group);
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
    public void setLastMessage(TIMGroupPendencyItem message){
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
