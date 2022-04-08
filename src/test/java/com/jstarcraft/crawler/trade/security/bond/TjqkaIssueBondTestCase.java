package com.jstarcraft.crawler.trade.security.bond;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.trade.security.bond.tjqka.TjqkaIssueBond;

public class TjqkaIssueBondTestCase {

    @Test
    public void testGetItemsByPage() {
        RestTemplate template = new RestTemplate();
        TjqkaIssueBond.getItemsByPage(template);
    }
    
}
