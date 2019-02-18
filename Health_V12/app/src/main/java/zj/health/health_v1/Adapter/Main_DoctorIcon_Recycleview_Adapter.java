package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.DoctorOnLineModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/4/8.
 */

public class Main_DoctorIcon_Recycleview_Adapter extends RecyclerView.Adapter<Main_DoctorIcon_Recycleview_Adapter.ViewHolder>{

    private List<DoctorOnLineModel> doctorOnLineModelList;
    private Context context;
    private RequestOptions requestOptions ;

    public Main_DoctorIcon_Recycleview_Adapter(Context context,List<DoctorOnLineModel> doctorOnLineModelList){
        this.context = context;
        this.doctorOnLineModelList = doctorOnLineModelList;
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
    public void setDoctorOnLineModelList(List<DoctorOnLineModel> doctorOnLineModelList){
        this.doctorOnLineModelList = doctorOnLineModelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.main_doctoricon_recy_item,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Glide.with(Health_AppLocation.instance).
                load(Constant.photo_IP+ StringUtil.trimNull(doctorOnLineModelList.get(position%doctorOnLineModelList.size()).getIconUrl())).apply(requestOptions).
                into(holder.doctor_head);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView doctor_head;

        public ViewHolder(View itemView) {
            super(itemView);
            doctor_head = (ImageView)itemView.findViewById(R.id.doctor_head);
        }
    }
}
