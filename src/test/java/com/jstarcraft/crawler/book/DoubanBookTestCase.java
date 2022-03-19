package com.jstarcraft.crawler.book;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.common.selection.regular.RegularSelector;
import com.jstarcraft.core.utility.StringUtility;

public class DoubanBookTestCase {

    private RegularSelector tagSelector = new RegularSelector("criteria\\s*=\\s*'([\\S]*)'");

    // ISBN:https://book.douban.com/subject/{}/
    @Test
    public void testTag() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://book.douban.com/subject/{}/", "35193035");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(XmlUtility.prettyHtml(content));
        System.out.println(tagSelector.selectSingle(content));
    }

}
