package com.test.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HttpUtils {

    @Resource
    private CloseableHttpClient httpClient;

    @Resource
    private ObjectMapper objectMapper;

    // ==================== GET 请求 ====================
    public String get(String url) {
        return get(url, new HashMap<>());
    }

    public String get(String url, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet, headers);
        return executeRequest(httpGet);
    }

    // ==================== POST 表单请求 ====================
    public String postForm(String url, Map<String, String> params) {
        return postForm(url, params, new HashMap<>());
    }

    public String postForm(String url, Map<String, String> params, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        setHeaders(httpPost, headers);

        List<NameValuePair> pairs = new ArrayList<>();
        params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
        httpPost.setEntity(formEntity);
        return executeRequest(httpPost);
    }

    // ==================== POST JSON 请求 ====================
    public String postJson(String url, Object body) {
        return postJson(url, body, new HashMap<>());
    }

    public String postJson(String url, Object body, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        setHeaders(httpPost, headers);

        try {
            String jsonStr = objectMapper.writeValueAsString(body);
            StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
            httpPost.setEntity(entity);
        } catch (Exception e) {
            log.error("postJson序列化失败", e);
            return null;
        }
        return executeRequest(httpPost);
    }

    // JSON请求直接转对象
    public <T> T postJsonForObj(String url, Object body, Class<T> clazz) {
        String resp = postJson(url, body);
        try {
            return objectMapper.readValue(resp, clazz);
        } catch (Exception e) {
            log.error("响应转换实体失败", e);
            return null;
        }
    }

    // ==================== 私有工具方法 ====================
    private void setHeaders(org.apache.hc.core5.http.ClassicHttpRequest request, Map<String, String> headers) {
        List<Header> headerList = new ArrayList<>();
        headers.forEach((k, v) -> headerList.add(new BasicHeader(k, v)));
        request.setHeaders(headerList.toArray(new Header[0]));
    }

    private String executeRequest(org.apache.hc.core5.http.ClassicHttpRequest request) {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // 获取响应体
            if (response.getEntity() != null) {
                return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            }
            return "";
        } catch (IOException e) {
            log.error("http请求异常 url:{}", request.getRequestUri(), e);
            return null;
        }
    }
}
