package com.jstarcraft.crawler.movie;

import java.time.Instant;
import java.util.List;

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
        Assert.assertEquals("星际穿越 Interstellar", movie.getTitle());
        Assert.assertEquals("tt0816692", movie.getImdb());
        Assert.assertEquals("9.4", movie.getScore());
        Assert.assertEquals(3, movie.getGenres().size());
        Assert.assertEquals(31, movie.getTags().size());
    }

    @Test
    public void testSearch() {
        String imdb = "tt0816692";
        RestTemplate template = new RestTemplate();
        List<DoubanMovie> movies = DoubanMovie.searchMoviesByKey(template, imdb);
        Assert.assertEquals(1, movies.size());
    }

    @Test
    public void testTag() {
        String tag = "科幻";
        RestTemplate template = new RestTemplate();
        List<DoubanMovie> movies = DoubanMovie.getMoviesByTag(template, tag, 0);
        Assert.assertEquals(20, movies.size());
    }

}
