package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.SecurityModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Binding_SelectedQuestion_Adapter extends RecyclerView.Adapter<Binding_SelectedQuestion_Adapter.ViewHolder>{

    private Context context;
    private List<SecurityModel> questionlist;
    private OnItemClick onItemClick;

    public Binding_SelectedQuestion_Adapter(Context context,List<SecurityModel> questionlist){
        this.context = context;
        this.questionlist = questionlist;
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.binding_selectedquestion_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.question_text.setText(questionlist.get(position).getQuestion());
        holder.click_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClickListener(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView question_text;
        RelativeLayout click_rela;

        public ViewHolder(View itemView) {
            super(itemView);
            question_text = (TextView)itemView.findViewById(R.id.question_text);
            click_rela = (RelativeLayout)itemView.findViewById(R.id.click_rela);
        }
    }
}
