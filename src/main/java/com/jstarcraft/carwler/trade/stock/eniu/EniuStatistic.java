package com.jstarcraft.carwler.trade.stock.eniu;

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

import com.jstarcraft.carwler.trade.Measure;
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
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
        Map<Measure, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(peUrl, code);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(content);
        HtmlElementNode root = new HtmlElementNode(document);
//        System.out.println(document.title());
        // 获取指标
        for (HtmlElementNode node : peStatisticSelector.selectContent(root)) {
            Element element = (Element) node.getValue();
            System.out.println(element);
        }
        for (HtmlElementNode node : perentSelector.selectContent(root)) {
            Element element = (Element) node.getValue();
            System.out.println(element);
        }
        return null;
    }

    public Map<String, String> getPb() {
        Map<Measure, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(pbUrl, code);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(content);
        HtmlElementNode root = new HtmlElementNode(document);
//        System.out.println(document.title());
        // 获取指标
        for (HtmlElementNode node : pbStatisticSelector.selectContent(root)) {
            Element element = (Element) node.getValue();
            System.out.println(element);
        }
        for (HtmlElementNode node : perentSelector.selectContent(root)) {
            Element element = (Element) node.getValue();
            System.out.println(element);
        }
        return null;
    }

    public Map<String, String> getPs() {
        Map<Measure, String> keyValues = new TreeMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(psUrl, code);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(content);
        HtmlElementNode root = new HtmlElementNode(document);
//        System.out.println(document.title());
        // 获取指标
        for (HtmlElementNode node : psStatisticSelector.selectContent(root)) {
            Element element = (Element) node.getValue();
            System.out.println(element);
        }
        for (HtmlElementNode node : perentSelector.selectContent(root)) {
            Element element = (Element) node.getValue();
            System.out.println(element);
        }
        return null;
    }

}
