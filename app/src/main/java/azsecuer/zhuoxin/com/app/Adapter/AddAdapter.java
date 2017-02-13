package azsecuer.zhuoxin.com.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import azsecuer.zhuoxin.com.app.R;

/**
 * Created by Administrator on 2016/12/29.
 */

public class AddAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;

    public AddAdapter(Context context) {
        this.context = context;
    }
    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.add_layout,null);
            viewHolder.textView= (TextView) view.findViewById(R.id.text);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(i));
        return view;
    }
    class ViewHolder{
        private TextView textView;
    }
    public void adddata(String s){
        this.list.add(s);
        notifyDataSetChanged();
    }
    public void move(String s){
        this.list.remove(s);
        notifyDataSetChanged();
    }
}
