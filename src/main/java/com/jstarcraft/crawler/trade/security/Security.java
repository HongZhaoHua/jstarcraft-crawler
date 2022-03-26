package com.jstarcraft.crawler.trade.security;

public interface Security {

    /**
     * 获取国际证券识别码(International Securities Identification Number/ISIN)
     * 
     * @return
     */
    public String getIsin();

    /**
     * 获取国际市场识别码(Market Identifier Code/MIC)
     * 
     * @return
     */
    public String getMic();

}
