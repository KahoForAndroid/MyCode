package zj.health.health_v1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Activity.IM_Test_Activity;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/4/12.
 */

public class My_Patient_Adapter extends SecondaryListAdapter<My_Patient_Adapter.GroupItemViewHolder, My_Patient_Adapter.SubItemViewHolder> {


    private Context context;

    private List<DataTree<String,String>> dts = new ArrayList<>();


    public My_Patient_Adapter(Context context) {
        this.context = context;
    }

    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }
    public List getList(){
        return dts;
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_patient_recycle_group_item, parent, false);

        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_patient_recycle_sub_item, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(final RecyclerView.ViewHolder holder, int groupItemIndex) {
        ((GroupItemViewHolder)holder).user_type_text.setText(dts.get(groupItemIndex).getGroupItem());
    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {

    }



    @Override
    public void onGroupItemClick(Boolean isExpand, final GroupItemViewHolder holder, final int groupItemIndex) {
        if(isExpand){
            holder.stauts_img.setImageDrawable(context.getDrawable(R.mipmap.close_sub_list));
        }else{
            holder.stauts_img.setImageDrawable(context.getDrawable(R.mipmap.open_sub_list));
        }

    }

    @Override
    public void onSubItemClick(final SubItemViewHolder holder, final int groupItemIndex, final int subItemIndex) {
        Intent it = new Intent(context, IM_Test_Activity.class);
        context.startActivity(it);
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        ImageView stauts_img;
        TextView user_type_text;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            stauts_img = (ImageView)itemView.findViewById(R.id.stauts_img);
            user_type_text = (TextView)itemView.findViewById(R.id.user_type_text);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {



        public SubItemViewHolder(View itemView) {
            super(itemView);

        }
    }


}


