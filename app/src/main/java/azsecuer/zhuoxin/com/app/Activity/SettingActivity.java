package azsecuer.zhuoxin.com.app.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.MyBroadcastReceiver.MyBroadcastReciver;
import azsecuer.zhuoxin.com.app.MyNotification.MyNotifiction;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.seting_toolbar)
    Toolbar toolbar;
    @BindView(R.id.open_tog)
    ToggleButton openTog;
    @BindView(R.id.tongzhi)
    ToggleButton tongzhi;
    private SharedpreferencesUtil sharedpreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sharedpreferencesUtil=new SharedpreferencesUtil(this);
        tongzhi.setChecked(sharedpreferencesUtil.gettong());
        openTog.setChecked(sharedpreferencesUtil.getopen());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        openTog.setOnCheckedChangeListener(this);
        tongzhi.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
       switch (compoundButton.getId()){
           case R.id.tongzhi:
               sharedpreferencesUtil.savetong(b);//存储boolean
              if(b){
                  MyNotifiction.openNotification(this);
                  Toast.makeText(this, "显示通知栏", Toast.LENGTH_SHORT).show();
              }else {
                  MyNotifiction.closeNotification(this);
                  Toast.makeText(this, "关闭通知栏", Toast.LENGTH_SHORT).show();
              }
               break;
           case R.id.open_tog:
               sharedpreferencesUtil.saveopen(b);//存储boolean
               if(b){
                   Toast.makeText(this, "开机自启", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(this, "取消开机自启", Toast.LENGTH_SHORT).show();
               }
               break;
       }
    }
}
