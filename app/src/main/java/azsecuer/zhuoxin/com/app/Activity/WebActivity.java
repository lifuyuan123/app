package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import azsecuer.zhuoxin.com.app.Info.Bean.ShowapiResBodyBean.PagebeanBean.ContentlistBean;
import azsecuer.zhuoxin.com.app.Info.Info;
import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.MySQL.MyHelper;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.activity_web)
    LinearLayout activityWeb;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progress;
//    @BindView(R.id.web_linear)
//    LinearLayout webLinear;
    private String url;
    private boolean needupdata = false;
    private int data = 1;
    private MyHelper myhelper;
    private List<Info> lists;
    private int type;
    private ContentlistBean content;
    private SharedpreferencesUtil s;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress.setProgress(msg.arg1);
//            if(msg.arg1>50){
//                webLinear.setVisibility(View.GONE);
//                webview.setVisibility(View.VISIBLE);
//            }
            if (msg.arg1 == 100) {
                progress.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        s = new SharedpreferencesUtil(this);
        myhelper = new MyHelper(this);
        lists = myhelper.getAlldata();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("1111s.getdata()", "" + s.getdata() + "ssss" + data);
                choices();
                finish();
            }
        });


        url = getIntent().getStringExtra("url");
        webview.loadUrl(url);
        WebSettings webset = webview.getSettings();//获取设置相关
        webset.setUseWideViewPort(true);//设置此属性，可任意比例缩放 //将图片调整到适合webview的大小
        webset.setLoadWithOverviewMode(true);
        //使页面支持缩放
        webset.setJavaScriptEnabled(true);//支持js
        webset.setBuiltInZoomControls(true);
        webset.setSupportZoom(true);//支持缩放
        //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点。
        webview.requestFocusFromTouch();
        //设置webview的进度progress
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Message message = new Message();
                message.arg1 = newProgress;
                handler.sendMessage(message);
            }
        });

        //帮助WebView处理各种通知、请求事件的：//防止跳到其他页面，强制在webview中打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    //显示右上角收藏图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoucang, menu);
        Log.i("111111click", "取的是" + data);
        //判断是否被收藏过
        if (myhelper.getdata(url).size() != 0) {
            menu.getItem(0).setIcon(R.drawable.star_selected);
            type = 2;
            data = 2;
        } else {
            menu.getItem(0).setIcon(R.drawable.star_defult);
            type = 1;
            data = 1;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (type) {
            case 1:
                if (data == 2) {
                    item.setIcon(R.drawable.star_defult);
                    Log.i("111111选中", "" + data);
                    Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
                    data = 1;
                } else if (data == 1) {
                    item.setIcon(R.drawable.star_selected);
                    Log.i("111111未选中", "" + data);
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                    data = 2;
                }
                break;
            case 2:
                if (data == 1) {
                    item.setIcon(R.drawable.star_selected);
                    Log.i("111111未选中type2", "" + data);
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                    data = 2;
                } else if (data == 2) {
                    item.setIcon(R.drawable.star_defult);
                    Log.i("111111选中type2", "" + data);
                    Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
                    data = 1;
                }

                break;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            webview.goBack();
            return true;
        } else {
            Log.i("1111s.getdata()", "" + s.getdata() + "ssss" + data);
            choices();//选择是否收藏和删除
            finish();
            return true;
        }
    }

    //选择是否收藏和删除
    private void choices() {
        switch (type) {
            case 1:
                if (data != 1) {
                    Log.i("111111type1跳转", "" + data);
                    Intent i = new Intent();
                    i.putExtra("needupdata", true);
                    setResult(101, i);
                }
                break;
            case 2:
                if (data != 2) {
                    Log.i("111111type2跳转", "" + data);
                    Intent i = new Intent();
                    i.putExtra("needupdata", true);
                    setResult(101, i);
                }
                break;
        }
    }
}
