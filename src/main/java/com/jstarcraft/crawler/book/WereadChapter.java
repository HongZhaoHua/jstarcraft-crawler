package com.jstarcraft.crawler.book;

/**
 * 微信章节
 * 
 * @author Birdy
 *
 */
public class WereadChapter extends Chapter {

    /** 标识 */
    private int id;

    /** 层级 */
    private int level;

    public WereadChapter(String title, int id, int level) {
        super(title);
        this.id = id;
        this.level = level;
    }

    /**
     * 获取标识
     * 
     * @return
     */
    public int getChapterId() {
        return id;
    }

    /**
     * 获取层级
     * 
     * @return
     */
    public int getChapterLevel() {
        return level;
    }

}
