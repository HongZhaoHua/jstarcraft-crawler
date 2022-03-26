package com.jstarcraft.crawler;

/**
 * 爬虫
 * 
 * @author Birdy
 *
 */
public interface Crawler<T> {

    /**
     * 爬取数据
     * 
     * @return
     */
    public T carwlData();

}
