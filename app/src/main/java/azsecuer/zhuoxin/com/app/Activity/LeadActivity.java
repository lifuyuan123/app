package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import azsecuer.zhuoxin.com.app.Adapter.LeadpagerAdapter;
import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LeadActivity extends BaseActivity {
    private List<View> views;
    private LeadpagerAdapter adapter;
    private Animation animation;
    @BindView(R.id.lead_viewpager)
    ViewPager leadViewpager;
    @BindView(R.id.lead_bt)
    Button leadBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        ButterKnife.bind(this);
        setdata();
        leadViewpager.setAdapter(adapter);
        //监听滑到第三页显示button
        leadViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==2){
                    leadBt.setVisibility(View.VISIBLE);
                    leadBt.setAnimation(animation);
                }else {
                    leadBt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
             if(views.size()>1){
                 if(position<1){
                     position=3;
                     leadViewpager.setCurrentItem(3,false);
                 }
                 if(position>3){
                     position=1;
                     leadViewpager.setCurrentItem(1,false);
                 }
             }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //动画监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //点击按钮实现跳转
        leadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedpreferencesUtil sharedpreferencesUtil=new SharedpreferencesUtil(LeadActivity.this);
                sharedpreferencesUtil.saveVersionName(getAppVersionName());
                Intent intent=new Intent(LeadActivity.this,LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //添加图片的布局
    private void setdata() {
        views=new ArrayList<>();
        views.add(getLayoutInflater().inflate(R.layout.lead3,null));
        views.add(getLayoutInflater().inflate(R.layout.lead1,null));
        views.add(getLayoutInflater().inflate(R.layout.lead2,null));
        views.add(getLayoutInflater().inflate(R.layout.lead3,null));
        views.add(getLayoutInflater().inflate(R.layout.lead1,null));
        adapter=new LeadpagerAdapter(views);
        animation= AnimationUtils.loadAnimation(this,R.anim.anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
