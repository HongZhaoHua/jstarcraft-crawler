package com.jstarcraft.crawler.book;

import java.io.File;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.conversion.xml.XmlUtility;
import com.jstarcraft.core.utility.StringUtility;

public class WereadBookTestCase {

    @Test
    public void testSearch() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://weread.qq.com/web/search/global?keyword={}&maxIdx=0&fragmentSize=120&count=20", "9787521721331");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(JsonUtility.prettyJson(content));
    }

    @Test
    public void testMd5() {
        try {
            File file = new File(WereadBookTestCase.class.getResource("md5.js").toURI());
            String script = FileUtils.readFileToString(file, StringUtility.CHARSET);
            String ENGINE_NAME = "nashorn";
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName(ENGINE_NAME);
            engine.eval(script);
            Invocable invocable = (Invocable) engine;
            System.out.println(invocable.invokeFunction("getHref", "34261011"));
            System.out.println(invocable.invokeFunction("hex_md5", "34261011"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void testBook() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format("https://weread.qq.com/web/reader/3973284058a49f39706f0c0");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content.length());
        System.out.println(XmlUtility.prettyHtml(content));
    }

}
