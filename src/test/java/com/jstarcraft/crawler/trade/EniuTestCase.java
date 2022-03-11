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
import com.jstarcraft.carwler.trade.eniu.EniuNow;
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
    // 派息率:https://eniu.com/chart/pxla/{code}
    // ROE/ROA:https://eniu.com/chart/roea/{code}/q/{0,1,2,3,4}
    // 净利润:https://eniu.com/chart/profita/{code}/q/{0,1,2,3,4}
    // 现金流:https://eniu.com/chart/cashflowa/{code}/q/{0,1,2,3,4}
    // 毛利率:https://eniu.com/chart/grossprofitmargina/{code}/q/{0,1,2,3,4}
    @Test
    public void testAB_History() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://eniu.com/chart/psa/sh600000/t/1", HttpMethod.GET, request, String.class);
        String content = response.getBody();
//        System.out.println(Eniu.getBefore(Share.SH, "600000", Measure.PRICE));
//        System.out.println(Eniu.getBefore(Share.SH, "600000", Measure.PE));
//        System.out.println(Eniu.getBefore(Share.SH, "600000", Measure.PB));
//        System.out.println(Eniu.getBefore(Share.SH, "600000", Measure.PS));
        System.out.println(Eniu.getBefore(Share.SH, "600000", Measure.DY));
        System.out.println(Eniu.getBefore(Share.SH, "600000", Measure.DP));
//        System.out.println(Eniu.getBefore(Share.SH, "600000", Measure.ROE));

        System.out.println(Eniu.getBefore(Share.HK, "09988", Measure.ROE));
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
        RestTemplate template = new RestTemplate();
        System.out.println(EniuNow.SH.getMeasures(template, "601225").get(Measure.PRICE));
        System.out.println(EniuNow.SH.getMeasures(template, "600900").get(Measure.PRICE));
        System.out.println(EniuNow.SH.getMeasures(template, "600031").get(Measure.PRICE));
        System.out.println(EniuNow.SH.getMeasures(template, "601318").get(Measure.PRICE));
        System.out.println(EniuNow.SZ.getMeasures(template, "000895").get(Measure.PRICE));
        System.out.println(EniuNow.SZ.getMeasures(template, "000651").get(Measure.PRICE));
        System.out.println(EniuNow.SZ.getMeasures(template, "000063").get(Measure.PRICE));
        System.out.println(EniuNow.HK.getMeasures(template, "01810").get(Measure.PRICE));
        System.out.println(EniuNow.HK.getMeasures(template, "09988").get(Measure.PRICE));
        System.out.println(EniuNow.HK.getMeasures(template, "00700").get(Measure.PRICE));
        System.out.println(EniuNow.HK.getMeasures(template, "03333").get(Measure.PRICE));
        System.out.println(EniuNow.HK.getMeasures(template, "01448").get(Measure.PRICE));
    }

}
