package zj.health.health_v1.IM.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;
import java.util.logging.Handler;

import zj.health.health_v1.IM.AuxiliaryUtil.CircleImageView;
import zj.health.health_v1.IM.model.CustomMessage;
import zj.health.health_v1.IM.model.Message;
import zj.health.health_v1.Implements.IM_OnItemClick;
import zj.health.health_v1.R;

/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message>{

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private IM_OnItemClick im_onItemClick;
    private List<Message> objects;
    private String rightIconUrl,leftIconUrl;
    private android.os.Handler handler;

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return view != null ? view.getId() : position;
    }

    public void setIm_onItemClick(IM_OnItemClick im_onItemClick){
        this.im_onItemClick = im_onItemClick;
    }

    @Nullable
    @Override
    public Message getItem(int position) {
        return super.getItem(position);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.objects = objects;
        resourceId = resource;
    }
    public ChatAdapter(Context context, int resource, List<Message> objects, String rightIconUrl, String leftIconUril, android.os.Handler handler) {
        super(context, resource, objects);
        this.objects = objects;
        resourceId = resource;
        this.leftIconUrl = leftIconUril;
        this.rightIconUrl = rightIconUrl;
        this.handler = handler;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);
            viewHolder.leftAvatar = (CircleImageView)view.findViewById(R.id.leftAvatar);
            viewHolder.rightAvatar = (CircleImageView)view.findViewById(R.id.rightAvatar);

            viewHolder.leftMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    im_onItemClick.OnIiemLeftClick(getItem(position));
                }
            });
            viewHolder.rightMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    im_onItemClick.OnIiemRightClick(objects.get(position));
                }
            });
            view.setTag(viewHolder);
        }
        if (position < getCount()){
             Message data = getItem(position);
            data.setIcon(rightIconUrl,leftIconUrl);
            if(data instanceof CustomMessage){
                switch (((CustomMessage) data).getType()){
                    case INVALID:
                        ((CustomMessage) data).showMessageCustom(viewHolder, getContext(), (CustomMessage) data,handler);
                        break;
                }
//                if(data.getDesc()!=null){
//                    ((CustomMessage) data).showMessageCustom(viewHolder, getContext(), (CustomMessage) data);
//                }
            }else{
                data.showMessage(viewHolder, getContext());
            }

//            data.showMessage(viewHolder, getContext());

        }
         return view;
    }


    public class ViewHolder{
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
        public CircleImageView rightAvatar;
        public CircleImageView leftAvatar;
    }
}
