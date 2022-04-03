package com.jstarcraft.crawler.mind;

import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 亿图脑图单元测试
 * 
 * @author Birdy
 *
 */
public class EdrawsoftMindTestCase {

    private void show(Topic topic) {
        System.out.println(topic.getText());
        List<Topic> children = topic.getChildren();
        if (children != null) {
            for (Topic child : children) {
                show(child);
            }
        }
    }

    @Test
    public void testGetSummary() {
        RestTemplate template = new RestTemplate();
        String id = "137642";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://masterapi.edrawsoft.cn/api/user/0/work/info?work_id={}", id);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(data.length());
        System.out.println(JsonUtility.prettyJson(data));
    }

    @Test
    public void testGetDetail() throws Exception {
        RestTemplate template = new RestTemplate();
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            // TODO StringHttpMessageConverter默认字符集为ISO_8859_1,重置字符集为UTF-8
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) converter;
                messageConverter.setDefaultCharset(StringUtility.CHARSET);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://edrawcloudpubliccn.oss-cn-shenzhen.aliyuncs.com/work/16066015/2021-6-28/1624880100/outline.json");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(data.length());
        System.out.println(JsonUtility.prettyJson(data));
        Topic topic = JsonUtility.string2Object(data, Topic.class);
//        System.out.println(topic.getChild().size());
        show(topic);
    }

}
