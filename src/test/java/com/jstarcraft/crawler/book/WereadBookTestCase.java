package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

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
        List<WereadBook> books = WereadBook.getBooksByKey(template, isbn);
        Assert.assertEquals(1, books.size());
    }

    @Test
    public void testGetHerf() {
        String id = "630480";
        Assert.assertEquals("13f329c0599ed013ff80b18", WereadBook.getHerf(id));
    }

    @Test
    public void testUpdateBook() {
        RestTemplate template = new RestTemplate();
        String id = "630480";
        WereadBook book = new WereadBook(template, id);
        book.update(Instant.now());
        Assert.assertEquals("630480", book.getId());
        Assert.assertEquals("星际穿越", book.getTitle());
        Assert.assertEquals("9787213066856", book.getIsbn());
        Assert.assertEquals("86.0", book.getScore());
        Assert.assertEquals(51, book.getChapters().size());
        Assert.assertEquals(5, book.getTags().size());
    }

    @Test
    public void testSearchContent() {
        RestTemplate template = new RestTemplate();
        String id = "630480";
        WereadBook book = new WereadBook(template, id);
        List<WereadSummary> summaries = book.search("人类", 0, 10);
        Assert.assertEquals(7, summaries.size());
    }

}
