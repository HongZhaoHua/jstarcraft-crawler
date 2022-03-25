package com.jstarcraft.crawler.book;

import java.time.Instant;
import java.util.List;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.carwler.book.DoubanBook;

public class DoubanBookTestCase {

    @Test
    public void testBook() {
        RestTemplate template = new RestTemplate();
        String id = "26297606";
        DoubanBook book = new DoubanBook(template, id);
        book.update(Instant.now());
        System.out.println(book.getTitle());
        System.out.println(book.getIsbn());
        System.out.println(book.getScore());
        for (String chapter : book.getChapters()) {
            System.out.println(chapter);
        }
        for (String tag : book.getTags()) {
            System.out.println(tag);
        }
    }

    @Test
    public void testSearch() {
        String isbn = "9787545540444";
        RestTemplate template = new RestTemplate();
        List<DoubanBook> books = DoubanBook.searchBooksByKey(template, isbn);
        System.out.println(books.size());
    }

    @Test
    public void testTag() {
        String tag = "科普";
        RestTemplate template = new RestTemplate();
        List<DoubanBook> books = DoubanBook.getBooksByTag(template, tag, 0);
        System.out.println(books.size());
    }

}
