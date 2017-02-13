package azsecuer.zhuoxin.com.app.MyImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/12/20.
 */

public class MyImage extends ImageView {
    private Paint paint=new Paint();
    public MyImage(Context context) {
        super(context,null);
    }

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public MyImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

     //重写画的方法
    @Override
    protected void onDraw(Canvas canvas) {
        //获取drawable
        Drawable drawable=getDrawable();
        if(drawable!=null){
            //获取bitmap
            Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
            Bitmap mbitmap=getbitmap(bitmap);
            Rect rectSrc=new Rect(0,0,mbitmap.getWidth(),mbitmap.getHeight());
            Rect rectDest=new Rect(0,0,getWidth(),getHeight());
            paint.ascent();
            canvas.drawBitmap(mbitmap,rectSrc,rectDest,paint);
        }else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getbitmap(Bitmap bitmap){
        int width=bitmap.getWidth(),height=bitmap.getHeight();
        int r=0;
        if(width>height){
            r=height;
        }else {
            r=width;
        }
        Bitmap mbitmap=Bitmap.createBitmap(r,r, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(mbitmap);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        RectF rectF=new RectF(0,0,r,r);
        canvas.drawRoundRect(rectF,r/2,r/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,null,rectF,paint);
        return mbitmap;
    }
}
