package zj.health.health_v1.IM.model;

import android.content.Context;
import android.view.View;

import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMMessage;


import java.util.Iterator;
import java.util.Map;

import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.IM.adapters.ChatAdapter;
import zj.health.health_v1.R;

/**
 * 群tips消息
 */
public class GroupTipMessage extends Message {


    public GroupTipMessage(TIMMessage message){
        this.message = message;
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.leftPanel.setVisibility(View.GONE);
        viewHolder.rightPanel.setVisibility(View.GONE);
        viewHolder.systemMessage.setVisibility(View.VISIBLE);
        viewHolder.systemMessage.setText(getSummary());
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        final TIMGroupTipsElem e = (TIMGroupTipsElem) message.getElement(0);
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String,TIMGroupMemberInfo>> iterator = e.getChangedGroupMemberInfo().entrySet().iterator();
        switch (e.getTipsType()){
            case CancelAdmin:
            case SetAdmin:
                return Health_AppLocation.instance.getString(R.string.summary_group_admin_change);
            case Join:
                while(iterator.hasNext()){
                    Map.Entry<String,TIMGroupMemberInfo> item = iterator.next();
                    stringBuilder.append(getName(item.getValue()));
                    stringBuilder.append(" ");
                }
                return stringBuilder +
                        Health_AppLocation.instance.getString(R.string.summary_group_mem_add);
            case Kick:
                return e.getUserList().get(0) +
                        Health_AppLocation.instance.getString(R.string.summary_group_mem_kick);
            case ModifyMemberInfo:
                while(iterator.hasNext()){
                    Map.Entry<String,TIMGroupMemberInfo> item = iterator.next();
                    stringBuilder.append(getName(item.getValue()));
                    stringBuilder.append(" ");
                }
                return stringBuilder +
                        Health_AppLocation.instance.getString(R.string.summary_group_mem_modify);
            case Quit:
                return e.getOpUser() +
                        Health_AppLocation.instance.getString(R.string.summary_group_mem_quit);
            case ModifyGroupInfo:
                return Health_AppLocation.instance.getString(R.string.summary_group_info_change);
        }
        return "";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private String getName(TIMGroupMemberInfo info){
        if (info.getNameCard().equals("")){
            return info.getUser();
        }
        return info.getNameCard();
    }
}
