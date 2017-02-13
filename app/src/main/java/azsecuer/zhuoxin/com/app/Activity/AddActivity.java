package azsecuer.zhuoxin.com.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import azsecuer.zhuoxin.com.app.Adapter.AddAdapter;
import azsecuer.zhuoxin.com.app.FlowLayout.FlowLayout;
import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.add_toolbar)
    Toolbar addToolbar;
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.activity_add)
    LinearLayout activityAdd;
    private boolean needupdata;
    //flowlayout需要一个set集合的数据源
    private Set<String> strings;
    //listview需要一个list集合数据源
    private List<String> titles;
//    private ArrayAdapter<String> adapter;
    private AddAdapter addAdapter;
    private SharedpreferencesUtil sharedpreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        sharedpreferencesUtil=new SharedpreferencesUtil(this);
        setSupportActionBar(addToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveboolean();
                finish();
            }
        });
        addAdapter=new AddAdapter(this);
        addAdapter.setList(getListviewtitles());
        listview.setAdapter(addAdapter);

        strings=sharedpreferencesUtil.getSetTitles();
        flowlayout.getSetData(strings);
        flowlayout.setCallback(new FlowLayout.Flowlayoutcallback() {
            @Override
            public void afterOnChildClick(String s) {
                Toast.makeText(AddActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                needupdata=true;
                Log.i("111111sss",s);
                addAdapter.adddata(s);
                strings.remove(s);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s=titles.get(i);
                if(strings.add(s)){
                   flowlayout.getSetData(strings);
                    addAdapter.move(s);
                    Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    needupdata=true;
                }

            }
        });

    }

    //遍历删除必须使用iterator迭代器删除  否则报错concurrentmodificationexception异常
    private List<String> getListviewtitles(){
        List<String> list=new ArrayList<>();
        List<String> alllist=new ArrayList<>();
        alllist=getalldata();
        list=sharedpreferencesUtil.getListTitles();

        Iterator<String> iterator=alllist.iterator();
        while (iterator.hasNext()){
            String s=iterator.next();
            for (int i = 0; i <list.size() ; i++) {
                if (list.get(i).equals(s))
                    iterator.remove();
            }
        }
        titles=alllist;
        return titles;
    }

    //获取所有数据
    private List<String> getalldata() {
        List<String> lists=new ArrayList<>();
        lists.add("国内");
        lists.add("国际");
        lists.add("军事");
        lists.add("财经");
        lists.add("互联网");
        lists.add("房产");
        lists.add("汽车");
        lists.add("体育");
        lists.add("娱乐");
        lists.add("游戏");
        lists.add("女人");
        return lists;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            saveboolean();
            finish();
            return  false;
        }else
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         saveboolean();
        finish();
    }

    private void saveboolean(){
        Intent intent=new Intent();
        intent.putExtra("needupdata",needupdata);
        setResult(101,intent);
        sharedpreferencesUtil.saveTitles(strings);
        Log.i("1111111savetitles",""+strings.toString());
    }
}
