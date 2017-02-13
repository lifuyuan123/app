package azsecuer.zhuoxin.com.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import azsecuer.zhuoxin.com.app.R;

/**
 * Created by Administrator on 2016/11/24.
 */

public class HotFragment extends Fragment {
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmeng_hot,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
    }

    private void setView(View view) {
        textView= (TextView) view.findViewById(R.id.text);
        textView.setText("这个是热点页面");
    }
}
