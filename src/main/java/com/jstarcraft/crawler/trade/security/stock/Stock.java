package com.jstarcraft.crawler.trade.security.stock;

import java.time.LocalDate;
import java.time.Year;

import com.jstarcraft.crawler.trade.Measure;
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
    default String getStockCusip() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取股票SEDOL
     * 
     * @return
     */
    default String getStockSedol() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取股票价格
     * 
     * @return
     */
    public Measure<LocalDate> getStockPrice();

    /**
     * 获取股票市净率
     * 
     * @return
     */
    public Measure<LocalDate> getPrice2Book();

    /**
     * 获取股票市盈率
     * 
     * @return
     */
    public Measure<LocalDate> getPrice2Earn();

    /**
     * 获取股票市销率
     * 
     * @return
     */
    public Measure<LocalDate> getPrice2Sale();

    /**
     * 获取股票资产回报率
     * 
     * @return
     */
    public Measure<LocalDate> getReturnAsset();

    /**
     * 获取股票权益回报率
     * 
     * @return
     */
    public Measure<LocalDate> getReturnEquity();

    /**
     * 获取股票股息率
     * 
     * @return
     */
    public Measure<Year> getDividendYield();

    /**
     * 获取股票派息率
     * 
     * @return
     */
    public Measure<Year> getDividendPayout();

}
