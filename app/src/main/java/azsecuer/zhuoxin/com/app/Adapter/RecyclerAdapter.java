package azsecuer.zhuoxin.com.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.L;

import java.util.List;
import azsecuer.zhuoxin.com.app.Info.Bean.ShowapiResBodyBean.PagebeanBean.ContentlistBean;
import azsecuer.zhuoxin.com.app.R;

/**
 * Created by Administrator on 2016/12/26.
 */

public class RecyclerAdapter extends RecyclerView.Adapter {
    private List<ContentlistBean> contentlistBeanList;
    private Context context;
    private Callback callback;
    private DisplayImageOptions options;
    public static final int ITEM_HEAD=0;
    public static final int ITEM_NORMAL=1;
    public static final int ITEM_LAST=2;
    public static int LOAD_TYPE=0;

    public RecyclerAdapter(Context context) {
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

    public List<ContentlistBean> getContentlistBeanList() {
        return contentlistBeanList;
    }

    public void setContentlistBeanList(List<ContentlistBean> contentlistBeanList) {
        this.contentlistBeanList = contentlistBeanList;
    }

    //显示尾布局类型的方法
    public void setType(int type){
        LOAD_TYPE=type;
        notifyItemChanged(contentlistBeanList.size());//刷新最后一项  而不是全部刷新
    }

    @Override
    public int getItemCount() {
        return contentlistBeanList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        //通过position判断返回的item类型
        if(position==0){
            return ITEM_HEAD;
        }else if(position==contentlistBeanList.size()){
            return ITEM_LAST;
        }else {
            return ITEM_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //通过类型判断使用哪种view
        if(viewType==ITEM_HEAD){
           View headview= LayoutInflater.from(context).inflate(R.layout.item_recycle_head,parent,false);
            return  new HeadViewHolder(headview);
        }
        if(viewType==ITEM_NORMAL){
            View normalview= LayoutInflater.from(context).inflate(R.layout.item_recycle_normal,parent,false);
            return  new NormalViewHolder(normalview);
        }
        if(viewType==ITEM_LAST){
            View lastview= LayoutInflater.from(context).inflate(R.layout.item_recycle_last,parent,false);
            return  new LastViewHolder(lastview);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(contentlistBeanList.size()!=0){
        if(holder instanceof HeadViewHolder){
                ContentlistBean content= contentlistBeanList.get(0);
                ((HeadViewHolder) holder).textView.setText(content.getTitle());
                List<ContentlistBean.ImageEntity> imageentitys=content.getImageurls();
                if(imageentitys.size()!=0){
                    ImageLoader.getInstance().displayImage(imageentitys.get(0).getUrl()
                            , ((HeadViewHolder) holder).imageView, options);
                }
            if(callback!=null){
                ((HeadViewHolder) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callback.oniteamlistener(position);
                    }
                });
                ((HeadViewHolder) holder).relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        callback.onlonglinstener(position);
                        return true;
                    }
                });
            }

        }else if(holder instanceof NormalViewHolder){
            ContentlistBean content=contentlistBeanList.get(position);
            ((NormalViewHolder) holder).item_news_title.setText(content.getTitle());
            ((NormalViewHolder) holder).item_news_time.setText(content.getPubDate());
            ((NormalViewHolder) holder).item_news_content.setText(content.getSource());
            List<ContentlistBean.ImageEntity> imageEntities = content.getImageurls();
            if (imageEntities.size() != 0)
                ImageLoader.getInstance().displayImage(imageEntities.get(0).getUrl()
                        , ((NormalViewHolder) holder).item_news_icon, options);
            if(callback!=null){
                ((NormalViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callback.oniteamlistener(position);
                    }
                });
                ((NormalViewHolder) holder).linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        callback.onlonglinstener(position);
                        return true;
                    }
                });
            }
        }else if (holder instanceof LastViewHolder){
            switch (LOAD_TYPE){
                case 0:
                    ((LastViewHolder) holder).textView.setText("上拉加载更多");
                    ((LastViewHolder) holder).progressBar.setVisibility(View.GONE);
                    break;
                case 1:
                    ((LastViewHolder) holder).textView.setText("加载中...");
                    ((LastViewHolder) holder).textView.setVisibility(View.VISIBLE);
                    ((LastViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ((LastViewHolder) holder).textView.setText("没有更多数据了哦！");
                    ((LastViewHolder) holder).textView.setVisibility(View.VISIBLE);
                    ((LastViewHolder) holder).progressBar.setVisibility(View.GONE);
                    break;

            }
        }
        }

    }

    //三种布局
    class HeadViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public HeadViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.item_head_img);
            textView= (TextView) itemView.findViewById(R.id.item_head_text);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.head_relativelayout);
        }
    }
    class NormalViewHolder extends RecyclerView.ViewHolder{
        public TextView item_news_title, item_news_time, item_news_content;
        public ImageView item_news_icon;
        public LinearLayout linearLayout;
        public NormalViewHolder(View itemView) {
            super(itemView);
            item_news_title = (TextView) itemView.findViewById(R.id.item_tv_top);
            item_news_time = (TextView) itemView.findViewById(R.id.item_tv_buttomRight);
            item_news_content = (TextView) itemView.findViewById(R.id.item_tv_buttomleft);
            item_news_icon = (ImageView) itemView.findViewById(R.id.iv_item);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.normal_lineatlayout);
        }
    }
    class LastViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;
        public TextView textView;
        public LastViewHolder(View itemView) {
            super(itemView);
            progressBar=(ProgressBar)itemView.findViewById(R.id.loading_progressbar);
            textView=(TextView)itemView.findViewById(R.id.loading_textview);
        }
    }
    public void addLists(List<ContentlistBean> lists){
        if(contentlistBeanList!=null){
            this.contentlistBeanList.addAll(lists);
            notifyDataSetChanged();
        }
    }

    public void moveall(){
        if(contentlistBeanList!=null){
           contentlistBeanList=null;
        }
    }

    public interface Callback{
        void oniteamlistener(int position);
        void onlonglinstener(int position);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

}

