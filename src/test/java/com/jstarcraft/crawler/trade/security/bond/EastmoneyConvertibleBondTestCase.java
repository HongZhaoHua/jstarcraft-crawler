package com.jstarcraft.crawler.trade.security.bond;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class EastmoneyConvertibleBondTestCase {

    @Test
    public void testGetItemsByPage() {
        RestTemplate template = new RestTemplate();
        EastmoneyConvertibleBond.getItemsByPage(template, 1);
    }

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
