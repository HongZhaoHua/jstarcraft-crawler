package com.jstarcraft.crawler.cosmetic;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.selection.css.JsoupCssSelector;

public class CosmednaTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(CosmednaTestCase.class);

    private static final String brandUrl = "http://www.cosmedna.com/query/";

    private static final String batchUrl = "http://www.cosmedna.com/query/?ppid={}&scph={}";

    private static final JsoupCssSelector alphabetSelector = new JsoupCssSelector("div.m-hct-lst");

    private static final JsoupCssSelector brandSelector = new JsoupCssSelector("a.js-hotcitylist");

    @Test
    public void testGetBrand() {
        RestTemplate template = new RestTemplate();
        CosmednaCosmeticManager manager = new CosmednaCosmeticManager(template);
        System.out.println(manager.getBrands());
    }

    @Test
    public void testGetBatch() {
        RestTemplate template = new RestTemplate();
        CosmednaCosmeticManager manager = new CosmednaCosmeticManager(template);
        System.out.println(manager.getCosmetic("Shiseido/资生堂", "9294GN"));
    }

}
