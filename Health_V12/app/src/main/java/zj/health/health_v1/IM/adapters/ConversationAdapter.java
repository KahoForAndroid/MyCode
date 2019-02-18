package zj.health.health_v1.IM.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.IM.AuxiliaryUtil.CircleImageView;
import zj.health.health_v1.IM.model.Conversation;
import zj.health.health_v1.IM.utils.TimeUtil;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;

/**
 * 会话界面adapter
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private Context context;
    private RequestOptions requestOptions ;


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        resourceId = resource;
        this.context = context;
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.place_photo) //加载中图片
                .error(R.drawable.place_photo) //加载失败图片
                .fallback(R.drawable.place_photo) //url为空图片
                .centerCrop()
                .transform(new GlideCircleTransform(context))
                // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.lastMessage = (TextView) view.findViewById(R.id.last_message);
            viewHolder.time = (TextView) view.findViewById(R.id.message_time);
            viewHolder.unread = (TextView) view.findViewById(R.id.unread_num);
            viewHolder.counseling_rela = (RelativeLayout)view.findViewById(R.id.counseling_rela);
            view.setTag(viewHolder);
        }
        final Conversation data = getItem(position);

        viewHolder.counseling_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        String friendicon = data.getFriendIcon();
        Glide.with(context).
                load(Constant.photo_IP+data.getFriendIcon()).
                apply(requestOptions).
                into(viewHolder.avatar);
        viewHolder.tvName.setText(data.getName());
        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0){
            viewHolder.unread.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10){
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point1));
            }else{
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point2));
                if (unRead > 99){
                    unReadStr = getContext().getResources().getString(R.string.time_more);
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder{
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;
        public RelativeLayout counseling_rela;

    }
}
