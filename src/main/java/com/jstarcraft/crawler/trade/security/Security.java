package com.jstarcraft.crawler.trade.security;

/**
 * 证券
 * 
 * @author Birdy
 *
 */
public interface Security {

    /**
     * 获取国际证券识别码(International Securities Identification Number/ISIN)
     * 
     * @return
     */
    public String getSecurityIsin();

    /**
     * 获取国际市场识别码(Market Identifier Code/MIC)
     * 
     * @return
     */
    public String getSecurityMic();

}
