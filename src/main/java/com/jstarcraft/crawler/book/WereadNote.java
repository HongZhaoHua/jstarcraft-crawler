package com.jstarcraft.crawler.book;

import org.springframework.web.client.RestTemplate;

/**
 * 微信笔记
 * 
 * @author Birdy
 *
 */
public class WereadNote {

    private final RestTemplate template;

    private final String cookie;

    private final String id;

    public WereadNote(RestTemplate template, String cookie, String id) {
        this.template = template;
        this.cookie = cookie;
        this.id = id;
    }

    /**
     * 获取自己的划线
     */
    public void getOwnMarks() {

    }

    /**
     * 获取别人的划线
     */
    public void getOtherMarks() {

    }

    /**
     * 获取自己的想法
     */
    public void getOwnThoughts() {

    }

    /**
     * 获取自己的想法
     */
    public void getOtherThoughts() {

    }

}
