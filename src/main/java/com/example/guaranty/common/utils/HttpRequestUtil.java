package com.example.guaranty.common.utils;

import com.alipay.api.internal.util.file.IOUtils;
import com.example.guaranty.common.exception.BusinessException;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网络请求工具
 *
 * @author HuangChao
 * @version 1.0.0
 * @date 2019/2/18
 */
public class HttpRequestUtil {

    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 70000;

    static {
        // 设置连接池
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        requestConfig = configBuilder.build();

    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url 地址
     * @return String
     */
    public static String doGet(String url) throws BusinessException {
        return doGet(url, StandardCharsets.UTF_8.name(), Maps.newHashMap());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url    地址
     * @param params 请求参数
     * @return String
     */
    public static String doGet(String url, String charset, Map<String, Object> params) throws BusinessException {
        StringBuffer param = new StringBuffer();
        if (!CollectionUtils.isEmpty(params)) {
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    param.append("?");
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(params.get(key));
                i++;
            }
            url += param;
        }
        String result = null;
        HttpClient httpClient = HttpClients.createSystem();
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                result = IOUtils.toString(inputStream, charset);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param url 地址
     * @return 数据
     */
    public static String doPost(String url) throws BusinessException {
        return doPost(url, StandardCharsets.UTF_8, Maps.newHashMap());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param url    API接口URL
     * @param params 参数map
     * @return 数据
     */
    public static String doPost(String url, Charset charset, Map<String, Object> params) throws BusinessException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        List<NameValuePair> pairList = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
            pairList.add(pair);
        }
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, charset));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, charset);
        } catch (IOException e) {
            throw new BusinessException("Request fail!!");
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param url  地址
     * @param json json对象
     * @return 数据
     */
    public static String doPost(String url, Charset charset, Object json) throws BusinessException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        StringEntity stringEntity = new StringEntity(json.toString(), charset);
        stringEntity.setContentEncoding(StandardCharsets.UTF_8.name());
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, charset);
        } catch (IOException e) {
            throw new BusinessException("Request fail!!");
        }
        return httpStr;
    }

}
