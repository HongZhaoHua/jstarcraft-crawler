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
    default String getSecurityIsin() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取国际市场识别码(Market Identifier Code/MIC)
     * 
     * @return
     */
    default String getSecurityMic() {
        throw new UnsupportedOperationException();
    }

}
