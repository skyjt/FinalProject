package com.example.lujuntian.finalproject.getHttpData;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by sky on 16/6/7.
 *
 * 读取输入流，得到html的二进制数据
 *
 * @return
 * @throws Exception
 */

public class StreamTool {
    public static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
