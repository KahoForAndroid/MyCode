package zj.health.health_v1.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ConsultModel;
import zj.health.health_v1.MyView.KahoLabelLayout;
import zj.health.health_v1.MyView.MyTextView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/5/17.
 */

public class Health_history_Adapter extends RecyclerView.Adapter<Health_history_Adapter.ViewHolder>{

    private Context context;
    private ViewGroup.MarginLayoutParams marginLayoutParams;
    private List<ConsultModel> consultModelList;
//    private String dorctor_leavel [];
//    private String Selection_section [];
//    private String Communication_mode [];
//    private String address [];
    private OnItemClick onItemClick;


    public Health_history_Adapter(Context context,List<ConsultModel> consultModelList){
        this.context = context;
        this.consultModelList = consultModelList;
        marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(10, 10, 10, 10);
//        dorctor_leavel = context.getResources().getStringArray(R.array.dorctor_leavel);
//        Selection_section = context.getResources().getStringArray(R.array.Selection_section);
//        Communication_mode = context.getResources().getStringArray(R.array.Communication_mode);
//        address = context.getResources().getStringArray(R.array.address);
    }

    public void setConsultModelList(List<ConsultModel> consultModelList){
        this.consultModelList = consultModelList;
        notifyDataSetChanged();
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.health_history_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.Label_layout.setType(1);
//        if(holder.Label_layout.getChildCount()<=0){
//            for(int i = 0;i<list.length;i++){
//                AddTextView(list[i],holder.Label_layout,i);
//            }
//        }

        AddTextView(consultModelList.get(position).getLevel(),holder.Label_layout,position);
        AddTextView(consultModelList.get(position).getOffice(),holder.Label_layout,position);
        AddTextView(consultModelList.get(position).getCommType(),holder.Label_layout,position);
        AddTextView(consultModelList.get(position).getRegion(),holder.Label_layout,position);
        holder.content_edit.setText(StringUtil.trimNull(consultModelList.get(position).getDesc()));
        holder.data_text.setText(consultModelList.get(position).getCreatedTime().substring(0,10));
        holder.content_edit.setFocusable(false);
        holder.pay_for_day.setText("费用"+consultModelList.get(position).getCost()+"/次");

        holder.again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultModelList.size();
    }

    private void AddTextView(String text,KahoLabelLayout labelLayout,int position){
        MyTextView textView = new MyTextView(context);
        textView.setText(text);
        textView.setTextSize(14);
//        if(position == 0){
//            textView.setBackground(context.getResources().getDrawable(R.drawable.radius_kaho));
//            textView.setTextColor(context.getResources().getColor(R.color.black));
//        }else{
//            textView.setBackground(context.getResources().getDrawable(R.drawable.radius_blue));
//            textView.setTextColor(context.getResources().getColor(R.color.white));
//        }
        textView.setBackground(context.getResources().getDrawable(R.drawable.radius_blue));
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setPadding(10, 10, 10, 10);
        labelLayout.addView(textView, marginLayoutParams);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

         TextView data_text,pay_for_day;
         KahoLabelLayout Label_layout;
         EditText content_edit;
        ImageView again_button;

        public ViewHolder(View itemView) {
            super(itemView);
            data_text = (TextView)itemView.findViewById(R.id.data_text);
            pay_for_day = (TextView)itemView.findViewById(R.id.pay_for_day);
            Label_layout = (KahoLabelLayout)itemView.findViewById(R.id.Label_layout);
            content_edit = (EditText)itemView.findViewById(R.id.content_edit);
            again_button = (ImageView)itemView.findViewById(R.id.again_button);
        }
    }
}

