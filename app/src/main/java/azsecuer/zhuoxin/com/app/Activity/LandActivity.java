package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import azsecuer.zhuoxin.com.app.Info.User;
import azsecuer.zhuoxin.com.app.MySQL.MyHelperland;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandActivity extends AppCompatActivity {

    @BindView(R.id.usename)
    EditText usename;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.regist)
    Button regist;
    @BindView(R.id.unregist)
    Button unregist;
    @BindView(R.id.activity_land)
    LinearLayout activityLand;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    private MyHelperland myHelperland;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);
        ButterKnife.bind(this);
        myHelperland = new MyHelperland(this);
        init();
        island();
    }

    private void init() {
        toolbar.setTitle("登陆");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.regist, R.id.unregist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regist:
                landing();
                break;
            case R.id.unregist:
                Toast.makeText(this, "点击注册", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, RegistActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private void landing() {
        if (usename.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
        } else {
            String name = usename.getText().toString();
            String pass = password.getText().toString();
            if (myHelperland != null) {
                //储存登陆成功的操作
                SharedPreferences sharedPreferences = getSharedPreferences("landing", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                user = myHelperland.chaUser(name);
                if (user != null) {
                    if (user.getUsename().equals(name) && user.getPassword().equals(pass)) {
                        //储存登陆成功的操作
                        editor.putBoolean("boolean", true);
                        Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("name", user.getUsename());
                        startActivity(intent);
                        finish();
                    } else {
                        editor.putBoolean("boolean", false);
                    }
                    editor.commit();//提交

                } else {
                    Toast.makeText(this, "账号或者密码有误", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }


    public void island() {
        User user = myHelperland.chaUseristrue();
        if (user != null) {
            if (user.getIstrue()) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("name", user.getUsename());
                startActivity(intent);
                finish();
            }
        }
    }

    @OnClick(R.id.checkbox)
    public void onClick() {
        if(checkbox.isChecked()){
            User user=myHelperland.chaUser(usename.getText().toString());
            user.setIstrue(true);
            myHelperland.changeUseristure(usename.getText().toString(),user);
            Log.i("1111111user",user.toString());
            Log.i("1111111userall",myHelperland.chaUseList().toString());

        }
    }
}
