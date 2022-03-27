package com.jstarcraft.crawler.trade.security.stock;

import org.jaxen.Navigator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.xpath.JaxenXpathSelector;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlElementNode;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlNavigator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 富途牛牛股票单元测试
 * 
 * https://www.futunn.com/
 * 
 * @author Birdy
 *
 */
public class FutonnStockTestCase {

    private static final Navigator navigator = HtmlNavigator.getInstance();

    private static final JaxenXpathSelector<HtmlElementNode> isinSelector = new JaxenXpathSelector<>("//div[@class='company-item']//div[text()='ISIN代码']/following-sibling::div", navigator);

    // ISIN:https://www.futunn.com/stock/{code}-HK/company-profile
    @Test
    public void testIsin() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://www.futunn.com/stock/{}-HK/company-profile", "00700");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(content);
        HtmlElementNode root = new HtmlElementNode(document);
        Element isin = (Element) isinSelector.selectSingle(root).getValue();
        System.out.println(isin.text());
    }

}
