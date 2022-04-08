package com.jstarcraft.crawler.trade.security;

import java.time.LocalDate;

/**
 * 新股新债
 * 
 * @author Birdy
 *
 */
public interface IssueSecurity {

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
    public LocalDate ListDate();

}
