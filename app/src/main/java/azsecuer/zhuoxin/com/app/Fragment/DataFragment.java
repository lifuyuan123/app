package azsecuer.zhuoxin.com.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import azsecuer.zhuoxin.com.app.Activity.WebActivity;
import azsecuer.zhuoxin.com.app.Adapter.RecyclerAdapter;
import azsecuer.zhuoxin.com.app.Info.Bean;
import azsecuer.zhuoxin.com.app.Info.Bean.ShowapiResBodyBean.PagebeanBean.ContentlistBean;
import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;
import azsecuer.zhuoxin.com.app.Manager.URL;
import azsecuer.zhuoxin.com.app.MyApplication.MyApplication;
import azsecuer.zhuoxin.com.app.MySQL.MyHelper;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/21.
 */

public class DataFragment extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.linear)
    LinearLayout linear;
    private List<ContentlistBean> contentlistBeanList;
    private SharedpreferencesUtil sharedpreferencesUtil;
    private RecyclerAdapter adapter;
    private String title;
    private int allpage;
    private int page = 1;
    private MyHelper myHelper;
    private boolean first=true,flush=false;
    private int type;
    final Gson gson = MyApplication.getGson();
    private ContentlistBean content;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        title = getArguments().getString("titles");
        View view = inflater.inflate(R.layout.fragment_data, container, false);
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
        sharedpreferencesUtil=new SharedpreferencesUtil(getActivity());
        contentlistBeanList = new ArrayList<>();
        adapter = new RecyclerAdapter(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        swiperefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        //下拉刷新
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flush=true;
                getdata(title, page);
                swiperefresh.setRefreshing(false);
                flush=false;
            }
        });
        Log.i("111111成功", "aaaaa");
        getdata(title, 1);

        adapter.setCallback(new RecyclerAdapter.Callback() {
            @Override
            public void oniteamlistener(int position) {
                Intent intent=new Intent(getContext(), WebActivity.class);
                content=adapter.getContentlistBeanList().get(position);
                intent.putExtra("url",content.getLink());
                startActivityForResult(intent,100);
            }

            @Override
            public void onlonglinstener(int position) {

            }
        });

        //滑动监听（上啦加载）
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!ViewCompat.canScrollVertically(recyclerView,1)){
                    if(first){
                        first=false;
                        page=2;
                        Log.i("111111onScrolled_1",page+"");
                    }
                    adapter.setType(1);
                    getdata(title,page);
                    if(page>allpage){
                        adapter.setType(2);
                    }
                    page++;
                    Log.i("111111onScrolled",page+"");
                }
            }
        });
    }




    //获取数据的方法
    private void getdata(String name, final int page) {
        RequestQueue requestQueue = MyApplication.getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.getName(name, page),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //成功
                        Log.i("111111成功", response);
                        try {
                            Bean bean = gson.fromJson(response, Bean.class);
                            contentlistBeanList = bean.getShowapi_res_body().getPagebean().getContentlist();
                            allpage = bean.getShowapi_res_body().getPagebean().getAllPages();
                        } catch (Exception e) {
                            e.getMessage();
                        } finally {
                            if (contentlistBeanList == null || contentlistBeanList.size() == 0) {
                                return;
                            }

                            //删除没有图片或者空的item
                            Iterator<ContentlistBean> iterator = contentlistBeanList.iterator();
                            ContentlistBean contentlistBean = null;
                            while (iterator.hasNext()) {
                                contentlistBean = iterator.next();
                                if (contentlistBean == null || contentlistBean.getImageurls().size() == 0 ||
                                        contentlistBean.getImageurls().get(0).getUrl() == null) {
                                    iterator.remove();
                                }
                            }

                            //设置数据
                            if (contentlistBeanList != null) {
                                if(first){
                                    adapter.setContentlistBeanList(contentlistBeanList);
                                    //只能第一次设置数据源  之后添加进来的数据由adapter刷新  不能再次设置数据源否则惠跑到第一页
                                    recyclerview.setAdapter(adapter);
                                    Log.i("111111BeanList1", contentlistBeanList.toString());
                                }else {
                                    if(flush){
                                        adapter.moveall();
                                        adapter.setContentlistBeanList(contentlistBeanList);
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        adapter.addLists(contentlistBeanList);
                                    }

                                    Log.i("111111BeanList"+page, adapter.getContentlistBeanList().toString());
                                }
                                recyclerview.setVisibility(View.VISIBLE);
                                linear.setVisibility(View.GONE);
                                adapter.setType(0);
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //失败
                Log.i("111111失败", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("apikey", URL.GETapikey);
                return map;
            }
        };
        requestQueue.add(stringRequest);//加入消息队列

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode==101&&data.getBooleanExtra("needupdata",false)){
                        //添加到数据库的操作
                        if(myHelper.getdata(content.getLink()).size()==0){
                            myHelper.addData(content.getTitle(),content.getImageurls().get(0).getUrl(),
                                    content.getPubDate(),content.getSource(),content.getLink());
                            Log.i("11111返回",""+"添加");
                        }else {
                            myHelper.remove(content.getLink());
                            Log.i("11111返回",""+"删除");
                        }

                }
                    }

            }
        }


