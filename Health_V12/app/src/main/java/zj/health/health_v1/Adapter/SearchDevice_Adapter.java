package zj.health.health_v1.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/6/19.
 */

public class SearchDevice_Adapter extends RecyclerView.Adapter<SearchDevice_Adapter.ViewHolder>{

    private Context context;
    private List<BluetoothDevice> bluetoothDeviceList;
    private OnItemClick onItemClick;
    private List<BluetoothDevice> bluetoothDevices;

    public SearchDevice_Adapter(Context context, List<BluetoothDevice> bluetoothDeviceList){
        this.context = context;
        this.bluetoothDeviceList = bluetoothDeviceList;
    }


    public SearchDevice_Adapter(Context context, List<BluetoothDevice> bluetoothDevices,String a){
        this.context = context;
        this.bluetoothDeviceList = bluetoothDeviceList;
        this.bluetoothDevices = bluetoothDevices;
    }
    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    public void setBluetoothDeviceList(List<BluetoothDevice> bluetoothDeviceList){
        this.bluetoothDeviceList = bluetoothDeviceList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_device_recy_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(!StringUtil.isEmpty(bluetoothDeviceList.get(position).getName())){
            holder.deviceName_text.setText(bluetoothDeviceList.get(position).getName());
        }else{
            holder.deviceName_text.setText(bluetoothDeviceList.get(position).getAddress());
        }
        holder.deviceAddress_text.setText(bluetoothDeviceList.get(position).getAddress());
        holder.connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClickListener(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bluetoothDeviceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView deviceName_text,deviceAddress_text;
        Button connect_button;
        public ViewHolder(View itemView) {
            super(itemView);
            deviceName_text = (TextView)itemView.findViewById(R.id.deviceName_text);
            deviceAddress_text = (TextView)itemView.findViewById(R.id.deviceAddress_text);
            connect_button = (Button)itemView.findViewById(R.id.connect_button);
        }
    }
}
