package com.jstarcraft.crawler.trade.security.bond;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.trade.security.bond.eastmoney.EastmoneyIssueBond;

public class EastmoneyIssueBondTestCase {

    @Test
    public void testGetItemsByPage() {
        RestTemplate template = new RestTemplate();
        EastmoneyIssueBond.getItemsByPage(template, 1, 5);
    }

}
