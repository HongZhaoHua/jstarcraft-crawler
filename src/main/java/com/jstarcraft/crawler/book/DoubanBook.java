package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.noear.snack.ONode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.common.selection.regular.RegularSelector;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 豆瓣书籍
 * 
 * @author Birdy
 *
 */
public class DoubanBook implements Book<Chapter> {

    /** 搜索路径模板 */
    // https://search.douban.com/book/subject_search?search_text={}&start={}
    private static final String searchUrl = "https://book.douban.com/j/subject_suggest?q={}";

    /** 标签路径模板 */
    // T:综合,R:日期,S:评价
    // https://book.douban.com/tag/{}?start={}&type={T,R,S}
    private static final String tagUrl = "https://book.douban.com/tag/{}?start={}&type={}";

    private static final JsoupCssSelector itemSelector = new JsoupCssSelector("li.subject-item a[title]");

    private static final RegularSelector idSelector = new RegularSelector("https://book.douban.com/subject/(\\d+)/", 0, 1);

    /** 图书路径模板 */
    private static final String bookUrl = "https://book.douban.com/subject/{}/";

    private static final JsoupCssSelector titleSelector = new JsoupCssSelector("meta[property='og:title']");

    private static final JsoupCssSelector isbnSelector = new JsoupCssSelector("meta[property='book:isbn']");

    private static final JsoupCssSelector scoreSelector = new JsoupCssSelector("div.rating_self > strong");

    private static final RegularSelector tagSelector = new RegularSelector("criteria\\s*=\\s*'([\\S]*)'", 0, 1);

    private final RestTemplate template;

    /** 标识 */
    private final String id;

    /** 标题 */
    private String title;

    /** 章节 */
    private List<Chapter> chapters;

    /** ISBN */
    private String isbn;

    /** 得分 */
    private String score;

    /** 标签 */
    private List<String> tags;

    private Instant instant;

    /**
     * 按关键字获取图书
     * 
     * @param template
     * @param key
     * @return
     */
    public static List<DoubanBook> getBooksByKey(RestTemplate template, String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(searchUrl, key);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        ONode root = ONode.load(content);
        List<ONode> nodes = root.ary();
        List<DoubanBook> books = new ArrayList<>(nodes.size());
        for (ONode node : nodes) {
            String id = node.get("id").getString();
            DoubanBook book = new DoubanBook(template, id);
            books.add(book);
        }
        return books;
    }

    /**
     * 按标签获取图书
     * 
     * @param template
     * @param tag
     * @param offset
     * @return
     */
    // https:// book.douban.com/tag/{}?start={}&type={T,R,S}
    public static List<DoubanBook> getBooksByTag(RestTemplate template, String tag, int offset) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(tagUrl, tag, offset, "T");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        Document document = Jsoup.parse(content);
        List<Element> elements = itemSelector.selectMultiple(document.root());
        List<DoubanBook> books = new ArrayList<>(elements.size());
        for (Element element : elements) {
            String id = idSelector.selectSingle(element.attr("href"));
            DoubanBook book = new DoubanBook(template, id);
            books.add(book);
        }
        return books;
    }

    public DoubanBook(RestTemplate template, String id) {
        this.template = template;
        this.id = id;
    }

    public void update(Instant instant) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(bookUrl, id);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        Document document = Jsoup.parse(content);
        // 获取标题
        this.title = titleSelector.selectSingle(document.root()).attr("content");
        // 获取章节
        Element catalogue = document.getElementById(StringUtility.format("dir_{}_full", id));
        String[] chapters;
        if (catalogue == null) {
            chapters = new String[] {};
        } else {
            chapters = catalogue.html().split("[\\s]*<br>[\\s]*");
            chapters = Arrays.copyOf(chapters, chapters.length - 1);
        }
        // 剔除最后一个章节
        this.chapters = new ArrayList<Chapter>(chapters.length);
        for (String chapter : chapters) {
            this.chapters.add(new Chapter(chapter));
        }
        // 获取ISBN
        this.isbn = isbnSelector.selectSingle(document.root()).attr("content");
        // 获取评分
        this.score = scoreSelector.selectSingle(document.root()).text();
        // 获取标签
        String[] tags = tagSelector.selectSingle(content).split("\\|");
        // 剔除最后一个标签
        tags = Arrays.copyOf(tags, tags.length - 1);
        for (int index = 0, size = tags.length; index < size; index++) {
            tags[index] = tags[index].replaceAll("7:([\\s\\S]*)", "$1");
        }
        this.tags = Arrays.asList(tags);
        this.instant = instant;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<Chapter> getChapters() {
        return chapters;
    }

    @Override
    public String getIsbn() {
        return isbn;
    }

    @Override
    public String getScore() {
        return score;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    public Instant getInstant() {
        return instant;
    }

}
