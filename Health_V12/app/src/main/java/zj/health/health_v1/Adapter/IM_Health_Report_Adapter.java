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

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Implements.Picture_OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/9/12.
 */

public class IM_Health_Report_Adapter extends RecyclerView.Adapter<IM_Health_Report_Adapter.ViewHolder>{

    private Context context;
    private List<LocalMedia> list;
    private Picture_OnItemClick onItemClick;

    public IM_Health_Report_Adapter(Context context,List<LocalMedia> list){
        this.context = context;
        this.list = list;
    }


    public void setList(List<LocalMedia> list){
        this.list = list;
    }
    public void setOnItemClick(Picture_OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.health_report_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String url = null;
        if(list.get(position).getCompressPath().contains("/image/")){
                url = Constant.postIP+list.get(position).getCompressPath().
                        substring(1,list.get(position).getCompressPath().length());
        }else{
            url = list.get(position).getCompressPath();
        }

            Glide.with(context).load(url).into(holder.item_img);
            holder.delete_img.setVisibility(View.GONE);
//            holder.item_img.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
      return list.size();
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

