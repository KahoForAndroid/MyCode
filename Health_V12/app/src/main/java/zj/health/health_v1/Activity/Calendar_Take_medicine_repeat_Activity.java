package zj.health.health_v1.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/6/20.
 */

public class Calendar_Take_medicine_repeat_Activity extends BaseActivity{

    private ImageView back;
    private TextView title;
    private RecyclerView recy_repat;
    private DayAdapter adapter;
    private List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_take_medicine_repat_activity);
        initView();
        BindListener();
        setDate();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        recy_repat = (RecyclerView)findViewById(R.id.recy_repat);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0x114);
                finish();
            }
        });
    }
    private void setDate(){
        title.setText(getString(R.string.repeat));
        stringList = Arrays.asList(getResources().getStringArray(R.array.repeat_days));
        final int positions = getIntent().getIntExtra("position",0);
        final String name = getIntent().getStringExtra("name");
        adapter = new DayAdapter(this,stringList,name);
        recy_repat.setLayoutManager(new LinearLayoutManager(this));
        recy_repat.setAdapter(adapter);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent it = new Intent();
                it.putExtra("position",positions);
                it.putExtra("name",position+1+"");
                setResult(0x113,it);
                finish();
            }
        });
    }

    class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>{

        Context context;
        List<String> stringList;
        String names;
        OnItemClick onItemClick;

        public DayAdapter(Context context,List<String> stringList,String names){
            this.context = context;
            this.stringList = stringList;
            this.names = names;
        }
        public void setOnItemClick(OnItemClick onItemClick){
            this.onItemClick = onItemClick;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.days_recy,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.day_text.setText(stringList.get(position));
            holder.days_rela.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.OnItemClickListener(view,position);
                }
            });
            if(stringList.get(position).equals(names)){
                holder.green_gou_img.setVisibility(View.VISIBLE);
            }else{
                holder.green_gou_img.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return stringList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            RelativeLayout days_rela;
            TextView day_text;
            ImageView green_gou_img;

            public ViewHolder(View itemView) {
                super(itemView);
                days_rela = (RelativeLayout)itemView.findViewById(R.id.days_rela);
                day_text = (TextView)itemView.findViewById(R.id.day_text);
                green_gou_img = (ImageView)itemView.findViewById(R.id.green_gou_img);
            }
        }
    }
}
