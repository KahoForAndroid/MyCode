package zj.health.health_v1.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.zxing.qrcode.encoder.Encoder;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Base.Health_AppLocation;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/4.
 */

public class  HttpUtil {

    private static OkHttpClient okHttpClient = null;

    /**
     * 使用okhttp3进行post的请求
     * @param url 请求接口
     * @param map 上传参数
     * @param handler handler
     * @param baseActivity activity
     */
    public void Post(String url, Map<String,String> map, final android.os.Handler handler, final BaseActivity baseActivity){
        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;
        FormBody.Builder builder = new FormBody.Builder();
        if(map!=null){
            for(Map.Entry<String,String> entry : map.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        body = builder.build();
        final Request request = new Request.Builder().url(url).post(body).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String head = response.toString();
                String str = response.body().string();
                 if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                     call.cancel();
                 }

            }
        });
    }



    /**
     * 使用okhttp3进行post的请求
     * @param url 请求接口
     * @param map 上传参数
     * @param handler handler
     * @param baseActivity activity
     */
    public void NewPost(String url, Map<String,String> map, final android.os.Handler handler, final BaseActivity baseActivity){
        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;
        FormBody.Builder builder = new FormBody.Builder();
        if(map!=null){
            for(Map.Entry<String,String> entry : map.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        body = builder.build();
        final Request request;
        if(Health_AppLocation.instance.Token == null){
            String token = Health_AppLocation.instance.
                    getSharedPreferences("loginToken", 0).
                    getString("token",null);
            if(token == null){
                Message message = new Message();
                message.what = Constant.UserErrorCode;
                handler.sendMessage(message);
                return;
            }else{
                request = new Request.Builder().addHeader("Authorization",token).url(url).post(body).build();
            }

        }else{
            request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).post(body).build();
        }

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String head = response.toString();
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }





    /**
     * 使用okhttp3进行post的请求
     * @param url 请求接口
     * @param map 上传参数
     * @param handler handler
     */
    public void NewPostToIm(String url, Map<String,String> map, final android.os.Handler handler, final FragmentActivity fragmentActivity){
        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;
        FormBody.Builder builder = new FormBody.Builder();
        if(map!=null){
            for(Map.Entry<String,String> entry : map.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        body = builder.build();
        final Request request;
        if(Health_AppLocation.instance.Token == null){
            String token = Health_AppLocation.instance.
                    getSharedPreferences("loginToken", 0).
                    getString("token",null);
            if(token == null){
                Message message = new Message();
                message.what = Constant.UserErrorCode;
                handler.sendMessage(message);
                return;
            }else{
                request = new Request.Builder().addHeader("Authorization",token).url(url).post(body).build();
            }

        }else{
            request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).post(body).build();
        }

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!fragmentActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String head = response.toString();
                String str = response.body().string();
                if(!fragmentActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }


    /**
     * 使用okhttp3进行（json作为参数）post的请求
     * @param url 请求接口
     * @param jsonObject json
     * @param handler handler
     * @param baseActivity activity
     */
    public void  JsonPost(String url, JSONObject jsonObject, final android.os.Handler handler, final BaseActivity baseActivity){
        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        body = RequestBody.create(JSON,String.valueOf(jsonObject));
        Request request = null;
        if(Health_AppLocation.instance.Token != null){
            request = new Request.Builder().
                    addHeader("Authorization", Health_AppLocation.instance.Token).
                    url(url).post(body).build();
        }else{
            Message message = new Message();
            message.what = Constant.UserErrorCode;
            handler.sendMessage(message);
            return;
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String head = response.toString();
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }



    /**
     * 使用okhttp3进行（json作为参数）post的请求
     * @param url 请求接口
     * @param json json
     * @param handler handler
     * @param baseActivity activity
     */
    public void  JsonPost(String url, String json, final android.os.Handler handler, final BaseActivity baseActivity){
        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        body = RequestBody.create(JSON,json);
        Request request = null;
        if(Health_AppLocation.instance.Token != null){
            request = new Request.Builder().
                    addHeader("Authorization", Health_AppLocation.instance.Token).
                    url(url).post(body).build();
        }else{
//            request = new Request.Builder().
//                    url(url).post(body).build();
            Message message = new Message();
            message.what = Constant.UserErrorCode;
            handler.sendMessage(message);
            return;
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String head = response.toString();
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }


    /**
     * 使用okhttp3进行（json作为参数）post的请求
     * @param url 请求接口
     * @param jsonObject json
     * @param handler handler
     * @param context context
     */
    public void  JsonPost(String url, JSONObject jsonObject, final android.os.Handler handler, final Context context){
        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        body = RequestBody.create(JSON,String.valueOf(jsonObject));
        Request request = null;
        if(Health_AppLocation.instance.Token != null){
            request = new Request.Builder().
                    addHeader("Authorization", Health_AppLocation.instance.Token).
                    url(url).post(body).build();
        }else{
            Message message = new Message();
            message.what = Constant.UserErrorCode;
            handler.sendMessage(message);
            return;
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 100;
                message.obj = "error";
                handler.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String head = response.toString();
                String str = response.body().string();

                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);


            }
        });
    }

    public void PostForService(String url, Map<String,String> map, final android.os.Handler handler){
        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;
        FormBody.Builder builder = new FormBody.Builder();
        if(map!=null){
            for(Map.Entry<String,String> entry : map.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        body = builder.build();
        final Request request = new Request.Builder().url(url).post(body).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String head = response.toString();
                String str = response.body().string();
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);


            }
        });
    }


    /**
     * 使用okhttp3进行post的请求(单张图片)
     * @param url 请求接口
     * @param handler handler
     * @param baseActivity activity
     */
    public void PostImage(String url, String filePath,final android.os.Handler handler, final BaseActivity baseActivity){

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        File file = new File(filePath);
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("file",file.getName(),RequestBody.create(MEDIA_TYPE_PNG, file));
        body = builder.build();

        final Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }

            }
        });
    }




    /**
     * 使用okhttp3进行post的请求(单张图片)
     * @param url 请求接口
     * @param handler handler
     * @param baseActivity activity
     */
    public void NewPostImage(String url, String filePath,final android.os.Handler handler, final BaseActivity baseActivity){

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        File file = new File(filePath);
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("img",file.getName(),RequestBody.create(MEDIA_TYPE_PNG, file));

        body = builder.build();

        if(Health_AppLocation.instance.Token == null){
            Message message = new Message();
            message.what = Constant.UserErrorCode;
            handler.sendMessage(message);
            return;
        }
        final Request request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).post(body).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }



    /**
     * 使用okhttp3进行post的请求(单张图片)并且带其他参数
     * @param url 请求接口
     * @param handler handler
     * @param baseActivity activity
     */
    public void NewPostImage2(String url, String filePath,Map<String,String> map,final android.os.Handler handler, final BaseActivity baseActivity){

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        File file = new File(filePath);
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("img",file.getName(),RequestBody.create(MEDIA_TYPE_PNG, file));

        if(map.size()>0){
            for (Map.Entry<String,String> entry : map.entrySet()){
                builder.addFormDataPart(entry.getKey(),entry.getValue());
            }
        }

        body = builder.build();

        final Request request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }



    /**
     * 使用okhttp3进行post的请求(带多张图片)
     * @param url 请求接口
     * @param map 上传参数
     * @param handler handler
     * @param baseActivity activity
     */
    public void Post_imgs(String url, Map<String,String> map, List<LocalMedia> list,final android.os.Handler handler, final BaseActivity baseActivity){

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        if(okHttpClient == null){
            okHttpClient = genericClient();
        }
        RequestBody body = null;

         MultipartBody.Builder builder = new MultipartBody.Builder();
//        FormBody.Builder builder = new FormBody.Builder();
        builder.setType(MultipartBody.FORM);
        for(Map.Entry<String,String> entry : map.entrySet()){
            builder.addFormDataPart(entry.getKey(),entry.getValue());
        }
        if(list!=null && list.size()>0){
            for (int i = 0;i<list.size();i++){
                File file = new File(list.get(i).getCompressPath());
                builder.addFormDataPart("img",file.getName(),RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        body = builder.build();

        final Request request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).post(body).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = 100;
                    message.obj = "error";
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }

            }
        });
    }



    /**
     * 使用okhttp3进行Get的请求
     * @param url 请求接口
     * @param haveToken 是否需要添加token
     * @param handler handler
     * @param baseActivity activity
     */
    public static void Get(String url,boolean haveToken,final android.os.Handler handler, final BaseActivity baseActivity){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        Request request = null;
        if(haveToken){
            if(Health_AppLocation.instance.Token == null){
                Message message = new Message();
                message.what = Constant.UserErrorCode;
                handler.sendMessage(message);
                return;


            }else{
                request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).build();
            }
        }else{
                request = new Request.Builder().url(url).build();

        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 100;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    call.cancel();
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }


    /**
     * 使用okhttp3进行Get的请求
     * @param url 请求接口
     * @param haveToken 是否需要添加token
     * @param handler handler
     * @param baseActivity activity
     */
    public static void Delete(String url,boolean haveToken,final android.os.Handler handler, final BaseActivity baseActivity){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        Request request = null;
        if(haveToken){
            if(Health_AppLocation.instance.Token == null){
                Message message = new Message();
                message.what = Constant.UserErrorCode;
                handler.sendMessage(message);
                return;


            }else{
                request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).delete().build();
            }
        }else{
            request = new Request.Builder().url(url).delete().build();

        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 100;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!baseActivity.isDestroyed()){
                    call.cancel();
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }


    /**
     * 使用okhttp3进行Get的请求
     * @param url 请求接口
     * @param haveToken 是否需要添加token
     * @param handler handler
     */
    public static void Delete(String url,boolean haveToken,final android.os.Handler handler, final FragmentActivity fragmentActivity){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        Request request = null;
        if(haveToken){
            if(Health_AppLocation.instance.Token == null){
                Message message = new Message();
                message.what = Constant.UserErrorCode;
                handler.sendMessage(message);
                return;


            }else{
                request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).delete().build();
            }
        }else{
            request = new Request.Builder().url(url).delete().build();

        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 100;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!fragmentActivity.isDestroyed()){
                    call.cancel();
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }


    /**
     * 使用okhttp3进行Get的请求
     * @param url 请求接口
     * @param haveToken 是否需要添加token
     * @param handler handler
     */
    public static void Get(String url,boolean haveToken,final android.os.Handler handler, final FragmentActivity fragmentActivity){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        Request request = null;
        if(haveToken){
            if(Health_AppLocation.instance.Token == null){
                Message message = new Message();
                message.what = Constant.UserErrorCode;
                handler.sendMessage(message);
                return;


            }else{
                request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).build();
            }
        }else{
            request = new Request.Builder().url(url).build();

        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 100;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(!fragmentActivity.isDestroyed()){
                    call.cancel();
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = str;
                    handler.sendMessage(message);
                }else{
                    call.cancel();
                }

            }
        });
    }


    /**
     * 使用okhttp3进行Get的请求
     * @param url 请求接口
     * @param haveToken 是否需要添加token
     * @param handler handler
     */
    public static void Get(String url, boolean haveToken, final android.os.Handler handler, final Context context){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        Request request = null;
        if(haveToken){
            if(Health_AppLocation.instance.Token == null){
                Message message = new Message();
                message.what = Constant.UserErrorCode;
                handler.sendMessage(message);
                return;


            }else{
                request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).build();
            }
        }else{
            request = new Request.Builder().url(url).build();

        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 100;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                call.cancel();
                Message message = new Message();
                message.what = response.code();
                message.obj = str;
                handler.sendMessage(message);

            }
        });
    }



    /**
     * 使用okhttp3进行Get的请求
     * @param url 请求接口
     * @param haveToken 是否需要添加token
     * @param handler handler
     * @param baseActivity activity
     */
    public static void ByteGet(String url, final int position, boolean haveToken, final android.os.Handler handler, final BaseActivity baseActivity){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        Request request = null;
        final String[] jsons = {null};
        if(haveToken){
            if(Health_AppLocation.instance.Token == null){

            }else{
                request = new Request.Builder().addHeader("Authorization",Health_AppLocation.instance.Token).url(url).build();
            }
        }else{
            request = new Request.Builder().url(url).build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                jsons[0] = "";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String json = StringUtil.StrToBinstr(response.body().string());
                byte b [] = response.body().bytes();
               String json = new String(android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT));
                if(!baseActivity.isDestroyed()){
                    Message message = new Message();
                    message.what = response.code();
                    message.obj = json;
                    message.arg1 = position;
                    handler.sendMessage(message);
                }
            }
        });
    }





    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                                .addHeader("Accept-Encoding", "gzip, deflate")
