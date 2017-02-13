package azsecuer.zhuoxin.com.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import azsecuer.zhuoxin.com.app.Activity.AddActivity;
import azsecuer.zhuoxin.com.app.Adapter.FragmentAdapter;
import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/20.
 */

public class NewsFragment extends Fragment {
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.news_viewpaper)
    ViewPager newsViewpaper;
    private List<Fragment> fragments;
    private List<String> titles;
    private SharedpreferencesUtil sharedpreferencesUtil;
    private FragmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferencesUtil=new SharedpreferencesUtil(getActivity());
        init();

    }

    private void init(){
        titles=sharedpreferencesUtil.getListTitles();
        Log.i("111111titles-----",""+titles.toString());
        fragments=new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            DataFragment dataFragment=new DataFragment();
            fragments.add(dataFragment);
            Bundle bundle=new Bundle();
            bundle.putString("titles",titles.get(i));
            dataFragment.setArguments(bundle);//将标题传入到DataFragment()以便再操作DataFragment()的时候通过getArguments获取标题
        }
        adapter=new FragmentAdapter(getActivity().getSupportFragmentManager(),fragments,titles);
        newsViewpaper.setAdapter(adapter);
        tablayout.setupWithViewPager(newsViewpaper);
        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AddActivity.class);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode==101&&data.getBooleanExtra("needupdata",false)){
              init();
                Log.i("11111返回","成功");
            }
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        sharedpreferencesUtil.saveTitles(getSetlist());
//        Log.i("111111titles--destroy",""+titles.toString());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        sharedpreferencesUtil.saveTitles(getSetlist());
//        Log.i("111111titles--destroy",""+titles.toString());
//    }
//
//    private Set<String> getSetlist(){
//        Set<String> strings=new TreeSet<>();
//        for (String s:titles) {
//            strings.add(s);
//        }
//        return strings;
//    }
}
