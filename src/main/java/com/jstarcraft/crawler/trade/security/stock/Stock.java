package com.jstarcraft.crawler.trade.security.stock;

import com.jstarcraft.crawler.trade.security.Security;

/**
 * 股票
 * 
 * @author Birdy
 *
 */
public interface Stock extends Security {

    /**
     * 获取股票代号
     * 
     * @return
     */
    public String getStockSymbol();

    /**
     * 获取股票CUSIP
     * 
     * @return
     */
    public String getStockCusip();

    /**
     * 获取股票SEDOL
     * 
     * @return
     */
    public String getStockSedol();

    /**
     * 获取股票价格
     * 
     * @return
     */
    public float getStockPrice();

    /**
     * 获取股票市净率
     * 
     * @return
     */
    public float getPrice2Book();

    /**
     * 获取股票市盈率
     * 
     * @return
     */
    public float getPrice2Earn();

    /**
     * 获取股票市销率
     * 
     * @return
     */
    public float getPrice2Sale();

    /**
     * 获取股票资产回报率
     * 
     * @return
     */
    public float getReturnAsset();

    /**
     * 获取股票权益回报率
     * 
     * @return
     */
    public float getReturnEquity();

    /**
     * 股息率
     * 
     * @return
     */
    public float getDividendYield();

    /**
     * 派息率
     * 
     * @return
     */
    public float getDividendPayout();

}
