package com.jstarcraft.carwler;

/**
 * 爬虫
 * 
 * @author Birdy
 *
 */
public interface Carwler<T> {

    /**
     * 爬取数据
     * 
     * @return
     */
    public T carwlData();

}
