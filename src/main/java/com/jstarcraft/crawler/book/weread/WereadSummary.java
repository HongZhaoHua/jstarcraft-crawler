package com.jstarcraft.crawler.book.weread;

import com.jstarcraft.core.utility.StringUtility;

/**
 * 微信摘要
 * 
 * @author Birdy
 *
 */
public class WereadSummary {

    /** 书籍 */
    private String book;

    /** 章节 */
    private int chapter;

    /** 内容 */
    private String content;

    /** 评论 */
    private String comment;

    public WereadSummary(String book, int chapter, String content) {
        this(book, chapter, content, StringUtility.EMPTY);
    }

    public WereadSummary(String book, int chapter, String content, String comment) {
        this.book = book;
        this.chapter = chapter;
        this.content = content;
        this.comment = comment;
    }

    public String getBookId() {
        return book;
    }

    public int getChapterId() {
        return chapter;
    }

    public String getContent() {
        return content;
    }

    public String getComment() {
        return comment;
    }

}
