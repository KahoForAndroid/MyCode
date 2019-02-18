package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Prescription_Item_Details_Adapter extends RecyclerView.Adapter<Prescription_Item_Details_Adapter.ViewHolder>{

    private Context context;

    public Prescription_Item_Details_Adapter(Context context){
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.prescription_item_details_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
