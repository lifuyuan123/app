package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import azsecuer.zhuoxin.com.app.Fragment.BaiduFragment;
import azsecuer.zhuoxin.com.app.Fragment.HotFragment;
import azsecuer.zhuoxin.com.app.Fragment.NewsFragment;
import azsecuer.zhuoxin.com.app.Fragment.SerchFragment;
import azsecuer.zhuoxin.com.app.Fragment.VedioplayerFrgment;
import azsecuer.zhuoxin.com.app.Info.User;
import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.MyImage.MyImage;
import azsecuer.zhuoxin.com.app.MyNotification.MyNotifiction;
import azsecuer.zhuoxin.com.app.MySQL.MyHelperland;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.Main_toolbar)
    Toolbar MainToolbar;
    @BindView(R.id.Main_framelayout)
    FrameLayout MainFramelayout;
    @BindView(R.id.radionews)
    RadioButton radionews;
    @BindView(R.id.radiohot)
    RadioButton radiohot;
//    @BindView(R.id.radioserch)
//    RadioButton radioserch;
    @BindView(R.id.radiomap)
    RadioButton radiomap;
//    @BindView(R.id.radioqq)
//    RadioButton radioqq;
    @BindView(R.id.navigationview)
    NavigationView navigationview;
    @BindView(R.id.Main_drawerlayout)
    DrawerLayout MainDrawerlayout;
    MyImage head_Icon;
    TextView headText;
    private View view;//头布局
    private SharedpreferencesUtil sharedpreferencesUtil;
    private boolean tongzhi;
    private boolean b=false ;//判断登陆的布尔值
    private String name;//登陆获取的name
    private MyHelperland myHelperland;


    //将activit和fragment关联的管理类
    private FragmentManager fragmentmanager;
    private FragmentTransaction transaction;
    private Fragment[] fragment=new Fragment[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferencesUtil=new SharedpreferencesUtil(this);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this,"16cd1856c5f1e");
        setdata();
        radionews.setChecked(true);
        choiceFragment(0);
        landing();

    }

    private void landing() {
        myHelperland=new MyHelperland(this);
        name=getIntent().getStringExtra("name");
        b= getSharedPreferences("landing",MODE_PRIVATE).getBoolean("boolean",false);
        if(b){
            headText.setText(name+"(已登陆）");
            head_Icon.setImageResource(R.drawable.app);
        }
    }

    private void setdata() {
        //判断通知按钮是否未开  显示通知布局
        tongzhi= sharedpreferencesUtil.gettong();
        if(tongzhi){
            MyNotifiction.openNotification(this);
        }
        //得到碎片管理类
        fragmentmanager=getSupportFragmentManager();
        transaction=fragmentmanager.beginTransaction();
        setSupportActionBar(MainToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar返回侧滑
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, MainDrawerlayout, MainToolbar,
                R.string.app_name, R.string.app_name);
        toggle.syncState();//同步状态
        MainDrawerlayout.addDrawerListener(toggle);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.a:
                        Intent intent1=new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intent1);
                        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.d:
                        Toast.makeText(MainActivity.this, "我的收藏", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,ShouActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.s:
                        Toast.makeText(MainActivity.this, "关于我们", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.out:
                        SharedPreferences sharedPreferences=getSharedPreferences("landing",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean("boolean",false);
                        editor.commit();
                        //退出登陆后istrue设置为false
                        User user=myHelperland.chaUser(name);
                        user.setIstrue(false);
                        myHelperland.changeUseristure(name,user);

                        Intent intent3=new Intent(MainActivity.this,LandActivity.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
                return true;
            }
        });

        //侧滑中的头布局
        view = navigationview.inflateHeaderView(R.layout.headview);
        headText= (TextView)view.findViewById(R.id.head_text);
        head_Icon= (MyImage) view.findViewById(R.id.head_Icon);
        headText.setOnClickListener(this);
        head_Icon.setOnClickListener(this);
        radionews.setOnClickListener(this);
        radiohot.setOnClickListener(this);
//        radioserch.setOnClickListener(this);
//        radioqq.setOnClickListener(this);
        radiomap.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
           case R.id.share:
               showShare();
               break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_text:
                Toast.makeText(MainActivity.this, "登陆", Toast.LENGTH_SHORT).show();
                if(!b){
                    Intent intent=new Intent(this,LandActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.head_Icon:
                Toast.makeText(MainActivity.this, "头像", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(MainActivity.this,UserActivity.class);
                i.putExtra("name",name);
                Log.i("111111name",name);
                startActivity(i);
                finish();
                break;
            case R.id.radionews:
                Toast.makeText(MainActivity.this, "radionews", Toast.LENGTH_SHORT).show();
                choiceFragment(0);
                break;
            case R.id.radiohot:
                Toast.makeText(MainActivity.this, "radioserch", Toast.LENGTH_SHORT).show();
                choiceFragment(1);
                break;
//            case R.id.radioserch:
//                Toast.makeText(MainActivity.this, "radiohot", Toast.LENGTH_SHORT).show();
//                choiceFragment(2);
//                break;
//            case R.id.radioqq:
//                Toast.makeText(MainActivity.this, "radioqq", Toast.LENGTH_SHORT).show();
//                choiceFragment(4);
//                break;
            case R.id.radiomap:
                Toast.makeText(MainActivity.this, "radiomap", Toast.LENGTH_SHORT).show();
                choiceFragment(3);
                break;
        }
    }
    private void choiceFragment(int i){
        hideAllFragment();
        transaction=fragmentmanager.beginTransaction();
        if(fragment[i]==null){
           switch (i){
               case 0:
                   fragment[i]=new NewsFragment();
                   transaction.add(R.id.Main_framelayout,fragment[i]);
                   break;
               case 1:
                   fragment[i]=new SerchFragment();
                   transaction.add(R.id.Main_framelayout,fragment[i]);
                   break;
               case 2:
                   fragment[i]=new HotFragment();
                   transaction.add(R.id.Main_framelayout,fragment[i]);
                   break;
               case 3:
                   fragment[i]=new BaiduFragment();
                   transaction.add(R.id.Main_framelayout,fragment[i]);
                   break;
               //视频播放
//               case 4:
//                   fragment[i]=new VedioplayerFrgment();
//                   transaction.add(R.id.Main_framelayout,fragment[i]);
//                   break;

           }
        }else {
            transaction.show(fragment[i]);
        }
        transaction.commit();
    }

    private void hideAllFragment() {
        transaction=fragmentmanager.beginTransaction();

        for(int i=0;i<fragment.length;i++){
            //判断不为空防止报空指针
            if(fragment[i]!=null){
                transaction.hide(fragment[i]);
            }
        }
        transaction.commit();
    }

//分享的方法
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
// 启动分享GUI
        oks.show(this);
    }


//加入以下代码  在这个页面状态按下Backspace键app则会finish
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        finish();
//        return super.onKeyDown(keyCode, event);
//
//    }
}
