package com.jstarcraft.crawler;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.security.SecurityUtility;
import com.jstarcraft.core.utility.StringUtility;

public class TaoBaoTestCase {

    private static RestTemplate template = new RestTemplate();

    private static String token = null;
    private static String encode = null;
    private static List<String> setCookies = new ArrayList<>();

    private static void setCookie() {
        String baseUrl = "https://h5api.m.taobao.com/h5/mtop.alimama.union.xt.en.api.entry/1.0/?jsv=2.5.1&appKey=12574478&t=1623332407239&sign=a31a065e6ae148c6261f3188bb0dbcb7&api=mtop.alimama.union.xt.en.api.entry&v=1.0&AntiCreep=true&timeout=20000&AntiFlood=true&type=jsonp&dataType=jsonp&callback=mtopjsonp2&data=%7B%22pNum%22%3A0%2C%22pSize%22%3A%2260%22%2C%22refpid%22%3A%22mm_26632258_3504122_32538762%22%2C%22variableMap%22%3A%22%7B%5C%22q%5C%22%3A%5C%22%E5%A5%B3%E8%A3%85%5C%22%2C%5C%22navigator%5C%22%3Afalse%2C%5C%22recoveryId%5C%22%3A%5C%22201_11.8.80.153_10438465_1605619321049%5C%22%7D%22%2C%22qieId%22%3A%2236308%22%2C%22spm%22%3A%22a2e0b.20350158.31919782%22%2C%22app_pvid%22%3A%22201_11.8.80.153_10438465_1605619321049%22%2C%22ctm%22%3A%22spm-url%3A%3Bpage_url%3Ahttps%253A%252F%252Fuland.taobao.com%252Fsem%252Ftbsearch%253Frefpid%253Dmm_26632258_3504122_32538762%2526keyword%253D%2525E5%2525A5%2525B3%2525E8%2525A3%252585%2526clk1%253D84e8265c9bc383b4974d0cac1842e135%2526upsid%253D84e8265c9bc383b4974d0cac1842e135%22%7D";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>((Void) null, headers);
        ResponseEntity<String> response = template.exchange(baseUrl, HttpMethod.GET, request, String.class);
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
//        System.out.println(cookies);
        for (String cookie : cookies) {
            if (cookie.startsWith("_m_h5_tk=")) {
//                setCookies.add(cookie);
                token = cookie;
                token = token.split(";")[0];
                token = token.split("=")[1];
                setCookies.add(StringUtility.format("_m_h5_tk={}", token));
                token = token.split("_")[0];
                continue;
            }
            if (cookie.startsWith("_m_h5_tk_enc=")) {
//                setCookies.add(cookie);
                encode = cookie;
                encode = encode.split(";")[0];
                encode = encode.split("=")[1];
                setCookies.add(StringUtility.format("_m_h5_tk_enc={}", encode));
                continue;
            }
        }
//        setCookies.add("Path=/");
//        setCookies.add("Domain=taobao.com");
//        setCookies.add("Max-Age=604800");
//        System.out.println(token);
//        System.out.println(setCookies);
    }

    private static String signature(String token, String tme, String appKey, String data) {
        String content = token + "&" + tme + "&" + appKey + "&" + data;
        System.out.println("content=" + content);
        byte[] bytes = content.getBytes(StringUtility.CHARSET);
        String signature = SecurityUtility.byte2Hex(SecurityUtility.signatureMd5(bytes));
        System.out.println("signature=" + signature);
        return signature;
    }

    private static void search(String word, int page, int size) throws Exception {
        String pageUrl = StringUtility.format("https://uland.taobao.com/sem/tbsearch?refpid=mm_26632258_3504122_32538762&keyword={}&clk1=00304da0d4a567574289c0124f3bd1df&upsId=00304da0d4a567574289c0124f3bd1df", URLEncoder.encode(word, "UTF-8"));
        System.out.println("pageUrl=" + pageUrl);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("pNum", page);
        data.put("pSize", size + "");
        data.put("refpid", "mm_26632258_3504122_32538762");
        Map<String, Object> variableMap = new LinkedHashMap<>();
        variableMap.put("q", word);
        variableMap.put("navigator", false);
        variableMap.put("recoveryId", "201_11.186.100.175_10506131_1623332407239");
        data.put("variableMap", JsonUtility.object2String(variableMap));
        data.put("qieId", "36308");
        data.put("spm", "a2e0b.20350158.31919782");
        data.put("app_pvid", "201_11.186.100.175_10506131_1623332407239");
        data.put("ctm", "spm-url:;page_url:" + pageUrl);
        String stringData = JsonUtility.object2String(data);
        System.out.println("stringData=" + stringData);
//        String tme = System.currentTimeMillis() + "";
        String tme = "1624756541422";
        System.out.println("tme=" + tme);
//        String signature = signature(token, tme, "12574478", stringData);
        String signature = signature("6a01cc9baf32dbc935b9cbde7a11d178", tme, "12574478", stringData);
        stringData = URLEncoder.encode(stringData, "UTF-8");
        System.out.println("data=" + stringData);
        String url = StringUtility.format("https://h5api.m.taobao.com/h5/mtop.alimama.union.xt.en.api.entry/1.0/?jsv=2.5.1&appKey=12574478&t={}&sign={}&api=mtop.alimama.union.xt.en.api.entry&v=1.0&AntiCreep=true&timeout=20000&AntiFlood=true&type=jsonp&dataType=jsonp&callback=mtopjsonp2&data={}", tme, signature, stringData);
        System.out.println("url=" + url);
        HttpHeaders headers = new HttpHeaders();
//        headers.put(HttpHeaders.COOKIE, setCookies);
        headers.add(HttpHeaders.COOKIE, "_m_h5_tk=6a01cc9baf32dbc935b9cbde7a11d178_1624763741570; _m_h5_tk_enc=b0ee15f6e18b6b28f9f3410eb96e8543");

//        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(URI.create(url), HttpMethod.GET, request, String.class);
        String content = response.getBody();
        System.out.println(content);
    }

    public static void main(String[] arguments) throws Exception {
        setCookie();
        search("衣服", 1, 20);

//        String signature = signature("6a01cc9baf32dbc935b9cbde7a11d178", "1624756541422", "12574478", "{\"pNum\":1,\"pSize\":\"20\",\"refpid\":\"mm_26632258_3504122_32538762\",\"variableMap\":\"{\\\"q\\\":\\\"上衣\\\",\\\"navigator\\\":false,\\\"recoveryId\\\":\\\"201_11.186.100.175_10506131_1623332407239\\\"}\",\"qieId\":\"36308\",\"spm\":\"a2e0b.20350158.31919782\",\"app_pvid\":\"201_11.186.100.175_10506131_1623332407239\",\"ctm\":\"spm-url:;page_url:https://uland.taobao.com/sem/tbsearch?refpid=mm_26632258_3504122_32538762&keyword=%E4%B8%8A%E8%A1%A3&clk1=00304da0d4a567574289c0124f3bd1df&upsId=00304da0d4a567574289c0124f3bd1df\"}");
//        System.out.println(signature);
    }

}
