package zj.health.health_v1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import zj.health.health_v1.Activity.PrescriptionUsers_Item_Details_Activity;
import zj.health.health_v1.Activity.Prescription_Item_Details_Activity;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.PrescModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Prescription_Fragment_Adapter extends RecyclerView.Adapter<Prescription_Fragment_Adapter.ViewHolder>{


    private BaseActivity context;
    private List<PrescModel> prescModelList;
    private OnItemClick onItemClick;

    public Prescription_Fragment_Adapter(BaseActivity context, List<PrescModel> prescModelList){
        this.context = context;
        this.prescModelList = prescModelList;
    }
    public void setPrescModelList(List<PrescModel> prescModelList){
        this.prescModelList = prescModelList;
        notifyDataSetChanged();
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    @Override
    public Prescription_Fragment_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Prescription_Fragment_Adapter.ViewHolder viewHolder = new Prescription_Fragment_Adapter.ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.prescription_users_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Prescription_Fragment_Adapter.ViewHolder holder, final int position) {
        if(StringUtil.isEmpty(prescModelList.get(position).getId())){
            holder.drug_rela.setVisibility(View.GONE);
            holder.add_rela.setVisibility(View.GONE);
        }else{
            holder.add_rela.setVisibility(View.GONE);
            holder.disease_name.setText(prescModelList.get(position).getApplyIllness());
            try {
                holder.time_text.setText(DateUtil.UTCToCST(prescModelList.get(position).getLastModifiedDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            View view1 = LayoutInflater.from(context).inflate(R.layout.prescription_tablerow_item,null);
            TextView prescription_name = (TextView)view1.findViewById(R.id.prescription_name_text);
            TextView Course_of_treatment = (TextView)view1.findViewById(R.id.Course_of_treatment_text);
            prescription_name.setText(R.string.prescription_name);
            prescription_name.setTextColor(context.getResources().getColor(R.color.black));
            Course_of_treatment.setText(R.string.Course_of_treatment);
            Course_of_treatment.setTextColor(context.getResources().getColor(R.color.black));
            holder.tablelayout.addView(view1);

            for(int i = 0;i<prescModelList.get(position).getMedicines().size();i++){
                View view = LayoutInflater.from(context).inflate(R.layout.prescription_tablerow_item,null);
                TextView prescription_name_text = (TextView)view.findViewById(R.id.prescription_name_text);
                TextView Course_of_treatment_text = (TextView)view.findViewById(R.id.Course_of_treatment_text);
                prescription_name_text.setText(prescModelList.get(position).getMedicines().get(i).getMedicineName());
                Course_of_treatment_text.setText(prescModelList.get(position).getMedicines().get(i).getTreatment());
                holder.tablelayout.addView(view);
            }

            holder.Advice_text.setText(StringUtil.trimNull(prescModelList.get(position).getRemark()));
        }

        holder.doctor_name_text.setText(prescModelList.get(position).getDoctorName()+context.getString(R.string.doctor));

        holder.add_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, Prescription_Item_Details_Activity.class);
                it.putExtra("id",prescModelList.get(position).getId());
                context.startActivityForResult(it,0x1);
            }
        });
        holder.check_more_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, PrescriptionUsers_Item_Details_Activity.class);
                it.putExtra("suggestionId",prescModelList.get(position).getId());
                context.startActivityForResult(it,0x1);
            }
        });
       holder.add_to_remind.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onItemClick.OnItemClickListener(v,position);
           }
       });

    }

    @Override
    public int getItemCount() {
        return prescModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TableLayout tablelayout;
        TextView Advice_text,check_more_text,disease_name,time_text,doctor_name_text;
        RelativeLayout add_rela;
        LinearLayout drug_rela;
        ImageView delete_img;
        Button add_to_remind;

        public ViewHolder(View itemView) {
            super(itemView);
            tablelayout = (TableLayout)itemView.findViewById(R.id.tablelayout);
            Advice_text = (TextView)itemView.findViewById(R.id.Advice_text);
            check_more_text = (TextView)itemView.findViewById(R.id.check_more_text);
            add_rela = (RelativeLayout)itemView.findViewById(R.id.add_rela);
            drug_rela = (LinearLayout)itemView.findViewById(R.id.drug_rela);
            disease_name = (TextView)itemView.findViewById(R.id.disease_name);
            time_text = (TextView)itemView.findViewById(R.id.time_text);
            delete_img = (ImageView)itemView.findViewById(R.id.delete_img);
            doctor_name_text = (TextView)itemView.findViewById(R.id.doctor_name_text);
            add_to_remind = (Button)itemView.findViewById(R.id.add_to_remind);
        }
    }

}
