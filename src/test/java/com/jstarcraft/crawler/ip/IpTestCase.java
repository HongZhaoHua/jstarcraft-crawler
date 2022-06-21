package com.jstarcraft.crawler.ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
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
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.utility.StringUtility;

public class IpTestCase {

    @Test
    public void testT086() {
        RestTemplate template = new RestTemplate();
        String ip = "119.131.76.71";
        String ipUrl = "http://ip.t086.com/?ip={}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(ipUrl, ip);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(XmlUtility.prettyHtml(data));
    }

    @Test
    public void testIp138() {
        RestTemplate template = new RestTemplate();
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            // TODO StringHttpMessageConverter默认字符集为ISO_8859_1,重置字符集为GB2312
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) converter;
                messageConverter.setDefaultCharset(Charset.forName("GB2312"));
            }
        }
        String ip = "119.131.76.71";
        String ipUrl = "https://www.ip138.com/iplookup.asp?ip={}&action=2";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(ipUrl, ip);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(XmlUtility.prettyHtml(data));
    }

    @Test
    public void testIpapi() {
        RestTemplate template = new RestTemplate();
        String ip = "119.131.76.71";
        String ipUrl = "http://ip-api.com/json/{}?lang=zh-CN";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(ipUrl, ip);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(JsonUtility.prettyJson(data));
    }

    @Test
    public void testGeoplugin() {
        RestTemplate template = new RestTemplate();
        String ip = "119.131.76.71";
        String ipUrl = "http://www.geoplugin.net/php.gp?ip={}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(ipUrl, ip);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        // Library for serializing Java objects into the PHP serializing format and
        // unserializing data from this format back into Java objects.
        // https://github.com/kayahr/pherialize
    }

    @Test
    public void testPconline() {
        RestTemplate template = new RestTemplate();
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            // TODO StringHttpMessageConverter默认字符集为ISO_8859_1,重置字符集为GBK
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) converter;
                messageConverter.setDefaultCharset(Charset.forName("GBK"));
            }
        }
        String ip = "119.131.76.71";
        String ipUrl = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip={}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(ipUrl, ip);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(JsonUtility.prettyJson(data));
    }

}
