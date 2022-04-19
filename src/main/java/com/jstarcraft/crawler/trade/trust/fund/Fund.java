package com.jstarcraft.crawler.trade.trust.fund;

import java.time.temporal.Temporal;

import com.jstarcraft.crawler.trade.Measure;

/**
 * 基金
 * 
 * @author Birdy
 *
 * @param <T>
 */
public interface Fund<T extends Temporal> {

    /**
     * 获取基金净值
     * 
     * @return
     */
    public Measure<T> getAssetValue();

}
