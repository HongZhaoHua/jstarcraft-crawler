package com.jstarcraft.crawler.trade.security.bond;

import java.time.LocalDate;

import com.jstarcraft.crawler.trade.security.stock.Stock;

/**
 * 转债
 * 
 * <pre>
 * 转债包含债权 / 期权 / 股权
 * </pre>
 * 
 * @author Birdy
 *
 */
public interface ConvertibleBond {

    /**
     * 获取转股股票/转换股票
     * 
     * @return
     */
    public Stock getConversionStock();

    /**
     * 获取转股价格/转换价格
     * 
     * @return
     */
    public float getConversionPrice();

    /**
     * 获取转股价值/转换价值
     * 
     * <pre>
     * 转股价值 = 转债面值 ÷ 转股价格 × 正股价格
     * </pre>
     * 
     * @return
     */
    public float getConversionValue();

    /**
     * 获取转股折溢价格
     * 
     * <pre>
     * 转股溢价 = 转债价格 - 转股价值
     * </pre>
     * 
     * @return
     */
    public float getConversionPremium();

    /**
     * 获取转股折溢价率
     * 
     * @return
     */
    public float getConversionPremiumRatio();

    /**
     * 获取转股日期/转换日期
     * 
     * @return
     */
    public LocalDate getConversionDate();

    /**
     * 获取回售价格(Putability/Resale)
     * 
     * <pre>
     * 债权人回售债券
     * </pre>
     * 
     * @return
     */
    public float getPutablePrice();

    /**
     * 获取赎回价格(Callability/Redeem)
     * 
     * <pre>
     * 债务人赎回债券
     * </pre>
     * 
     * @return
     */
    public float getCallablePrice();

}
