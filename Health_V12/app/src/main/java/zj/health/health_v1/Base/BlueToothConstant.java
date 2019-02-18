package zj.health.health_v1.Base;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/2.
 */

public class BlueToothConstant {


    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    public static final String UPDATECONNECTDEVICELIST = "UpdateConnectDeviceList";
    public static final int BODY_HEARTRATE_CONNECTRESULT = 14;
    public static final int BODY_WEIGHT_CONNECTRESULT = 12;
    public static final int BODY_BLOODPRESSIRE_CONNECTRESULT = 11;
    public static final int BODY_TEMPERATURE_CONNECTRESULT = 13;
    public static final int BODY_BLOODSUGAR_CONNECTRESULT = 15;


    //描述UUID
    public static final UUID CCCD = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");

    //服务UUID
    public static final UUID RX_SERVICE_UUID = UUID
            .fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    //特性为写入的UUID
    public static final UUID RX_CHAR_UUID = UUID
            .fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    //通知特性UUID
    public static final UUID TX_CHAR_UUID = UUID
            .fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
}
