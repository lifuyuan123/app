package azsecuer.zhuoxin.com.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import azsecuer.zhuoxin.com.app.Activity.WebActivity;
import azsecuer.zhuoxin.com.app.Adapter.Mydapter;
import azsecuer.zhuoxin.com.app.Info.Info;
import azsecuer.zhuoxin.com.app.MySQL.MyHelper;
import azsecuer.zhuoxin.com.app.R;
import azsecuer.zhuoxin.com.app.SerchView.MyEditview;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/6.
 */

public class SerchFragment extends Fragment {
    @BindView(R.id.searchview)
    MyEditview myEditview;
    @BindView(R.id.serch_listview)
    ListView listView;
    private ArrayList<String> arrayList;
    private MyHelper myHelper;
    private List<Info> infoList;
    private Mydapter mydapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serch, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        myHelper=new MyHelper(getContext());
        myEditview.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO  搜索
                int a=0;
                //添加历史搜索记录，除重复
                for (int i = 0; i <arrayList.size() ; i++) {
                    if(!arrayList.get(i).equals(myEditview.editText.getText().toString())){
                        a++;
                    }
                }
                if(a==arrayList.size()){
                    arrayList.add(myEditview.editText.getText().toString());
                    myEditview.setdata(arrayList);
                }

                //设置显示的数据（数据库中调出来）
                infoList=myHelper.getMoHudata(myEditview.editText.getText().toString());
                mydapter=new Mydapter(infoList,getContext());
                if(infoList!=null){
                if(infoList.size()==0){
                    infoList.add(new Info("没有数据哦。","","","",""));
                }
                    listView.setAdapter(mydapter);
                    listView.setVisibility(View.VISIBLE);

                    mydapter.setCallback(new Mydapter.Callback() {
                        @Override
                        public void click(int k) {
                            Info info=infoList.get(k);
                            Intent intent=new Intent(getActivity(), WebActivity.class);
                            intent.putExtra("url",info.url);
                            startActivity(intent);
                        }

                        @Override
                        public void longclick(int k) {

                        }
                    });
                }

            }
        });
        arrayList=new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            arrayList.add("数据"+i);
        }
        myEditview.setdata(arrayList);
        myEditview.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(myEditview.editText.getText().toString().length()!=0){
                    myEditview.imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
