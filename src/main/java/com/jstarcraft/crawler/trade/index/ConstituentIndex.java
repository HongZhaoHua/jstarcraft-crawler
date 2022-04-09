package com.jstarcraft.crawler.trade.index;

import java.time.temporal.Temporal;
import java.util.List;

/**
 * 成分指数
 * 
 * @author Birdy
 *
 * @param <C>
 * @param <T>
 */
public interface ConstituentIndex<C, T extends Temporal> extends Index<T> {

    /**
     * 获取指数成分
     * 
     * @return
     */
    public List<C> getIndexConstituents();

}
