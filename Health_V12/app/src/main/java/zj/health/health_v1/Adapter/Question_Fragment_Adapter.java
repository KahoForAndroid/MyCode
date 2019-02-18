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
 * Created by Administrator on 2018/4/20.
 */

public class Question_Fragment_Adapter extends RecyclerView.Adapter<Question_Fragment_Adapter.ViewHolderAdd> {

    private Context context;
    private OnItemClick onItemClick;

    public Question_Fragment_Adapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolderAdd onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderAdd viewHolderAdd = new ViewHolderAdd(LayoutInflater.from(context).
                inflate(R.layout.question_list_add_item,parent,false));
        return viewHolderAdd;
    }

    @Override
    public void onBindViewHolder(ViewHolderAdd holder, int position) {
        holder.add_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
