package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public WereadNote(RestTemplate template, String id) {
        this.template = template;
        this.id = id;
    }

    public void update(Instant instant) {

    }

    private List<WereadSummary> getMarks(List<ONode> nodes) {
        List<WereadSummary> summaries = new ArrayList<>(nodes.size());
        for (ONode mark : nodes) {
            int chapter = mark.get("chapterUid").getInt();
            String content = mark.get("markText").getString();
            WereadSummary summary = new WereadSummary(id, chapter, content);
            summaries.add(summary);
        }
        return summaries;
    }

    /**
     * 获取自己的划线
     */
    public List<WereadSummary> getOwnMarks(String cookie) {
        List<String> cookies = Arrays.asList(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format("https://i.weread.qq.com/book/bookmarklist?bookId={}", id);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("updated").ary();
        return getMarks(nodes);
    }

    /**
     * 获取别人的划线
     */
    public List<WereadSummary> getOtherMarks(String cookie) {
        List<String> cookies = Arrays.asList(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format("https://i.weread.qq.com/book/bestbookmarks?bookId={}", id);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("items").ary();
        return getMarks(nodes);
    }

    private List<WereadSummary> getThoughts(List<ONode> nodes) {
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

    /**
     * 获取自己的想法
     */
    public List<WereadSummary> getOwnThoughts(String cookie, int chapter) {
        List<String> cookies = Arrays.asList(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format("https://i.weread.qq.com/review/list?bookId={}&chapterUid={}&listType=11&mine=1&synckey=0&listMode=0", id, chapter);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("reviews").ary();
        return getThoughts(nodes);
    }

    /**
     * 获取别人的想法
     */
    public List<WereadSummary> getOtherThoughts(String cookie, int chapter) {
        List<String> cookies = Arrays.asList(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format("https://i.weread.qq.com/review/list?bookId={}&chapterUid={}&listType=8&synckey=0&listMode=0", id, chapter);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("reviews").ary();
        return getThoughts(nodes);
    }

}
