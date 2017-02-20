package azsecuer.zhuoxin.com.app.MySQL;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/2/14.
 */

public class MyDbland extends SQLiteOpenHelper {


    public MyDbland(Context context) {
        super(context, "land.db", null, 1);//上下文对象，文件名，null，版本号
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建表名lands，和列名
        sqLiteDatabase.execSQL("create table if not exists lands(usename text,password text,age text,sex text,phone text,istrue text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
