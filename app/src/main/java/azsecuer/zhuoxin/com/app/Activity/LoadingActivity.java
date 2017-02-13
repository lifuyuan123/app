package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends BaseActivity {
    private Animation animation;
    private SharedpreferencesUtil sharedpreferencesUtil;
    @BindView(R.id.loading_iv)
    ImageView loadingIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //判断首次进入或者更新后进入是否跳进引导页面
        sharedpreferencesUtil=new SharedpreferencesUtil(this);
        String name=sharedpreferencesUtil.getVersionName();
//        getAppVersionName()BaseActivity里面的方法
        if(name.equals("")||!name.equals(getAppVersionName())){
            Intent intent=new Intent(this,LeadActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        animation= AnimationUtils.loadAnimation(this,R.anim.anim);
        loadingIv.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
