package com.jstarcraft.crawler.trade.security.bond;

import java.time.Duration;
import java.time.LocalDate;

import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;

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
     * 获取债券期限
     * 
     * @return
     */
    public Duration getBondDuration();

    /**
     * 获取债券利率(TODO 兼容分段利率)
     * 
     * @return
     */
    public Object2FloatSortedMap<LocalDate> getInterestRate();

    /**
     * 获取债券起息日期
     * 
     * @return
     */
    public LocalDate getBeginDate();

    /**
     * 获取债券止息日期
     * 
     * @return
     */
    public LocalDate getEndDate();

    /**
     * 获取债券信用评级
     * 
     * @return
     */
    public String getCreditRank();

}
