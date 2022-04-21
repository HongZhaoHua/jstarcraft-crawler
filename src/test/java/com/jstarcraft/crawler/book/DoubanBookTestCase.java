package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.book.douban.DoubanBook;

/**
 * 豆瓣书籍单元测试
 * 
 * @author Birdy
 *
 */
public class DoubanBookTestCase {

    @Test
    public void testBook() {
        RestTemplate template = new RestTemplate();
        String id = "26413155";
        DoubanBook book = new DoubanBook(template, id);
        book.update(Instant.now());
        Assert.assertEquals("星际穿越", book.getBookTitle());
        Assert.assertEquals("9787213066856", book.getBookIsbn());
        Assert.assertEquals("8.8", book.getBookScore());
        Assert.assertEquals(187, book.getBookChapters().size());
        Assert.assertEquals(10, book.getBookTags().size());
    }

    @Test
    public void testSearch() {
        RestTemplate template = new RestTemplate();
        String isbn = "9787213066856";
        Map<String, String> items = DoubanBook.getTuplesByKey(template, isbn, 0);
        Assert.assertEquals(1, items.size());
    }

    @Test
    public void testTag() {
        RestTemplate template = new RestTemplate();
        String tag = "科普";
        Map<String, String> items = DoubanBook.getTuplesByTag(template, tag, 0);
        Assert.assertEquals(20, items.size());
    }

}
