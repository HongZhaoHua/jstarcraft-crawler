package com.jstarcraft.crawler.book;

import java.util.Collection;
import java.util.List;

/**
 * 书籍
 * 
 * @author Birdy
 *
 */
public interface Book {

    /**
     * 获取标识
     * 
     * @return
     */
    public String getId();

    /**
     * 获取标题
     * 
     * @return
     */
    public String getTitle();

    /**
     * 获取章节
     * 
     * @return
     */
    public Collection<String> getChapters();

    /**
     * 获取国际标准书号(International Standard Book Number/ISBN)
     * 
     * @return
     */
    public String getIsbn();

    /**
     * 获取得分
     * 
     * @return
     */
    public String getScore();

    /**
     * 获取标签
     * 
     * @return
     */
    public List<String> getTags();

}