//                                .addHeader("Connection", "keep-alive")
//                                .addHeader("Accept", "*/*")
//                                .addHeader("Set-Cookie", "token=290ae3849d5d050e8d23c86fc08e4c5e; Max-Age=7200; Expires=Mon, 21-May-2018 11:15:39 GMT; Path=/")
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Set-Cookie", "token=290ae3849d5d050e8d23c86fc08e4c5e; Max-Age=7200; Expires=Mon, 21-May-2018 11:15:39 GMT; Path=/")
                                .addHeader("User-Agent","Mozila/4.0(compatible;MSIE5.01;Window NT5.0)")
                                .build();
                        return chain.proceed(request);
                    }

                })
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }


    public static OkHttpClient genericClientForImg() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                                .addHeader("Accept-Encoding", "gzip, deflate")
//                                .addHeader("Connection", "keep-alive")
//                                .addHeader("Accept", "*/*")
//                                .addHeader("Set-Cookie", "token=290ae3849d5d050e8d23c86fc08e4c5e; Max-Age=7200; Expires=Mon, 21-May-2018 11:15:39 GMT; Path=/")
//                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Set-Cookie", "token=290ae3849d5d050e8d23c86fc08e4c5e; Max-Age=7200; Expires=Mon, 21-May-2018 11:15:39 GMT; Path=/")
                                .addHeader("User-Agent","Mozila/4.0(compatible;MSIE5.01;Window NT5.0)")
                                .build();
                        return chain.proceed(request);
                    }

                })
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }


    /**
     * 对于gzip格式数据编码的解码处理
     * @param is 流对象
     * @return 把返回数据转换成字符串类型
     * @throws IOException
     */
    private String zipInputStream(InputStream is) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(is);
        BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null)
            buffer.append(line + "\n");
        is.close();
        return buffer.toString();
    }

    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes("UTF-8"));
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("UTF-8");
    }


    public String getLocalIpAddress() {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
//            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

}
