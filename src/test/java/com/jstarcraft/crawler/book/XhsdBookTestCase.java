package com.jstarcraft.crawler.book;

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
 * 新华书店书籍单元测试
 * 
 * @author Birdy
 *
 */
public class XhsdBookTestCase {

    @Test
    public void testSearch() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://search.xhsd.com/search?keyword={}", "9787213066856");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(data.length());
        System.out.println(XmlUtility.prettyHtml(data));
    }

    /**
     * https://item.xhsd.com/api/item/comment/paging?pageNo=1&pageSize=20&parentId=-1&itemId={}
     */
    @Test
    public void testScore() {

    }

}
