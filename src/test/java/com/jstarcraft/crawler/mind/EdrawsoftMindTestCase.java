package com.jstarcraft.crawler.mind;

import java.time.Instant;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.utility.StringUtility;

/**
 * 亿图脑图单元测试
 * 
 * @author Birdy
 *
 */
public class EdrawsoftMindTestCase {

    private int count(EdrawsoftTopic topic) {
        int count = 1;
        List<EdrawsoftTopic> children = topic.getChildren();
        if (children != null) {
            for (EdrawsoftTopic child : children) {
                count += count(child);
            }
        }
        return count;
    }

    @Test
    public void testGetMind() {
        try {
            RestTemplate template = new RestTemplate();
            List<HttpMessageConverter<?>> converters = template.getMessageConverters();
            for (HttpMessageConverter<?> converter : converters) {
                // TODO StringHttpMessageConverter默认字符集为ISO_8859_1,重置字符集为UTF-8
                if (converter instanceof StringHttpMessageConverter) {
                    StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) converter;
                    messageConverter.setDefaultCharset(StringUtility.CHARSET);
                }
            }
            EdrawsoftMind mind = new EdrawsoftMind(template, "137642");
            mind.update(Instant.now());
            Assert.assertEquals("137642", mind.getId());
            Assert.assertEquals("《财富自由之路》读书笔记", mind.getTitle());
            Assert.assertEquals(0, mind.getTags().size());
            EdrawsoftTopic topic = mind.getContent();
            Assert.assertEquals(859, count(topic));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
