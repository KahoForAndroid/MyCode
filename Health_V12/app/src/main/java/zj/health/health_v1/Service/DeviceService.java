package zj.health.health_v1.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import zj.health.health_v1.Activity.MainsActivity;
import zj.health.health_v1.Base.BlueToothConstant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Broadcast.DeviceBroadCast;
import zj.health.health_v1.Model.BlueToothDeviceModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.BlueToothUtil.WearfitUtil;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/7/6.
 */

public class   DeviceService extends Service {


    private String mBluetoothDeviceAddress;
    public BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

//    public final static UUID UUID_HEART_RATE_MEASUREMENT =
//            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    public static final UUID CCCD = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID
            .fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID RX_CHAR_UUID = UUID
            .fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID TX_CHAR_UUID = UUID
            .fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    private static final int SEND_PACKET_SIZE = 20;
    private static final int FREE = 0;
    private static final int SENDING = 1;
    private static final int RECEIVING = 2;


    private int ble_status = FREE;
    private int packet_counter = 0;
    private int send_data_pointer = 0;
    private byte[] send_data = null;
    private boolean first_packet = false;
    private boolean final_packet = false;
    private boolean packet_send = false;
    private Timer mTimer;
    private int time_out_counter = 0;
    private int TIMER_INTERVAL = 100;
    private int TIME_OUT_LIMIT = 100;
    public ArrayList<byte[]> data_queue = new ArrayList<>();
    boolean sendingStoredData = false;
    private final static String TAG = DeviceService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private int DeviceType = 0;//当前连接的设备归属于哪个体征
    public Timer timer = null;
    private long delay = 1000;//延迟timer的执行时间
//    private long timeInMillis;//获取手环指定时间之后的数据(如果有更新的数据)

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Health_AppLocation.instance.UpdateHeartTime = intent.getLongExtra("TimeMillis",0);
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public DeviceService getService() {
            return DeviceService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Health_AppLocation.instance.mBluetoothManager != null && Health_AppLocation.instance.mBluetoothAdapter != null && mBluetoothGatt != null) {
            BLE_send_data_set(WearfitUtil.setTimeSync(), false);
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(Health_AppLocation.instance.mBluetoothManager != null && Health_AppLocation.instance.mBluetoothAdapter != null && mBluetoothGatt != null) {
                        BLE_send_data_set(WearfitUtil.syncData(Health_AppLocation.instance.UpdateHeartTime), false);
                        Health_AppLocation.instance.UpdateHeartTime = System.currentTimeMillis() - 1000 * 60 * 5 + 5000;
                        delay = 1000 * 60 * 5;
                    }else {
                        GotoBroadcast(4,0,0,0);
                    }
                }
            }, delay);
