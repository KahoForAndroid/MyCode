package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Report_Adapter extends RecyclerView.Adapter<Report_Adapter.ViewHolder>{

    private Context context;
    private List<String> typeList;
    private int width;
    private int clickPosition = 0;
    private OnItemClick onItemClick;

    public Report_Adapter(Context context,List<String> typeList,int width){
        this.context = context;
        this.typeList = typeList;
        this.width = width;
    }
    public void setclick(int position){
        this.clickPosition = position;
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    public void setTypeList(List<String> typeList){
        this.typeList = typeList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.report_recycle_type_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(position == clickPosition){
            holder.type_rela.setBackgroundColor(context.getResources().getColor(R.color.main_color));
            holder.type_text.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.type_rela.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.type_text.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.type_text.setText(typeList.get(position));

        holder.type_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClickListener(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout type_rela;
        TextView type_text;

        public ViewHolder(View itemView) {
            super(itemView);
            type_rela = (RelativeLayout)itemView.findViewById(R.id.type_rela);
            type_text = (TextView)itemView.findViewById(R.id.type_text);
        }
    }
}
