package azsecuer.zhuoxin.com.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipError;

import azsecuer.zhuoxin.com.app.Activity.BNDemoGuideActivity;
import azsecuer.zhuoxin.com.app.Info.Data;
import azsecuer.zhuoxin.com.app.MyOrientationListener.MyOrientationListener;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baidu.navisdk.adapter.PackageUtil.getSdcardDir;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by Administrator on 2017/2/2.
 */

public class BaiduFragment extends Fragment {
    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.id_info_img)
    ImageView idInfoImg;
    @BindView(R.id.id_info_name)
    TextView idInfoName;
    @BindView(R.id.id_info_distance)
    TextView idInfoDistance;
    @BindView(R.id.id_info_zan)
    TextView idInfoZan;
    @BindView(R.id.id_maker_ly)
    RelativeLayout idMakerLy;
    @BindView(R.id.iv_heart)
    ImageView ivHeart;
    private BaiduMap baiduMap;
    private ArrayList<String> strings;
    private ArrayAdapter<String> arrayAdapter;
    private PopupWindow popupWindow;
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    private String mSDCardPath = null;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static List<Activity> activityList = new LinkedList<Activity>();
    //定位相关
    private LocationClient locationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirst = true;
    private double latitude, longitude;
    private MyOrientationListener myOrientationListener;
    private float myX;
    private MyLocationConfiguration.LocationMode locationMode;//地图模式
    private LatLng mDestlacationData;//终点坐标
    private LatLng latLng;//我的位置坐标
    //自定义图标
    private BitmapDescriptor bitmap;

    //覆盖物相关
    private BitmapDescriptor mMarker;
    //设置覆盖物上的text（地址）
    private InfoWindow infoWindows;
    private int a = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//设置后才会有menu
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化SDK引用的Context 全局变量
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_baidumap, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initdata();
        initview();
        //初始化定位
