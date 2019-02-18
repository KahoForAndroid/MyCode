package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Prescription_Manage_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClick onItemClick;

    public Prescription_Manage_List_Adapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderAdd viewHolderAdd = new ViewHolderAdd(LayoutInflater.from(context).
                inflate(R.layout.rescription_anage_list_add_item,parent,false));
        return ((RecyclerView.ViewHolder)viewHolderAdd);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolderAdd extends RecyclerView.ViewHolder{

        RelativeLayout add_rela;
        public ViewHolderAdd(View itemView) {
            super(itemView);
            add_rela = (RelativeLayout)itemView.findViewById(R.id.add_rela);
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
