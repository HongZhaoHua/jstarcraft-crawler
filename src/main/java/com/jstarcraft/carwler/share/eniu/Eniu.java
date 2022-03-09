package com.jstarcraft.carwler.share.eniu;

import java.util.HashMap;
import java.util.List;
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

    private static final Map<String, Measure> aMeasures = new HashMap<>();

    private static final Map<String, Measure> hMeasures = new HashMap<>();

    private static final String regular = "^(-?[\\d\\.]+)([^\\d]+)$";

    static {
        aMeasures.put("价 格", Measure.PRICE);
        aMeasures.put("市盈率", Measure.PE);
        aMeasures.put("市净率", Measure.PB);
        aMeasures.put("股息率", Measure.DY);
        aMeasures.put("ROE", Measure.ROE);
        aMeasures.put("派息率", Measure.DP);

        hMeasures.put("前复权股价", Measure.PRICE);
        hMeasures.put("历史市盈率", Measure.PE);
        hMeasures.put("历史市净率", Measure.PB);
        hMeasures.put("历史股息率", Measure.DY);
        hMeasures.put("前复权股价", Measure.PRICE);
    }

    public static Map<Measure, String> getStock(Share share, String code) {
        if (share == Share.A) {
            // A股
            return getAB_Stock("sh" + code);
        }
        if (share == Share.B) {
            // B股
            return getAB_Stock("sz" + code);
        }
        if (share == Share.H) {
            // H股
            return getH_Stock("hk" + code);
        }
        throw new IllegalArgumentException();
    }

    // A股,B股股票
    // https://eniu.com/gu/{code}
    private static Map<Measure, String> getAB_Stock(String code) {
        Map<Measure, String> measures = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://eniu.com/gu/" + code, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        Document document = Jsoup.parse(content);
        JsoupCssSelector changyong = new JsoupCssSelector("div#changyong > p > a[title]");
        List<Element> changyongElements = changyong.selectContent(document.root());
        for (Element element : changyongElements) {
            String key = element.attr("title");
            String value = element.text();
            Measure measure = aMeasures.get(key);
            if (measure != null) {
                value = value.replaceAll(regular, "$1");
                measures.put(measure, value);
            }
        }
        JsoupCssSelector caiwu = new JsoupCssSelector("div#caiwu > p > a[title]");
        List<Element> caiwuElements = caiwu.selectContent(document.root());
        for (Element element : caiwuElements) {
            String key = element.attr("title");
            String value = element.text();
            Measure measure = aMeasures.get(key);
            if (measure != null) {
                value = value.replaceAll(regular, "$1");
                measures.put(measure, value);
            }
        }
        return measures;
    }

    // H股股票
    // https://eniu.com/gu/{code}
    private static Map<Measure, String> getH_Stock(String code) {
        Map<Measure, String> measures = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://eniu.com/gu/" + code, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        Document document = Jsoup.parse(content);
        JsoupCssSelector changyong = new JsoupCssSelector("div.panel-body > div.row > p > a[title]");
        List<Element> changyongElements = changyong.selectContent(document.root());
        for (Element element : changyongElements) {
            String key = element.attr("title");
            String value = element.text();
            Measure measure = hMeasures.get(key);
            if (measure != null) {
                value = value.replaceAll(regular, "$1");
                measures.put(measure, value);
            }
        }
        return measures;
    }

}
