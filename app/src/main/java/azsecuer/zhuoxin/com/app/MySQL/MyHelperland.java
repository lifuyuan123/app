package azsecuer.zhuoxin.com.app.MySQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import azsecuer.zhuoxin.com.app.Info.User;

/**
 * Created by Administrator on 2017/2/14.
 */

public class MyHelperland {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private MyDbland myDbland;
    private User user;

    public MyHelperland(Context context) {
        this.context = context;
        myDbland=new MyDbland(context);
    }

    //添加用户的方法
    public void addUser(User user){
        //打开数据库帮助类
        sqLiteDatabase=myDbland.getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("usename",user.getUsename());
        contentValues.put("password",user.getPassword());
        contentValues.put("age",user.getAge());
        contentValues.put("sex",user.getSex());
        contentValues.put("phone",user.getPhone());
        String s=null;
        if(user.getIstrue()){
            s="true";
        }else{
            s="false";
        }
        contentValues.put("istrue",s);
        sqLiteDatabase.insert("lands",null,contentValues);//添加道数据库lands表中
        sqLiteDatabase.close();
    }
    //修改用户资料的方法
    public void changeUser(User user){
        sqLiteDatabase=myDbland.getReadableDatabase();
        sqLiteDatabase.execSQL("updata lands set usename=?,password=?,age=?,sex=?,phone=?,istrue=?",
                new Object[]{user.getUsename(),user.getPassword(),user.getAge(),user.getSex(),user.getPhone(),user.getIstrue()});
        sqLiteDatabase.close();
    }
    //删除用户的方法
    public void deleteUser(String usename){
        sqLiteDatabase=myDbland.getReadableDatabase();
        sqLiteDatabase.execSQL("delete from lands where usename=?",new Object[]{usename});
        sqLiteDatabase.close();
    }
    //查询单个用户
    public User chaUser(String s){
        sqLiteDatabase=myDbland.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from lands where usename = ?", new String[]{s});
        if(cursor.moveToFirst()){//代表查询道数据
            String usename=cursor.getString(cursor.getColumnIndex("usename"));
            String password=cursor.getString(cursor.getColumnIndex("password"));
            String age=cursor.getString(cursor.getColumnIndex("age"));
            String sex=cursor.getString(cursor.getColumnIndex("sex"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String istrues=cursor.getString(cursor.getColumnIndex("istrue"));
            boolean istrue=false;
            if(istrues.equals("true")){
                istrue=true;
            }else if(istrues.equals("false")){
                istrue=false;
            }
            user=new User(usename,password,age,sex,phone,istrue);
        }

        return user;
    }
    //查询单个用户,用于免登陆
    public User chaUseristrue(){
        sqLiteDatabase=myDbland.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from lands ", null);
        while (cursor.moveToNext()){
            String usename=cursor.getString(cursor.getColumnIndex("usename"));
            String password=cursor.getString(cursor.getColumnIndex("password"));
            String age=cursor.getString(cursor.getColumnIndex("age"));
            String sex=cursor.getString(cursor.getColumnIndex("sex"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String istrues=cursor.getString(cursor.getColumnIndex("istrue"));
            boolean istrue=false;
            if(istrues.equals("true")){
                istrue=true;
                user=new User(usename,password,age,sex,phone,istrue);
            }
        }
        return user;
    }
    //修改用户资料免登陆布尔值
    public void changeUseristure(String  name,User user){
        sqLiteDatabase=myDbland.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usename",user.getUsename());
        contentValues.put("password",user.getPassword());
        contentValues.put("age",user.getAge());
        contentValues.put("sex",user.getSex());
        contentValues.put("phone",user.getPhone());
        String s=null;
        if(user.getIstrue()){
            s="true";
        }else{
            s="false";
        }
        contentValues.put("istrue",s);
        sqLiteDatabase.update("lands",contentValues,"usename=?",new String[]{name});
        sqLiteDatabase.close();
    }

    //查询所有用户
    public List<User> chaUseList(){
        sqLiteDatabase=myDbland.getReadableDatabase();
        List<User> users=new ArrayList<>();
        User user1=null;
        Cursor cursor = sqLiteDatabase.rawQuery("select * from lands ", null);
        while (cursor.moveToNext()){
            String usename=cursor.getString(cursor.getColumnIndex("usename"));
            String password=cursor.getString(cursor.getColumnIndex("password"));
            String age=cursor.getString(cursor.getColumnIndex("age"));
            String sex=cursor.getString(cursor.getColumnIndex("sex"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            String istrues=cursor.getString(cursor.getColumnIndex("istrue"));
            boolean istrue=false;
            if(istrues.equals("true")){
                istrue=true;
            }else if(istrues.equals("false")){
                istrue=false;
            }
            user1=new User(usename,password,age,sex,phone,istrue);
            users.add(user1);
        }
        return users;
    }
}
