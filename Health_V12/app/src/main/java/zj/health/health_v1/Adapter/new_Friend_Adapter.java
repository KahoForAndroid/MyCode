package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.FriendModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/4/18.
 */

public class new_Friend_Adapter extends RecyclerView.Adapter<new_Friend_Adapter.ViewHolder>{

    private Context context;
    private OnItemClick onItemClick;
    private List<FriendModel> friendModelList;
    private RequestOptions requestOptions ;

    public new_Friend_Adapter(Context context,List<FriendModel> friendModelList){
        this.context = context;
        this.friendModelList = friendModelList;
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_head) //加载中图片
                .error(R.drawable.icon_head) //加载失败图片
                .fallback(R.drawable.icon_head) //url为空图片
                .centerCrop()
                .transform(new GlideCircleTransform(context))
                // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    public void setFriendModelList(List<FriendModel> friendModelList){
        this.friendModelList = friendModelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.new_friend_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.user_name_text.setText(friendModelList.get(position).getNickname());
        Glide.with(context).
                load(Constant.photo_IP+ StringUtil.trimNull(friendModelList.get(position).getIconUrl())).
                apply(requestOptions).
                into(holder.head_img);
        holder.accept_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClickListener(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView head_img;
        RelativeLayout accept_rela;
        TextView user_name_text;

        public ViewHolder(View itemView) {
            super(itemView);
            head_img = (ImageView)itemView.findViewById(R.id.head_img);
            user_name_text = (TextView)itemView.findViewById(R.id.user_name_text);
            accept_rela = (RelativeLayout)itemView.findViewById(R.id.accept_rela);
        }
    }
    }
