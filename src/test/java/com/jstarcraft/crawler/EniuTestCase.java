package com.jstarcraft.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.carwler.share.Share;
import com.jstarcraft.carwler.share.Stock;
import com.jstarcraft.carwler.share.eniu.Eniu;
import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.resource.ResourceManager;
import com.jstarcraft.core.resource.annotation.ResourceAccessor;

/**
 * 亿牛单元测试
 * 
 * https://eniu.com
 * 
 * @author Birdy
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EniuTestCase {

    @ResourceAccessor
    private ResourceManager<String, Stock> stocks;

    // 行业
    // https://eniu.com/industry/{industry}/market/{share}
    // https://eniu.com/industry/{industry}/market/{share}/json/true
    // A股sh
    // B股sz
    // H股hk
    @Test
    public void testIndustry() throws InterruptedException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://eniu.com/industry/银行/market/sh/json/true", HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(JsonUtility.prettyJson(content));
    }

    // A股股票
    // 价格:https://eniu.com/chart/pricea/{code}/t/{all,months}
    // 市盈率:https://eniu.com/chart/pea/{code}/t/{all,months}
    // 市净率:https://eniu.com/chart/pba/{code}/t/{all,months}
    // 市销率:https://eniu.com/chart/psa/{code}/t/{all,months}
    // 股息率:https://eniu.com/chart/dva/{code}/t/{all,months}
    // ROE/ROA:https://eniu.com/chart/roea/{code}/q/{0,1,2,3,4}
    // 净利润:https://eniu.com/chart/profita/{code}/q/{0,1,2,3,4}
    // 现金流:https://eniu.com/chart/cashflowa/{code}/q/{0,1,2,3,4}
    // 毛利率:https://eniu.com/chart/grossprofitmargina/{code}/q/{0,1,2,3,4}
    // 派息率:https://eniu.com/chart/pxla/{code}
    @Test
    public void testAB_History() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://eniu.com/chart/pricea/sh600000/t/1", HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(JsonUtility.prettyJson(content));
    }

    // H股股票
    // 价格:https://eniu.com/chart/priceh/{code}
    // 市盈率:https://eniu.com/chart/peh/{code}
    // 市净率:https://eniu.com/chart/pbh/{code}
    // 股息率:https://eniu.com/chart/dvh/{code}
    // ROE/ROA:https://eniu.com/chart/roeh/{code}
    // 市值:https://eniu.com/chart/marketvalueh/{code}
    @Test
    public void testH_History() {

    }

    // 列表
    // https://eniu.com/static/data/stock_list.json

    @Test
    public void testStock() {
        System.out.println(Eniu.getStock(Share.A, "600000"));
        System.out.println(Eniu.getStock(Share.H, "09988"));
    }

}