//        initLocation();
        //判断是否是6.0及以上，需要手动申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                //上面已经写好的拨号方法
                initLocation();
            }
        } else {
            //上面已经写好的拨号方法
            initLocation();
        }
        //覆盖物相关
        initmarker();
        //点击marke显示图片信息的监听
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //获取设置的info
                Bundle bundle = marker.getExtraInfo();
                final Data data = (Data) bundle.getSerializable("data");
                if(data.getZan()!=0){
                    a = data.getZan();
                    idInfoImg.setImageResource(data.getImgId());
                    idInfoName.setText(data.getName());
                    idInfoZan.setText(data.getZan() + "");
                    idInfoDistance.setText(data.getDistance());

                    TextView t = new TextView(getContext());
                    t.setBackgroundResource(R.drawable.location_tips);
                    t.setGravity(Gravity.CENTER);//设置文字居中
                    t.setPadding(30, 20, 30, 50);
                    t.setText(data.getName());
                    t.setTextColor(Color.parseColor("#ffffff"));
                    final LatLng latLng = marker.getPosition();//获取经纬度
                    //转化为点point
                    Point point = baiduMap.getProjection().toScreenLocation(latLng);
                    point.y -= 50;
                    point.x-=12;
                    LatLng ll = baiduMap.getProjection().fromScreenLocation(point);
                    idMakerLy.setVisibility(View.VISIBLE);
                    infoWindows = new InfoWindow(t, ll, 0);
                    baiduMap.showInfoWindow(infoWindows);
                    //点赞的监听
                    ivHeart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ++a;
                            idInfoZan.setText(a + "");
                            data.setZan(a);
                        }
                    });
                }
                return true;
            }
        });
        //点击地图的时候marke消失
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                idMakerLy.setVisibility(View.GONE);
                baiduMap.hideInfoWindow();//点击地图infowindows消失
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        //设置目的地的长按的监听
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mDestlacationData=latLng;//终点坐标赋值
                Toast.makeText(getContext(), "获取终点位置成功", Toast.LENGTH_SHORT).show();
                //设置图标
                baiduMap.clear();//清除图层
                OverlayOptions option=new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.maker))
                        .zIndex(5);
               Marker marker= (Marker) baiduMap.addOverlay(option);

                //通过bundle传data过去
                Bundle bundle = new Bundle();
                Data data=new Data(latLng.latitude,latLng.longitude,0,"","",0);
                bundle.putSerializable("data", data);
                marker.setExtraInfo(bundle);
            }
        });

        //初始化导航相关
        if (initDirs()) {
            Log.i("1111111111111","fsdfsdfasdfasdf");
            initNavi();
            Log.i("1111111111111","fsdfsdfasdfasdfsdfsadf");
        }
    }

    private void initmarker() {
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);


    }

    //手动申请权限需要重写此方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    initLocation();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "CALL_PHONE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    //定位的方法
    private void initLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        locationClient = new LocationClient(getContext());
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);

        locationClient.setLocOption(option);
        //定位图标
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        myOrientationListener = new MyOrientationListener(getContext());
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void OnOrientationChanged(float x) {
                myX = x;
            }
        });
    }

    private void initdata() {
        strings = new ArrayList<>();
        strings.add("普通地图");
        strings.add("卫星地图");
        strings.add("实时交通（off）");
        strings.add("我的位置");
        strings.add("普通模式");
        strings.add("跟随模式");
        strings.add("罗盘模式");
        strings.add("添加覆盖物");
        strings.add("模拟导航");
        strings.add("开始导航");
        strings.add("清除覆盖物");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, strings);
    }

    private void initview() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null) {
                    showpopwindow();
                } else {
                    closepopwindows();
                }
            }
        });
        //初始化百度地图
        baiduMap = bmapView.getMap();
        //设置显示的比列大小
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        baiduMap.setMapStatus(msu);//比列大小设置上去
    }

    //地图的生命周起与app同步


    @Override
    public void onStart() {
        super.onStart();
        baiduMap.setMyLocationEnabled(true);
        if (!locationClient.isStarted())
            locationClient.start();//开启定位
        //开启方向传感器
        myOrientationListener.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止定位
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        //停止方向传感器
        myOrientationListener.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
    }

    //显示popowindows
    private void showpopwindow() {
        String s = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater1;
        layoutInflater1 = (LayoutInflater) getContext().getSystemService(s);
        View view1 = layoutInflater1.inflate(R.layout.windows, null, false);
        ListView listView = (ListView) view1.findViewById(R.id.list);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                baiduMap.showInfoWindow(infoWindows);
                idMakerLy.setVisibility(View.GONE);
                if (((TextView) view).getText().equals("普通地图")) {
                    Toast.makeText(getContext(), "普通", Toast.LENGTH_SHORT).show();
                    //普通
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                } else if (((TextView) view).getText().equals("卫星地图")) {
                    Toast.makeText(getContext(), "卫星", Toast.LENGTH_SHORT).show();
                    //卫星
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                } else if (((TextView) view).getText().equals("实时交通（off）")
                        || ((TextView) view).getText().equals("实时交通（on）")) {
                    if (baiduMap.isTrafficEnabled()) {
                        baiduMap.setTrafficEnabled(false);
                        ((TextView) view).setText("实时交通（off）");
                    } else {
                        baiduMap.setTrafficEnabled(true);
                        ((TextView) view).setText("实时交通（on）");
                    }
                } else if (((TextView) view).getText().equals("我的位置")) {
                    LatLng latLng = new LatLng(latitude, longitude);
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(msu);
                } else if (((TextView) view).getText().equals("添加覆盖物")) {
                    //添加覆盖物集合的方法
                    addOverlays(Data.infos);
                } else if (((TextView) view).getText().equals("普通模式")) {
                    locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                } else if (((TextView) view).getText().equals("跟随模式")) {
                    locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                } else if (((TextView) view).getText().equals("罗盘模式")) {
                    locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                }else if (((TextView) view).getText().equals("清除覆盖物")) {
                    baiduMap.clear();
                }
                else if (((TextView) view).getText().equals("模拟导航")) {
                    Toast.makeText(getContext(), "模拟导航", Toast.LENGTH_SHORT).show();
                    if(mDestlacationData==null){
                        Toast.makeText(getContext(), "长按地图设置终点", Toast.LENGTH_SHORT).show();
                    }else {
                    if (BaiduNaviManager.isNaviInited()) {
                        routeplanToNavi(false);
                    }
                    }
//                    routeplanToNavi(false);
                }else if (((TextView) view).getText().equals("开始导航")) {
                    Toast.makeText(getContext(), "开始导航", Toast.LENGTH_SHORT).show();
                    if(mDestlacationData==null){
                        Toast.makeText(getContext(), "长按地图设置终点", Toast.LENGTH_SHORT).show();
                    }else {
                        if (BaiduNaviManager.isNaviInited()) {
                            routeplanToNavi(true);
                        }
                    }
//                    routeplanToNavi(true);
                }
            }
        });


        popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorAccent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(button.getWidth());//获取editext的宽
        //---------------------------点击空白除消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        //------------------------------
        popupWindow.showAsDropDown(getView(), 900, -1400);


    }

    private void addOverlays(List<Data> infos) {
        baiduMap.clear();//清理图层
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        for (Data data : infos) {
            //获取经纬度
            latLng = new LatLng(data.getLatitude(), data.getLongitude());
            //图标位置
            options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
            marker = (Marker) baiduMap.addOverlay(options);
            //通过bundle传data过去
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            marker.setExtraInfo(bundle);
        }
        //移动道最后一个marker的地方
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(msu);
    }

    //关闭popowindows
    private void closepopwindows() {
        popupWindow.dismiss();
        popupWindow = null;
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData data = new MyLocationData.Builder()//
                    .direction(myX)       //设置方向
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            baiduMap.setMyLocationData(data);

            //设置图标
            MyLocationConfiguration config = new MyLocationConfiguration(locationMode,//变量  地图模式
                    true, bitmap);
            baiduMap.setMyLocationConfigeration(config);

            //记录最新的经纬度
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (isFirst) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
                isFirst = false;
                Toast.makeText(getContext(), location.getAddrStr() + "", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;
    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getContext(), authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(getContext(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
            }

            public void initStart() {
                Toast.makeText(getContext(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(getContext(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }


        },  null, ttsHandler, ttsPlayStateListener);

    }
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi(boolean open) {//open决定是虚拟导航还是真实的导航
        BNRoutePlanNode.CoordinateType coType= BNRoutePlanNode.CoordinateType.GCJ02;
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
//        sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);//开始位置
//        eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);//终点位置
//        坐标的转换
        BDLocation b=new BDLocation();
        b.setLatitude(latLng.latitude);
        b.setLongitude(latLng.longitude);
        double slong = LocationClient.getBDLocationInCoorType(b, BDLocation.BDLOCATION_BD09LL_TO_GCJ02).getLongitude();
        double slati = LocationClient.getBDLocationInCoorType(b, BDLocation.BDLOCATION_BD09LL_TO_GCJ02).getLatitude();
//        坐标的转换
        BDLocation b2=new BDLocation();
        b2.setLatitude(mDestlacationData.latitude);
        b2.setLongitude(mDestlacationData.longitude);
        double elong = LocationClient.getBDLocationInCoorType(b2, BDLocation.BDLOCATION_BD09LL_TO_GCJ02).getLongitude();
        double elati = LocationClient.getBDLocationInCoorType(b2, BDLocation.BDLOCATION_BD09LL_TO_GCJ02).getLatitude();

        sNode=new BNRoutePlanNode(slong,slati,"我的位置",null,coType);
        eNode=new BNRoutePlanNode(elong,elati,"目标位置",null,coType);

        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1, open, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(getActivity(), BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(getContext(), "算路失败", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default :
                    break;
            }
        }
    };
    public void showToastMsg(final String msg) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    private void initSetting(){
        // 设置是否双屏显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        // 设置导航播报模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 是否开启路况
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

}
