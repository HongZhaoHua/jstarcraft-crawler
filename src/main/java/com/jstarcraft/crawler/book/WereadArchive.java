package com.jstarcraft.crawler.book;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 微信档案
 * 
 * @author Birdy
 *
 */
public class WereadArchive {

    protected static final Logger logger = LoggerFactory.getLogger(WereadArchive.class);

    /** 书架路径模板 */
    private static final String shelfUrl = "https://weread.qq.com/web/shelf";

    private static final JsoupCssSelector scriptSelector = new JsoupCssSelector("script[nonce]");

    /** 标识 */
    private String id;

    /** 名称 */
    private String name;

    /** 笔记 */
    private Map<String, WereadProgress> progresses;

    private static Map<String, WereadProgress> getProgresses(List<ONode> nodes) {
        Map<String, WereadProgress> progresses = new HashMap<>(nodes.size());
        for (ONode book : nodes) {
            String id = book.get("bookId").getString();
            String title = book.get("title").getString();
            int progress = book.get("progress").getInt();
            progresses.put(id, new WereadProgress(id, title, progress));
        }
        return progresses;
    }

    /**
     * 按书架获取档案
     * 
     * @param template
     * @param cookie
     * @return
     */
    public static Map<String, WereadArchive> getArchivesByShelf(RestTemplate template, String cookie) {
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
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(script));
        }
        ONode root = ONode.load(script);
        List<ONode> nodes = root.get("shelf").get("archive").ary();
        Map<String, WereadArchive> archives = new HashMap<>(nodes.size());
        for (ONode node : nodes) {
            String id = node.get("archiveId").getString();
            String name = node.get("name").getString();
            Map<String, WereadProgress> progresses = getProgresses(node.get("bookInfos").ary());
            WereadArchive archive = new WereadArchive(id, name, progresses);
            archives.put(id, archive);
        }
        Map<String, WereadProgress> progresses = getProgresses(root.get("shelf").get("books").ary());
        WereadArchive archive = new WereadArchive(StringUtility.EMPTY, StringUtility.EMPTY, progresses);
        archives.put(StringUtility.EMPTY, archive);
        return archives;
    }

    public WereadArchive(String id, String name, Map<String, WereadProgress> progresses) {
        this.id = id;
        this.name = name;
        this.progresses = progresses;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, WereadProgress> getProgresses() {
        return progresses;
    }

}
