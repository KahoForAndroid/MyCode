package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.IM.model.Conversation;
import zj.health.health_v1.IM.utils.TimeUtil;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.Model.ConsultModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;

/**
 * Created by Administrator on 2018/11/15.
 */

public class DoctorServiceAdapter extends RecyclerView.Adapter<DoctorServiceAdapter.ViewHolder>{

    private Context context;
    private ConsultItemClick consultItemClick;
    private List<ConsultModel> consultModelList;
    private RequestOptions requestOptions ;
    private List<Conversation> conversationList;

    public DoctorServiceAdapter(Context context,List<ConsultModel> consultModelList,List<Conversation> conversationList){
        this.context = context;
        this.consultModelList = consultModelList;
        this.conversationList = conversationList;
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_head) //加载中图片
                .error(R.drawable.icon_head) //加载失败图片
                .fallback(R.drawable.icon_head) //url为空图片
                .centerCrop()
                .transform(new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略
    }

    public void setConsultItemClick(ConsultItemClick consultItemClick){
        this.consultItemClick = consultItemClick;
    }
    public void setConsultListModelList(List<ConsultModel> consultModelList){
        this.consultModelList = consultModelList;
        notifyDataSetChanged();
    }
    public void setConversationList(List<Conversation> conversationList){
        this.conversationList = conversationList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       ViewHolder viewHolder = new ViewHolder(View.inflate(context, R.layout.consult_list_item,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).
                load(Constant.photo_IP+consultModelList.get(position).getDoctorIcon()).
                apply(requestOptions).
                into(holder.doctorIcon);

        holder.nick_name.setText(consultModelList.get(position).getDoctorName());
        holder.counseling_img.setVisibility(View.GONE);
        holder.text_top.setVisibility(View.GONE);
        holder.text_bottom.setVisibility(View.GONE);
        holder.Communicating_rela.setVisibility(View.VISIBLE);
        holder.Communicating_rela.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.Communicating_text.setTextColor(context.getResources().getColor(R.color.black));
        for (int i = 0;i<conversationList.size();i++){
            if(consultModelList.get(position).getDoctorId().equals(conversationList.get(i).getIdentify())){
                consultModelList.get(position).setConversationListPosition(i);
                holder.remark_text.setText(conversationList.get(i).getLastMessageSummary());
//                conversationList.get(i).getLastMessageTime()
                holder.Communicating_text.setText(TimeUtil.getTimeStr(conversationList.get(i).getLastMessageTime()));
                if(conversationList.get(i).getUnreadNum()>0){
                    holder.point.setVisibility(View.VISIBLE);
                }else{
                    holder.point.setVisibility(View.GONE);
                }
                break;
            }
        }

        holder.item_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultItemClick.onConsultItemClick(v,position,1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return consultModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView counseling_text,remark_text,nick_name,age_text,sex_text,text_top,text_bottom,Communicating_text;
        ImageView doctorIcon,counseling_img,point;
        RelativeLayout item_rela,Communicating_rela;

        public ViewHolder(View itemView) {
            super(itemView);
            counseling_img = (ImageView)itemView.findViewById(R.id.counseling_button);
            counseling_text = (TextView)itemView.findViewById(R.id.counseling_text);
            Communicating_text = (TextView)itemView.findViewById(R.id.Communicating_text);
            remark_text = (TextView)itemView.findViewById(R.id.remark_text);
            nick_name = (TextView)itemView.findViewById(R.id.nick_name);
            age_text = (TextView)itemView.findViewById(R.id.age_text);
            sex_text = (TextView)itemView.findViewById(R.id.sex_text);
            doctorIcon = (ImageView)itemView.findViewById(R.id.doctorIcon);
            text_top = (TextView)itemView.findViewById(R.id.text_top);
            text_bottom = (TextView)itemView.findViewById(R.id.text_bottom);
            point = (ImageView)itemView.findViewById(R.id.point);
            item_rela = (RelativeLayout)itemView.findViewById(R.id.item_rela);
            Communicating_rela = (RelativeLayout)itemView.findViewById(R.id.Communicating_rela);
        }
    }
    public interface ConsultItemClick{
        void onConsultItemClick(View view,int Position,int Type);
    }
}
