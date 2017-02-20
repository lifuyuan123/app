package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SuccessActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.activity_success)
    RelativeLayout activitySuccess;
    private Intent intent=null;
    private CountDownTimer time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);
        intent=new Intent(this,LandActivity.class);
        //设置参数，5000总时间，1000间隔事件
        time=new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long l) {//long 1表示剩余的世间
             text.setText(l/1000+"s");
            }

            @Override
            public void onFinish() {
                startActivity(intent);
                finish();
            }
        }.start();
    }


    @OnClick(R.id.text2)
    public void onClick() {
        startActivity(intent);
        time.cancel();
        finish();
    }
}
