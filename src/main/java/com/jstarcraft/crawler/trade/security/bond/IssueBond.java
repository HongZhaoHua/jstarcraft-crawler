package com.jstarcraft.crawler.trade.security.bond;

import java.time.LocalDate;

/**
 * 新债
 * 
 * @author Birdy
 *
 */
public interface IssueBond {

    /**
     * 获取发行日期/申购日期
     * 
     * @return
     */
    public LocalDate getIssueDate();

    /**
     * 获取上市日期
     * 
     * @return
     */
    public LocalDate getListDate();
    
    /**
     * 获取下市日期
     * 
     * @return
     */
    public LocalDate getDelistDate();

}
