package zj.health.health_v1.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Administrator on 2018/7/1.
 */

public class ClientThread implements Runnable {

    final String TAG = "ClientThread";

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;

    Handler uiHandler;
    Handler writeHandler;

    public static BluetoothSocket socket;
    public static OutputStream out;
    public static InputStream in;
    BufferedReader reader;


    public ClientThread(BluetoothAdapter bluetoothAdapter, BluetoothDevice device,
                        Handler handler) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.device = device;
        this.uiHandler = handler;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-1231-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = tmp;
    }

    @Override
    public void run() {

        Log.e(TAG, "----------------- do client thread run()");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        //            socket.connect();
        //reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "-----------do client read run()");
                try {


                        Method m = null;
                        m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                        socket = (BluetoothSocket) m.invoke(device, 1);
                        socket.connect();

                    out = socket.getOutputStream();
//                    out.write("[XL*334588000000156*0004*FIND]".getBytes());
                    in = socket.getInputStream();
//                    socket.close();
                    byte[] buffer = new byte[1024];
                    int len;
                    String content;

                    Message message = new Message();
                    message.what = 1;
                    uiHandler.sendMessage(message);

//                    int a = in.read();
//                    if(a == 0){
//                        Message message = new Message();
//                        message.what = 1;
//                        uiHandler.sendMessage(message);
//                    }else{
//                        while ((len=in.read(buffer)) != -1) {
//                            content=new String(buffer, 0, len);
//                            Message message = new Message();
//                            message.what = 1;
//                            message.obj = content;
//                            uiHandler.sendMessage(message);
//                            Log.e(TAG, "------------- client read data in while ,send msg ui" + content);
//                        }
//                    }

                    System.out.println("s");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "-------------- exception");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//            Looper.prepare();
//            writeHandler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    switch (msg.what) {
//                        case Params.MSG_CLIENT_WRITE_NEW:
//                            String data = msg.obj.toString() + "\n";
//                            try {
//                                out.write(data.getBytes("utf-8"));
//                                Log.e(TAG, "-------------client write data " + data);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//
//                    }
//                }
//            };
//            Looper.loop();

    }


    public void write(String data){
//        data = data+"\r\n";
        try {
            out.write(data.getBytes());
            Log.e(TAG, "---------- write data ok "+data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
