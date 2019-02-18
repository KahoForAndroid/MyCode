package zj.health.health_v1.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.logging.Handler;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.Model.ReportModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.HttpUtil;
import zj.health.health_v1.Utils.ImageUtils;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * Created by Administrator on 2018/8/1.
 */

public class NewBodyReportAdapter  extends RecyclerView.Adapter<NewBodyReportAdapter.ViewHolder>{

    private Context context;
    private List<NewReportModel> list;
    private RequestOptions requestOptions ;
    private OnItemClick onItemClick;
    private ViewHolder viewHolder;
    private boolean haveFriendId;
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                synchronized (this){
                    list.get(msg.arg1).setLogo(msg.obj.toString());
//                    notifyDataSetChanged();
                }
            }else{

            }
        }
    };

    public NewBodyReportAdapter(Context context,List<NewReportModel> list,boolean haveFriendId){
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(haveFriendId){
            holder.delete_img.setVisibility(View.GONE);
        }else{
            holder.delete_img.setVisibility(View.VISIBLE);
        }

            if(list.get(position).getLogo()!=null){
                byte[] bitmapArray;
                bitmapArray = Base64.decode(list.get(position).getLogo(), Base64.DEFAULT);
                Glide.with(context).
                        load(bitmapArray).apply(requestOptions).
                        into(holder.photo_img);
            }else{
                if(list.get(position).getImages()!=null && list.get(position).getImages().size()>0){

                    Glide.with(context).
                            load(Constant.postIP+ list.get(position).getImages().get(0).substring(1,list.get(position).getImages().get(0).length())).
                            apply(requestOptions).
                            into(holder.photo_img);

                }else{
                    holder.photo_img.setImageResource(R.drawable.place_photo);
                }
            }

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

