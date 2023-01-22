package com.jstarcraft.crawler.book.weread;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.common.selection.regular.RegularSelector;
import com.jstarcraft.core.script.ScriptContext;
import com.jstarcraft.core.script.js.JsFunction;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.book.Book;

/**
 * 微信书籍
 * 
 * @author Birdy
 *
 */
public class WereadBook implements Book<WereadChapter> {

    protected static final Logger logger = LoggerFactory.getLogger(WereadBook.class);

    private static final JsFunction function;

    static {
        try {
            File file = new File(WereadBook.class.getClassLoader().getResource("weread.js").toURI());
            String script = FileUtils.readFileToString(file, StringUtility.CHARSET);
            ScriptContext context = new ScriptContext();
            function = new JsFunction(context, script, "getHref");
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    /** 查找路径模板 */
    // https://weread.qq.com/web/search/global?keyword={key}&maxIdx={offset}&fragmentSize=120&count=20
    private static final String findUrl = "https://weread.qq.com/web/search/global?keyword={}&maxIdx={}&fragmentSize=120&count=20";

    /** 书籍路径模板 */
    // https://weread.qq.com/web/bookDetail/{href}
    private static final String bookUrl = "https://weread.qq.com/web/bookDetail/{}";

    /** 版权路径模板 */
    // https://weread.qq.com/web/reader/{href}
    private static final String licenseUrl = "https://weread.qq.com/web/reader/{}";

    private static final JsoupCssSelector titleSelector = new JsoupCssSelector("div.bookInfo_right_header_title");

    private static final JsoupCssSelector pictureSelector = new JsoupCssSelector("img.wr_bookCover_img");

    private static final JsoupCssSelector chapterSelector = new JsoupCssSelector("span.chapterItem_text");

    private static final JsoupCssSelector scriptSelector = new JsoupCssSelector("script[nonce]");

    private static final JsoupCssSelector scoreSelector = new JsoupCssSelector("div.book_ratings_container");

    private static final RegularSelector regularSelector = new RegularSelector("([\\d\\.]+)%", 0, 1);

    private static final JsoupCssSelector tagSelector = new JsoupCssSelector("meta[name='keywords']");

    /** 搜索路径模板 */
    private static final String searchUrl = "https://weread.qq.com/web/book/search?bookId={}&keyword={}&maxIdx={}&count={}&fragmentSize=150&onlyCount=0";

    private final RestTemplate template;

    private final String href;

    /** 标识 */
    private String id;

    /** 标题 */
    private String title;

    /** 封面 */
    private String pricture;

    /** 章节 */
    private List<WereadChapter> chapters;

    /** ISBN */
    private String isbn;

    /** 价格 */
    private Float price;

    /** 得分 */
    private Float score;

    /** 标签 */
    private List<String> tags;

    private Instant instant;

    public WereadBook(RestTemplate template, String href) {
        this.template = template;
        this.href = href;
    }

    @Deprecated
    public void update(Instant instant) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(bookUrl, href);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(XmlUtility.prettyHtml(data));
        }
        Document document = Jsoup.parse(data);
        // 获取标题
        this.title = titleSelector.selectSingle(document.root()).text();
        // 获取封面
        this.pricture = pictureSelector.selectSingle(document.root()).attr("src");
        String script = scriptSelector.selectSingle(document.root()).html();
        script = script.replaceAll("window.__INITIAL_STATE__=([\\s\\S]*);\\(function[\\s\\S]*\\(\\)\\);", "$1");
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(script));
        }
        ONode root = ONode.load(script);
        ONode book = root.get("reader");
        // 获取标识
        this.id = book.get("bookId").getString();
        // 获取章节
        Map<String, WereadChapter> chapters = new HashMap<>();
        for (ONode chapter : book.get("chapterInfos").ary()) {
            int id = chapter.get("chapterUid").getInt();
            String title = chapter.get("title").getString();
            int level = chapter.get("level").getInt();
            chapters.put(title, new WereadChapter(title, id, level));
            for (ONode anchor : chapter.get("anchors").ary()) {
                title = anchor.get("title").getString();
                level = anchor.get("level").getInt();
                chapters.put(title, new WereadChapter(title, id, level));
            }
        }
        List<Element> elements = chapterSelector.selectMultiple(document.root());
        this.chapters = new ArrayList<>(elements.size());
        for (Element element : elements) {
            String title = element.text();
            this.chapters.add(chapters.get(title));
        }
        // 获取ISBN
        this.isbn = book.get("bookInfo").get("isbn").getString();
        // 获取价格
        this.price = book.get("bookInfo").get("publishPrice").getFloat();
        // 获取评分
        this.score = Float.valueOf(regularSelector.selectSingle(scoreSelector.selectSingle(document.root()).text()));
        // 获取标签
        String[] tags = tagSelector.selectSingle(document.root()).attr("content").split(",");
        this.tags = Arrays.asList(tags);
        this.instant = instant;
    }

    @Override
    public String getBookId() {
        return id;
    }

    @Override
    public String getBookTitle() {
        return title;
    }

    @Override
    public String getBookPicture() {
        return pricture;
    }

    @Override
    public List<WereadChapter> getBookChapters() {
        return chapters;
    }

    @Override
    public String getBookIsbn() {
        return isbn;
    }

    @Override
    public Float getBookPrice() {
        return price;
    }

    @Override
    public Float getBookScore() {
        return score;
    }

    @Override
    public List<String> getBookTags() {
        return tags;
    }

    public Instant getInstant() {
        return instant;
    }

    /**
     * 搜索内容
     * 
     * @param key
     * @param offset
     * @param size
     * @return
     */
    public static List<WereadSummary> searchContent(RestTemplate template, String id, String key, int offset, int size) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format(searchUrl, id, key, offset, size);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("result").ary();
        List<WereadSummary> summaries = new ArrayList<>(nodes.size());
        for (ONode node : nodes) {
            int chapter = node.get("chapterUid").getInt();
            String content = node.get("abstract").getString();
            WereadSummary summary = new WereadSummary(id, chapter, content);
            summaries.add(summary);
        }
        return summaries;
    }

    public static String getHref(String id) {
        return function.doWith(String.class, id);
    }

    /**
     * 按关键字获取图书
     * 
     * @param template
     * @param key
     * @return
     */
    public static Map<String, String> getTuplesByKey(RestTemplate template, String key, int offset) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(findUrl, key, offset);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("books").ary();
        Map<String, String> tuples = new LinkedHashMap<>(nodes.size());
        for (ONode node : nodes) {
            String id = node.get("bookInfo").get("bookId").getString();
            String title = node.get("bookInfo").get("title").getString();
            tuples.put(id, title);
        }
        return tuples;
    }

}
