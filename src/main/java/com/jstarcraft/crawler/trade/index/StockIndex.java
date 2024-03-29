package com.jstarcraft.crawler.trade.index;

import java.time.LocalDate;
import java.time.temporal.Temporal;

import com.jstarcraft.crawler.trade.Measure;

/**
 * 股票指数
 * 
 * @author Birdy
 *
 */
public interface StockIndex<C, T extends Temporal> extends ConstituentIndex<C, T> {

    /**
     * 获取指数市净率
     * 
     * @return
     */
    public Measure<LocalDate> getPrice2Book();

    /**
     * 获取指数市盈率
     * 
     * @return
     */
    public Measure<LocalDate> getPrice2Earn();

    /**
     * 获取指数股息率
     * 
     * @return
     */
    public Measure<LocalDate> getDividendYield();

}
