package azsecuer.zhuoxin.com.app.MySQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import azsecuer.zhuoxin.com.app.Info.Info;

/**
 * Created by Administrator on 2016/12/28.
 */

public class MyHelper {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private MyDB myDB;
    private Info info;
    public MyHelper(Context context) {
        this.context = context;
        myDB=new MyDB(context);
    }

    //添加数据
    public void addData(String title,String iconurl,String time,String type,String url){
        sqLiteDatabase=myDB.getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",title);
        contentValues.put("icon",iconurl);
        contentValues.put("time",time);
        contentValues.put("type",type);
        contentValues.put("url",url);
        sqLiteDatabase.insert("shoucang",null,contentValues);
        sqLiteDatabase.close();
    }
    public void remove(String name){
        sqLiteDatabase=myDB.getReadableDatabase();
        sqLiteDatabase.delete("shoucang","url=?", new String[]{name});
        sqLiteDatabase.close();
    }
    //查询全部数据
    public List<Info> getAlldata(){
        sqLiteDatabase=myDB.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from shoucang ",null);
        List<Info> list=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                info=new Info(cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("icon")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("url")));
                list.add(info);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        cursor.close();
        return list;
    }

    //查询一条数据(url为查询条件)
    public List<Info> getdata(String name){
        sqLiteDatabase=myDB.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from shoucang where url= ? ",new String[]{name});
        List<Info> list=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                info=new Info(cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("icon")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("url")));
                list.add(info);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        cursor.close();
        return list;
    }

    //模糊查询
    public List<Info> getMoHudata(String name){
        sqLiteDatabase=myDB.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from shoucang where title like ? ",new String[]{'%'+name+'%'});
        List<Info> list=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                info=new Info(cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("icon")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("url")));
                list.add(info);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }
}
