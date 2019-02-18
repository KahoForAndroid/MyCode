package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Free_Doctor_Adapter extends RecyclerView.Adapter<Free_Doctor_Adapter.ViewHolder>{

    private Context context;
    private List<String> list;

    public Free_Doctor_Adapter(Context context,List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.free_doctor_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
