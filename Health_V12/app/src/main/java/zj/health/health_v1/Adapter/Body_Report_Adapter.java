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
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.Model.ReportModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/5/3.
 */

public class Body_Report_Adapter extends RecyclerView.Adapter<Body_Report_Adapter.ViewHolder>{

    private Context context;
    private List<NewReportModel> list;
    private RequestOptions requestOptions ;
    private OnItemClick onItemClick;
    private boolean haveFriendId;

    public Body_Report_Adapter(Context context,List<NewReportModel> list,boolean haveFriendId){
        this.context = context;
        this.list = list;
        this.haveFriendId = haveFriendId;
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.place_photo) //加载中图片
                .error(R.drawable.place_photo) //加载失败图片
                .fallback(R.drawable.place_photo) //url为空图片
                .centerCrop()
//                .transform(new GlideCircleTransform(context))
                // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    public void setList(List<NewReportModel> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.body_reprot_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(haveFriendId){
            holder.delete_img.setVisibility(View.GONE);
        }else{
            holder.delete_img.setVisibility(View.VISIBLE);
        }
        Glide.with(context).
                load(Constant.postIP+list.get(position).getImages().get(0).substring(1,list.get(position).getImages().get(0).length())).apply(requestOptions).
                into(holder.photo_img);
        holder.name_text.setText(StringUtil.trimNull(list.get(position).getTitle()));
        holder.mark_text.setText(StringUtil.trimNull(list.get(position).getMark()));
        holder.date_text.setText(StringUtil.trimNull(list.get(position).getCreateDate().substring(0,10)));
        holder.item_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClickListener(view,position);
            }
        });
        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photo_img,delete_img;
        TextView name_text,mark_text,date_text;
        RelativeLayout item_rela;

        public ViewHolder(View itemView) {
            super(itemView);
            photo_img = (ImageView)itemView.findViewById(R.id.photo_img);
            name_text = (TextView) itemView.findViewById(R.id.name_text);
            mark_text = (TextView)itemView.findViewById(R.id.mark_text);
            date_text = (TextView)itemView.findViewById(R.id.date_text);
            item_rela = (RelativeLayout)itemView.findViewById(R.id.item_rela);
            delete_img = (ImageView)itemView.findViewById(R.id.delete_img);
        }
    }
}
