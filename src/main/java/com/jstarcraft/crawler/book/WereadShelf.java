package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.noear.snack.ONode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 微信书架
 * 
 * @author Birdy
 *
 */
public class WereadShelf {

    /** 书架路径模板 */
    private static final String shelfUrl = "https://weread.qq.com/web/shelf";

    private static final JsoupCssSelector scriptSelector = new JsoupCssSelector("script[nonce]");

    private final RestTemplate template;

    private final String cookie;

    public WereadShelf(RestTemplate template, String cookie) {
        this.template = template;
        this.cookie = cookie;
    }

    /**
     * 获取档案
     * 
     * @return
     */
    public Map<String, WereadArchive> getArchives() {
        return null;
    }

    /**
     * 获取笔记
     * 
     * @return
     */
    public Map<String, WereadNote> getNodes() {
        return null;
    }

    public void update(Instant instant) {
        List<String> cookies = Arrays.asList(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format(shelfUrl);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        Document document = Jsoup.parse(data);
        String script = scriptSelector.selectSingle(document.root()).html();
        script = script.replaceAll("window.__INITIAL_STATE__=([\\s\\S]*);\\(function[\\s\\S]*\\(\\)\\);", "$1");
        System.out.println(data.length());
        System.out.println(JsonUtility.prettyJson(script));
        ONode root = ONode.load(script);
        List<ONode> archives = root.get("shelf").get("archive").ary();
        for (ONode archive : archives) {
            String name = archive.get("name").getString();
            List<ONode> nodes = archive.get("bookInfos").ary();
            ArrayList<KeyValue<String, String>> books = new ArrayList<>(nodes.size());
            for (ONode book : nodes) {
                String id = book.get("bookId").getString();
                String title = book.get("title").getString();
                KeyValue<String, String> keyValue = new KeyValue<>(id, title);
                books.add(keyValue);
            }
        }
    }

}
