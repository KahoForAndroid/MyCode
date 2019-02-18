package zj.health.health_v1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.CounselingListModel;
import zj.health.health_v1.MyView.RatingBarView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/8/24.
 */

public class Counseling_List_Adapter extends RecyclerView.Adapter<Counseling_List_Adapter.ViewHolder>{

    private Context context;
    private List<CounselingListModel> counselingListModelList;
    private RequestOptions requestOptions ;
    private OnItemClick onItemClick;

    public Counseling_List_Adapter(Context context, List<CounselingListModel> counselingListModelList){
        this.context = context;
        this.counselingListModelList = counselingListModelList;
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

    public void setCounselingListModelList(List<CounselingListModel> counselingListModelList){
        this.counselingListModelList = counselingListModelList;
        notifyDataSetChanged();
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.counseling_list_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.nick_name.setText(StringUtil.trimNull(counselingListModelList.get(position).getNickname()));
        holder.doctor_level.setText("("+StringUtil.trimNull(counselingListModelList.get(position).getLevel())+")");
        holder.location_text.setText(StringUtil.trimNull(counselingListModelList.get(position).getHospital()));
//        holder.ratingBar.setStepSize(Integer.parseInt(counselingListModelList.get(position).getEvaluate()));
        holder.ratingBar.setRating(Integer.parseInt(counselingListModelList.get(position).getEvaluate())/2);
        Glide.with(context).
                load(Constant.photo_IP+ counselingListModelList.get(position).getIconUrl()).
                apply(requestOptions).
                into(holder.doctorIcon);
        holder.counseling_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
            }
        });
        holder.item_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return counselingListModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView counseling_text,nick_name,doctor_level,location_text;
        RelativeLayout counseling_rela,item_rela;
        RatingBar ratingBar;
        ImageView doctorIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            counseling_text = (TextView)itemView.findViewById(R.id.counseling_text);
            nick_name = (TextView)itemView.findViewById(R.id.nick_name);
            doctor_level = (TextView)itemView.findViewById(R.id.doctor_level);
            location_text = (TextView)itemView.findViewById(R.id.location_text);
            counseling_rela = (RelativeLayout)itemView.findViewById(R.id.counseling_rela);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            doctorIcon = (ImageView)itemView.findViewById(R.id.doctorIcon);
            item_rela =(RelativeLayout)itemView.findViewById(R.id.item_rela);
        }
    }
}
