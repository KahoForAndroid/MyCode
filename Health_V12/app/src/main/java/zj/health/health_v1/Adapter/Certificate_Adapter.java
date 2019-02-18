package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import zj.health.health_v1.Implements.Picture_OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/7/24.
 */

public class Certificate_Adapter extends RecyclerView.Adapter<Certificate_Adapter.ViewHolder>{

    private Context context;
    private List<LocalMedia> list;
    private Picture_OnItemClick onItemClick;

    public Certificate_Adapter(Context context,List<LocalMedia> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<LocalMedia> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public void setOnItemClick(Picture_OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() == 0 && position == 0){
            return 0;
        }else if(position == list.size()){
            return 0;
        }else{
            return 1;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.health_report_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        int a = getItemCount();
        if(getItemViewType(position) == 1){
            Glide.with(context).load(list.get(position).getCompressPath()).into(holder.item_img);
            holder.delete_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.OnItemClickListener(view,position,true);
                }
            });
        }else{
            holder.delete_img.setVisibility(View.GONE);
            holder.item_img.setImageDrawable(context.getResources().getDrawable(R.drawable.add_picture));
            holder.item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.OnItemClickListener(view,position,false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(list.size() == 0){
            return 1;
        }else if(list.size() == 9){
            return list.size();
        }else{
            return list.size()+1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView item_img,delete_img;

        public ViewHolder(View itemView) {
            super(itemView);
            item_img = (ImageView)itemView.findViewById(R.id.item_img);
            delete_img = (ImageView)itemView.findViewById(R.id.delete_img);
        }
    }
}
