package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/9/21.
 */

public class Share_Adapter extends RecyclerView.Adapter<Share_Adapter.ViewHolder>{

    private Context context;
    private List<String> stringList;
    private OnItemClick onItemClick;

    public Share_Adapter(Context context,List<String> stringList){
        this.context = context;
        this.stringList = stringList;
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.share_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        switch (position){
            case 0:
                holder.share_icon.setImageResource(R.drawable.share_wechat);
                break;
            case 1:
                holder.share_icon.setImageResource(R.drawable.share_wechat_friend);
                break;
                default:
                    break;
        }
        holder.share_text.setText(stringList.get(position));
        holder.share_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView share_icon;
        TextView share_text;
        RelativeLayout share_rela;

        public ViewHolder(View itemView) {
            super(itemView);
            share_icon = (ImageView)itemView.findViewById(R.id.share_icon);
            share_text = (TextView)itemView.findViewById(R.id.share_text);
            share_rela = (RelativeLayout)itemView.findViewById(R.id.share_rela);
        }
    }
}
