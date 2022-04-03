package com.jstarcraft.crawler.book;

/**
 * 微信进度
 * 
 * @author Birdy
 *
 */
public class WereadProgress {

    /** 标识 */
    private String id;

    /** 标题 */
    private String title;

    /** 进度 */
    private int progress;

    public WereadProgress(String id, String title, int progress) {
        this.id = id;
        this.title = title;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

}
