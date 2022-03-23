package com.jstarcraft.crawler.book;

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
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.common.selection.regular.RegularSelector;
import com.jstarcraft.core.utility.StringUtility;

public class DoubanBookTestCase {

    // ISBN:https://book.douban.com/subject/{}/
    @Test
    public void testIsbn() {
        JsoupCssSelector titleSelector = new JsoupCssSelector("meta[property='og:title']");
        JsoupCssSelector isbnSelector = new JsoupCssSelector("meta[property='book:isbn']");
        JsoupCssSelector rateSelector = new JsoupCssSelector("div.rating_self > strong");
        RegularSelector tagSelector = new RegularSelector("criteria\\s*=\\s*'([\\S]*)'", 0, 1);

        String bookId = "26297606";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://book.douban.com/subject/{}/", bookId);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(XmlUtility.prettyHtml(content));

        Document document = Jsoup.parse(content);

        // 获取图书目录章节
        String id = StringUtility.format("dir_{}_full", bookId);
        Element catalogue = document.getElementById(id);
        for (String chapter : catalogue.html().split("[\\s]*<br>[\\s]*")) {
            System.out.println(chapter);
        }

        // 获取ISBN
        System.out.println(titleSelector.selectSingle(document.root()).attr("content"));
        System.out.println(isbnSelector.selectSingle(document.root()).attr("content"));

        // 获取图书评分
        System.out.println(rateSelector.selectSingle(document.root()).text());

        // 获取图书标签
        System.out.println(tagSelector.selectSingle(content));
    }

    // 标签:https:// book.douban.com/tag/{}?start={}&type={T,R,S}
    @Test
    public void testTag() {
        JsoupCssSelector itemSelector = new JsoupCssSelector("li.subject-item a[title]");
        String tag = "科普";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://book.douban.com/tag/{}?start={}&type={}", tag, 0, "T");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(XmlUtility.prettyHtml(content));

        Document document = Jsoup.parse(content);
        for (Element item : itemSelector.selectMultiple(document.root())) {
            System.out.println(item.attr("title"));
        }
    }
}
