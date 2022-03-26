package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.book.DoubanBook;

public class DoubanBookTestCase {

    @Test
    public void testBook() {
        RestTemplate template = new RestTemplate();
        String id = "26413155";
        DoubanBook book = new DoubanBook(template, id);
        book.update(Instant.now());
        Assert.assertEquals("星际穿越", book.getTitle());
        Assert.assertEquals("9787213066856", book.getIsbn());
        Assert.assertEquals("8.8", book.getScore());
        Assert.assertEquals(187, book.getChapters().size());
        Assert.assertEquals(10, book.getTags().size());
    }

    @Test
    public void testSearch() {
        String isbn = "9787213066856";
        RestTemplate template = new RestTemplate();
        List<DoubanBook> books = DoubanBook.searchBooksByKey(template, isbn);
        Assert.assertEquals(1, books.size());
    }

    @Test
    public void testTag() {
        String tag = "科普";
        RestTemplate template = new RestTemplate();
        List<DoubanBook> books = DoubanBook.getBooksByTag(template, tag, 0);
        Assert.assertEquals(1, books.size());
    }

}
