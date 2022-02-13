package com.jstarcraft.crawler;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 亿牛单元测试
 * 
 * @author Birdy
 *
 */
public class EniuTestCase {

    // 行业
    // https://eniu.com/industry/{industry}/market/sh
    // https://eniu.com/industry/{industry}/market/sh/json/true
    @Test
    public void testIndustry() throws InterruptedException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://eniu.com/industry/石油行业/market/sh/json/true", HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(content);

        response = template.exchange("https://eniu.com/industry/石油行业/market/sh", HttpMethod.GET, request, String.class);
        content = response.getBody();
        System.out.println(content.length());
        System.out.println(content);
    }

    // 股票
    // https://eniu.com/gu/{code}
    // 价格:https://eniu.com/chart/pricea/{code}/t/all
    // 市盈率:https://eniu.com/chart/pea/{code}/t/all
    // 市净率:https://eniu.com/chart/pba/{code}/t/all
    // 市销率:https://eniu.com/chart/psa/{code}/t/all
    // 股息率:https://eniu.com/chart/dva/{code}/t/all
    // 派息率:https://eniu.com/chart/pxla/{code}
    // ROE/ROA:https://eniu.com/chart/roea/{code}/q/0
    @Test
    public void test() {

    }

    // 列表
    // https://eniu.com/static/data/stock_list.json

}
