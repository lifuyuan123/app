package azsecuer.zhuoxin.com.app.MyNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import azsecuer.zhuoxin.com.app.Activity.MainActivity;
import azsecuer.zhuoxin.com.app.R;

/**
 * Created by Administrator on 2017/1/4.
 */

public class MyNotifiction {
    private static NotificationManager notificationManager;
    private static Notification notification;
    public static NotificationManager getInstense(Context context){
        if(notificationManager==null){
            notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    public static void openNotification(Context context){
        notificationManager=getInstense(context);
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.notification);//布局文件
        Intent intent=new Intent(context, MainActivity.class);//点击跳转
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification=new Notification.Builder(context).
                setContentIntent(pendingIntent).//跳转
                setContent(remoteViews).//布局
                setTicker("有新消息").//提示消息
                setSmallIcon(R.drawable.app).//小图标
                build()
                ;
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        notificationManager.notify(1,notification);

    }
    public static void closeNotification(Context context){
        notificationManager=getInstense(context);
        notificationManager.cancelAll();//关闭通知栏
    }


}
