package azsecuer.zhuoxin.com.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import azsecuer.zhuoxin.com.app.Adapter.Mydapter;
import azsecuer.zhuoxin.com.app.Info.Info;
import azsecuer.zhuoxin.com.app.MySQL.MyHelper;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShouActivity extends AppCompatActivity {
    @BindView(R.id.shoutoolbar)
    Toolbar shoutoolbar;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.activity_shou)
    LinearLayout activityShou;
    @BindView(R.id.linear)
    LinearLayout linear;
    private List<Info> infoList;
    private Mydapter mydapter;
    private MyHelper m;
    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shou);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        m = new MyHelper(this);
        setSupportActionBar(shoutoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        shoutoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mydapter = new Mydapter(getdata(), this);
        listview.setAdapter(mydapter);

        mydapter.setCallback(new Mydapter.Callback() {
            @Override
            public void click(int k) {
                info = infoList.get(k);
                Intent i = new Intent(ShouActivity.this, WebActivity.class);
                i.putExtra("url", infoList.get(k).url);
                startActivityForResult(i, 100);
//                finish();  注意不能结束  不然无法得到返回值
            }

            @Override
            public void longclick(final int k) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShouActivity.this);
                builder.setTitle("删除").setMessage("确定删除？" + "\n" + infoList.get(k).title)
                        .setNegativeButton("取消", null).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m.remove(infoList.get(k).url);//从数据库删除这项
                                infoList.remove(infoList.get(k));//从listview中删除这项
                                mydapter.notifyDataSetChanged();//刷新
                            }
                        });
                builder.show();
            }
        });

    }

    //获取数据源
    public List<Info> getdata() {
        infoList = m.getAlldata();
        if (infoList.size()==0){
            listview.setVisibility(View.GONE);
            linear.setVisibility(View.VISIBLE);
        }else {
            listview.setVisibility(View.VISIBLE);
            linear.setVisibility(View.GONE);
        }
        return infoList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 101 && data.getBooleanExtra("needupdata", false)) {
                //添加到数据库的操作
                if (m.getdata(info.url).size() == 0) {
                    m.addData(info.title, info.iconurl,
                            info.time, info.type, info.url);
                    Log.i("11111返回", "" + "添加");
                } else {
                    m.remove(info.url);
                    Log.i("11111返回", "" + "删除");
                }
                //必须重新获取刷新数据
                mydapter.setLists(m.getAlldata());
                if(m.getAlldata().size()==0){
                    listview.setVisibility(View.GONE);
                    linear.setVisibility(View.VISIBLE);
                }else {
                    listview.setVisibility(View.VISIBLE);
                    linear.setVisibility(View.GONE);
                }
                mydapter.notifyDataSetChanged();
            }
        }
    }
}
