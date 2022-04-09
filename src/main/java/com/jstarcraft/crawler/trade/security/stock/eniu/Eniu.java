package com.jstarcraft.crawler.trade.security.stock.eniu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.noear.snack.ONode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.trade.security.Exchange;
import com.jstarcraft.crawler.trade.security.stock.StockMeasure;

import it.unimi.dsi.fastutil.longs.Long2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;

/**
 * 亿牛
 * 
 * <pre>
 * https://eniu.com
 * </pre>
 * 
 * @author Birdy
 *
 */
public class Eniu {

    private static final RestTemplate template = new RestTemplate();

    private static final JsoupCssSelector[] abSelectors = { new JsoupCssSelector("div#changyong > p > a[title]"), new JsoupCssSelector("div#caiwu > p > a[title]") };

    private static final JsoupCssSelector[] hSelectors = { new JsoupCssSelector("div.panel-body > div.row > p > a[title]") };

    private static final String regular = "^(-?[\\d\\.]+)([^\\d]+)$";

    private static final JsoupCssSelector industrySelector = new JsoupCssSelector("div.col-md-6 a");

    private static final Map<String, StockMeasure> abName2Measures = new HashMap<>();

    private static final Map<String, StockMeasure> hName2Measures = new HashMap<>();

    static {
        abName2Measures.put("价 格", StockMeasure.PRICE);
        abName2Measures.put("市盈率", StockMeasure.PE);
        abName2Measures.put("市净率", StockMeasure.PB);
        abName2Measures.put("股息率", StockMeasure.DY);
        abName2Measures.put("派息率", StockMeasure.DP);
        abName2Measures.put("ROE", StockMeasure.ROE);

        hName2Measures.put("前复权股价", StockMeasure.PRICE);
        hName2Measures.put("历史市盈率", StockMeasure.PE);
        hName2Measures.put("历史市净率", StockMeasure.PB);
        hName2Measures.put("历史股息率", StockMeasure.DY);
        hName2Measures.put("前复权股价", StockMeasure.PRICE);
    }

    private static String getId(Exchange share, String code) {
        if (share == Exchange.SH) {
            // A股
            return "sh" + code;
        }
        if (share == Exchange.SZ) {
            // B股
            return "sz" + code;
        }
        if (share == Exchange.HK) {
            // H股
            return "hk" + code;
        }
        throw new IllegalArgumentException();
    }

    public static Map<StockMeasure, String> getNow(Exchange share, String code) {
        String url = StringUtility.format("https://eniu.com/gu/{}", getId(share, code));
        if (share == Exchange.SH) {
            // A股
            return getStock(url, abSelectors, abName2Measures);
        }
        if (share == Exchange.SZ) {
            // B股
            return getStock(url, abSelectors, abName2Measures);
        }
        if (share == Exchange.HK) {
            // H股
            return getStock(url, hSelectors, hName2Measures);
        }
        throw new IllegalArgumentException();
    }

    private static Map<StockMeasure, String> getStock(String url, JsoupCssSelector[] selectors, Map<String, StockMeasure> measures) {
        Map<StockMeasure, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(XmlUtility.prettyHtml(data));
        Document document = Jsoup.parse(data);
//        System.out.println(document.title());
        // 获取指标
        for (JsoupCssSelector selector : selectors) {
            for (Element element : selector.selectMultiple(document.root())) {
                String key = element.attr("title");
                String value = element.text();
                StockMeasure measure = measures.get(key);
                if (measure != null) {
                    value = value.replaceAll(regular, "$1");
                    keyValues.put(measure, value);
                }
            }
        }
        // 获取行业
        for (Element element : industrySelector.selectMultiple(document.root())) {
            if (element.id().isEmpty()) {
                String value = element.text();
//                System.out.println(value);
            }
        }
        return keyValues;
    }

    private static final Map<StockMeasure, String[]> abMeasure2Names = new HashMap<>();

    private static final Map<StockMeasure, String[]> hMeasure2Names = new HashMap<>();

    private static final Map<String, DateTimeFormatter> formatters = new HashMap<>();

    static {
        abMeasure2Names.put(StockMeasure.PRICE, new String[] { "https://eniu.com/chart/pricea/{}/t/1", "date", "price", "yyyy-MM-dd" });
        abMeasure2Names.put(StockMeasure.PE, new String[] { "https://eniu.com/chart/pea/{}/t/1", "date", "pe_ttm", "yyyy-MM-dd" });
        abMeasure2Names.put(StockMeasure.PB, new String[] { "https://eniu.com/chart/pba/{}/t/1", "date", "pb", "yyyy-MM-dd" });
        abMeasure2Names.put(StockMeasure.PS, new String[] { "https://eniu.com/chart/psa/{}/t/1", "date", "ps", "yyyy-MM-dd" });
        abMeasure2Names.put(StockMeasure.DY, new String[] { "https://eniu.com/chart/dva/{}/t/1", "date", "dv", "yyyy-MM-dd" });
        abMeasure2Names.put(StockMeasure.DP, new String[] { "https://eniu.com/chart/pxla/{}", "fhnd", "pxl", "yyyy" });
        abMeasure2Names.put(StockMeasure.ROA, new String[] { "https://eniu.com/chart/roea/{}/q/0", "date", "roa", "yyyy" });
        abMeasure2Names.put(StockMeasure.ROE, new String[] { "https://eniu.com/chart/roea/{}/q/0", "date", "roe", "yyyy" });

        hMeasure2Names.put(StockMeasure.PRICE, new String[] { "https://eniu.com/chart/priceh/{}", "date", "price", "yyyy-MM-dd" });
        hMeasure2Names.put(StockMeasure.PE, new String[] { "https://eniu.com/chart/peh/{}", "date", "pe", "yyyy-MM-dd" });
        hMeasure2Names.put(StockMeasure.PB, new String[] { "https://eniu.com/chart/pbh/{}", "date", "pb", "yyyy-MM-dd" });
        hMeasure2Names.put(StockMeasure.DY, new String[] { "https://eniu.com/chart/dvh/{}", "date", "dv", "yyyy-MM-dd" });
        hMeasure2Names.put(StockMeasure.ROE, new String[] { "https://eniu.com/chart/roeh/{}", "date", "roe", "yyyy" });

        formatters.put("yyyy", DateTimeFormatter.ofPattern("yyyy"));
        formatters.put("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static Long2FloatMap getBefore(Exchange share, String code, StockMeasure measure) {
        String[] names = null;
        if (share == Exchange.SH || share == Exchange.SZ) {
            // A股
            // B股
            names = abMeasure2Names.get(measure);
        }
        if (share == Exchange.HK) {
            // H股
            names = hMeasure2Names.get(measure);
        }

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(names[0], getId(share, code));
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(data.length());
        System.out.println(JsonUtility.prettyJson(data));
        ONode root = ONode.load(data);
        List<ONode> dates = root.get(names[1]).ary();
        List<ONode> values = root.get(names[2]).ary();
        Long2FloatMap map = new Long2FloatAVLTreeMap();
        int size = dates.size();
        for (int index = 0; index < size; index++) {
            LocalDate date = LocalDate.parse(dates.get(index).getString(), formatters.get(names[3]));
            long day = date.toEpochDay();
            float value = values.get(index).getFloat();
            map.put(day, value);
        }
        return map;
    }

}
