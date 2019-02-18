package zj.health.health_v1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.Model.BlueToothDeviceModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/7/9.
 */

public class BlueToothDeviceListAdapter extends RecyclerView.Adapter<BlueToothDeviceListAdapter.ViewHolder>{

    private Context context;
    private List<BlueToothDeviceModel> list ;
    private int type =0;
    private OnItemClick onItemClick;

    public BlueToothDeviceListAdapter(Context context,List<BlueToothDeviceModel> list){
        this.context = context;
        this.list = list;
    }

    public BlueToothDeviceListAdapter(Context context,List<BlueToothDeviceModel> list,int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    public void setList(List<BlueToothDeviceModel> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.bluetooth_devicelist_recy_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.deviceName_text.setText(list.get(position).getBluetoothDevice().getName());
        holder.deviceAddress_text.setText(list.get(position).getBluetoothDevice().getAddress());

        if(list.get(position).getFromType() == 0){
            holder.disconnect_button.setText(context.getString(R.string.measure_again));
        }
        holder.function_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.disconnect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClickListener(v,position);
                //根据类型判断从服务断开设备连接还是直接断开
//                if(type == 4){
//                    MainsActivity.mBluetoothLeService.disconnect();
//                    context.unbindService(MainsActivity.mServiceConnection);
//
//                }else{
//                    onItemClick.OnItemClickListener(v,position);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView deviceName_text,deviceAddress_text;
        Button function_button,disconnect_button;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName_text = (TextView)itemView.findViewById(R.id.deviceName_text);
            deviceAddress_text = (TextView)itemView.findViewById(R.id.deviceAddress_text);
            function_button = (Button)itemView.findViewById(R.id.function_button);
            disconnect_button = (Button)itemView.findViewById(R.id.disconnect_button);
        }
    }
}
