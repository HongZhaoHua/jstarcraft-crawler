package com.jstarcraft.crawler.book.weread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
import com.jstarcraft.core.utility.StringUtility;

/**
 * 微信笔记
 * 
 * @author Birdy
 *
 */
public class WereadNote {

    protected static final Logger logger = LoggerFactory.getLogger(WereadNote.class);

    private final RestTemplate template;

    private final String id;
    
    private final Supplier<String> cookie;

    public WereadNote(RestTemplate template, String id, Supplier<String> cookie) {
        this.template = template;
        this.id = id;
        this.cookie = cookie;
    }

    /** 笔记路径模板 */
    private static final String noteUrl = "https://i.weread.qq.com/user/notebooks";

    public static Map<String, String> getItems(RestTemplate template, String cookie) {
        List<String> cookies = Arrays.asList(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format(noteUrl);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("books").ary();
        Map<String, String> items = new HashMap<>(nodes.size());
        for (ONode book : nodes) {
            String id = book.get("bookId").getString();
            String title = book.get("title").getString();
            items.put(id, title);
        }
        return items;
    }

    private static List<WereadSummary> getMarks(String id, List<ONode> nodes) {
        List<WereadSummary> summaries = new ArrayList<>(nodes.size());
        for (ONode mark : nodes) {
            int chapter = mark.get("chapterUid").getInt();
            String content = mark.get("markText").getString();
            WereadSummary summary = new WereadSummary(id, chapter, content);
            summaries.add(summary);
        }
        return summaries;
    }

    /** 自己的划线路径模板 */
    // https://i.weread.qq.com/book/bookmarklist?bookId={id}
    private static final String ownMarkUrl = "https://i.weread.qq.com/book/bookmarklist?bookId={}";

    /**
     * 获取自己的划线
     * 
     * @param cookie
     * @return
     */
    public List<WereadSummary> getOwnMarks() {
        List<String> cookies = Arrays.asList(cookie.get());
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format(ownMarkUrl, id);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("updated").ary();
        return getMarks(id, nodes);
    }

    /** 别人的划线路径模板 */
    // https://i.weread.qq.com/book/bestbookmarks?bookId={id}
    private static final String otherMarkUrl = "https://i.weread.qq.com/book/bestbookmarks?bookId={}";

    /**
     * 获取别人的划线
     * 
     * @param cookie
     * @return
     */
    public List<WereadSummary> getOtherMarks() {
        List<String> cookies = Arrays.asList(cookie.get());
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format(otherMarkUrl, id);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("items").ary();
        return getMarks(id, nodes);
    }

    private static List<WereadSummary> getThoughts(String id, List<ONode> nodes) {
        List<WereadSummary> summaries = new ArrayList<>(nodes.size());
        for (ONode node : nodes) {
            ONode review = node.get("review");
            int chapter = review.get("chapterUid").getInt();
            String content = review.get("abstract").getString();
            String comment = review.get("content").getString();
            WereadSummary summary = new WereadSummary(id, chapter, content, comment);
            summaries.add(summary);
        }
        return summaries;
    }

    /** 自己的想法路径模板 */
    // https://i.weread.qq.com/review/list?bookId={id}&chapterUid={chapter}&listType=11&mine=1&synckey=0&listMode=0
    private static final String ownThoughtUrl = "https://i.weread.qq.com/review/list?bookId={}&chapterUid={}&listType=11&mine=1&synckey=0&listMode=0";

    /**
     * 获取自己的想法
     * 
     * @param cookie
     * @param chapter
     * @return
     */
    public List<WereadSummary> getOwnThoughts(int chapter) {
        List<String> cookies = Arrays.asList(cookie.get());
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format(ownThoughtUrl, id, chapter);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("reviews").ary();
        return getThoughts(id, nodes);
    }

    /** 别人的想法路径模板 */
    // https://i.weread.qq.com/review/list?bookId={id}&chapterUid={chapter}&listType=8&synckey=0&listMode=0
    private static final String otherThoughtUrl = "https://i.weread.qq.com/review/list?bookId={}&chapterUid={}&listType=8&synckey=0&listMode=0";

    /**
     * 获取别人的想法
     * 
     * @param cookie
     * @param chapter
     * @return
     */
    public List<WereadSummary> getOtherThoughts(int chapter) {
        List<String> cookies = Arrays.asList(cookie.get());
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format(otherThoughtUrl, id, chapter);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("reviews").ary();
        return getThoughts(id, nodes);
    }

}
