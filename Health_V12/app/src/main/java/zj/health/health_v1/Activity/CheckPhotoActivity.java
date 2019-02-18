package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.photoview.PhotoView;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.MyView.BaseDragZoomImageView;
import zj.health.health_v1.MyView.ZoomImageView;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.GlideCircleTransform;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/10/17.
 */

public class CheckPhotoActivity extends BaseActivity{

    private ImageView back;
    private PhotoView photo_img;
    private TextView title;
    private RequestOptions requestOptions ;
    public static float ScreenW;//屏幕的宽
    public static float ScreenH;//屏幕的高

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkphoto_activity);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.place_photo) //加载中图片
                .error(R.drawable.place_photo) //加载失败图片
                .fallback(R.drawable.place_photo) //url为空图片
                .centerCrop()
//                .transform(new GlideCircleTransform(this))
                // 填充方式
//            .override(600,600) //尺寸
//            .transform(new CircleCrop()) //圆角
//            .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE); //缓存策略
        back = (ImageView)findViewById(R.id.back);
        photo_img = (PhotoView)findViewById(R.id.photo_img);
        title = (TextView)findViewById(R.id.title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(getString(R.string.checkPhoto));
        String url = getIntent().getStringExtra("url");
        Glide.with(this).
                load(Constant.photo_IP+url).
                apply(requestOptions).
                into(photo_img);
    }



}
