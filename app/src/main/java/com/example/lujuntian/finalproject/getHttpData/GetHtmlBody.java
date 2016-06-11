package com.example.lujuntian.finalproject.getHttpData;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
/**
 * 获取网页json数据
 *
 * Created by sky on 16/6/6.
 */

public class GetHtmlBody {



    public static String getHtml(String url1 ,String keyname,int Page) throws Exception {
        keyname = URLEncoder.encode(keyname, "utf-8");
        String tmpUrl=url1;
        tmpUrl = tmpUrl.replace("哈哈哈", keyname);        //替换数据获取URL内字符
        tmpUrl = tmpUrl.replace("{2}", Integer.toString(Page));

        // 通过网络地址创建URL对象
        URL url = new URL(tmpUrl);
        // 根据URL
        // 打开连接，URL.openConnection函数会根据URL的类型，返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设定URL的请求类别，有POST、GET 两类
        conn.setRequestMethod("GET");
        //设置从主机读取数据超时（单位：毫秒）
        conn.setConnectTimeout(10000);
        //设置连接主机超时（单位：毫秒）
        conn.setReadTimeout(10000);
        // 通过打开的连接读取的输入流,获取html数据
        InputStream inStream = conn.getInputStream();
        // 得到html的二进制数据
        byte[] data = StreamTool.read(inStream);
        // 是用指定的字符集解码指定的字节数组构造一个新的字符串
        String html = new String(data, "utf-8");
        return html;
    }

}
