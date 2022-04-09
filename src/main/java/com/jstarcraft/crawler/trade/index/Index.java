package com.jstarcraft.crawler.trade.index;

/**
 * 指数
 * 
 * @author Birdy
 *
 */
public interface Index {

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
     * 获取指数市净率
     * 
     * @return
     */
    public float getPrice2Book();

    /**
     * 获取指数市盈率
     * 
     * @return
     */
    public float getPrice2Earn();

}
