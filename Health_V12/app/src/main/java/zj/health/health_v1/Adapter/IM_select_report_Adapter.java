package zj.health.health_v1.Adapter;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.NewReportModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/9/13.
 */

public class IM_select_report_Adapter extends RecyclerView.Adapter<IM_select_report_Adapter.ViewHolder>{

    private Context context;
    private List<NewReportModel> list;
    private RequestOptions requestOptions ;
    private OnItemClick onItemClick;
    private NewBodyReportAdapter.ViewHolder viewHolder;
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

    public IM_select_report_Adapter(Context context,List<NewReportModel> list){
        this.context = context;
        this.list = list;
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

//    public void setOnItemClick(OnItemClick onItemClick){
//        this.onItemClick = onItemClick;
//    }

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

        holder.checkBox.setVisibility(View.VISIBLE);
        holder.name_text.setText(StringUtil.trimNull(list.get(position).getTitle()));
        holder.mark_text.setText(StringUtil.trimNull(list.get(position).getMark()));
        holder.date_text.setText(StringUtil.trimNull(list.get(position).getCreateDate().substring(0,10)));
        holder.item_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onItemClick.OnItemClickListener(view,position);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.checkBox.setChecked(isChecked);
                list.get(position).setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photo_img;
        TextView name_text,mark_text,date_text;
        RelativeLayout item_rela;
        AppCompatCheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            photo_img = (ImageView)itemView.findViewById(R.id.photo_img);
            name_text = (TextView) itemView.findViewById(R.id.name_text);
            mark_text = (TextView)itemView.findViewById(R.id.mark_text);
            date_text = (TextView)itemView.findViewById(R.id.date_text);
            item_rela = (RelativeLayout)itemView.findViewById(R.id.item_rela);
            checkBox = (AppCompatCheckBox)itemView.findViewById(R.id.checkbox);
        }
    }
}


