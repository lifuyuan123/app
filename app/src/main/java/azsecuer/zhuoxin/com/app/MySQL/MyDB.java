package azsecuer.zhuoxin.com.app.MySQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/28.
 */

public class MyDB extends SQLiteOpenHelper {
    public MyDB(Context context) {
        super(context, "mydata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
   sqLiteDatabase.execSQL("create table if not exists shoucang(title text,icon text,time text,type text,url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
