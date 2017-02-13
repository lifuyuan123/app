package azsecuer.zhuoxin.com.app.MyBroadcastReceiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/4.
 */

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        // Service被启动时，将会有弹出消息提示[MyService onStart]
        Toast.makeText(this, "[MyService onStart]", Toast.LENGTH_SHORT).show();
    }
}
