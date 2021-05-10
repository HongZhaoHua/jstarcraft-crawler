package com.jstarcraft.crawler;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * 英为单元测试
 * 
 * @author Birdy
 *
 */
public class InvestingTestCase {

    public static void main(String[] arguments) {
        final String host = "127.0.0.1";
        final int port = 1080;
        HttpHost proxy = new HttpHost(host, port);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setProxy(proxy);
        HttpClient client = builder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);
        try {
            RestTemplate template = new RestTemplate(factory);
            // 拦截器
            template.getInterceptors().add(new ClientHttpRequestInterceptor() {

                @Override
                public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                    ClientHttpResponse response = execution.execute(request, body);
                    return response;
                }

            });
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            headers.add(HttpHeaders.ACCEPT, "text/plain, */*; q=0.01");
            headers.add(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
            headers.add(HttpHeaders.ORIGIN, "https://cn.investing.com");
            headers.add(HttpHeaders.REFERER, "https://cn.investing.com");
            headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
            headers.add("x-requested-with", "XMLHttpRequest");
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
            parameters.add("curr_id", "944220");
            parameters.add("smlID", "1550189");
            parameters.add("header", "000651历史数据");
            parameters.add("st_date", "2021/04/15");
            parameters.add("end_date", "2021/04/20");
            parameters.add("interval_sec", "Daily");
            parameters.add("sort_col", "date");
            parameters.add("sort_ord", "DESC");
            parameters.add("action", "historical_data");
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
            // 通过英为股票接口获取历史股价
            ResponseEntity<String> response = template.exchange("https://cn.investing.com/instruments/HistoricalDataAjax", HttpMethod.POST, request, String.class);
            String content = response.getBody();
            System.out.println(content);
        } catch (HttpStatusCodeException exception) {
            System.out.println(exception.getStatusCode());
            System.out.println(exception.getResponseBodyAsString());
        }

    }

}
