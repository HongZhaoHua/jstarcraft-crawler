package com.jstarcraft.crawler.trade.security.stock;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 前瞻单元测试
 * 
 * @author Birdy
 *
 */
public class QianzhanTestCase {

    // ISIN:https://xs.qianzhan.com/hs/zhengquan_{code}.{SH/SZ/HK}.html
    @Test
    public void testIsin() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://xs.qianzhan.com/hs/zhengquan_{}.{}.html", "600000", "SH");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(XmlUtility.prettyHtml(content));
    }

}
