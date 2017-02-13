package azsecuer.zhuoxin.com.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import azsecuer.zhuoxin.com.app.Info.Info;
import azsecuer.zhuoxin.com.app.R;

/**
 * Created by Administrator on 2016/12/28.
 */

public class Mydapter extends BaseAdapter {
    private List<Info> lists;
    private Context context;
    //图片处理
    private DisplayImageOptions options;

    public List<Info> getLists() {
        return lists;
    }

    public void setLists(List<Info> lists) {
        this.lists = lists;
    }

    public Mydapter(List<Info> lists, Context context) {
        this.lists = lists;
        this.context = context;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.img_news_lodinglose)
                .showImageOnFail(R.drawable.img_news_lodinglose)
                .showImageOnLoading(R.drawable.img_news_loding)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Viewholder viewholder=null;
        if(view==null){
            viewholder=new Viewholder();
            view= LayoutInflater.from(context).inflate(R.layout.mynews_item,null);
            viewholder.imageView=(ImageView)view.findViewById(R.id.iv_item);
            viewholder.textViewtop=(TextView)view.findViewById(R.id.item_tv_top);
            viewholder.textViewleft=(TextView)view.findViewById(R.id.item_tv_buttomleft);
            viewholder.textViewright=(TextView)view.findViewById(R.id.item_tv_buttomRight);
            viewholder.linearLayout= (LinearLayout) view.findViewById(R.id.manews_lineatlayout);
            view.setTag(viewholder);
        }else {
            viewholder=(Viewholder) view.getTag();
        }

        Info info=lists.get(i);
        ImageLoader.getInstance().displayImage(info.iconurl
                , viewholder.imageView, options);

        viewholder.textViewtop.setText(info.title);
        viewholder.textViewleft.setText(info.type);
        viewholder.textViewright.setText(info.time);
        //设置长按和点击监听
        viewholder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(callback!=null){
                 callback.click(i);
             }
            }
        });
        viewholder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(callback!=null){
                    callback.longclick(i);
                }
                return true;
            }
        });

        return view;
    }
    class Viewholder{
        private ImageView imageView;
        private TextView textViewtop,textViewleft,textViewright;
        private LinearLayout linearLayout;
    }

    public interface Callback{
        void click(int k);
        void longclick(int k);
    }
    private  Callback callback;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
