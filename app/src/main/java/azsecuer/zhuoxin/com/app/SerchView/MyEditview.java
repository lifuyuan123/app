package azsecuer.zhuoxin.com.app.SerchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import azsecuer.zhuoxin.com.app.R;

/**
 * Created by Administrator on 2017/1/25.
 */

public class MyEditview extends LinearLayout implements View.OnClickListener{
    private Context context;
    public ImageView imageView;
    public EditText editText;
    public Button button;
    public PopupWindow popupWindow=null;
    private ArrayList<String> strings=new ArrayList<>();

    public MyEditview(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search_layout,this);
        initview();
    }

    private void initview() {
        editText= (EditText) findViewById(R.id.edit_text);
        button= (Button)findViewById(R.id.button);
        imageView= (ImageView)findViewById(R.id.imageview);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow==null){
                    showpopwindow();
                }else {
                    closepopwindows();
                }
            }

        });


    }
    private void showpopwindow() {
        String s=Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater1;
        layoutInflater1= (LayoutInflater) getContext().getSystemService(s);
        View view1=layoutInflater1.inflate(R.layout.windows,null,false);
        ListView listView= (ListView) view1.findViewById(R.id.list);

        listView.setAdapter(new MyEditAdapter(getContext(),strings));
        popupWindow=new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorAccent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(editText.getWidth());//获取editext的宽
        //---------------------------点击空白除消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        //------------------------------
        popupWindow.showAsDropDown(this,20,-14);


    }

    private void closepopwindows() {
        popupWindow.dismiss();
        popupWindow=null;
    }

    public void setdata(ArrayList<String> arrayList){
        this.strings=arrayList;
//        //默认选中第一项
//        editText.setText(strings.get(0).toString());
//        //光标道最后
//        editText.setSelection(strings.get(0).length());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview:
                editText.setText("");
                showpopwindow();
                imageView.setVisibility(GONE);
                break;
            case R.id.button:

                break;
        }
    }

    //数据源类
    class MyEditAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<String> lists;
        private LayoutInflater inflater;
        public MyEditAdapter(Context context, ArrayList<String> lists) {
            this.context = context;
            this.lists = lists;
            inflater=LayoutInflater.from(context);
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
            ViewHolder viewHolder=null;
            if(view==null){
                viewHolder=new ViewHolder();
                view=inflater.inflate(R.layout.item,null);
                viewHolder.textView= (TextView) view.findViewById(R.id.text);
                viewHolder.linearLayout= (LinearLayout) view.findViewById(R.id.linear);
                view.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.textView.setText(lists.get(i).toString());
            viewHolder.linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setText(lists.get(i).toString());
                    editText.setSelection(lists.get(i).length());
                    imageView.setVisibility(VISIBLE);
                    closepopwindows();
                }
            });
            return view;

        }

        class ViewHolder{
            TextView textView;
            LinearLayout linearLayout;
        }
    }
    public interface MyEditListener{
        void adddata(ArrayList<String> strings);
    }
    private MyEditListener listener;

    public void setListener(MyEditListener listener) {
        this.listener = listener;
    }
}
