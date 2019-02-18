package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/11.
 */

public class WebActivity extends BaseActivity{

    private WebView webview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
//        webview = (WebView)findViewById(R.id.webview);
//        webview.loadUrl("file:///android_asset/web/登录页面.html");
//        WebSettings settings = webview.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webview.getSettings().setLoadWithOverviewMode(true);
    }
}
