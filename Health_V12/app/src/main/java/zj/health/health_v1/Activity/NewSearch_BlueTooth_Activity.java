package zj.health.health_v1.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import zj.health.health_v1.Adapter.SearchDevice_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Implements.OnItemClick;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.ClientThread;
import zj.health.health_v1.Utils.ServerThread;

/**
 * Created by Administrator on 2018/7/1.
 */

public class NewSearch_BlueTooth_Activity extends BaseActivity{


    private TextView title,serch_again_text;
    private ImageView back;
    private RecyclerView device_recy;
    private SearchDevice_Adapter adapter;
    private IntentFilter intentFilter = new IntentFilter();
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    public BluetoothAdapter mBluetoothAdapter = null;

    // 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
    private BluetoothDevice selectDevice;
    // 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
    private BluetoothSocket clientSocket;
    // 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
    private OutputStream os;
    private InputStream in;
    private Thread listenThread;
    private boolean listen = true;
    private Handler connectHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
//                clientThread.write("[XL*334588000000156*0004*FIND]");
            }
        }
    };

    private Handler ServerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){

            }else if(msg.what == 100){
//                Toast.makeText(MainActivity.this, "测量出错了", Toast.LENGTH_SHORT).show();
            }
        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bluetooth_activity);
        initView();
        BindListener();
        setData();
    }

    private void initView(){
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        device_recy = (RecyclerView)findViewById(R.id.device_recy);
        serch_again_text = (TextView)findViewById(R.id.serch_again_text);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setResult(0x2);
//                finish();
                if (os != null) {
                    // 需要发送的信息
                    String text = "[XL*356969030747519*0008*POWEROFF]";
                    byte [] b = text.getBytes();
                    // 以utf-8的格式发送出去
                    try {
                        byte [] aa = text.getBytes("UTF-8");
                        os.write(text.getBytes());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        serch_again_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void setData(){
        adapter = new SearchDevice_Adapter(this,bluetoothDeviceList);
        device_recy.setLayoutManager(new LinearLayoutManager(this));
        device_recy.setAdapter(adapter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(new MyBtReceiver(),intentFilter);
//        serverThread = new ServerThread(mBluetoothAdapter,ServerHandler);
//        acceptThread = new AcceptThread();
//        acceptThread.start();
//        new Thread(serverThread).start();
        mBluetoothAdapter.startDiscovery();


        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                mBluetoothAdapter.cancelDiscovery();
//                 clientThread = new ClientThread(mBluetoothAdapter,bluetoothDeviceList.get(position),connectHandler);
//                 new Thread(clientThread).start();

                if (selectDevice == null) {
                    //通过地址获取到该设备
                    selectDevice = mBluetoothAdapter.getRemoteDevice(bluetoothDeviceList.get(position).getAddress());
                }

                // 这里需要try catch一下，以防异常抛出
                BluetoothSocket tmp = null;
                try {
                    tmp = selectDevice
                            .createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    clientSocket = tmp;
//                    mBluetoothAdapter.cancelDiscovery();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                    // 判断客户端接口是否为空
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {

                          if (clientSocket != null) {
                              // 获取到客户端接口
                              // 向服务端发送连接
//                              Method m = null;
//                              ParcelUuid [] parcelUuids= selectDevice.getUuids();
//                              m = selectDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//                              clientSocket = (BluetoothSocket) m.invoke(selectDevice, 1);
                              clientSocket.connect();
                              // 获取到输出流，向外写数据
                              os = clientSocket.getOutputStream();
                              in = clientSocket.getInputStream();
                              os.write(hex2byte("cc80020301010001".getBytes()));
                              Thread.sleep(1000);
                              os.write(hex2byte("cc80020301020002".getBytes()));
                              listen(in);

                          }



//                              acceptThread = new AcceptThread();
//                              acceptThread.start();
                          // 判断是否拿到输出流


                          } catch (IOException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                              // 如果发生异常则告诉用户发送失败
//                    Toast.makeText(this, "发送信息失败", 0).show();
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  }).start();
                    // 吐司一下，告诉用户发送成功
//                    Toast.makeText(this, "发送信息成功，请查收", 0).show();


            }
        });
    }


    private void listen(final InputStream inputStream) {
        listenThread = new Thread(){
            @Override
            public void run() {
                int len;
                byte[] bytes = new byte[20];
                try {
                    while (listen) {
                        if ((len = inputStream.available()) > 0) {
                            inputStream.read(bytes, 0, len);
                            //SimpleLog.print("inputStream9999 " + BytesUtils.toString(bytes));
                            backBytes(bytes);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        listenThread.start();
    }



    private void backBytes(byte[] bytes) {
        byte codeByte = bytes[5];//区别码 04 是电量 5是过程中产生的数据  6是正式结果  7是错误结束
        switch (codeByte) {
            case 0x04:
                //item1.setText("电量：" + getIntValue(bytes[6], bytes[7]));
                int power = getIntValue(bytes[6], bytes[7]);
                break;
            case 0x05:
                //item2.setText("收缩压：" + getIntValue(bytes[11], bytes[12]));
                int shousuoya = getIntValue(bytes[11], bytes[12]);

                break;
            case 0x06:
//                item4.setText("脉搏：" + getIntValue(bytes[17], bytes[18]));
//                item3.setText("舒张压：" + getIntValue(bytes[15], bytes[16]));
//                item2.setText("收缩压：" + getIntValue(bytes[13], bytes[14]));
                int maibo = getIntValue(bytes[17], bytes[18]);
                int shuzhangya = getIntValue(bytes[15], bytes[16]);
                int shousuoya2 = getIntValue(bytes[13], bytes[14]);
                Message message = new Message();
                message.what = 200;
                message.obj = new int[]{shousuoya2,shuzhangya,maibo};
                ServerHandler.sendMessage(message);

                break;
            case 0x07:
//                item4.setText("脉搏：ERROR");
//                item3.setText("舒张压：ERROR");
//                item2.setText("收缩压：ERROR");
//                Toast.makeText(MainActivity.this, "测量出错了", Toast.LENGTH_SHORT).show();
                Message messageError = new Message();
                messageError.what = 100;
                ServerHandler.sendMessage(messageError);
                break;
        }
    }

    public static int getIntValue(byte byte1, byte byte2) {
        return (getIntValue(byte1) << 8) + getIntValue(byte2);
    }
    public static int getIntValue(byte byt) {
        return byt < 0 ? Math.abs(byt) + (128 - Math.abs(byt)) * 2 : byt;
    }


    public byte[] hex2byte(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length % 2 != 0) {
            System.out.println("ERROR:  le= " + paramArrayOfByte.length + " b:" + paramArrayOfByte.toString());
            return null;
        }
        byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
        for (int i = 0; i < paramArrayOfByte.length; i += 2) {
            String str = new String(paramArrayOfByte, i, 2);
            arrayOfByte[(i / 2)] = ((byte) Integer.parseInt(str, 16));
        }
        return arrayOfByte;
    }

    /**
     * 广播接受器
     */
    private class MyBtReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(context, "开始搜索", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(context, "搜索结束", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (isNewDevice(device)) {
                    bluetoothDeviceList.add(device);
                    adapter.setBluetoothDeviceList(bluetoothDeviceList);
                    Log.e("deviceName", "---------------- " + device.getName());
                }
            }else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                Toast.makeText(context, "搜索结束", Toast.LENGTH_SHORT).show();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int boundState = device.getBondState();
                if(boundState == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(context, device.getName()+"配对成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 判断搜索的设备是新蓝牙设备，且不重复
     * @param device
     * @return
     */
    private boolean isNewDevice(BluetoothDevice device){
        boolean repeatFlag = false;
        for (BluetoothDevice d :
                bluetoothDeviceList) {
            if (d.getAddress().equals(device.getAddress())){
                repeatFlag=true;
            }
        }
        //不是已绑定状态，且列表中不重复
        return device.getBondState() != BluetoothDevice.BOND_BONDED && !repeatFlag;
    }

//    public void writeData(String dataSend) {
//        if (serverThread != null) {
//            serverThread.write(dataSend);
//        } else if (clientThread != null) {
//            clientThread.write(dataSend);
//        }
//    }


    // 服务端接收信息线程
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;// 服务端接口
        private BluetoothSocket socket;// 获取到客户端的接口
        private InputStream is;// 获取到输入流
        private OutputStream os;// 获取到输出流

        public AcceptThread() {
            try {
                // 通过UUID监听请求，然后获取到对应的服务端接口
                serverSocket = mBluetoothAdapter
                        .listenUsingRfcommWithServiceRecord("501", UUID.fromString("00001105-0000-1000-8000-00805f9B34FB"));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        public void run() {
            try {
                // 接收其客户端的接口
                socket = serverSocket.accept();
                // 获取到输入流
                is = socket.getInputStream();
                // 获取到输出流
                os = socket.getOutputStream();

                // 无线循环来接收数据
                while (true) {
                    // 创建一个128字节的缓冲
                    byte[] buffer = new byte[128];
                    // 每次读取128字节，并保存其读取的角标
                    int count = is.read(buffer);
                    // 创建Message类，向handler发送数据
                    Message msg = new Message();
                    // 发送一个String的数据，让他向上转型为obj类型
                    msg.obj = new String(buffer, 0, count, "utf-8");
                    // 发送数据
                    ServerHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }

    private void unlisten() {
        listen = false;
        if (listenThread.isAlive())
            listenThread.interrupt();
        listenThread = null;
    }

    public void disConn() {
        try {
            unlisten();
            this.in.close();
            this.os.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
