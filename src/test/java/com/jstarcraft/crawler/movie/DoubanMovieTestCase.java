package com.jstarcraft.crawler.movie;

import java.time.Instant;
import java.util.List;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.carwler.book.DoubanBook;
import com.jstarcraft.carwler.movie.DoubanMovie;

public class DoubanMovieTestCase {

    @Test
    public void testMovie() {
        RestTemplate template = new RestTemplate();
        String id = "35352389";
        DoubanMovie movie = new DoubanMovie(template, id);
        movie.update(Instant.now());
        System.out.println(movie.getTitle());
        System.out.println(movie.getImdb());
        System.out.println(movie.getScore());
        for (String tag : movie.getTags()) {
            System.out.println(tag);
        }
    }

    @Test
    public void testSearch() {
        String imdb = "tt17023860";
        RestTemplate template = new RestTemplate();
        List<DoubanMovie> movies = DoubanMovie.searchMoviesByKey(template, imdb);
        System.out.println(movies.size());
    }

    @Test
    public void testTag() {
        String tag = "文艺";
        RestTemplate template = new RestTemplate();
        List<DoubanMovie> movies = DoubanMovie.getMoviesByTag(template, tag, 0);
//        System.out.println(movies.size());
    }

}
