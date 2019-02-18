package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;

/**
 * Created by Administrator on 2018/4/11.
 */

public class DoctorSerive_Adapter extends RecyclerView.Adapter<DoctorSerive_Adapter.ViewHolder>{

    private Context context;
    private OnItemClick onItemClick;
    private List<ConsultListModel> consultListModelList;
    private RequestOptions requestOptions ;

    public DoctorSerive_Adapter(Context context,List<ConsultListModel> consultListModelList){
        this.context = context;
        this.consultListModelList = consultListModelList;
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
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    public void setConsultListModelList(List<ConsultListModel> consultListModelList){
        this.consultListModelList = consultListModelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(View.inflate(context, R.layout.consult_list_item,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.age_text.setText(consultListModelList.get(position).getAge());
        holder.nick_name.setText(consultListModelList.get(position).getNickname());
        holder.remark_text.setText(consultListModelList.get(position).getIllnessDesc());
        Glide.with(context).
                load(Constant.photo_IP+consultListModelList.get(position).getIconUrl()).
                apply(requestOptions).
                into(holder.doctorIcon);
        holder.counseling_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClickListener(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultListModelList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
//        RelativeLayout to_chat_rela;
        TextView counseling_text,remark_text,nick_name,age_text,sex_text;
        ImageView doctorIcon,counseling_img;
        public ViewHolder(View itemView) {
            super(itemView);
//            to_chat_rela = (RelativeLayout)itemView.findViewById(R.id.to_chat_rela);
            counseling_img = (ImageView)itemView.findViewById(R.id.counseling_button);
            counseling_text = (TextView)itemView.findViewById(R.id.counseling_text);
            remark_text = (TextView)itemView.findViewById(R.id.remark_text);
            nick_name = (TextView)itemView.findViewById(R.id.nick_name);
            age_text = (TextView)itemView.findViewById(R.id.age_text);
            sex_text = (TextView)itemView.findViewById(R.id.sex_text);
            doctorIcon = (ImageView)itemView.findViewById(R.id.doctorIcon);

        }
    }
}