//        timer.scheduleAtFixedRate();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if(Health_AppLocation.instance.mBluetoothManager != null && Health_AppLocation.instance.mBluetoothAdapter != null && mBluetoothGatt != null) {
//                    BLE_send_data_set(WearfitUtil.syncData(System.currentTimeMillis() - 1000 * 60 * 5), false);
//                }else {
//                    GotoBroadcast(4,0,0);
//                }
//            }
//        },1000*60*5,1000*60*5);
        return super.onStartCommand(intent, flags, startId);
    }



    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (Health_AppLocation.instance.mBluetoothManager == null) {
            Health_AppLocation.instance.mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (Health_AppLocation.instance.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        Health_AppLocation.instance.mBluetoothAdapter = Health_AppLocation.instance.mBluetoothManager.getAdapter();
        if (Health_AppLocation.instance.mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address,int DeviceType) {
        this.DeviceType = DeviceType;

        if (Health_AppLocation.instance.mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = Health_AppLocation.instance.mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (Health_AppLocation.instance.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();

    }


    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }


    /**
     * 设置数据到内部缓冲区对BLE发送数据
     */
    private void BLE_send_data_set(byte[] data, boolean retry_status) {
        if (ble_status != FREE || mConnectionState != STATE_CONNECTED) {
            //蓝牙没有连接或是正在接受或发送数据，此时将要发送的指令加入集合
            if (sendingStoredData) {
                if (!retry_status) {
                    data_queue.add(data);
                }
                GotoBroadcast(4,0,0,0);
                return;
            } else {
                data_queue.add(data);
                start_timer();
            }

        } else {
            ble_status = SENDING;

            if (data_queue.size() != 0) {
                send_data = data_queue.get(0);
                sendingStoredData = false;
            } else {
                send_data = data;
            }
            packet_counter = 0;
            send_data_pointer = 0;
            //第一个包
            first_packet = true;
            BLE_data_send();

            if (data_queue.size() != 0) {
                data_queue.remove(0);
            }

            if (data_queue.size() == 0) {
                if (mTimer != null) {
                    mTimer.cancel();
                }
            }
        }
    }

    /**
     * 定时器
     */
    private void start_timer() {
        sendingStoredData = true;
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer_Tick();
            }
        }, 100, TIMER_INTERVAL);
    }

    /**
     * @brief Interval timer function.
     */
    private void timer_Tick() {

        if (data_queue.size() != 0) {
            sendingStoredData = true;
            BLE_send_data_set(data_queue.get(0), true);
        }

        if (time_out_counter < TIME_OUT_LIMIT) {
            time_out_counter++;
        } else {
            ble_status = FREE;
            time_out_counter = 0;
        }
        return;
    }



    /**
     * @brief Send data using BLE. 发送数据到蓝牙
     */
    private void BLE_data_send() {
        int err_count = 0;
        int send_data_pointer_save;
        int wait_counter;
        boolean first_packet_save;
        while (!final_packet) {
            //不是最后一个包
            byte[] temp_buffer;
            send_data_pointer_save = send_data_pointer;
            first_packet_save = first_packet;
            if (first_packet) {
                //第一个包

                if ((send_data.length - send_data_pointer) > (SEND_PACKET_SIZE)) {
                    temp_buffer = new byte[SEND_PACKET_SIZE];//20
                    for (int i = 0; i < SEND_PACKET_SIZE; i++) {
                        //将原数组加入新创建的数组
                        temp_buffer[i] = send_data[send_data_pointer];
                        send_data_pointer++;
                    }
                } else {
                    //发送的数据包不大于20
                    temp_buffer = new byte[send_data.length - send_data_pointer];
                    for (int i = 0; i < temp_buffer.length; i++) {
                        //将原数组未发送的部分加入新创建的数组
                        temp_buffer[i] = send_data[send_data_pointer];
                        send_data_pointer++;
                    }
                    final_packet = true;
                }
                first_packet = false;
            } else {
                //不是第一个包
                if (send_data.length - send_data_pointer >= SEND_PACKET_SIZE) {
                    temp_buffer = new byte[SEND_PACKET_SIZE];
                    temp_buffer[0] = (byte) packet_counter;
                    for (int i = 1; i < SEND_PACKET_SIZE; i++) {
                        temp_buffer[i] = send_data[send_data_pointer];
                        send_data_pointer++;
                    }
                } else {
                    final_packet = true;
                    temp_buffer = new byte[send_data.length - send_data_pointer + 1];
                    temp_buffer[0] = (byte) packet_counter;
                    for (int i = 1; i < temp_buffer.length; i++) {
                        temp_buffer[i] = send_data[send_data_pointer];
                        send_data_pointer++;
                    }
                }
                packet_counter++;
            }
            packet_send = false;

            boolean status = writeRXCharacteristic(temp_buffer);
            if ((status == false) && (err_count < 3)) {
                err_count++;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                send_data_pointer = send_data_pointer_save;
                first_packet = first_packet_save;
                packet_counter--;
            }
            // Send Wait
            for (wait_counter = 0; wait_counter < 5; wait_counter++) {
                if (packet_send == true) {
                    break;
                }
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        final_packet = false;
        ble_status = FREE;
    }




    /**
     * @brief writeRXCharacteristic
     */
    public boolean writeRXCharacteristic(byte[] value) {
        BluetoothGattService RxService = mBluetoothGatt
                .getService(RX_SERVICE_UUID);
        if (RxService == null) {
            return false;
        }

        BluetoothGattCharacteristic RxChar = RxService
                .getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            return false;
        }
        RxChar.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(RxChar);
//        Log.d(TAG, "Send command：status：" + status + "-->" + DataHandlerUtils.bytesToHexStr(value));
        return status;
    }

    /**
     * @brief enableTXNotification
     */
    @SuppressLint("InlinedApi")
    public void enableTXNotification() {
        BluetoothGattService RxService = mBluetoothGatt
                .getService(RX_SERVICE_UUID);
        if (RxService == null) {
            return;
        }

        BluetoothGattCharacteristic TxChar = RxService
                .getCharacteristic(TX_CHAR_UUID);
        if (TxChar == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(TxChar, true);
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }



    // connection change and services discovered.
    public final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "CONNECTED");
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;

                BlueToothDeviceModel model = new BlueToothDeviceModel();
                model.setBluetoothDevice(gatt.getDevice());
                model.setFromType(DeviceType);
                Health_AppLocation.instance.blueToothDeviceModels.add(model);

                Intent it = new Intent(BlueToothConstant.UPDATECONNECTDEVICELIST);
                sendBroadcast(it);
                if(timer != null){
                    timer.cancel();
                    broadcastUpdate(intentAction);
                }else{
                    broadcastUpdate(intentAction);
                }

                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start com.wakeup.bluetoothtest.service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "DISCONNECTED");
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                broadcastUpdate(intentAction);

                //通过循环把指定的设备从集合中移除
                for (int i = 0;i<Health_AppLocation.instance.blueToothDeviceModels.size();i++){
                    if(Health_AppLocation.instance.blueToothDeviceModels.get(i).getBluetoothDevice().getAddress().equals(gatt.getDevice().getAddress())){
                        Health_AppLocation.instance.blueToothDeviceModels.remove(i);
                    }
                }
                Intent it = new Intent(BlueToothConstant.UPDATECONNECTDEVICELIST);
                sendBroadcast(it);
                timer.cancel();
                close();

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                enableTXNotification();//允许接收蓝牙设备发送过来的数据
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {

//            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            List<Integer> datas = StringUtil.bytesToArrayList(characteristic.getValue());
            setBlueToothDataHeart(datas);


        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    };




    public void setBlueToothDataHeart(List<Integer> datas){
        if (datas.get(0) == 0xAB && datas.get(4) == 0x51 && datas.get(5) == 0x08) {
            Log.d(TAG,"steps calories and sleep data current");

            int steps = (datas.get(6) << 16) + (datas.get(7) << 8) + datas.get(8);
            float distance = (steps * 0.7f)/1000;//If the user does not tell you his stride, by default he walked 0.7m every step
            int calories =(datas.get(9) << 16) + (datas.get(10) << 8) + datas.get(11);
            BigDecimal bd = new BigDecimal((double) distance);
            BigDecimal bigDecimal = bd.setScale(2, RoundingMode.HALF_UP);
            float distance2 = bigDecimal.floatValue();
            Log.i(TAG,"steps: " +steps +"  distance: "+distance2+"  calories: "+calories);

            //睡眠
            long shallowSleep = (datas.get(12) * 60 + datas.get(13)) * 60 * 1000L;
            long deepSleep = (datas.get(14) * 60 + datas.get(15)) * 60 * 1000L;
            long sleepTime = shallowSleep+deepSleep;
            int wake_times = datas.get(16);

            GotoBroadcast(4,0,0,0);

        }
        //The second type of data  2.6.3 -----------------------------------------------------------
        if (datas.get(0) == 0xAB && datas.get(4) == 0x51 && datas.get(5) == 0x20){//Hourly
            Log.d(TAG,"the steps, calories, heart rate, blood oxygen,blood pressure data from hourly measure");
            int year = datas.get(6) + 2000;
            int month = datas.get(7);
            int day = datas.get(8);
            int hour = datas.get(9)+1;
            String time= year+"-"+month+"-"+day+" "+hour;
            Log.i(TAG,"hourly_time  "+time);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
            long timeInMillis=0;
            try {
                Date date = sdf.parse(time);
                timeInMillis=date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int steps = (datas.get(10) << 16) + (datas.get(11) << 8) + datas.get(12);
            int calories = (datas.get(13) << 16) + (datas.get(14) << 8) + datas.get(15);
            float distance = (steps * 0.7f)/1000;//If the user does not tell you his stride, by default he walked 0.7m every step

            BigDecimal bd = new BigDecimal((double) distance);
            BigDecimal bigDecimal = bd.setScale(2, RoundingMode.HALF_UP);
            float distance2 = bigDecimal.floatValue();
            int heartRate = datas.get(16);
            int bloodOxygen = datas.get(17);
            int bloodPressure_high = datas.get(18);
            int bloodPressure_low = datas.get(19);

        }
        if (datas.get(0) == 0){
            Log.d(TAG,"second packet data from hourly measure");
            long  shwllow_time = datas.get(1) * 60 * 60 * 1000L + datas.get(2) * 60 * 1000L;
            long deep_time = datas.get(3) * 60 * 60 * 1000L + datas.get(4) * 60 * 1000L;
            long total_time = shwllow_time + deep_time;
            int wake_times = datas.get(5);


        }
        //The third type of data 2.6.2 ------------------------------------------------------------
        if ((datas.get(0) == 0xAB && datas.get(4) == 0x51)){
            int year = datas.get(6) + 2000;
            int month = datas.get(7);
            int day = datas.get(8);
            int hour = datas.get(9);
            int min = datas.get(10);
            String time= year+"-"+month+"-"+day+" "+hour+":"+min;
            Log.i(TAG,"hourly_time  "+time);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            long timeInMillis=0;
            try {
                Date date = sdf.parse(time);
                timeInMillis=date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (datas.get(5) == 0x11){
                Log.d(TAG,"the Heart rate data from band measure");
                int hrValue = datas.get(11);

//                        Intent it = new Intent(MainActivity.this,BroadcastHeart.class);
//                        sendBroadcast(it);

                GotoBroadcast(4,1,hrValue,timeInMillis);
            }else if (datas.get(5) == 0x12){
                Log.d(TAG,"the Blood oxygen data from band measure");
                int bloodOxygen = datas.get(11);


            }else if (datas.get(5) == 0x14){
                Log.d(TAG,"the Blood pressure data from band measure");
                int bloodPressureHigh= datas.get(11);
                int bloodPressureLow = datas.get(12);

            }


        }
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    public void GotoBroadcast(int Type,int Status,int readData,long timeInMillis){
        Intent it = new Intent();
        it.setAction("sendData");
        it.putExtra("TYPE",Type);
        it.putExtra("Status",Status);
        it.putExtra("readData",readData);
        it.putExtra("timeInMillis",timeInMillis);
        sendBroadcast(it);
    }

}
