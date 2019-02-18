package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Model.bbsModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/5/30.
 */

public class Main_bbs_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<bbsModel> bbsModelList;
    private RequestOptions requestOptions = new RequestOptions()
            .placeholder(R.drawable.place_photo) //加载中图片
            .error(R.drawable.place_photo) //加载失败图片
            .fallback(R.drawable.place_photo) //url为空图片
            .centerCrop() // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略

    private LinearLayout.LayoutParams layoutParams = null;

    public Main_bbs_Adapter(Context context,List<bbsModel> bbsModelList){
        this.context = context;
        this.bbsModelList = bbsModelList;

        layoutParams = new LinearLayout.LayoutParams(StringUtil.dp2px(context,0),
                ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParams.setMargins(StringUtil.dp2px(context,1),StringUtil.dp2px(context,1),
                StringUtil.dp2px(context,1),StringUtil.dp2px(context,1));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == 1 || viewType == 2){
            view = LayoutInflater.from(context).inflate(R.layout.main_bbs_adapter,parent,false);
            viewHolder = new nullPictureOrOne_Holder(view);
        }else if(viewType == 3){
            view = LayoutInflater.from(context).inflate(R.layout.bbs_photos_recy_item,parent,false);
            viewHolder = new Pohotos_Holder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.bbs_photos_recy_item,parent,false);
            viewHolder = new Pohotos_Holder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 1){
            ((nullPictureOrOne_Holder) holder).item_title_text.setText(bbsModelList.get(position).getTitle());
            ((nullPictureOrOne_Holder) holder).item_content_text.setMaxEms(15);
            ((nullPictureOrOne_Holder) holder).item_content_text.setText(bbsModelList.get(position).getContent());
            ((nullPictureOrOne_Holder) holder).item_author_text.setText(bbsModelList.get(position).getAuthor());
            ((nullPictureOrOne_Holder) holder).item_date_text.setVisibility(View.GONE);
            ((nullPictureOrOne_Holder) holder).item_date_right_text.setVisibility(View.VISIBLE);
            ((nullPictureOrOne_Holder) holder).item_date_right_text.setText(bbsModelList.get(position).getDate());
            ((nullPictureOrOne_Holder) holder).item_photo_img.setVisibility(View.GONE);
        }else if(getItemViewType(position) == 2){
            ((nullPictureOrOne_Holder) holder).item_title_text.setText(bbsModelList.get(position).getTitle());
            ((nullPictureOrOne_Holder) holder).item_content_text.setText(bbsModelList.get(position).getContent());
            ((nullPictureOrOne_Holder) holder).item_author_text.setText(bbsModelList.get(position).getAuthor());
            ((nullPictureOrOne_Holder) holder).item_date_text.setVisibility(View.VISIBLE);
            ((nullPictureOrOne_Holder) holder).item_date_right_text.setVisibility(View.GONE);
            ((nullPictureOrOne_Holder) holder).item_date_text.setText(bbsModelList.get(position).getDate());

            String phones [] = bbsModelList.get(position).getPhoto().split(";");
            Glide.with(context).
                    load(phones[0]).apply(requestOptions).
                    into(((nullPictureOrOne_Holder) holder).item_photo_img);

        }else{
            ((Pohotos_Holder) holder).item_title_text.setText(bbsModelList.get(position).getTitle());
            ((Pohotos_Holder) holder).item_content_text.setText(bbsModelList.get(position).getContent());
            ((Pohotos_Holder) holder).item_author_text.setText(bbsModelList.get(position).getAuthor());
            ((Pohotos_Holder) holder).item_date_right_text.setText(bbsModelList.get(position).getDate());

            String phones [] = bbsModelList.get(position).getPhoto().split(";");


            for(int i = 0;i<phones.length;i++){
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(layoutParams);

                Glide.with(context).
                        load(phones[i]).apply(requestOptions).
                        into(imageView);
                ((Pohotos_Holder) holder).img_lin.addView(imageView);
            }

        }
    }

    @Override
    public int getItemCount() {
        return bbsModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(bbsModelList.get(position).getType() == 1){
            return 1;
        }else if(bbsModelList.get(position).getType() == 2){
            return 2;
        }else if(bbsModelList.get(position).getType() == 3){
            return 3;
        }else if(bbsModelList.get(position).getType() == 4){
            return 4;
        }else{
            return 4;
        }

    }

    class nullPictureOrOne_Holder extends RecyclerView.ViewHolder{

        TextView item_title_text,item_content_text,item_author_text,item_date_text,item_date_right_text;
        ImageView item_photo_img;

        public nullPictureOrOne_Holder(View itemView) {
            super(itemView);
            item_title_text = (TextView)itemView.findViewById(R.id.item_title_text);
            item_content_text = (TextView)itemView.findViewById(R.id.item_content_text);
            item_author_text = (TextView)itemView.findViewById(R.id.item_author_text);
            item_date_text = (TextView)itemView.findViewById(R.id.item_date_text);
            item_date_right_text = (TextView)itemView.findViewById(R.id.item_date_right_text);
            item_photo_img = (ImageView) itemView.findViewById(R.id.item_photo_img);
        }
    }

    class Pohotos_Holder extends RecyclerView.ViewHolder{

        TextView item_title_text,item_content_text,item_author_text,item_date_right_text;
        ImageView item_photo_img;
        LinearLayout img_lin;

        public Pohotos_Holder(View itemView) {
            super(itemView);
            item_title_text = (TextView)itemView.findViewById(R.id.item_title_text);
            item_content_text = (TextView)itemView.findViewById(R.id.item_content_text);
            item_author_text = (TextView)itemView.findViewById(R.id.item_author_text);
            item_date_right_text = (TextView)itemView.findViewById(R.id.item_date_right_text);
            item_photo_img = (ImageView) itemView.findViewById(R.id.item_photo_img);
            img_lin = (LinearLayout)itemView.findViewById(R.id.img_lin);
        }
    }

}
