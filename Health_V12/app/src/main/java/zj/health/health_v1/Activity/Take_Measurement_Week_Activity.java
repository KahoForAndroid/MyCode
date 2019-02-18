package zj.health.health_v1.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by Administrator on 2018/6/21.
 */

public class Take_Measurement_Week_Activity extends BaseActivity{

    private ImageView back;
    private TextView title;
    private RecyclerView recy_week;
    private DayAdapter adapter;
    private Button save_btn;
    private List<String> stringList = new ArrayList<>();
    private List<String> repeatList = new ArrayList<>();
    private int positions = 0;
    private String name,repeat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_measurement_week_activity);
        initView();
        BindListener();
        setDate();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        recy_week = (RecyclerView)findViewById(R.id.recy_week);
        save_btn = (Button)findViewById(R.id.save_btn);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0x114);
                finish();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.putExtra("position",positions);
                String weeks [] = getResources().getStringArray(R.array.weeks);
                StringBuffer stringBuffer = new StringBuffer();
                StringBuffer weekStrBuffer = new StringBuffer();
                for (int i = 0;i<repeatList.size();i++){
                    stringBuffer.append(repeatList.get(i));
                    if(repeatList.get(i).equals("1")){
                        if(i == repeatList.size()-1){
                            weekStrBuffer.append(weeks[i]);
                        }else{
                            weekStrBuffer.append(weeks[i]+"、");
                        }

                    }
                }

                it.putExtra("repeat",stringBuffer.toString());
                it.putExtra("name","星期"+weekStrBuffer.toString());
                setResult(0x113,it);
                finish();
            }
        });
    }
    private void setDate(){


        title.setText(getString(R.string.repeat));
        stringList = Arrays.asList(getResources().getStringArray(R.array.week_days));
        positions = getIntent().getIntExtra("position",0);
        name = getIntent().getStringExtra("name");
        repeat = getIntent().getStringExtra("repeat");
        for (int i= 0;i<repeat.length();i++){
            repeatList.add(repeat.charAt(i)+"");
        }
        adapter = new DayAdapter(this,stringList,name,repeatList);
        recy_week.setLayoutManager(new LinearLayoutManager(this));
        recy_week.setAdapter(adapter);
        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                if(repeatList.get(position).equals("0")){
                    repeatList.set(position,"1");
                }else{
                    repeatList.set(position,"0");
                }
                adapter.setRepeatList(repeatList);

            }
        });
    }

    class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>{

        Context context;
        List<String> stringList;
        String names;
        OnItemClick onItemClick;
        List<String> repeatList;

        public DayAdapter(Context context,List<String> stringList,String names,List<String> repeatList){
            this.context = context;
            this.stringList = stringList;
            this.names = names;
            this.repeatList = repeatList;
        }
        public void setOnItemClick(OnItemClick onItemClick){
            this.onItemClick = onItemClick;
        }
        public void setRepeatList( List<String> repeatList){
            this.repeatList = repeatList;
            notifyDataSetChanged();
        }
        @Override
        public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.week_recy,parent,false);
            DayAdapter.ViewHolder viewHolder = new DayAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(DayAdapter.ViewHolder holder, final int position) {
            holder.day_text.setText(stringList.get(position));
            holder.days_rela.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.OnItemClickListener(view,position);
                }
            });

                if(repeatList.get(position).equals("1")){
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
