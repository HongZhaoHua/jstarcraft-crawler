package com.jstarcraft.crawler.trade.security.bond;

/**
 * 债券
 * 
 * @author Birdy
 *
 */
public interface Bond {

    /**
     * 获取债券代号
     * 
     * @return
     */
    public String getBondCode();

    /***
     * 获取债券名称
     * 
     * @return
     */
    public String getBondName();

    /**
     * 获取债券面值
     * 
     * @return
     */
    public float getFaceValue();

    /**
     * 获取债券价格
     * 
     * @return
     */
    public float getBondPrice();

    /**
     * 获取债券涨跌额
     * 
     * @return
     */
    public float getBondChangeAmount();

    /**
     * 获取债券涨跌幅
     * 
     * @return
     */
    public float getBondChangeRate();

    /**
     * 获取债券开盘价格
     * 
     * @return
     */
    public float getBondOpeningPrice();

    /**
     * 获取债券收盘价格
     * 
     * @return
     */
    public float getBondClosingPrice();

    /**
     * 获取债券最高价格
     * 
     * @return
     */
    public float getBondHighestPrice();

    /**
     * 获取债券最低价格
     * 
     * @return
     */
    public float getBondLowestPrice();

    /**
     * 成交额(考虑迁移到Security)
     * 
     * @return
     */
    public float getTurnover();

    /**
     * 成交量(考虑迁移到Security)
     * 
     * @return
     */
    public float getVolume();

    /**
     * 获取债券净价/干净价格
     * 
     * @return
     */
    public float getClearPrice();

    /**
     * 获取债券全价/肮脏价格
     * 
     * @return
     */
    public float getDirtyPrice();

}
