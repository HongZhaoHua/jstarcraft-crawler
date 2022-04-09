package com.jstarcraft.crawler.trade;

import java.time.temporal.Temporal;

import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;

/**
 * 度量衡指标
 * 
 * @author Birdy
 *
 */
public interface Measure<T extends Temporal> {

    /**
     * 获取指标数值
     * 
     * @return
     */
    public float getMeasureValue();

    /**
     * 获取指标历史
     * 
     * @return
     */
    public Object2FloatSortedMap<T> getMeasureHistory();

}
