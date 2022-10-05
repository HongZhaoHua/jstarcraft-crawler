package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.book.weread.WereadBook;
import com.jstarcraft.crawler.book.weread.WereadSummary;

/**
 * 微信书籍单元测试
 * 
 * @author Birdy
 *
 */
public class WereadBookTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(WereadBookTestCase.class);

    @Test
    public void testGetBooks() {
        RestTemplate template = new RestTemplate();
        String isbn = "9787213066856";
        Map<String, String> items = WereadBook.getTuplesByKey(template, isbn, 0);
        Assert.assertEquals(1, items.size());
    }

    @Test
    public void testGetHerf() {
        String id = "630480";
        Assert.assertEquals("13f329c0599ed013ff80b18", WereadBook.getHref(id));
    }

    @Test
    public void testUpdateBook() {
        RestTemplate template = new RestTemplate();
        String href = "13f329c0599ed013ff80b18";
        WereadBook book = new WereadBook(template, href);
        book.update(Instant.now());
        Assert.assertEquals("630480", book.getBookId());
        Assert.assertEquals("星际穿越", book.getBookTitle());
        Assert.assertEquals("9787213066856", book.getBookIsbn());
        Assert.assertEquals(Float.valueOf(86.6F), book.getBookScore());
        Assert.assertEquals(51, book.getBookChapters().size());
        Assert.assertEquals(5, book.getBookTags().size());
    }

    @Test
    public void testSearchContent() {
        RestTemplate template = new RestTemplate();
        String id = "630480";
        List<WereadSummary> summaries = WereadBook.search(template, id, "人类", 0, 10);
        Assert.assertEquals(7, summaries.size());
    }

}
