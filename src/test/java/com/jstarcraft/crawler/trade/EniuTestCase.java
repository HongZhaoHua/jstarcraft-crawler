package com.jstarcraft.crawler.trade;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.carwler.trade.stock.eniu.EniuHistory;
import com.jstarcraft.carwler.trade.stock.eniu.EniuStatistic;
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

    // AB股股票
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

    // H股股票
    // 价格:https://eniu.com/chart/priceh/{code}
    // 市盈率:https://eniu.com/chart/peh/{code}
    // 市净率:https://eniu.com/chart/pbh/{code}
    // 股息率:https://eniu.com/chart/dvh/{code}
    // ROE/ROA:https://eniu.com/chart/roeh/{code}
    // 市值:https://eniu.com/chart/marketvalueh/{code}
    @Test
    public void testHistory() {
        RestTemplate template = new RestTemplate();

        System.out.println(EniuHistory.AB_PRICE.getHistory(template, "sh601225"));
        System.out.println(EniuHistory.AB_PE.getHistory(template, "sh601225"));
        System.out.println(EniuHistory.AB_PB.getHistory(template, "sh601225"));
        System.out.println(EniuHistory.AB_PS.getHistory(template, "sh601225"));
        System.out.println(EniuHistory.AB_DY.getHistory(template, "sh601225"));
        System.out.println(EniuHistory.AB_DP.getHistory(template, "sh601225"));
        System.out.println(EniuHistory.AB_ROA.getHistory(template, "sh601225"));
        System.out.println(EniuHistory.AB_ROE.getHistory(template, "sh601225"));

        System.out.println(EniuHistory.H_PRICE.getHistory(template, "hk09988"));
        System.out.println(EniuHistory.H_PE.getHistory(template, "hk09988"));
        System.out.println(EniuHistory.H_PB.getHistory(template, "hk09988"));
        System.out.println(EniuHistory.H_DY.getHistory(template, "hk09988"));
        System.out.println(EniuHistory.H_ROE.getHistory(template, "hk09988"));
    }

    // 列表
    // https://eniu.com/static/data/stock_list.json

    @Test
    public void testSummary() {
//        RestTemplate template = new RestTemplate();
//        System.out.println(EniuSummary.AB.getSummary(template, "sh601225").get(Measure.PB));
//        System.out.println(EniuSummary.AB.getSummary(template, "sh600900").get(Measure.PB));
//        System.out.println(EniuSummary.AB.getSummary(template, "sh600031").get(Measure.PB));
//        System.out.println(EniuSummary.AB.getSummary(template, "sh601318").get(Measure.PB));
//        System.out.println(EniuSummary.AB.getSummary(template, "sz000895").get(Measure.PB));
//        System.out.println(EniuSummary.AB.getSummary(template, "sz000651").get(Measure.PB));
//        System.out.println(EniuSummary.AB.getSummary(template, "sz000063").get(Measure.PB));
//        System.out.println(EniuSummary.H.getSummary(template, "hk01810").get(Measure.PB));
//        System.out.println(EniuSummary.H.getSummary(template, "hk09988").get(Measure.PB));
//        System.out.println(EniuSummary.H.getSummary(template, "hk00700").get(Measure.PB));
//        System.out.println(EniuSummary.H.getSummary(template, "hk03333").get(Measure.PB));
//        System.out.println(EniuSummary.H.getSummary(template, "hk01448").get(Measure.PB));
    }

    @Test
    public void testStatistic() {
//        EniuStatistic statistic = new EniuStatistic("sh600000");
//        EniuStatistic statistic = new EniuStatistic("hk00700");
//        System.out.println(statistic.getPe());
//        System.out.println(statistic.getPb());
//        System.out.println(statistic.getPs());

        System.out.println(new EniuStatistic("sh601225").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("sh600900").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("sh600031").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("sh601318").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("sz000895").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("sz000651").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("sz000063").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("hk01810").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("hk09988").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("hk00700").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("hk03333").getPb().get("历史平均"));
        System.out.println(new EniuStatistic("hk01448").getPb().get("历史平均"));
    }

}
