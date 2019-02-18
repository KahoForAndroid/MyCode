package zj.health.health_v1.IM.model;

import android.content.Context;

import com.tencent.imsdk.TIMConversationType;

import zj.health.health_v1.Model.ConsultListModel;

/**
 * 会话数据
 */
public abstract class Conversation implements Comparable {

    //会话对象id
    protected String identify;

    //会话类型
    protected TIMConversationType type;

    //会话对象名称
    protected String name;
    //对方头像
    protected String icon;
    //自己的头像
    protected String MyIcon;
    //患者数据实体类
    protected ConsultListModel consultListModel;
    //聊天次数
    protected int ChatCount;
    //咨询ID
    protected String ConsultID;
    //是否医生
    protected boolean isDoctor;
    //对方名字
    protected String titleStr;
    //医生ID
    protected String DoctorId;

    /**
     * 获取最后一条消息的时间
     */
    abstract public long getLastMessageTime();

    /**
     * 获取未读消息数量
     */
    abstract public long getUnreadNum();


    /**
     * 将所有消息标记为已读
     */
    abstract public void readAllMessage();


    /**
     * 获取头像
     */
    abstract public int getAvatar();

    /**
     * 设置头像
     */
    abstract public void setFriendIcon(String friendIcon);

    /**
     * 获取头像URL
     */
    abstract public String getMyIcon();


    /**
     * 设置头像
     */
    abstract public void setMyIcon(String myIcon);

    /**
     * 获取头像URL
     */
    abstract public String getFriendIcon();

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    abstract public void navToDetail(Context context);

    /**
     * 获取最后一条消息摘要
     */
    abstract public String getLastMessageSummary();

    /**
     * 获取名称
     */
    abstract public String getName();

    /**
     * 传患者数据
     */
    abstract public void setConsultListModel(ConsultListModel consultListModel);

    /**
     * 传递聊天次数
     */
    abstract public void setChatCount(int ChatCount);

    /**
     * 传患者数据
     */
    abstract public void setConsultID(String ConsultID);

    /**
     * 获取头像URL
     */
    abstract public ConsultListModel getConsultListModel();

    /**
     * 传入是否医生
     */
    abstract public void setIsDoctor(boolean isDoctor);

    /**
     * 获取是否为医生参数值
     */
    abstract public boolean getIsDoctor();
    /**
     * 设置对方名字
     */
    abstract public void setTitleStr(String titleStr);

    /**
     * 获取对方名字
     */
    abstract public String getTitleStr();
    /**
     * 传入医生id
     * */
    abstract public void setDoctorId(String id);

    /**
     * 获取医生id
     */
    abstract public String getDoctorId();


    public void setIdentify(String id){
        identify = id;
    }

    public String getIdentify(){
        return identify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        if (!identify.equals(that.identify)) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = identify.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }


    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Object another) {
        if (another instanceof Conversation){
            Conversation anotherConversation = (Conversation) another;
            long timeGap = anotherConversation.getLastMessageTime() - getLastMessageTime();
            if (timeGap > 0) return  1;
            else if (timeGap < 0) return -1;
            return 0;
        }else{
            throw new ClassCastException();
        }
    }



}
