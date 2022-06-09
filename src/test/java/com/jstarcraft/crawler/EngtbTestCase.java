package com.jstarcraft.crawler;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.utility.StringUtility;

/**
 * Engtb单元测试
 * 
 * @author Birdy
 *
 */
public class EngtbTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(EngtbTestCase.class);

    private static RestTemplate template = new RestTemplate();

    private String findUrl = "https://www.engtb.com/shop.html?action=list&tp=taobao&key={}&page={}";

    /**
     * 以词搜物
     */
    @Test
    public void testSearchItemsByWord() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(findUrl, "手机", 2);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(XmlUtility.prettyHtml(data));
        }
    }

    private String getUrl = "https://www.engtb.com/item/taobao/{}.html";

    public void testGetItem() {

    }

}
