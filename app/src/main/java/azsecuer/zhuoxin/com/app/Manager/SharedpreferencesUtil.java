package azsecuer.zhuoxin.com.app.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Administrator on 2016/12/20.
 */

public class SharedpreferencesUtil {
    private String versionName="versionName",titles="titles",tongzhi="tongzhi",open="open";
    private Context context;

    public SharedpreferencesUtil(Context context) {
        this.context = context;
    }
    //保存版本信息
    public void saveVersionName(String name){
        SharedPreferences sharedPreferences=context.getSharedPreferences(versionName,Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("VersionName",name);
        editor.commit();
    }
    //获取版本信息
    public String getVersionName(){
        return context.getSharedPreferences(versionName,0).getString("VersionName","");
    }


    //保存标题信息
    public void saveTitles(Set<String> title){
        SharedPreferences sharedPreferences=context.getSharedPreferences(titles,Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear().putStringSet("titles",title).apply();//1.在editor之后调用一次clear，强制重新保存数据
        editor.commit();
    }
//    总的来说，这是一个隐藏的小bug，解决办法有两个：
//    1.在editor之后调用一次clear，强制重新保存数据
//    edit().clear().putStringSet("titles",title).apply();
//
//    2.不要直接使用getStringSet返回的数据，需要重新创建一个HashSet
//
//   strings = new HashSet<>(context.getSharedPreferences(titles,0).getStringSet("titles",new HashSet<>));



    //转换为list
    public List<String> getListTitles(){
        List<String> titles=new ArrayList<>();
        Set<String> strings=getSetTitles();
        for (String s:strings) {
            titles.add(s);
        }
        return titles;
    }

    //初始化标题
    public Set<String> getSetTitles(){
        Set<String> title=context.getSharedPreferences(titles,0).getStringSet("titles",null);
        if(title==null){
            title=new TreeSet<>();
            title.add("体育");
            title.add("女人");
            title.add("娱乐");
            title.add("房产");
            title.add("汽车");
        }
        Log.i("11111gettiles--shared",""+title.toString());
        return title;
    }


    public void savedata(int b){
        SharedPreferences sharedPreferences=context.getSharedPreferences("shoucang",Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("int",b);
        editor.commit();
    }
    public int getdata(){
        return context.getSharedPreferences("shoucang",0).getInt("int",1);
    }


    //保存和获取通知栏的操作------------------------------------------------------------
    public void savetong(Boolean b){
        SharedPreferences sharedPreferences=context.getSharedPreferences(tongzhi,Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("tongzhi",b);
        editor.commit();
    }
    public void saveopen(Boolean b){
        SharedPreferences sharedPreferences=context.getSharedPreferences(open,Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("open",b);
        editor.commit();
    }
    public Boolean gettong(){
        return context.getSharedPreferences(tongzhi,0).getBoolean("tongzhi",false);
    }
    public Boolean getopen(){
        return context.getSharedPreferences(open,0).getBoolean("open",false);
    }
    //-------------------------------------------------------------------------------------



}
