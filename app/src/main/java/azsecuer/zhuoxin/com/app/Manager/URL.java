package azsecuer.zhuoxin.com.app.Manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/12/26.
 */

public class URL {
    public static String GETurl="http://route.showapi.com/109-35";
    public static String GETapikey="cb5e909074633518f2c4a9a75a9f9518";
    //拼接的方法
    public static String getName(String name, int page){
        return GETurl+"?channelName="+changeUTF_8(name+"焦点")+"&page="+page+"&showapi_sign=f4d8b9116813450b94afe4618ec11a41" +
                "&showapi_appid=31615";
    }
    public static String changeUTF_8(String name){
        try {
            name= URLEncoder.encode(name,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }
    public static String getTestVideo1(){
        return "http://o9ve1mre2.bkt.clouddn.com/raw_%E6%B8%A9%E7%BD%91%E7%94%B7%E5%8D%95%E5%86%B3%E8%B5%9B.mp4";
    }

    public static String getTestVideo2(){
        return "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
    }
}
