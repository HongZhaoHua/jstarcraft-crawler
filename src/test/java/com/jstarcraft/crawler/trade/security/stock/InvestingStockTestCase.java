package com.jstarcraft.crawler.trade.security.stock;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jaxen.Navigator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.selection.xpath.JaxenXpathSelector;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlElementNode;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlNavigator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 英为财情股票单元测试
 * 
 * <pre>
 * https://api.investing.com/api/search/v2/search?q={isin}
 * https://cn.investing.com
 * https://cn.investing.com/stock-screener/
 * https://cn.investing.com/equities/{}
 * 简介:https://cn.investing.com/equities/{}-company-profile
 * 历史:https://cn.investing.com/equities/{}-historical-data
 * 现金流:https://cn.investing.com/equities/{}-cash-flow
 * 比率:https://cn.investing.com/equities/{}-ratios
 * 股息:https://cn.investing.com/equities/{}-dividends
 * </pre>
 * 
 * @author Birdy
 *
 */
public class InvestingStockTestCase {

    @Test
    public void testHistory() {
        // 代理
        final String host = "127.0.0.1";
        final int port = 1080;
        HttpHost proxy = new HttpHost(host, port);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setProxy(proxy);
        HttpClient client = builder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);
        try {
            RestTemplate template = new RestTemplate(factory);
            // 拦截器
            template.getInterceptors().add(new ClientHttpRequestInterceptor() {

                @Override
                public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                    ClientHttpResponse response = execution.execute(request, body);
                    return response;
                }

            });
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
            ResponseEntity<String> response = template.exchange("https://cn.investing.com/equities/gree-electric-a-historical-data", HttpMethod.GET, request, String.class);
            String content = response.getBody();
            {
                Navigator navigator = HtmlNavigator.getInstance();
                Document document = Jsoup.parse(content);
                HtmlElementNode root = new HtmlElementNode(document);
                JaxenXpathSelector<HtmlElementNode> selector = new JaxenXpathSelector<>("//script[contains(string(), 'window.histDataExcessInfo')]", navigator);
                List<HtmlElementNode> scripts = selector.selectMultiple(root);
                Element element = (Element) scripts.get(0).getValue();
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(element.data());
                if (matcher.find()) {
                    System.out.println("pairId is: " + matcher.group(0));
                }
                if (matcher.find()) {
                    System.out.println("smlId is: " + matcher.group(0));
                }
            }

            headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            headers.add(HttpHeaders.ACCEPT, "text/plain, */*; q=0.01");
            headers.add(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
            headers.add(HttpHeaders.ORIGIN, "https://cn.investing.com");
            headers.add(HttpHeaders.REFERER, "https://cn.investing.com");
            headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
            headers.add("x-requested-with", "XMLHttpRequest");
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
            parameters.add("curr_id", "13994");
//            parameters.add("smlID", "1550180");
            parameters.add("header", "000651历史数据");
            parameters.add("st_date", "2021/04/15");
            parameters.add("end_date", "2021/04/20");
            parameters.add("interval_sec", "Daily");// Weekly,Monthly
            parameters.add("sort_col", "date");
            parameters.add("sort_ord", "DESC");
            parameters.add("action", "historical_data");
            request = new HttpEntity<>(parameters, headers);
            // 通过英为股票接口获取历史股价
            response = template.exchange("https://cn.investing.com/instruments/HistoricalDataAjax", HttpMethod.POST, request, String.class);
            content = response.getBody();
//            System.out.println(content);

            Navigator navigator = HtmlNavigator.getInstance();
            Document document = Jsoup.parse(content);
            HtmlElementNode root = new HtmlElementNode(document);
            JaxenXpathSelector<HtmlElementNode> selector = new JaxenXpathSelector<>("//tr", navigator);
            List<HtmlElementNode> trs = selector.selectMultiple(root);
            for (HtmlElementNode tr : trs) {
                Element element = (Element) tr.getValue();
                for (Element td : element.children()) {
                    System.out.println(td.text());
                }
            }
        } catch (HttpStatusCodeException exception) {
            System.err.println(exception.getStatusCode());
            System.err.println(exception.getResponseBodyAsString());
        }
    }

    @Test
    public void testSearch() {
        final String host = "127.0.0.1";
        final int port = 1080;
        HttpHost proxy = new HttpHost(host, port);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setProxy(proxy);
        HttpClient client = builder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);
        RestTemplate template = new RestTemplate(factory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Domain-ID", "cn");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://api.investing.com/api/search/v2/search?q={}", "KYG875721634");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(JsonUtility.prettyJson(content));
    }

}
