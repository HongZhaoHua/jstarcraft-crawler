package com.jstarcraft.crawler.trade.security.bond;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.trade.security.bond.$10jqka.$10jqkaIssueBond;

public class TjqkaIssueBondTestCase {

    @Test
    public void testGetBondsByPage() {
        RestTemplate template = new RestTemplate();
        $10jqkaIssueBond.getIssueBondByPage(template);
    }
    
}
