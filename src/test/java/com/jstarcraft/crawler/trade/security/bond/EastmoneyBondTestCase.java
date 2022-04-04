package com.jstarcraft.crawler.trade.security.bond;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 东方财富债券单元测试
 * 
 * <pre>
 * https://quote.eastmoney.com/
 * </pre>
 * 
 * @author Birdy
 *
 */
public class EastmoneyBondTestCase {

    // 行业
    // http://push2.eastmoney.com/api/qt/clist/get?pn={}&pz={}&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f243&fs=b:MK0354&fields=f1,f152,f2,f3,f12,f13,f14,f227,f228,f229,f230,f231,f232,f233,f234,f235,f236,f237,f238,f239,f240,f241,f242,f26,f243
    @Test
    public void testIndustry() throws InterruptedException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://eniu.com/industry/银行/market/sh/json/true", HttpMethod.GET, request, String.class);
        String data = response.getBody();
        System.out.println(data.length());
        System.out.println(JsonUtility.prettyJson(data));
    }

}
