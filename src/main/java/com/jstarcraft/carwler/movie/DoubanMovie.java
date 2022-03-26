package com.jstarcraft.carwler.movie;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jaxen.Navigator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.noear.snack.ONode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.common.selection.regular.RegularSelector;
import com.jstarcraft.core.common.selection.xpath.JaxenXpathSelector;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlElementNode;
import com.jstarcraft.core.common.selection.xpath.jsoup.HtmlNavigator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 豆瓣电影
 * 
 * @author Birdy
 *
 */
public class DoubanMovie {

    /** 搜索路径模板 */
    private static final String searchUrl = "https://movie.douban.com/j/subject_suggest?q={}";

    /** 标签路径模板 */
    // U:最热,T:最多,S:最高,R:最新
    // https://movie.douban.com/j/new_search_subjects?sort={U,T,S,R}&range=0,10&tags={}&start={}
    // https://movie.douban.com/tag/#/?sort={U,T,S,R}&range=0,10&tags={}&start={}
    private static final String tagUrl = "https://movie.douban.com/j/new_search_subjects?sort={}&range=0,10&tags={}&start={}";

    /** 电影路径模板 */
    private static final String bookUrl = "https://movie.douban.com/subject/{}/";

    private static final JsoupCssSelector titleSelector = new JsoupCssSelector("meta[property='og:title']");

    private static final Navigator navigator = HtmlNavigator.getInstance();

    private static final JaxenXpathSelector<HtmlElementNode> imdbSelector = new JaxenXpathSelector<>("//span[text()='IMDb:']/following-sibling::text()[1]", navigator);

    private static final JsoupCssSelector scoreSelector = new JsoupCssSelector("div.rating_self > strong");

    private static final JsoupCssSelector genreSelector = new JsoupCssSelector("span[property='v:genre']");

    private static final RegularSelector tagSelector = new RegularSelector("criteria\\s*=\\s*'([\\S]*)'", 0, 1);

    private final RestTemplate template;

    /** 标识 */
    private final String id;

    /** 标题 */
    private String title;

    /** IMDb */
    private String imdb;

    /** 得分 */
    private String score;

    /** 体裁 */
    private List<String> genres;

    /** 标签 */
    private List<String> tags;

    private Instant instant;

    /**
     * 按关键字搜索电影
     * 
     * @param template
     * @param key
     * @return
     */
    public static List<DoubanMovie> searchMoviesByKey(RestTemplate template, String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(searchUrl, key);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        ONode root = ONode.load(content);
        List<ONode> nodes = root.ary();
        List<DoubanMovie> movies = new ArrayList<>(nodes.size());
        for (ONode node : nodes) {
            String id = node.get("id").getString();
            DoubanMovie movie = new DoubanMovie(template, id);
            movies.add(movie);
        }
        return movies;
    }

    /**
     * 按标签获取电影
     * 
     * @param template
     * @param tag
     * @param offset
     * @return
     */
    public static List<DoubanMovie> getMoviesByTag(RestTemplate template, String tag, int offset) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(tagUrl, "T", tag, offset);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        ONode root = ONode.load(content);
        List<ONode> nodes = root.get("data").ary();
        List<DoubanMovie> movies = new ArrayList<>(nodes.size());
        for (ONode node : nodes) {
            String id = node.get("id").getString();
            DoubanMovie movie = new DoubanMovie(template, id);
            movies.add(movie);
        }
        return movies;
    }

    public DoubanMovie(RestTemplate template, String id) {
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
        this.title = titleSelector.selectSingle(document.root()).attr("content");
        HtmlElementNode root = new HtmlElementNode(document);
        // 获取IMDb
        this.imdb = ((TextNode) (imdbSelector.selectSingle(root).getValue())).text().trim();
        // 获取评分
        this.score = scoreSelector.selectSingle(document.root()).text();
        // 获取体裁
        List<Element> genres = genreSelector.selectMultiple(document.root());
        this.genres = new ArrayList<>(genres.size());
        for (Element element : genres) {
            this.genres.add(element.text());
        }
        // 获取标签
        String[] tags = tagSelector.selectSingle(content).split("\\|");
        // 剔除最后一个标签
        tags = Arrays.copyOf(tags, tags.length - 1);
        for (int index = 0, size = tags.length; index < size; index++) {
            tags[index] = tags[index].split(":")[1];
        }
        this.tags = Arrays.asList(tags);
        this.instant = instant;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImdb() {
        return imdb;
    }

    public String getScore() {
        return score;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getTags() {
        return tags;
    }

    public Instant getInstant() {
        return instant;
    }

}
