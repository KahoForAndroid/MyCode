package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import zj.health.health_v1.Model.BalanceListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;

/**
 * Created by Administrator on 2018/4/10.
 */

public class My_Wallet_Adapter extends RecyclerView.Adapter<My_Wallet_Adapter.ViewHolder>{

    private Context context;
    private List<BalanceListModel> balanceListModelList;

    public My_Wallet_Adapter(Context context,List<BalanceListModel> balanceListModelList){
        this.context = context;
        this.balanceListModelList = balanceListModelList;
    }
    public void setBalanceListModelList(List<BalanceListModel> balanceListModelList){
        this.balanceListModelList = balanceListModelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.wallet_recycle_item,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.desc_text.setText(balanceListModelList.get(position).getDesc());
        holder.cost_text.setText(balanceListModelList.get(position).getCost()+context.getString(R.string.yuan));
        try {
            holder.time_text.setText(DateUtil.UTCToCST(balanceListModelList.get(position).getCreatedDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return balanceListModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView desc_text,cost_text,time_text;

        public ViewHolder(View itemView) {
            super(itemView);
            desc_text = (TextView)itemView.findViewById(R.id.desc_text);
            cost_text = (TextView)itemView.findViewById(R.id.cost_text);
            time_text = (TextView)itemView.findViewById(R.id.time_text);
        }
    }
}
