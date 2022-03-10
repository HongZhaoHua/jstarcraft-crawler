package com.jstarcraft.crawler.trade;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.carwler.trade.Measure;
import com.jstarcraft.carwler.trade.Share;
import com.jstarcraft.carwler.trade.eniu.Eniu;
import com.jstarcraft.core.common.conversion.json.JsonUtility;

/**
 * 亿牛单元测试
 * 
 * <pre>
 * 数据源 https://eniu.com
 * 代码元 https://github.com/akfamily/akshare/blob/master/akshare/stock_feature/stock_a_indicator.py
 * </pre>
 * 
 * @author Birdy
 *
 */
public class EniuTestCase {

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
        System.out.println(Eniu.getStock(Share.SH, "601225"));
        System.out.println(Eniu.getStock(Share.SH, "600900"));
        System.out.println(Eniu.getStock(Share.SH, "600031"));
        System.out.println(Eniu.getStock(Share.SH, "601318"));
        System.out.println(Eniu.getStock(Share.SZ, "000895"));
        System.out.println(Eniu.getStock(Share.SZ, "000651"));
        System.out.println(Eniu.getStock(Share.SZ, "000063"));
        System.out.println(Eniu.getStock(Share.HK, "01810"));
        System.out.println(Eniu.getStock(Share.HK, "09988"));
        System.out.println(Eniu.getStock(Share.HK, "00700"));
        System.out.println(Eniu.getStock(Share.HK, "03333"));
        System.out.println(Eniu.getStock(Share.HK, "01448"));
    }

}
