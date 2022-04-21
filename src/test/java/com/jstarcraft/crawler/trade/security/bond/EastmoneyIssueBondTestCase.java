package com.jstarcraft.crawler.trade.security.bond;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.trade.security.bond.eastmoney.EastmoneyIssueBond;

public class EastmoneyIssueBondTestCase {

    @Test
    public void testGetBondsByPage() {
        RestTemplate template = new RestTemplate();
        EastmoneyIssueBond.getIssueBondsByPage(template, 1, 10);
    }

}
