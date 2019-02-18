package zj.health.health_v1.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.UserDevices;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class BlueToothData_Adapter extends RecyclerView.Adapter<BlueToothData_Adapter.ViewHolder>{

    private List<UserDevices> list;
    private Context context;
    private OnItemClick onItemClick;
    private int ColorPosition = 0;

    public BlueToothData_Adapter(Context context, List<UserDevices> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<UserDevices> list){
        this.list = list;
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    public void setOnClickPositionBgColor(int position){
        ColorPosition = position;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.bluetooth_data_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(Integer.valueOf(list.get(position).getId()) == 1){
            holder.body_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.blood_pressure));
        }else  if(Integer.valueOf(list.get(position).getId()) == 2){
            holder.body_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.weight));
        }else  if(Integer.valueOf(list.get(position).getId()) == 3){
            holder.body_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.temperature));
        }else  if(Integer.valueOf(list.get(position).getId()) == 4){
            holder.body_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_rate));
        }else  if(Integer.valueOf(list.get(position).getId()) == 5){
            holder.body_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.blood_sugar));
        }

        if(ColorPosition == position){
            holder.body_type_recy_item.setBackgroundColor(context.getResources().getColor(R.color.share_line_color));
        }else{
            holder.body_type_recy_item.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.body_type_text.setText(list.get(position).getTitle());
        holder.body_type_recy_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClickListener(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView body_type_img;
        TextView body_type_text;
        LinearLayout body_type_recy_item;

        public ViewHolder(View itemView) {
            super(itemView);
            body_type_img = (ImageView)itemView.findViewById(R.id.body_type_img);
            body_type_text = (TextView) itemView.findViewById(R.id.body_type_text);
            body_type_recy_item =(LinearLayout)itemView.findViewById(R.id.body_type_recy_item);
        }
    }
}
