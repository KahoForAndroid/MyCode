package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import zj.health.health_v1.Model.illnessModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/9/6.
 */

public class Drug_List_Adapter extends RecyclerView.Adapter<Drug_List_Adapter.ViewHolder>{

    private List<illnessModel> model;
    private List<illnessModel> model_for_users;
    private Context context;

    public Drug_List_Adapter(Context context,List<illnessModel> model,List<illnessModel> model_for_users){
        this.context = context;
        this.model = model;
        this.model_for_users = model_for_users;
    }
    public void setModel(List<illnessModel> model){
        this.model = model;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.drug_list_item_layout,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.illness_name.setText(model.get(position).getDesc());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.checkBox.setChecked(true);
                    model.get(position).setChecked(true);
                }else{
                    holder.checkBox.setChecked(false);
                    model.get(position).setChecked(false);
                }
            }
        });
        for (int i = 0;i<model_for_users.size();i++){
            boolean checked = false;
            if(Integer.parseInt(model_for_users.get(i).getId())
                    == Integer.parseInt(model.get(position).getId())){
                model.get(position).setChecked(true);
                holder.checkBox.setChecked(true);
                checked = true;
            }else{
                model.get(position).setChecked(false);
                holder.checkBox.setChecked(false);
                checked = false;
            }
            if(checked){
                break;
            }
        }
    }

    public List<illnessModel> getModelList(){
        return model;
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView illness_name;
        AppCompatCheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            illness_name = (TextView)itemView.findViewById(R.id.illness_name);
            checkBox = (AppCompatCheckBox)itemView.findViewById(R.id.checkbox);
        }
    }
}
