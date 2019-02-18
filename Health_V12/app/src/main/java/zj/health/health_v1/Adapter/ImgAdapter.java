package zj.health.health_v1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Activity.CheckPhotoActivity;
import zj.health.health_v1.Activity.Report_defult_Activity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;

/**
 * Created by Administrator on 2018/12/27.
 */

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder>{

    private Context context;
    private List<String> imgs;
    private RequestOptions requestOptions;

    public ImgAdapter(Context context, List<String> imgs){
        this.context = context;
        this.imgs = imgs;
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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.img_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).
                load(Constant.photo_IP+imgs.get(position)).
                apply(requestOptions).
                into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,CheckPhotoActivity.class);
                it.putExtra("url",imgs.get(position));
                context.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(imgs==null){
            return 0;
        }else{
            return imgs.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.img);
        }
    }
}
