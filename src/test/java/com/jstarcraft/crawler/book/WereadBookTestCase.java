package com.jstarcraft.crawler.book;

import java.io.File;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.noear.snack.ONode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.StringUtility;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;

public class WereadBookTestCase {

    @Test
    public void testSearch() {
        RestTemplate template = new RestTemplate();
        String isbn = "9787213066856";
        List<WereadBook> books = WereadBook.searchBooksByKey(template, isbn);
        Assert.assertEquals(1, books.size());
    }

    @Test
    public void testGetHerf() {
        String id = "630480";
        Assert.assertEquals("13f329c0599ed013ff80b18", WereadBook.getHerf(id));
    }

    @Test
    public void testBook() {
        RestTemplate template = new RestTemplate();
        String href = "13f329c0599ed013ff80b18";
        WereadBook book = new WereadBook(template, href);
        book.update(Instant.now());
        Assert.assertEquals("630480", book.getId());
        Assert.assertEquals("星际穿越", book.getTitle());
        Assert.assertEquals("9787213066856", book.getIsbn());
        Assert.assertEquals("86.0", book.getScore());
        Assert.assertEquals(51, book.getChapters().size());
        Assert.assertEquals(5, book.getTags().size());
    }

    /**
     * 获取个人书架:https://weread.qq.com/web/shelf
     */
    @Test
    public void testGetShelf() {
        try {
            RestTemplate template = new RestTemplate();
            File file = new File(WereadBookTestCase.class.getResource("cookie.txt").toURI());
            String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
            if (cookie.isEmpty()) {
                throw new RuntimeException("必须填写Cookie才能获取信息");
            }
            List<String> cookies = Arrays.asList(cookie);
            HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeaders.COOKIE, cookies);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
            String url = StringUtility.format("https://weread.qq.com/web/shelf");
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            String content = response.getBody();
            System.out.println(content.length());
            System.out.println(XmlUtility.prettyHtml(content));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 获取个笔记:https://i.weread.qq.com/user/notebooks
     */
    @Test
    public void testGetNotes() {
        try {
            RestTemplate template = new RestTemplate();
            File file = new File(WereadBookTestCase.class.getResource("cookie.txt").toURI());
            String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
            if (cookie.isEmpty()) {
                throw new RuntimeException("必须填写Cookie才能获取信息");
            }
            List<String> cookies = Arrays.asList(cookie);
            HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeaders.COOKIE, cookies);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
            String url = StringUtility.format("https://i.weread.qq.com/user/notebooks");
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            String content = response.getBody();
            System.out.println(JsonUtility.prettyJson(content));
            ONode root = ONode.load(content);
            List<ONode> books = root.get("books").ary();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 获取个人划线:https://i.weread.qq.com/book/bookmarklist?bookId={}
     */
    @Test
    public void testGetMarks() {
        try {
            RestTemplate template = new RestTemplate();
            File file = new File(WereadBookTestCase.class.getResource("cookie.txt").toURI());
            String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
            if (cookie.isEmpty()) {
                throw new RuntimeException("必须填写Cookie才能获取信息");
            }
            List<String> cookies = Arrays.asList(cookie);
            HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeaders.COOKIE, cookies);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
            String url = StringUtility.format("https://i.weread.qq.com/book/bookmarklist?bookId={}", "35177944");
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            String content = response.getBody();
//            System.out.println(content.length());
//            System.out.println(JsonUtility.prettyJson(content));
            ONode root = ONode.load(content);
            Int2ObjectSortedMap<String> chapters = new Int2ObjectAVLTreeMap<>();
            Int2ObjectSortedMap<TreeMap<String, String>> marks = new Int2ObjectAVLTreeMap<>();
            for (ONode chapter : root.get("chapters").ary()) {
                int key = chapter.get("chapterUid").getInt();
                String value = chapter.get("title").getString();
                chapters.put(key, value);
                marks.put(key, new TreeMap<>());
            }
            System.out.println(chapters.size());
            for (ONode mark : root.get("updated").ary()) {
                int key = mark.get("chapterUid").getInt();
                String id = mark.get("bookmarkId").getString();
                String value = mark.get("markText").getString();
                marks.get(key).put(id, value);
            }
            System.out.println(marks.size());
            for (Int2ObjectMap.Entry<String> keyValue : chapters.int2ObjectEntrySet()) {
                int key = keyValue.getIntKey();
                System.out.println(keyValue.getValue());
                for (String mark : marks.get(key).values()) {
                    System.out.println("\t" + mark);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 获取个人想法:https://i.weread.qq.com/review/list?bookId={}&listType=11&mine=1&synckey=0&listMode=0
     */
    @Test
    public void testGetThought() {
        try {
            RestTemplate template = new RestTemplate();
            File file = new File(WereadBookTestCase.class.getResource("cookie.txt").toURI());
            String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
            if (cookie.isEmpty()) {
                throw new RuntimeException("必须填写Cookie才能获取信息");
            }
            List<String> cookies = Arrays.asList(cookie);
            HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeaders.COOKIE, cookies);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
            String url = StringUtility.format("https://i.weread.qq.com/review/list?bookId={}&listType=11&mine=1", "35177944");
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            String content = response.getBody();
//            System.out.println(content.length());
//            System.out.println(JsonUtility.prettyJson(content));
            ONode root = ONode.load(content);
            Int2ObjectSortedMap<TreeMap<String, KeyValue<String, String>>> reviews = new Int2ObjectAVLTreeMap<>();
            for (ONode node : root.get("reviews").ary()) {
                ONode review = node.get("review");
                int chapterUid = review.get("chapterUid").getInt();
                TreeMap<String, KeyValue<String, String>> chapters = reviews.get(chapterUid);
                if (chapters == null) {
                    chapters = new TreeMap<>();
                    reviews.put(chapterUid, chapters);
                }
                String reviewId = review.get("reviewId").getString();
                String key = review.get("abstract").getString();
                String value = review.get("content").getString();
                chapters.put(reviewId, new KeyValue<>(key, value));
            }
            System.out.println(reviews.size());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 获取热门划线:https://i.weread.qq.com/book/bestbookmarks?bookId={}
     */
    @Test
    public void testGetHotMarks() {
        try {
            RestTemplate template = new RestTemplate();
            File file = new File(WereadBookTestCase.class.getResource("cookie.txt").toURI());
            String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
            if (cookie.isEmpty()) {
                throw new RuntimeException("必须填写Cookie才能获取信息");
            }
            List<String> cookies = Arrays.asList(cookie);
            HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeaders.COOKIE, cookies);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
            String url = StringUtility.format("https://i.weread.qq.com/book/bestbookmarks?bookId={}", "35177944");
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            String content = response.getBody();
            System.out.println(content.length());
            System.out.println(JsonUtility.prettyJson(content));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 获取书籍详情:https://i.weread.qq.com/book/info?bookId={}
     */
    @Test
    public void testGetDetail() {
        try {
            RestTemplate template = new RestTemplate();
            File file = new File(WereadBookTestCase.class.getResource("cookie.txt").toURI());
            String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
            if (cookie.isEmpty()) {
                throw new RuntimeException("必须填写Cookie才能获取信息");
            }
            List<String> cookies = Arrays.asList(cookie);
            HttpHeaders headers = new HttpHeaders();
            headers.put(HttpHeaders.COOKIE, cookies);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
            String url = StringUtility.format("https://i.weread.qq.com/book/info?bookId={}", "35177944");
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            String content = response.getBody();
            System.out.println(content.length());
            System.out.println(JsonUtility.prettyJson(content));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
