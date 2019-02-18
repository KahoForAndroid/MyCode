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
import zj.health.health_v1.Model.HospitalModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/7/23.
 */

public class Hospital_Adapter extends RecyclerView.Adapter<Hospital_Adapter.ViewHolder>{

    private List<HospitalModel> list;
    private OnItemClick onItemClick;
    private Context context;

    public Hospital_Adapter(Context context,List<HospitalModel> list){
        this.context = context;
        this.list = list;
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    public void setList(List<HospitalModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.hospital_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.item_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
            }
        });
        holder.hospital_name.setText(list.get(position).getHospitalName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout item_rela;
        TextView hospital_name;

        public ViewHolder(View itemView) {
            super(itemView);
            item_rela = (RelativeLayout)itemView.findViewById(R.id.item_rela);
            hospital_name = (TextView)itemView.findViewById(R.id.hospital_name);

        }
    }
}
