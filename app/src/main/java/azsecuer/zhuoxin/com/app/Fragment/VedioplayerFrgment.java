package azsecuer.zhuoxin.com.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import azsecuer.zhuoxin.com.app.Manager.URL;
import azsecuer.zhuoxin.com.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/2/7.
 */

public class VedioplayerFrgment extends Fragment {
    @BindView(R.id.viedeoview)
    VideoView viedeoview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_player, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viedeoview.setVideoPath(URL.getTestVideo1());//设置url
        MediaController mediaController=new MediaController(getContext());//控制器
        mediaController.setPrevNextListeners(null,null);
        viedeoview.setMediaController(mediaController);
        viedeoview.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viedeoview.stopPlayback();
    }
}
