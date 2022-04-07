package com.jstarcraft.crawler.book;

/**
 * 章节
 * 
 * @author Birdy
 *
 */
public class Chapter {

    /** 标题 */
    private String title;

    public Chapter(String title) {
        this.title = title;
    }

    /**
     * 获取标题
     * 
     * @return
     */
    public String getChapterTitle() {
        return title;
    }

}
