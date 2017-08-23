package com.zhongyaogang.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lidroid.xutils.HttpUtils;

public class SystemUtil {
    public static HttpUtils getHttpUtils()
    {
        HttpUtils hu = new HttpUtils();
        hu.configResponseTextCharset("UTF-8");//获得服务器应答数据的编码一致！
        return hu;
    }
    /**
     * 工具方法  把流里面的内容转换成字符串
     * @param is
     * @return
     * @throws IOException
     */
    public static String readStream(InputStream is) throws IOException{
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        while(( len = is.read(buffer))!=-1){
            baos.write(buffer, 0, len);
        }
        is.close();
        String result = baos.toString("UTF-8");//utf-8
        System.out.println(baos.toString("UTF-8"));
        baos.close();
        return result;
    }
    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }
}
