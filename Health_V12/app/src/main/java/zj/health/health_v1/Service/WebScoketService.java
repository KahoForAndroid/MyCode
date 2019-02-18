package zj.health.health_v1.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Internal;
import okio.ByteString;
import rx.functions.Action1;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.Model.ConsultListModel;
import zj.health.health_v1.Utils.BlueToothUtil.WearfitUtil;

/**
 * Created by Administrator on 2018/12/3.
 */

public class WebScoketService extends Service{

    private boolean mNeedConnect = false;
    private StompClient stompClient;
    private OkHttpClient client;
    private Request request;
    private EchoWebSocketListener socketListener;
    public Timer timer = new Timer();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//            connect();
//            registerStompTopic();
//        startForeground(1,new Notification());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initSocket();
        return super.onStartCommand(intent, flags, startId);

    }

    private void initSocket(){

        client = new OkHttpClient.Builder().readTimeout(10000, TimeUnit.MILLISECONDS).connectTimeout(10000, TimeUnit.SECONDS).build();
        request = new Request.Builder().url("ws://112.90.146.226:9090/ws?token="+Health_AppLocation.instance.users.getToken()).build();
        socketListener = new EchoWebSocketListener();
        client.newWebSocket(request,socketListener);
//        client.dispatcher().executorService().shutdown();

    }

    private final class EchoWebSocketListener extends WebSocketListener{
        @Override
        public void onOpen(final WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    webSocket.send("test");
                }
            }, 1000,1000*60);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            try {
                JSONObject jsonObject1 = new JSONObject(text);
                int cmd = jsonObject1.optInt("cmd");
                Intent it = new Intent();

                switch (cmd){
                    case 1001:
//                            ConsultListModel consultListModel = new Gson().fromJson(jsonObject1.optString("data"),ConsultListModel.class);
                        it.setAction("pushConsult");
                        it.putExtra("cmd",1001);
                        JSONObject jsonObjects = new JSONObject(jsonObject1.optString("data"));
                        it.putExtra("userId",jsonObjects.optString("userId"));
                        sendBroadcast(it);
                        break;
                    case 1002:
                        it.setAction("pushConsultUsers");
                        it.putExtra("cmd",1002);
                        sendBroadcast(it);
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            webSocket.close(1000,"closeing");
            client.newWebSocket(request,socketListener);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
//            webSocket.close(1000,null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d("ScoketOnFailure","scoket is onFailure");
            client.newWebSocket(request,socketListener);
        }

    }

    private void connect() throws UnsupportedEncodingException {
        stompClient = Stomp.over(org.java_websocket.WebSocket.class,"ws://112.90.146.226:9090/ws?token="+Health_AppLocation.instance.users.getToken());
        stompClient.connect();
        stompClient.lifecycle().subscribe(new Action1<LifecycleEvent>() {
            @Override
            public void call(LifecycleEvent lifecycleEvent) {
                switch (lifecycleEvent.getType()){
                    case OPENED:
                        mNeedConnect = false;
                        break;
                    case ERROR:
                        mNeedConnect = true;
                        break;
                    case CLOSED:
                        stompClient.connect();
                          mNeedConnect = true;
                        break;
                }
            }
        });

    }

//    //点对点订阅，根据用户名来推送消息
    private void registerStompTopic() {
        stompClient.topic("/user/" + Health_AppLocation.instance.users.getId() + "/msg").subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage stompMessage) {
                Log.d("WebSocket", "forlan debug msg is " + stompMessage.getPayload());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
