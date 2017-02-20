package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import azsecuer.zhuoxin.com.app.Info.User;
import azsecuer.zhuoxin.com.app.MySQL.MyHelperland;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends AppCompatActivity {

    @BindView(R.id.usename)
    EditText usename;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.sex)
    EditText sex;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.regist)
    Button regist;
    @BindView(R.id.unregist)
    Button unregist;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_regist)
    LinearLayout activityRegist;
    private MyHelperland myHelperland;
    private User user;
    //用于显示秒数
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //初始化数据库工具
        myHelperland = new MyHelperland(this);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);//设置actionbar为toolbar
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回的箭头出现可点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegistActivity.this,LandActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @OnClick({R.id.regist, R.id.unregist, R.id.checkBox})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regist:
                registint();//注册的方法
                break;
            case R.id.unregist:
                Toast.makeText(this, "取消注册", Toast.LENGTH_SHORT).show();
                deletedata();
                break;
            case R.id.checkBox:
                Toast.makeText(this, "同意条款", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void deletedata() {
        usename.setText("");
        password.setText("");
        age.setText("");
        sex.setText("");
        password.setText("");
        checkBox.setChecked(false);
    }

    private void registint() {
        if (usename.getText().toString() != "" && password.getText().toString() != "" && age.getText().toString() != "" &&
                sex.getText().toString() != "" && phone.getText().toString() != ""&&phone.getText().toString().length()==11 && checkBox.isChecked()) {
            user = new User(usename.getText().toString(), password.getText().toString(), age.getText().toString()
                    , sex.getText().toString(), phone.getText().toString(),false);
            if (myHelperland != null) {
                myHelperland.addUser(user);
            }
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(RegistActivity.this,SuccessActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "请完成信息的填写", Toast.LENGTH_SHORT).show();
        }

    }


}
