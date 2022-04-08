package com.jstarcraft.crawler.trade.security.bond;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.crawler.trade.security.bond.eastmoney.EastmoneyConvertibleBond;

public class EastmoneyConvertibleBondTestCase {

    @Test
    public void testGetItemByCode() {
        RestTemplate template = new RestTemplate();
        EastmoneyConvertibleBond.getItemByCode(template, "113057");
    }

    @Test
    public void testGetHistoryByCode() {
        RestTemplate template = new RestTemplate();
        EastmoneyConvertibleBond.getHistoryByCode(template, "113057");
    }

}
