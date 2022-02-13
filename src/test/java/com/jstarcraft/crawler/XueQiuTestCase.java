package com.jstarcraft.crawler;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 雪球单元测试
 * 
 * https://xueqiu.com/
 * 
 * @author Birdy
 *
 */
public class XueQiuTestCase {

    public static void main(String[] arguments) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>((Void) null, headers);
        ResponseEntity<String> response = template.exchange("https://xueqiu.com/", HttpMethod.GET, request, String.class);
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        System.out.println(cookies);
        String token = null;
        for (String cookie : cookies) {
            if (cookie.startsWith("xq_a_token")) {
                token = cookie;
                break;
            }
        }
        headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, Arrays.asList(token));
        request = new HttpEntity<>((Void) null, headers);
        // 通过雪球股票接口获取实时股价
        response = template.exchange("https://stock.xueqiu.com/v5/stock/quote.json?symbol=SZ000651&extend=detail", HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content);
    }

}
