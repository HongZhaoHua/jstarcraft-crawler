package com.jstarcraft.crawler.trade.index.legulegu;

import java.net.URLDecoder;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.selection.regular.RegularSelector;
import com.jstarcraft.core.common.tuple.MapTuple;

/**
 * 乐咕乐股指数
 * 
 * 量化数据导航:https://legulegu.com/stockdata/box
 * 
 * 指数列表页面:https://legulegu.com/stockdata/indices
 * 指数估值页面:https://legulegu.com/stockdata/{code}-{ttm-lyr,pb}
 * 
 * A股股息率统计数据历史数据:https://legulegu.com/stockdata/guxilv
 * 中证行业指数列表页面:https://legulegu.com/stockdata/industry
 * 申万行业指数列表页面:https://legulegu.com/stockdata/sw-industry-overview
 * 规模、主题、策略、雪球指数列表页面:https://legulegu.com/stockdata/index-basic-overview
 * 
 * @author Birdy
 *
 */
public class LeguleguStockIndex {

    /** 指数列表模板 */
    private static final String indexUrl = "https://legulegu.com/static/js/user/user-settings.js";

    private static final RegularSelector indexSelector = new RegularSelector("download\\('([\\S]+)',\\s([\\S]+)[,\\)]", 0, 0);

    public static Map<String, MapTuple> getTuples(RestTemplate template) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange(indexUrl, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        indexSelector.matchContent(data);
        return null;
    }
    // https://legulegu.com/static/js/user/user-settings.js
    // https://legulegu.com/stockdata/market_pe/getmarket_pe?token={token}

}
