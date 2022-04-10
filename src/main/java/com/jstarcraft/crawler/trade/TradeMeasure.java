package com.jstarcraft.crawler.trade;

import java.time.temporal.Temporal;
import java.util.function.Supplier;

import com.jstarcraft.crawler.exception.CrawlerException;

import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;

/**
 * 交易指标
 * 
 * @author Birdy
 *
 * @param <T>
 */
public class TradeMeasure<T extends Temporal> implements Measure<T> {

    private float value;

    private Supplier<Object2FloatSortedMap<T>> history;

    public TradeMeasure(float value) {
        this(value, null);
    }

    public TradeMeasure(float value, Supplier<Object2FloatSortedMap<T>> history) {
        this.value = value;
        this.history = history;
    }

    @Override
    public float getMeasureValue() {
        return value;
    }

    @Override
    public Object2FloatSortedMap<T> getMeasureHistory() {
        if (history == null) {
            throw new CrawlerException();
        }
        return history.get();
    }

}
