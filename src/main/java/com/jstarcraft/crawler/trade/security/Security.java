package com.jstarcraft.crawler.trade.security;

import com.jstarcraft.crawler.exception.CrawlerException;

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
        throw new CrawlerException();
    }

    /**
     * 获取国际市场识别码(Market Identifier Code/MIC)
     * 
     * @return
     */
    default String getSecurityMic() {
        throw new CrawlerException();
    }

}
