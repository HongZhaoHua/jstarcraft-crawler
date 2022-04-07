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

}
