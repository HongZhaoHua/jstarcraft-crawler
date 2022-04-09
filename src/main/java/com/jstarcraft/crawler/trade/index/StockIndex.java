package com.jstarcraft.crawler.trade.index;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import com.jstarcraft.crawler.trade.Measure;
import com.jstarcraft.crawler.trade.security.stock.Stock;

/**
 * 股票指数
 * 
 * @author Birdy
 *
 */
public interface StockIndex extends Index {

    /**
     * 获取成分股票
     * 
     * @return
     */
    public List<Stock> getConstituentStocks();

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
    public Measure<Year> getDividendYield();

}
