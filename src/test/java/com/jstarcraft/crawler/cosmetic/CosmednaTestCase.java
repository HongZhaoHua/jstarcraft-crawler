package com.jstarcraft.crawler.cosmetic;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
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

public class CosmednaTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(CosmednaTestCase.class);

    public static final String brandUrl = "http://www.cosmedna.com/query/";

    public static final String batchUrl = "http://www.cosmedna.com/query/?ppid={}&scph={}";

    private static final JsoupCssSelector alphabetSelector = new JsoupCssSelector("div.m-hct-lst");

    private static final JsoupCssSelector brandSelector = new JsoupCssSelector("a.js-hotcitylist");

    @Test
    public void testGetBrand() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(brandUrl);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(XmlUtility.prettyHtml(data));
        }
        Document document = Jsoup.parse(data);
        List<Element> alphabets = alphabetSelector.selectMultiple(document.root());
        for (Element alphabet : alphabets) {
            List<Element> brands = brandSelector.selectMultiple(alphabet);
            for (Element brand : brands) {
                System.out.println(brand.id());
            }
        }
    }

    @Test
    public void testGetBatch() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(batchUrl, "Shiseido/资生堂", "9294GN");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(XmlUtility.prettyHtml(data));
        }
    }

}
