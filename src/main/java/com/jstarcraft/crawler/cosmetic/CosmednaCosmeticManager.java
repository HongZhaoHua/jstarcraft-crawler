package com.jstarcraft.crawler.cosmetic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.utility.StringUtility;

public class CosmednaCosmeticManager {

    protected static final Logger logger = LoggerFactory.getLogger(CosmednaCosmeticManager.class);

    private static final String brandUrl = "http://www.cosmedna.com/query/";

    private static final String batchUrl = "http://www.cosmedna.com/query/?ppid={}&scph={}";

    private static final JsoupCssSelector alphabetSelector = new JsoupCssSelector("div.m-hct-lst");

    private static final JsoupCssSelector brandSelector = new JsoupCssSelector("a.js-hotcitylist");

    // 化妆品正则表达式
    private static final Pattern cosmeticPattern = Pattern.compile("(\\S+)\\s+批号：(\\S+)\\s生产日期：(\\d+年\\d+月\\d+日)\\s过期日期：(\\d+年\\d+月\\d+日)");

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private RestTemplate template;

    public CosmednaCosmeticManager(RestTemplate template) {
        this.template = template;
    }

    public Map<String, Set<String>> getBrands() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(brandUrl);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(XmlUtility.prettyHtml(data));
        }
        Map<String, Set<String>> keyValues = new LinkedHashMap<>();
        Document document = Jsoup.parse(data);
        List<Element> alphabets = alphabetSelector.selectMultiple(document.root());
        for (Element alphabet : alphabets) {
            String key = alphabet.getElementsByTag("dt").text();
            Set<String> values = new TreeSet<>();
            List<Element> brands = brandSelector.selectMultiple(alphabet);
            for (Element brand : brands) {
                values.add(brand.id());
            }
            keyValues.put(key, values);
        }
        return keyValues;
    }

    public CosmednaCosmetic getCosmetic(String brand, String batch) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(batchUrl, brand, batch);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(XmlUtility.prettyHtml(data));
        }
        Document document = Jsoup.parse(data);
        String content = document.text();
        Matcher matcher = cosmeticPattern.matcher(content);
        if (matcher.find()) {
            LocalDate make = LocalDate.parse(matcher.group(3), formatter);
            LocalDate expire = LocalDate.parse(matcher.group(4), formatter);
            return new CosmednaCosmetic(brand, batch, make, expire);
        } else {
            return null;
        }
    }

}
