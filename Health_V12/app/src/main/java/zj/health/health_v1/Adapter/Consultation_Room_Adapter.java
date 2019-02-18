package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import zj.health.health_v1.Activity.Consultation_Room_Activity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.IM.model.Conversation;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Consultation_Room_Adapter extends RecyclerView.Adapter<Consultation_Room_Adapter.ViewHolder>{

    private Context context;
    private ConsultItemClick consultItemClick;
    private List<ConsultListModel> consultListModelList;
    private RequestOptions requestOptions ;
    private List<Conversation> conversationList;

    public Consultation_Room_Adapter(Context context,List<ConsultListModel> consultListModelList){
        this.context = context;
        this.consultListModelList = consultListModelList;
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_head) //加载中图片
                .error(R.drawable.icon_head) //加载失败图片
                .fallback(R.drawable.icon_head) //url为空图片
                .centerCrop()
                .transform(new GlideCircleTransform(context))
                // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略
    }

    public Consultation_Room_Adapter(Context context, List<ConsultListModel> consultListModelList, List<Conversation> conversationList){
        this.context = context;
        this.consultListModelList = consultListModelList;
        this.conversationList = conversationList;
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_head) //加载中图片
                .error(R.drawable.icon_head) //加载失败图片
                .fallback(R.drawable.icon_head) //url为空图片
                .centerCrop()
                .transform(new GlideCircleTransform(context))
                // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略
    }

    public void setOnItemClick(ConsultItemClick consultItemClick){
        this.consultItemClick = consultItemClick;
    }

    public void setConsultListModelList(List<ConsultListModel> consultListModelList){
        this.consultListModelList = consultListModelList;
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
        holder.age_text.setText(consultListModelList.get(position).getAge());
        holder.nick_name.setText(consultListModelList.get(position).getNickname());
        holder.sex_text.setText(consultListModelList.get(position).getSex());
        holder.remark_text.setText(consultListModelList.get(position).getIllnessDesc());
        Glide.with(context).
                load(Constant.photo_IP+consultListModelList.get(position).getIconUrl()).
                apply(requestOptions).
                into(holder.doctorIcon);
        switch (Integer.parseInt(consultListModelList.get(position).getStatus())){
            case 1:
                holder.Communicating_rela.setVisibility(View.GONE);
                holder.point.setVisibility(View.GONE);
                holder.counseling_img.setVisibility(View.VISIBLE);
                holder.text_top.setText(R.string.Invitation);
                holder.text_bottom.setText(R.string.counsel);
                holder.counseling_img.setImageResource(R.drawable.blue_circle);
                break;
            case 2:
                holder.Communicating_rela.setVisibility(View.GONE);
                holder.point.setVisibility(View.GONE);
                holder.counseling_img.setVisibility(View.VISIBLE);
                holder.text_top.setText(R.string.wait);
                holder.text_bottom.setText(R.string.Respond);
                holder.counseling_img.setImageResource(R.drawable.gary_circle);
                break;
            case 3:
                holder.Communicating_rela.setVisibility(View.GONE);
                holder.point.setVisibility(View.GONE);
                holder.counseling_img.setVisibility(View.VISIBLE);
                holder.text_top.setText(R.string.wait);
                holder.text_bottom.setText(R.string.Respond);
                holder.counseling_img.setImageResource(R.drawable.gary_circle);
                break;
            case 4:
//                holder.text_top.setText(R.string.immediately);
//                holder.text_bottom.setText(R.string.Communicate);
//                holder.counseling_img.setImageResource(R.drawable.orange_circle);
                holder.counseling_img.setVisibility(View.GONE);
                holder.text_top.setVisibility(View.GONE);
                holder.text_bottom.setVisibility(View.GONE);
                holder.Communicating_rela.setVisibility(View.VISIBLE);
                for (int i = 0;i<conversationList.size();i++){
                    if(consultListModelList.get(position).getUserId().equals(conversationList.get(i).getIdentify())){
                        consultListModelList.get(position).setConversationListPosition(i);
                        holder.remark_text.setText(conversationList.get(i).getLastMessageSummary());
                        if(conversationList.get(i).getUnreadNum()>0){
                            holder.point.setVisibility(View.VISIBLE);
                        }else{
                            holder.point.setVisibility(View.GONE);
                        }
                        break;
                    }
                }
                break;
                default:
                    holder.Communicating_rela.setVisibility(View.GONE);
                    holder.counseling_img.setVisibility(View.VISIBLE);
                    holder.text_top.setText(R.string.Invitation);
                    holder.text_bottom.setText(R.string.counsel);
                    holder.counseling_img.setImageResource(R.drawable.blue_circle);


                    break;
        }
        holder.counseling_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultItemClick.onConsultItemClick(view,position,2);
            }
        });
        holder.item_rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultItemClick.onConsultItemClick(v,position,1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return consultListModelList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        //        RelativeLayout to_chat_rela;
        TextView counseling_text,remark_text,nick_name,age_text,sex_text,text_top,text_bottom;
        ImageView doctorIcon,counseling_img,point;
        RelativeLayout item_rela,Communicating_rela;
        public ViewHolder(View itemView) {
            super(itemView);
//            to_chat_rela = (RelativeLayout)itemView.findViewById(R.id.to_chat_rela);
            counseling_img = (ImageView)itemView.findViewById(R.id.counseling_button);
            counseling_text = (TextView)itemView.findViewById(R.id.counseling_text);
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
