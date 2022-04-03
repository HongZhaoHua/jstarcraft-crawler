package com.jstarcraft.crawler.trade.security.stock.eniu;

import java.util.Map;
import java.util.TreeMap;

import org.jaxen.Navigator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.selection.xpath.JaxenXpathSelector;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlElementNode;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlNavigator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 亿牛统计
 * 
 * <pre>
 * https://eniu.com
 * </pre>
 * 
 * @author Birdy
 *
 */
public class EniuStatistic {

    private static final RestTemplate template = new RestTemplate();

    private static final String peUrl = "https://eniu.com/gu/{}/pe_ttm";

    private static final String pbUrl = "https://eniu.com/gu/{}/pb";

    private static final String psUrl = "https://eniu.com/gu/{}/ps";

    private static final Navigator navigator = HtmlNavigator.getInstance();

    private static final JaxenXpathSelector<HtmlElementNode> peStatisticSelector = new JaxenXpathSelector<>("//h3[text()='市盈率统计']/parent::div/following-sibling::div[@class='panel-body']//div[@class='col-xs-6 col-md-3']", navigator);

    private static final JaxenXpathSelector<HtmlElementNode> pbStatisticSelector = new JaxenXpathSelector<>("//h3[text()='市净率统计']/parent::div/following-sibling::div[@class='panel-body']//div[@class='col-xs-6 col-md-3']", navigator);

    private static final JaxenXpathSelector<HtmlElementNode> psStatisticSelector = new JaxenXpathSelector<>("//h3[text()='市销率统计']/parent::div/following-sibling::div[@class='panel-body']//div[@class='col-xs-6 col-md-3']", navigator);

    private static final JaxenXpathSelector<HtmlElementNode> perentSelector = new JaxenXpathSelector<>("//h3[@class]/a[@target='_blank']/parent::h3/parent::div/following-sibling::div[@class='panel-body']//div[@class='col-xs-6 col-md-3']", navigator);

    private final String code;

    public EniuStatistic(String code) {
        this.code = code;
    }

    public Map<String, String> getPe() {
        Map<String, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(peUrl, code);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
//        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(data);
        HtmlElementNode root = new HtmlElementNode(document);
//        System.out.println(document.title());
        // 获取指标
        for (HtmlElementNode node : peStatisticSelector.selectMultiple(root)) {
            Element element = (Element) node.getValue();
            keyValues.put(element.getElementsByTag("span").text(), element.getElementsByTag("h3").text());
        }
        for (HtmlElementNode node : perentSelector.selectMultiple(root)) {
            Element element = (Element) node.getValue();
            String[] text = element.getElementsByTag("p").text().split("：");
            keyValues.put(text[0], text[1]);
        }
        return keyValues;
    }

    public Map<String, String> getPb() {
        Map<String, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(pbUrl, code);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
//        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(data);
        HtmlElementNode root = new HtmlElementNode(document);
//        System.out.println(document.title());
        // 获取指标
        for (HtmlElementNode node : pbStatisticSelector.selectMultiple(root)) {
            Element element = (Element) node.getValue();
            keyValues.put(element.getElementsByTag("span").text(), element.getElementsByTag("h3").text());
        }
        for (HtmlElementNode node : perentSelector.selectMultiple(root)) {
            Element element = (Element) node.getValue();
            String[] text = element.getElementsByTag("p").text().split("：");
            keyValues.put(text[0], text[1]);
        }
        return keyValues;
    }

    public Map<String, String> getPs() {
        Map<String, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(psUrl, code);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
//        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(data);
        HtmlElementNode root = new HtmlElementNode(document);
//        System.out.println(document.title());
        // 获取指标
        for (HtmlElementNode node : psStatisticSelector.selectMultiple(root)) {
            Element element = (Element) node.getValue();
            keyValues.put(element.getElementsByTag("span").text(), element.getElementsByTag("h3").text());
        }
        for (HtmlElementNode node : perentSelector.selectMultiple(root)) {
            Element element = (Element) node.getValue();
            String[] text = element.getElementsByTag("p").text().split("：");
            keyValues.put(text[0], text[1]);
        }
        return keyValues;
    }

}
