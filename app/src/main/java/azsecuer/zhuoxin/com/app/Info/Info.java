package azsecuer.zhuoxin.com.app.Info;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Info {
   public String title,iconurl, time,type,url;

    public Info(String type, String time) {
        this.type = type;
        this.time = time;
    }

    public Info(String title, String iconurl, String time, String type, String url) {
        this.title = title;
        this.iconurl = iconurl;
        this.time = time;
        this.type = type;
        this.url=url;
    }

    @Override
    public String toString() {
        return "Info{" +
                "title='" + title + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
