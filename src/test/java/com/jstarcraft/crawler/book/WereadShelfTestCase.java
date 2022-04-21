package com.jstarcraft.crawler.book;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
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
import com.jstarcraft.crawler.book.weread.WereadArchive;
import com.jstarcraft.crawler.book.weread.WereadNote;
import com.jstarcraft.crawler.book.weread.WereadSummary;

public class WereadShelfTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(WereadShelfTestCase.class);

    /**
     * 获取个人书架:https://weread.qq.com/web/shelf
     */
    @Test
    public void testGetArchives() throws Exception {
        RestTemplate template = new RestTemplate();
        File file = new File(WereadShelfTestCase.class.getClassLoader().getResource("cookie.txt").toURI());
        String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
        if (cookie.isEmpty()) {
            throw new RuntimeException("必须填写Cookie才能获取信息");
        }
        Map<String, WereadArchive> archives = WereadArchive.getArchivesByShelf(template, cookie);
        Assert.assertEquals(35, archives.size());
    }

    /**
     * 获取个人笔记:https://i.weread.qq.com/user/notebooks
     */
    @Test
    public void testGetNotes() throws Exception {
        RestTemplate template = new RestTemplate();
        File file = new File(WereadShelfTestCase.class.getClassLoader().getResource("cookie.txt").toURI());
        String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
        if (cookie.isEmpty()) {
            throw new RuntimeException("必须填写Cookie才能获取信息");
        }
        Map<String, String> items = WereadNote.getTuples(template, cookie);
        Assert.assertEquals(32, items.size());
    }

    /**
     * 获取个人划线:https://i.weread.qq.com/book/bookmarklist?bookId={}
     */
    @Test
    public void testGetOwnMarks() throws Exception {
        RestTemplate template = new RestTemplate();
        File file = new File(WereadShelfTestCase.class.getClassLoader().getResource("cookie.txt").toURI());
        String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
        if (cookie.isEmpty()) {
            throw new RuntimeException("必须填写Cookie才能获取信息");
        }
        WereadNote note = new WereadNote(template, "855812", () -> cookie);
        List<WereadSummary> summaries = note.getOwnMarks();
        Assert.assertEquals(860, summaries.size());
    }

    /**
     * 获取热门划线:https://i.weread.qq.com/book/bestbookmarks?bookId={}
     */
    @Test
    public void testGetOtherMarks() throws Exception {
        RestTemplate template = new RestTemplate();
        File file = new File(WereadShelfTestCase.class.getClassLoader().getResource("cookie.txt").toURI());
        String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
        if (cookie.isEmpty()) {
            throw new RuntimeException("必须填写Cookie才能获取信息");
        }
        WereadNote note = new WereadNote(template, "855812", () -> cookie);
        List<WereadSummary> summaries = note.getOtherMarks();
        Assert.assertEquals(366, summaries.size());
    }

    /**
     * 获取个人想法:https://i.weread.qq.com/review/list?bookId={}&chapterUid={}&listType=11&mine=1&synckey=0&listMode=0
     */
    @Test
    public void testGetOwnThoughts() throws Exception {
        RestTemplate template = new RestTemplate();
        File file = new File(WereadShelfTestCase.class.getClassLoader().getResource("cookie.txt").toURI());
        String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
        if (cookie.isEmpty()) {
            throw new RuntimeException("必须填写Cookie才能获取信息");
        }
        WereadNote note = new WereadNote(template, "855812", () -> cookie);
        List<WereadSummary> summaries = note.getOwnThoughts(0);
        Assert.assertEquals(8, summaries.size());
    }

    /**
     * 获取他人想法:https://i.weread.qq.com/review/list?bookId={}&chapterUid={}&listType=8&synckey=0&listMode=3
     */
    @Test
    public void testGetOtherThoughts() throws Exception {
        RestTemplate template = new RestTemplate();
        File file = new File(WereadShelfTestCase.class.getClassLoader().getResource("cookie.txt").toURI());
        String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
        if (cookie.isEmpty()) {
            throw new RuntimeException("必须填写Cookie才能获取信息");
        }
        WereadNote note = new WereadNote(template, "855812", () -> cookie);
        List<WereadSummary> summaries = note.getOtherThoughts(0);
        Assert.assertEquals(500, summaries.size());
    }

    /**
     * 获取书籍详情:https://i.weread.qq.com/book/info?bookId={}
     */
    @Test
    public void testGetDetail() throws Exception {
        RestTemplate template = new RestTemplate();
        File file = new File(WereadShelfTestCase.class.getClassLoader().getResource("cookie.txt").toURI());
        String cookie = FileUtils.readFileToString(file, StringUtility.CHARSET);
        if (cookie.isEmpty()) {
            throw new RuntimeException("必须填写Cookie才能获取信息");
        }
        List<String> cookies = Arrays.asList(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        String url = StringUtility.format("https://i.weread.qq.com/book/info?bookId={}", "855812");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(data.length());
        System.out.println(JsonUtility.prettyJson(data));
    }

}
