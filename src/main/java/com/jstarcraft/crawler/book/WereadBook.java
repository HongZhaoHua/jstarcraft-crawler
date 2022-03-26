package com.jstarcraft.crawler.book;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
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

import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.script.ScriptContext;
import com.jstarcraft.core.script.js.JsFunction;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 微信图书
 * 
 * @author Birdy
 *
 */
public class WereadBook {

    private static final JsFunction function;

    static {
        try {
            File file = new File(WereadBook.class.getResource("getHref.js").toURI());
            String script = FileUtils.readFileToString(file, StringUtility.CHARSET);
            function = new JsFunction(new ScriptContext(), script, "getHref");
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    /** 搜索路径模板 */
    private static final String searchUrl = "https://weread.qq.com/web/search/global?keyword={}&maxIdx={}&fragmentSize=120&count=20";

    /** 图书路径模板 */
    private static final String bookUrl = "https://weread.qq.com/web/reader/{}";

    private static final JsoupCssSelector titleSelector = new JsoupCssSelector("div.bookInfo_right_header_title");

    private static final JsoupCssSelector chapterSelector = new JsoupCssSelector("span.chapterItem_text");

    private static final JsoupCssSelector scriptSelector = new JsoupCssSelector("script[nonce]");

    private static final JsoupCssSelector scoreSelector = new JsoupCssSelector("div.book_ratings_header > span");

    private static final JsoupCssSelector tagSelector = new JsoupCssSelector("meta[name='keywords']");

    private final RestTemplate template;

    /** 标识 */
    private final String id;

    private final String herf;

    /** 标题 */
    private String title;

    /** 章节 */
    private List<String> chapters;

    /** ISBN */
    private String isbn;

    /** 得分 */
    private String score;

    /** 标签 */
    private List<String> tags;

    private Instant instant;

    public static String getHerf(String id) {
        return function.doWith(String.class, id);
    }

    public static List<WereadBook> searchBooksByKey(RestTemplate template, String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(searchUrl, key, 0);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        ONode root = ONode.load(content);
        List<ONode> nodes = root.ary();
        List<WereadBook> books = new ArrayList<>(nodes.size());
        for (ONode node : nodes) {
            String id = node.get("id").getString();
            WereadBook book = new WereadBook(template, id);
            books.add(book);
        }
        return books;
    }

    public WereadBook(RestTemplate template, String id) {
        this.template = template;
        this.id = id;
        this.herf = getHerf(id);
    }

    public void update(Instant instant) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(bookUrl, herf);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(XmlUtility.prettyHtml(content));
        Document document = Jsoup.parse(content);
        // 获取标题
        this.title = titleSelector.selectSingle(document.root()).text();
        // 获取章节
        List<Element> elements = chapterSelector.selectMultiple(document.root());
        this.chapters = new ArrayList<>(elements.size());
        for (Element element : elements) {
            this.chapters.add(element.text());
        }
        // 获取ISBN
        String script = scriptSelector.selectSingle(document.root()).html();
        script = script.replaceAll("window.__INITIAL_STATE__=([\\s\\S]*);\\(function[\\s\\S]*\\(\\)\\);", "$1");
        ONode root = ONode.load(script);
        ONode book = root.get("reader");
        this.isbn = book.get("bookInfo").get("isbn").getString();
        // 获取评分
        this.score = scoreSelector.selectSingle(document.root()).text().replaceAll("([\\S]*)%", "$1");
        // 获取标签
        String[] tags = tagSelector.selectSingle(document.root()).attr("content").split(",");
        this.tags = Arrays.asList(tags);
        this.instant = instant;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getScore() {
        return score;
    }

    public List<String> getTags() {
        return tags;
    }

    public Instant getInstant() {
        return instant;
    }

}
