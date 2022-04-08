package com.jstarcraft.crawler.trade.security.bond;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.trade.security.bond.essence.EssenceIssueBond;

public class EssenceIssueBondTestCase {

    @Test
    public void testGetItemsByPage() {
        RestTemplate template = new RestTemplate();
        EssenceIssueBond.getItemsByPage(template, 1, 10);
    }

}
