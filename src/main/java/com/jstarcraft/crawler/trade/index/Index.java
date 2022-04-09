package com.jstarcraft.crawler.trade.index;

import java.time.temporal.Temporal;

import com.jstarcraft.crawler.trade.Measure;

/**
 * 指数
 * 
 * @author Birdy
 *
 * @param <T>
 */
public interface Index<T extends Temporal> {

    /**
     * 获取指数代号
     * 
     * @return
     */
    public String getIndexCode();

    /**
     * 获取指数名称
     * 
     * @return
     */
    public String getIndexName();

    /**
     * 获取指数点数
     * 
     * @return
     */
    public Measure<T> getIndexValue();

}
