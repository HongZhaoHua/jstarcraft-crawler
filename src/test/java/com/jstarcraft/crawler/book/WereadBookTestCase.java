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

/**
 * 微信书籍单元测试
 * 
 * @author Birdy
 *
 */
public class WereadBookTestCase {

    @Test
    public void testSearch() {
        RestTemplate template = new RestTemplate();
        String isbn = "9787213066856";
        List<WereadBook> books = WereadBook.getBooksByKey(template, isbn);
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
        Assert.assertEquals("85.9", book.getScore());
        Assert.assertEquals(51, book.getChapters().size());
        Assert.assertEquals(5, book.getTags().size());
    }

    /**
     * 搜索书籍内容:https://weread.qq.com/web/book/search?bookId={}&keyword={}&maxIdx={}&count={}&fragmentSize=150&onlyCount=0
     */
    @Test
    public void testSearchContent() {
        try {
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
            String url = StringUtility.format("https://weread.qq.com/web/book/search?bookId={}&keyword={}&maxIdx={}&count={}&fragmentSize=150&onlyCount=0", "855812", "人类", "0", "10");
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            String content = response.getBody();
            System.out.println(content.length());
            System.out.println(JsonUtility.prettyJson(content));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
