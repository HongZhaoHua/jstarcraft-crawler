package com.jstarcraft.crawler.trade.security.bond;

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

import jodd.net.URLDecoder;

/**
 * 东方财富转债
 * 
 * 转债列表页面: https://data.eastmoney.com/kzz/default.html
 * 转债列表字段: https://data.eastmoney.com/kzz/default.html
 * 转债列表接口:https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns={column}&sortTypes={-1:降序,1:升序}&pageNumber={page}&pageSize=50&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO
 * 
 * 转债详情页面: https://data.eastmoney.com/kzz/detail/{code}.html
 * 转债详情字段:https://data.eastmoney.com/newstatic/js/kzz/detail.js
 * 转债详情接口:https://datacenter-web.eastmoney.com/api/data/v1/get?reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=&filter=(SECURITY_CODE%3D%22{code}%22)
 * 转债历史接口:https://datacenter-web.eastmoney.com/api/data/get?sty=ALL&st=date&sr=1&type=RPTA_WEB_KZZ_LS&filter=(zcode%3D%22{code}%22)&p={page}&ps=8000
 * 
 * @author Birdy
 *
 *
 */
public class EastmoneyConvertibleBond {

    protected static final Logger logger = LoggerFactory.getLogger(EastmoneyConvertibleBond.class);

    /** 转债列表模板 */
    // https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns={column}&sortTypes={-1:降序,1:升序}&pageNumber={page}&pageSize=50&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO
    private static final String pageUrl = URLDecoder.decode("https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns={}&sortTypes={}&pageNumber={}&pageSize=50&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO");

    /** 转债详情模板 */
    // https://datacenter-web.eastmoney.com/api/data/v1/get?reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=&filter=(SECURITY_CODE%3D%22{code}%22)
    private static final String bondUrl = URLDecoder.decode("https://datacenter-web.eastmoney.com/api/data/v1/get?reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=&filter=(SECURITY_CODE%3D%22{}%22)");

    /** 转债历史模板 */
    // https://datacenter-web.eastmoney.com/api/data/get?sty=ALL&st=date&sr=1&type=RPTA_WEB_KZZ_LS&filter=(zcode%3D%22{code}%22)&p={page}&ps=8000
    private static final String historyUrl = URLDecoder.decode("https://datacenter-web.eastmoney.com/api/data/get?sty=ALL&st=date&sr=1&type=RPTA_WEB_KZZ_LS&filter=(zcode%3D%22{}%22)&p={}&ps=8000");

    public static Map<String, String> getItemsByPage(RestTemplate template, int page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(pageUrl, "PUBLIC_START_DATE", -1, page);
        url = URLDecoder.decode(url);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("result").get("data").ary();
        Map<String, String> items = new LinkedHashMap<>();
        for (ONode node : nodes) {
            String id = node.get("id").getString();
            String title = node.get("title").getString();
            items.put(id, title);
        }
        return items;
    }

    public static Map<String, String> getItemByCode(RestTemplate template, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(bondUrl, code);
        url = URLDecoder.decode(url);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
//        List<ONode> nodes = root.get("result").get("data").ary();
        Map<String, String> items = new LinkedHashMap<>();
        return items;
    }

    public static Map<String, String> getHistoryByCode(RestTemplate template, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(historyUrl, code, 1);
        url = URLDecoder.decode(url);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
//        List<ONode> nodes = root.get("result").get("data").ary();
        Map<String, String> items = new LinkedHashMap<>();
        return items;
    }

}
