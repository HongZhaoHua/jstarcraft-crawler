package com.jstarcraft.carwler.share.eniu;

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

import com.jstarcraft.carwler.share.Measure;
import com.jstarcraft.carwler.share.Share;
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;

/**
 * 亿牛
 * 
 * https://eniu.com
 * 
 * @author Birdy
 *
 */
public class Eniu {

    private static final RestTemplate template = new RestTemplate();

    private static final JsoupCssSelector[] abSelectors = { new JsoupCssSelector("div#changyong > p > a[title]"), new JsoupCssSelector("div#caiwu > p > a[title]") };

    private static final JsoupCssSelector[] hSelectors = { new JsoupCssSelector("div.panel-body > div.row > p > a[title]") };

    private static final Map<String, Measure> abMeasures = new HashMap<>();

    private static final Map<String, Measure> hMeasures = new HashMap<>();

    private static final String regular = "^(-?[\\d\\.]+)([^\\d]+)$";

    private static final JsoupCssSelector industrySelector = new JsoupCssSelector("div.col-md-6 a");

    static {
        abMeasures.put("价 格", Measure.PRICE);
        abMeasures.put("市盈率", Measure.PE);
        abMeasures.put("市净率", Measure.PB);
        abMeasures.put("股息率", Measure.DY);
        abMeasures.put("ROE", Measure.ROE);
        abMeasures.put("派息率", Measure.DP);

        hMeasures.put("前复权股价", Measure.PRICE);
        hMeasures.put("历史市盈率", Measure.PE);
        hMeasures.put("历史市净率", Measure.PB);
        hMeasures.put("历史股息率", Measure.DY);
        hMeasures.put("前复权股价", Measure.PRICE);
    }

    public static Map<Measure, String> getStock(Share share, String code) {
        if (share == Share.A) {
            // A股
            return getStock("https://eniu.com/gu/sh" + code, abSelectors, abMeasures);
        }
        if (share == Share.B) {
            // B股
            return getStock("https://eniu.com/gu/sz" + code, abSelectors, abMeasures);
        }
        if (share == Share.H) {
            // H股
            return getStock("https://eniu.com/gu/hk" + code, hSelectors, hMeasures);
        }
        throw new IllegalArgumentException();
    }

    private static Map<Measure, String> getStock(String url, JsoupCssSelector[] selectors, Map<String, Measure> measures) {
        Map<Measure, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(content);
//        System.out.println(document.title());
        for (JsoupCssSelector selector : selectors) {
            for (Element element : selector.selectContent(document.root())) {
                String key = element.attr("title");
                String value = element.text();
                Measure measure = measures.get(key);
                if (measure != null) {
                    value = value.replaceAll(regular, "$1");
                    keyValues.put(measure, value);
                }
            }
        }
        for (Element element : industrySelector.selectContent(document.root())) {
            if (element.id().isEmpty()) {
                String value = element.text();
                System.out.println(value);
            }
        }
        return keyValues;
    }

}
