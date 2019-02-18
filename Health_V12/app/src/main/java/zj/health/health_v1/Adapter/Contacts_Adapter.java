package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/29.
 */

public class Contacts_Adapter extends RecyclerView.Adapter<Contacts_Adapter.ViewHolder>{

    private Context context;
    private List<Map<String, String>> list;

    public Contacts_Adapter(Context context,List<Map<String, String>> list){
        this.context = context;
        this.list = list;
    }

    public void setList(List<Map<String, String>> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.contacts_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(list.get(position).get("name"));
        holder.phone_number.setText(list.get(position).get("phone"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone_number;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            phone_number = (TextView)itemView.findViewById(R.id.phone_number);
        }
    }
}
