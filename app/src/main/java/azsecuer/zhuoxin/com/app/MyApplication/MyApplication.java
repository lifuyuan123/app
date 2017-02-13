package azsecuer.zhuoxin.com.app.MyApplication;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;

/**
 * Created by Administrator on 2016/12/26.
 */

public class MyApplication extends Application {
    public static Gson gson;
    public static RequestQueue requestQueue;

    public static Gson getGson() {
        return gson;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gson=new Gson();
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480,800)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY-1)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
//                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(50)
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())// default
//                .imageDownloader(new BaseImageDownloader(this))//default
                .imageDecoder(new BaseImageDecoder(true))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())// default
                .writeDebugLogs()
                .build();
        // 2.将配置信息给予我们的ImageLoader对象
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);

        //百度地图
        //初始化SDK引用的Context 全局变量
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ApiStoreSDK.init(this,"cb5e909074633518f2c4a9a75a9f9518");
        requestQueue.cancelAll(this);//停止所有请求
    }
}
