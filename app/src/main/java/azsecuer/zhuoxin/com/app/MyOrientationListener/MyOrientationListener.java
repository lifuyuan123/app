package azsecuer.zhuoxin.com.app.MyOrientationListener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2017/2/2.
 */

public class MyOrientationListener implements SensorEventListener {

    private SensorManager sensorManager;
    private Context context;
    private Sensor sensor;
    private float lastX;


    public void start(){
        sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager!=null){
            //获得方向传感器
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            if(sensor!=null){
                sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
            }
        }

    }

    public void stop(){
     sensorManager.unregisterListener(this);
    }

    public MyOrientationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
      if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
          float x=sensorEvent.values[SensorManager.DATA_X];
          if(Math.abs(x-lastX)>1.0){
           if(onOrientationListener!=null){
               onOrientationListener.OnOrientationChanged(x);
           }
          }
           lastX=x;
      }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface OnOrientationListener{
        void OnOrientationChanged(float x);
    }
    private OnOrientationListener onOrientationListener;

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }
}
