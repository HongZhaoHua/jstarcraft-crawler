package com.jstarcraft.crawler.trade.index.funddb;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
import com.jstarcraft.core.common.reflection.TypeUtility;
import com.jstarcraft.core.common.selection.jsonpath.SnackJsonPathSelector;
import com.jstarcraft.core.common.tuple.Duet;
import com.jstarcraft.core.common.tuple.MapTuple;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.trade.Measure;
import com.jstarcraft.crawler.trade.TradeMeasure;
import com.jstarcraft.crawler.trade.index.StockIndex;
import com.jstarcraft.crawler.trade.security.bond.eastmoney.EastmoneyIssueBond;

import it.unimi.dsi.fastutil.objects.Object2FloatRBTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;
import jodd.net.URLDecoder;

/**
 * 韭圈儿指数
 * 
 * 指数列表页面(类型):https://funddb.cn/site/index
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
public class FunddbIndex implements StockIndex<FunddbConstituent, LocalDate> {

    protected static final Logger logger = LoggerFactory.getLogger(EastmoneyIssueBond.class);

    private static final Type type = TypeUtility.parameterize(ArrayList.class, FunddbConstituent.class);

    private RestTemplate template;

    private String code;

    private String name;

    private Map<String, ONode> attributes;

    private List<FunddbConstituent> constituents;

    protected FunddbIndex(RestTemplate template, String code, ONode root) {
        this.template = template;
        this.code = code;
        this.name = root.get("data").get("gu_name").getString();
        this.attributes = new HashMap<>();
        for (ONode node : root.get("data").get("top_data").ary()) {
            attributes.put(node.get("attribute").getString(), node);
        }
        this.constituents = JsonUtility.string2Object(root.get("data").get("cl_many_info").toJson(), type);
    }

    @Override
    public String getIndexCode() {
        return code;
    }

    @Override
    public String getIndexName() {
        return name;
    }

    @Override
    public Measure<LocalDate> getIndexValue() {
        ONode attribute = attributes.get("close");
        float value = attribute.get("new_value").get("value").getFloat();
        // TODO 准备支持百分位
        String percent = attribute.get("new_percent_value").get("value").getString();
        // TODO 准备支持平均数
        float average = attribute.get("new_avg").get("value").getFloat();
        Supplier<Object2FloatSortedMap<LocalDate>> history = getHistoryByCode(template, getIndexCode(), "price");
        return new TradeMeasure<LocalDate>(value, history);
    }

    @Override
    public List<FunddbConstituent> getIndexConstituents() {
        return constituents;
    }

    @Override
    public Measure<LocalDate> getPrice2Book() {
        ONode attribute = attributes.get("pb");
        float value = attribute.get("new_value").get("value").getFloat();
        // TODO 准备支持百分位
        String percent = attribute.get("new_percent_value").get("value").getString();
        // TODO 准备支持平均数
        float average = attribute.get("new_avg").get("value").getFloat();
        Supplier<Object2FloatSortedMap<LocalDate>> history = getHistoryByCode(template, getIndexCode(), "pb");
        return new TradeMeasure<LocalDate>(value, history);
    }

    @Override
    public Measure<LocalDate> getPrice2Earn() {
        ONode attribute = attributes.get("pe");
        float value = attribute.get("new_value").get("value").getFloat();
        // TODO 准备支持百分位
        String percent = attribute.get("new_percent_value").get("value").getString();
        // TODO 准备支持平均数
        float average = attribute.get("new_avg").get("value").getFloat();
        Supplier<Object2FloatSortedMap<LocalDate>> history = getHistoryByCode(template, getIndexCode(), "pe");
        return new TradeMeasure<LocalDate>(value, history);
    }

    @Override
    public Measure<LocalDate> getDividendYield() {
        ONode attribute = attributes.get("xilv");
        float value = attribute.get("new_value").get("value").getFloat();
        // TODO 准备支持百分位
        String percent = attribute.get("new_percent_value").get("value").getString();
        // TODO 准备支持平均数
        float average = attribute.get("new_avg").get("value").getFloat();
        Supplier<Object2FloatSortedMap<LocalDate>> history = getHistoryByCode(template, getIndexCode(), "dy");
        return new TradeMeasure<LocalDate>(value, history);
    }

    /**
     * 获取指数风险折溢价格
     * 
     * @return
     */
    public Measure<LocalDate> getRiskPremium() {
        ONode attribute = attributes.get("fed");
        float value = attribute.get("new_value").get("value").getFloat();
        // TODO 准备支持百分位
        String percent = attribute.get("new_percent_value").get("value").getString();
        // TODO 准备支持平均数
        float average = attribute.get("new_avg").get("value").getFloat();
        Supplier<Object2FloatSortedMap<LocalDate>> history = getHistoryByCode(template, getIndexCode(), "rp");
        return new TradeMeasure<LocalDate>(value, history);
    }

    /** 指数列表模板 */
    // https://api.jiucaishuo.com/v2/guzhi/showcategory?category_id={category}
    private static final String categoryUrl = URLDecoder.decode("https://api.jiucaishuo.com/v2/guzhi/showcategory?category_id={}");

    /**
     * 按照类型获取元组
     * 
     * @param template
     * @param category {0:全部,1:主题,2:规模,3:行业,4:风格,5:境外,6:自选}
     * @return
     */
    public static Map<String, MapTuple> getTuplesByCategory(RestTemplate template, int category) {
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
        Map<String, MapTuple> tuples = new LinkedHashMap<>();
        for (ONode node : nodes) {
            String id = node.get("id").getString();
            // TODO 考虑如何映射Tuple
            tuples.put(id, null);
        }
        return tuples;
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
        return new FunddbIndex(template, code, root);
    }

    /** 指数历史模板 */
    // https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata
    // {"gu_code":"{code}","pe_category":"{pe,pb,xilv}","year":{3,5,10,-1},"ver":"new"}
    private static final String historyUrl = URLDecoder.decode("https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata");

    private static final Map<String, Duet<String, SnackJsonPathSelector>> measures = new HashMap<>();

    static {
        measures.put("price", new Duet<>("fed", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '收盘价(点击隐藏)')].data")));
        measures.put("pb", new Duet<>("pb", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '市净率')].data")));
        measures.put("pe", new Duet<>("pe", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '市盈率')].data")));
        measures.put("dy", new Duet<>("xilv", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '股息率')].data")));
        measures.put("rp", new Duet<>("fed", new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '风险溢价')].data")));
    }

    /**
     * 按照代码获取历史
     * 
     * @param template
     * @param code
     * @param measure
     * @return
     */
    public static Supplier<Object2FloatSortedMap<LocalDate>> getHistoryByCode(RestTemplate template, String code, String measure) {
        Duet<String, SnackJsonPathSelector> duet = measures.get(measure);
        Supplier<Object2FloatSortedMap<LocalDate>> history = () -> {
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
            parameters.add("gu_code", code);
            parameters.add("pe_category", duet.getA());
            parameters.add("year", -1);
            parameters.add("ver", "new");
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
            ResponseEntity<String> response = template.exchange(historyUrl, HttpMethod.POST, request, String.class);
            String data = response.getBody();
            if (logger.isDebugEnabled()) {
                logger.debug(JsonUtility.prettyJson(data));
            }
            ONode root = ONode.load(data);
            SnackJsonPathSelector selector = duet.getB();
            root = selector.selectSingle(root);
            Object2FloatSortedMap<LocalDate> keyValues = new Object2FloatRBTreeMap<>();
            // 区分数组和对象
            if (root.isArray()) {
                // 其它历史为数组
                root.forEach((node) -> {
                    Instant instant = Instant.ofEpochMilli(node.get(0).getLong());
                    LocalDate date = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(8)).toLocalDate();
                    float value = node.get(1).getFloat();
                    keyValues.put(date, value);
                });
            } else {
                // 价格历史为对象
                root.forEach((key, node) -> {
                    Instant instant = Instant.ofEpochMilli(node.get(0).getLong());
                    LocalDate date = LocalDateTime.ofInstant(instant, ZoneOffset.ofHours(8)).toLocalDate();
                    float value = node.get(1).getFloat();
                    keyValues.put(date, value);
                });
            }
            return keyValues;
        };
        return history;
    }

}
