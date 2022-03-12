package com.jstarcraft.carwler.trade.stock.eniu;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.carwler.trade.Measure;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 亿牛概要
 * 
 * <pre>
 * https://eniu.com
 * </pre>
 * 
 * @author Birdy
 *
 */
public enum EniuSummary {

    AB(new String[] { "价格", "市盈率", "市净率", "股息率", "派息率", "ROE" },

            new Measure[] { Measure.PRICE, Measure.PE, Measure.PB, Measure.DY, Measure.DP, Measure.ROE },

            new String[] { "div#changyong > p > a[title]", "div#caiwu > p > a[title]" }),

    H(new String[] { "前复权股价", "历史市盈率", "历史市净率", "历史股息率", "ROE" },

            new Measure[] { Measure.PRICE, Measure.PE, Measure.PB, Measure.DY, Measure.ROE },

            new String[] { "div.panel-body > div.row > p > a[title]" });

    private static final String regular = "^(-?[\\d\\.]+)([^\\d]+)$";

    private static final JsoupCssSelector industrySelector = new JsoupCssSelector("div.col-md-6 a");

    private final Map<String, Measure> name2Measures;

    private final JsoupCssSelector[] selectors;

    private EniuSummary(String[] names, Measure[] measures, String[] queries) {
        if (names.length != measures.length) {
            throw new IllegalArgumentException();
        }
        this.name2Measures = new HashMap<>();
        for (int index = 0, size = names.length; index < size; index++) {
            this.name2Measures.put(names[index], measures[index]);
        }
        int size = queries.length;
        this.selectors = new JsoupCssSelector[size];
        for (int index = 0; index < size; index++) {
            this.selectors[index] = new JsoupCssSelector(queries[index]);
        }
    }

    /**
     * 获取摘要
     * 
     * @param template
     * @param code
     * @return
     */
    public Map<Measure, String> getSummary(RestTemplate template, String code) {
        String url = StringUtility.format("https://eniu.com/gu/{}", code);
        Map<Measure, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        Document document = Jsoup.parse(content);
        // 获取指标
        for (JsoupCssSelector selector : selectors) {
            for (Element element : selector.selectContent(document.root())) {
                String key = element.attr("title").replaceAll(StringUtility.SPACE, StringUtility.EMPTY);
                String value = element.text();
                Measure measure = name2Measures.get(key);
                if (measure != null) {
                    value = value.replaceAll(regular, "$1");
                    keyValues.put(measure, value);
                }
            }
        }
        // TODO 获取名称
        document.title();
        // TODO 获取行业
        for (Element element : industrySelector.selectContent(document.root())) {
            if (element.id().isEmpty()) {
                String value = element.text();
            }
        }
        return keyValues;
    }

}
