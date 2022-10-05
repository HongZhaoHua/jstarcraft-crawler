package com.jstarcraft.crawler.book;

import java.util.List;

/**
 * 书籍
 * 
 * @author Birdy
 *
 */
public interface Book<C extends Chapter> {

    /**
     * 获取书籍标识
     * 
     * @return
     */
    public String getBookId();

    /**
     * 获取书籍标题
     * 
     * @return
     */
    public String getBookTitle();

    /**
     * 获取书籍封面
     * 
     * @return
     */
    public String getBookPicture();

    /**
     * 获取书籍章节
     * 
     * @return
     */
    public List<C> getBookChapters();

    /**
     * 获取国际标准书号(International Standard Book Number/ISBN)
     * 
     * @return
     */
    public String getBookIsbn();

    /**
     * 获取书籍价格
     * 
     * @return
     */
    public Float getBookPrice();

    /**
     * 获取书籍得分
     * 
     * @return
     */
    public Float getBookScore();

    /**
     * 获取书籍标签
     * 
     * @return
     */
    public List<String> getBookTags();

}
