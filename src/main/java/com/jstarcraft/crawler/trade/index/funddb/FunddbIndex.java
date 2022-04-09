package com.jstarcraft.crawler.trade.index.funddb;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.trade.security.bond.eastmoney.EastmoneyIssueBond;

import jodd.net.URLDecoder;

/**
 * 韭圈儿指数
 * 
 * 指数列表页面:https://funddb.cn/site/index
 * 指数列表接口:https://api.jiucaishuo.com/v2/guzhi/showcategory?category_id={category}
 * 指数代码:https://github.com/akfamily/akshare/blob/master/akshare/index/zh_stock_index_csindex.py
 * 指数详情接口:https://api.jiucaishuo.com/v2/guzhi/newtubiaodata
 * {"gu_code":"{code}","pe_category":"{pe,pb,xilv}","year":{3,5,10,-1}}
 * 指数历史接口:https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata
 * {"gu_code":"{code}","pe_category":"{pe,pb,xilv}","year":{3,5,10,-1}}
 * 
 * @author Birdy
 *
 */
public class FunddbIndex {

    protected static final Logger logger = LoggerFactory.getLogger(EastmoneyIssueBond.class);

    /** 指数列表模板 */
    // https://api.jiucaishuo.com/v2/guzhi/showcategory?category_id={category}
    private static final String categoryUrl = URLDecoder.decode("https://api.jiucaishuo.com/v2/guzhi/showcategory?category_id={}");

    public static Map<String, FunddbIndex> getItemsByCategory(RestTemplate template, int category) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(categoryUrl, category);
        url = URLDecoder.decode(url);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("result").get("data").ary();
        // TODO 获取总数
        int count = root.get("result").get("count").getInt();
        Map<String, FunddbIndex> items = new LinkedHashMap<>();
//        for (ONode node : nodes) {
//            EastmoneyIssueBond bond = new EastmoneyIssueBond(node);
//            items.put(bond.getBondCode(), bond);
//        }
        return items;
    }

}
