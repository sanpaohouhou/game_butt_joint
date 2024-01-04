package com.tl.tgGame.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static HttpUtil instance;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        return getInstance(Charset.defaultCharset());
    }

    public static HttpUtil getInstance(Charset charset) {
        if (instance == null) {
            instance = new HttpUtil();
        }
        return instance;

    }

    public static void setInstance(HttpUtil instance) {
        HttpUtil.instance = instance;
    }


    public static String doGet(String url, Map<String ,Object> params){
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url.trim());
        String str = null;
        try {
            str = EntityUtils.toString(new UrlEncodedFormEntity(map2NameValuePairList(params)), "utf-8");
            String uri = httpGet.getURI().toString();
            if(uri.contains("?")){
                httpGet.setURI(new URI(uri + "&" + str));
            }else {
                httpGet.setURI(new URI(uri + "?" + str));
            }
            HttpResponse execute = client.execute(httpGet);
            return EntityUtils.toString(execute.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String doPost(String url,Map<String ,Object> map){
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        String str = null;
        try {
            str = EntityUtils.toString(new UrlEncodedFormEntity(map2NameValuePairList(map)), "utf-8");
            StringEntity entity = new StringEntity(str);
            httpPost.setEntity(entity);
            CloseableHttpResponse execute = client.execute(httpPost);
            return EntityUtils.toString(execute.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String doPost(String url,Map<String ,Object> map,String charset){
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        String str = null;
        try {
            str = EntityUtils.toString(new UrlEncodedFormEntity(map2NameValuePairList(map)), charset);
            StringEntity entity = new StringEntity(str, ContentType.create("multipart/form-data",charset));
            httpPost.setEntity(entity);
            CloseableHttpResponse execute = client.execute(httpPost);
            return EntityUtils.toString(execute.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String url,Map<String ,Object> map,String charset,String contentType){
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        String str = null;
        try {
            str = EntityUtils.toString(new UrlEncodedFormEntity(map2NameValuePairList(map)), "utf-8");
            StringEntity entity = new StringEntity(str,ContentType.create(contentType,charset));
            httpPost.setEntity(entity);
            CloseableHttpResponse execute = client.execute(httpPost);
            return EntityUtils.toString(execute.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





    //将map转为list
    public static List<BasicNameValuePair> map2NameValuePairList(Map<String ,Object> map){
        List<BasicNameValuePair> list = new ArrayList<>();
        if(!StringUtils.isEmpty(map)){
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                if(!StringUtils.isEmpty(key)){
                    list.add(new BasicNameValuePair(key,String.valueOf(map.get(key))));
                }
            }
        }
        return list;
    }


}
