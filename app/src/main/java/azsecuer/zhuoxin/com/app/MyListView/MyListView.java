package azsecuer.zhuoxin.com.app.MyListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/1/6.
 */

public class MyListView extends ListView {
    private int width=0;
    public MyListView(Context context) {
        super(context,null,0);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    // 重写onMeasure方法 解决默认横向占满屏幕问题

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height=getHeight();
        for (int i = 0; i <getChildCount() ; i++) {
            int childWidth=getChildAt(i).getMeasuredWidth();
            width=Math.max(width,childWidth);//取宽
        }
        //设置宽高
        setMeasuredDimension(width,height);
    }
//    给出一个方法设置宽度，如果不设置，则默认包裹内容
    public void setListViewWidth(int width){
        this.width=width;
    }
}
