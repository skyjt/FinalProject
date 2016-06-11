package com.example.lujuntian.finalproject.getHttpData;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sky on 16/6/7.
 * 获取图片数据
 */

public class ImageService {
    public static byte[] getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//基于HTTP协议连接对象
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        if(conn.getResponseCode() == 200){
            InputStream inStream = conn.getInputStream();
            return StreamTool.read(inStream);
        }
        return null;
    }
}
