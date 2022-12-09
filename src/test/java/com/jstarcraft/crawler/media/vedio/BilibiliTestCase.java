package com.jstarcraft.crawler.media.vedio;

import java.util.List;

import org.apache.http.impl.client.HttpClientBuilder;
import org.jaxen.Navigator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.common.selection.xpath.JaxenXpathSelector;
import com.jstarcraft.core.common.selection.xpath.html.HtmlElementNode;
import com.jstarcraft.core.common.selection.xpath.html.HtmlNavigator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 哔哩哔哩单元测试
 * 
 * @author Birdy
 *
 */
public class BilibiliTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(BilibiliTestCase.class);

    private static final JsoupCssSelector pageSelector = new JsoupCssSelector("span.cur-page");

    private static final Navigator navigator = HtmlNavigator.getInstance();

    private static final JaxenXpathSelector<HtmlElementNode> scriptSelector = new JaxenXpathSelector<>("//script[not(@*)]", navigator);

    @Test
    public void testDownload() {
        // RestTemplate支持Gzip
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        RestTemplate template = new RestTemplate(factory);
        String bvid = "BV1Zb411b7QW";
        String bilibiliUrl = "https://www.bilibili.com/video/{}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        headers.add(HttpHeaders.ACCEPT, "text/html");
        headers.add(HttpHeaders.REFERER, "www.bilibili.com");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(bilibiliUrl, bvid);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
//        if (logger.isDebugEnabled()) {
//            logger.debug(XmlUtility.prettyHtml(data));
//        }

        Document document = Jsoup.parse(data);
        String size = pageSelector.selectSingle(document.root()).text();
        System.out.println(size);

        HtmlElementNode root = new HtmlElementNode(document);
        List<HtmlElementNode> elements = scriptSelector.selectMultiple(root);
        {
            Element element = (Element) elements.get(0).getValue();
            String script = element.html();
            script = script.replaceAll("window.__playinfo__=([\\s\\S]*)", "$1");
            if (logger.isDebugEnabled()) {
                logger.debug(JsonUtility.prettyJson(script));
            }
            ONode node = ONode.load(script);
            System.out.println(node.get("data").get("dash").get("video").ary().size());
            for (ONode vedio : node.get("data").get("dash").get("video").ary()) {
                System.out.println(vedio.get("baseUrl").getString());
            }
            System.out.println(node.get("data").get("dash").get("audio").ary().size());
            for (ONode audio : node.get("data").get("dash").get("audio").ary()) {
                System.out.println(audio.get("baseUrl").getString());
            }
        }

        {
            Element element = (Element) elements.get(1).getValue();
            String script = element.html();
            script = script.replaceAll("window.__INITIAL_STATE__=([\\s\\S]*);\\(function[\\s\\S]*\\(\\)\\);", "$1");
//            if (logger.isDebugEnabled()) {
//                logger.debug(JsonUtility.prettyJson(script));
//            }
            ONode node = ONode.load(script);
            String title = node.get("videoData").get("title").getString();
            for (ONode page : node.get("videoData").get("pages").ary()) {
                String part = page.get("part").getString();
                int duration = page.get("duration").getInt();
                System.out.println(part + "-" + duration);
            }
        }
    }

}
