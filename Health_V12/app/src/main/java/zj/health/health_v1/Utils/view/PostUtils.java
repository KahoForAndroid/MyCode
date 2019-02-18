package zj.health.health_v1.Utils.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Utils.HttpUtil;

/**
 * Created by Administrator on 2018/6/1.
 */

public class PostUtils {

    public void getPost(final String postName, final Map<String,String> map, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().Post(Constant.ip+postName,map,handler,baseActivity);
            }
        }).start();
    }


    public void getNewPost(final String postName, final Map<String,String> map, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().NewPost(Constant.postIP+postName,map,handler,baseActivity);
            }
        }).start();
    }

    public void getNewPostIM(final String postName, final Map<String,String> map, final Handler handler, final FragmentActivity fragmentActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().NewPostToIm(Constant.postIP+postName,map,handler,fragmentActivity);
            }
        }).start();
    }



    public void RegisterPost(final String postName, final JSONObject jsonObject, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().JsonPost(Constant.postIP+postName,jsonObject,handler,baseActivity);
            }
        }).start();
    }

    public void JsonPost(final String postName, final JSONObject jsonObject, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().JsonPost(Constant.postIP+postName,jsonObject,handler,baseActivity);
            }
        }).start();
    }

    public void JsonPost(final String postName, final JSONObject jsonObject, final Handler handler, final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().JsonPost(Constant.postIP+postName,jsonObject,handler,context);
            }
        }).start();
    }

    public void JsonPost(final String postName, final String json, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().JsonPost(Constant.postIP+postName,json,handler,baseActivity);
            }
        }).start();
    }

    public void Get(final String postName, final boolean haveToken, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().Get(Constant.postIP+postName,haveToken,handler,baseActivity);
            }
        }).start();
    }
    public void Get(final String postName, final boolean haveToken, final Handler handler, final FragmentActivity fragmentActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().Get(Constant.postIP+postName,haveToken,handler,fragmentActivity);
            }
        }).start();
    }
    public void Get(final String postName, final boolean haveToken, final Handler handler, final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().Get(Constant.postIP+postName,haveToken,handler,context);
            }
        }).start();
    }
    public void Delete(final String postName, final boolean haveToken, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().Delete(Constant.postIP+postName,haveToken,handler,baseActivity);
            }
        }).start();
    }
    public void Delete(final String postName, final boolean haveToken, final Handler handler, final FragmentActivity fragmentActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().Delete(Constant.postIP+postName,haveToken,handler,fragmentActivity);
            }
        }).start();
    }

    public void ByteGet(final String postName, final int position, final boolean haveToken, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().ByteGet(Constant.postIP+postName,position,haveToken,handler,baseActivity);
            }
        }).start();
    }

    public void getPostToService(final String postName, final Map<String,String> map, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().PostForService(Constant.ip+postName,map,handler);
            }
        }).start();
    }

    public void getPost_img(final String postName, final Map<String,String> map, final List<LocalMedia> list, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().Post_imgs(Constant.postIP+postName,map,list,handler,baseActivity);
            }
        }).start();
    }

    public void UploadImage(final String Path,final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().PostImage(Constant.ip+"api/upload?",Path,handler,baseActivity);
            }
        }).start();
    }


    /**
     * 上传头像
     * @param Path 图片路径
     * @param handler 上传之后通知handler
     * @param baseActivity 上下文
     */
    public void NewUploadImage(final String postName, final String Path, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().NewPostImage(Constant.postIP+postName,Path,handler,baseActivity);
            }
        }).start();
    }

    /**
     * 上传头像
     * @param Path 图片路径
     * @param handler 上传之后通知handler
     * @param baseActivity 上下文
     */
    public void NewUploadImage2(final String postName, final String Path, final Map<String,String> map, final Handler handler, final BaseActivity baseActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtil().NewPostImage2(Constant.postIP+postName,Path,map,handler,baseActivity);
            }
        }).start();
    }


}
