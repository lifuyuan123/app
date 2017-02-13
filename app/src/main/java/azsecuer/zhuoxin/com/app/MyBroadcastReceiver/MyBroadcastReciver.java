package azsecuer.zhuoxin.com.app.MyBroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import azsecuer.zhuoxin.com.app.Activity.LoadingActivity;
import azsecuer.zhuoxin.com.app.Activity.MainActivity;
import azsecuer.zhuoxin.com.app.Manager.SharedpreferencesUtil;

/**
 * Created by Administrator on 2017/1/4.
 */

public class MyBroadcastReciver extends BroadcastReceiver {
    public static final String ACTION="android.intent.action.BOOT_COMPLETED";
    private SharedpreferencesUtil sharedpreferencesUtil;
    private boolean open;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "广播", Toast.LENGTH_SHORT).show();
        sharedpreferencesUtil=new SharedpreferencesUtil(context);
        open=sharedpreferencesUtil.getopen();
        if(intent.getAction().equals(ACTION)){
            if(open){
            Intent intent1=new Intent(context, LoadingActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent1);

            Intent service=new Intent(context,MyService.class);
            context.startService(service);
            }else {
                return;
            }
        }
    }
}
