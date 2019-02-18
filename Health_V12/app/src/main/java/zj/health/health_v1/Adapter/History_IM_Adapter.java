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
import com.tencent.imsdk.TIMUserProfile;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;

/**
 * Created by Administrator on 2018/9/25.
 */

public class History_IM_Adapter extends RecyclerView.Adapter<History_IM_Adapter.ViewHolder>{

    private Context context;
    private List<TIMUserProfile> result;
    private RequestOptions requestOptions;
    private OnItemClick onItemClick;

    public History_IM_Adapter(Context context,List<TIMUserProfile> result){
        this.context = context;
        this.result = result;
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

    public void setResult(List<TIMUserProfile> result){
        this.result = result;
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.history_im_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(result.get(position).getFaceUrl().contains(Constant.photo_IP)){
            Glide.with(context).
                    load(result.get(position).getFaceUrl()).
                    apply(requestOptions).
                    into(holder.im_icon);
        }else{
            Glide.with(context).
                    load(Constant.photo_IP+result.get(position).getFaceUrl()).
                    apply(requestOptions).
                    into(holder.im_icon);
        }

        holder.nick_name.setText(result.get(position).getNickName());
        holder.rela_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return result.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView im_icon;
        TextView nick_name;
        RelativeLayout rela_item;

        public ViewHolder(View itemView) {
            super(itemView);
            im_icon = (ImageView)itemView.findViewById(R.id.im_icon);
            nick_name = (TextView)itemView.findViewById(R.id.nick_name);
            rela_item = (RelativeLayout)itemView.findViewById(R.id.rela_item);
        }
    }
}
