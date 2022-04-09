package com.jstarcraft.crawler.trade.index.funddb;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.selection.jsonpath.SnackJsonPathSelector;
import com.jstarcraft.core.common.tuple.Duet;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.trade.Measure;
import com.jstarcraft.crawler.trade.index.StockIndex;
import com.jstarcraft.crawler.trade.security.bond.eastmoney.EastmoneyIssueBond;
import com.jstarcraft.crawler.trade.security.stock.Stock;

import it.unimi.dsi.fastutil.objects.Object2FloatRBTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;
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
public class FunddbIndex implements StockIndex {

    protected static final Logger logger = LoggerFactory.getLogger(EastmoneyIssueBond.class);

    private ONode node;

    protected FunddbIndex(ONode node) {
        this.node = node;
    }

    @Override
    public String getIndexCode() {
        return node.get("gu_code").getString();
    }

    @Override
    public String getIndexName() {
        return node.get("gu_code").getString();
    }

    @Override
    public Measure getIndexValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Stock> getConstituentStocks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Measure<LocalDate> getPrice2Book() {
        return null;
    }

    @Override
    public Measure<LocalDate> getPrice2Earn() {
        return null;
    }

    @Override
    public Measure<Year> getDividendYield() {
        // TODO Auto-generated method stub
        return null;
    }

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
        List<ONode> nodes = root.get("data").get("right_list").ary();
        // TODO 获取总数
        int count = nodes.size();
        Map<String, FunddbIndex> items = new LinkedHashMap<>();
//        for (ONode node : nodes) {
//            EastmoneyIssueBond bond = new EastmoneyIssueBond(node);
//            items.put(bond.getBondCode(), bond);
//        }
        return items;
    }

    /** 指数详情模板 */
    // https://api.jiucaishuo.com/v2/guzhi/newtubiaodata
    // {"gu_code":"{code}","year":{3,5,10,-1},"ver":"new"}
    private static final String indexUrl = URLDecoder.decode("https://api.jiucaishuo.com/v2/guzhi/newtubiaodata");

    public static FunddbIndex getIndexByCode(RestTemplate template, String code) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("gu_code", code);
        parameters.add("year", -1);
        parameters.add("ver", "new");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = template.exchange("https://api.jiucaishuo.com/v2/guzhi/newtubiaodata", HttpMethod.POST, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        root.get("data").get("top_data");
        root.get("data").get("cl_many_info");
        return null;
    }

    /** 指数历史模板 */
    // https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata
    // {"gu_code":"{code}","pe_category":"{pe,pb,xilv}","year":{3,5,10,-1},"ver":"new"}
    private static final String historyUrl = URLDecoder.decode("https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata");

    private static final Map<String, Duet<String, SnackJsonPathSelector>> measures = new HashMap<>();

    static {
        measures.put("pb", new Duet<>("pb", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '市净率')].data")));
        measures.put("pe", new Duet<>("pe", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '市盈率')].data")));
        measures.put("dy", new Duet<>("xilv", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '股息率')].data")));
    }

    public static Supplier<Object2FloatSortedMap<LocalDateTime>> getHistoryByCode(RestTemplate template, String code, String measure) {
        Duet<String, SnackJsonPathSelector> duet = measures.get(measure);
        Supplier<Object2FloatSortedMap<LocalDateTime>> history = () -> {
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
            parameters.add("gu_code", code);
            parameters.add("pe_category", duet.getA());
            parameters.add("year", -1);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
            ResponseEntity<String> response = template.exchange(historyUrl, HttpMethod.POST, request, String.class);
            String data = response.getBody();
            if (logger.isDebugEnabled()) {
                logger.debug(JsonUtility.prettyJson(data));
            }
            ONode root = ONode.load(data);
            SnackJsonPathSelector selector = duet.getB();
            List<ONode> nodes = selector.selectSingle(root).ary();
            Object2FloatSortedMap<LocalDateTime> keyValues = new Object2FloatRBTreeMap<>();
            for (ONode node : nodes) {
                Instant instant = Instant.ofEpochMilli(node.get(0).getLong());
                LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(8));
                float value = node.get(1).getFloat();
                keyValues.put(date, value);
            }
            return keyValues;
        };
        return history;
    }

}
