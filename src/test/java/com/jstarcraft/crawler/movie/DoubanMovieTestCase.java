package com.jstarcraft.crawler.movie;

import java.time.Instant;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * 豆瓣电影单元测试
 * 
 * @author Birdy
 *
 */
public class DoubanMovieTestCase {

    @Test
    public void testMovie() {
        RestTemplate template = new RestTemplate();
        String id = "1889243";
        DoubanMovie movie = new DoubanMovie(template, id);
        movie.update(Instant.now());
        Assert.assertEquals("星际穿越 Interstellar", movie.getMovieTitle());
        Assert.assertEquals("tt0816692", movie.getMovieImdb());
        Assert.assertEquals("9.4", movie.getMovieScore());
        Assert.assertEquals(3, movie.getMovieGenres().size());
        Assert.assertEquals(31, movie.getMovieTags().size());
    }

    @Test
    public void testSearch() {
        String imdb = "tt0816692";
        RestTemplate template = new RestTemplate();
        Map<String, String> items = DoubanMovie.getTuplesByKey(template, imdb, 0);
        Assert.assertEquals(1, items.size());
    }

    @Test
    public void testTag() {
        String tag = "科幻";
        RestTemplate template = new RestTemplate();
        Map<String, String> items = DoubanMovie.getTuplesByTag(template, tag, 0);
        Assert.assertEquals(20, items.size());
    }

}
