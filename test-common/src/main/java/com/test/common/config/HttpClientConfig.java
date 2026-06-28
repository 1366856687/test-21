package com.test.common.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;

/**
 * HttpClient5 连接池配置
 */
@Configuration
public class HttpClientConfig {

    // 最大总连接数
    private static final int MAX_TOTAL = 200;
    // 单个路由最大连接
    private static final int MAX_PER_ROUTE = 50;
    // 连接超时 毫秒
    private static final long CONNECT_TIMEOUT = 3000;
    // 读取响应超时
    private static final long READ_TIMEOUT = 5000;
    // 从连接池获取连接等待超时
    private static final long CONNECTION_POOL_TIMEOUT = 2000;

    /**
     * 连接池管理器
     */
    @Bean(destroyMethod = "close")
    public HttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        // 全局最大连接
        manager.setMaxTotal(MAX_TOTAL);
        // 单域名最大连接
        manager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

        // 全局连接配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(CONNECT_TIMEOUT))
                .setSocketTimeout(Timeout.ofMilliseconds(READ_TIMEOUT))
                .build();
        manager.setDefaultConnectionConfig(connectionConfig);
        return manager;
    }

    /**
     * 请求全局参数
     */
    private RequestConfig requestConfig() {
        return RequestConfig.custom()
                // 从池获取连接等待超时
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(CONNECTION_POOL_TIMEOUT))
                .build();
    }

    /**
     * 注入可关闭HttpClient（单例，全局复用）
     */
    @Bean(destroyMethod = "close")
    public CloseableHttpClient httpClient(HttpClientConnectionManager connectionManager) {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig())
                .disableAutomaticRetries()
                .build();
    }

    // ===================== 工具静态方法（可选） =====================
    /**
     * 安全释放http响应资源
     */
    public static void closeResponse(HttpEntity entity) {
        if (entity != null) {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                throw new RuntimeException("释放http响应流失败", e);
            }
        }
    }
}