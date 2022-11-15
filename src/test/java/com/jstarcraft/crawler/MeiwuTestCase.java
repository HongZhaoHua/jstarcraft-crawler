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

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.utility.StringUtility;

public class MeiwuTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(MeiwuTestCase.class);

    private static RestTemplate template = new RestTemplate();

    private static final String getUserUrl = "https://api.meiwulist.com/api_service/api/v1/user/getNewUserId";

    @Test
    public void testGetUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(getUserUrl);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
    }

    private static final String getNewsUrl = "https://api.meiwulist.com/api_service/api/v1/content/listYangmaoBaoliaoV2?pageNo={}&pageSize={}&userId={}&category={}";

    /**
     * 实时爆料
     */
    @Test
    public void testGetNews() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        int pageIndex = 1;
        int pageSize = 10;
        String url = StringUtility.format(getNewsUrl, pageIndex, pageSize, 5, "");
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
    }

    private static final String getRankUrl = "https://api.meiwulist.com/api_service/api/v1/meiwu/listDtkCategoryGoods?pageNo={}&pageSize={}&rankType={}&cid={}";

    /**
     * 排行榜单
     */
    @Test
    public void testGetRank() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        int pageIndex = 1;
        int pageSize = 10;
        // 1代表2小时,2代表全天
        int rankType = 1;
        String url = StringUtility.format(getRankUrl, pageIndex, pageSize, rankType, 0);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
    }

    private static final String getGuessUrl = "https://api.meiwulist.com/api_service/api/v1/meiwu/listPindaoGoods?pageNo={}&pageSize={}&pindaoId={}";

    /**
     * 猜你喜欢
     */
    @Test
    public void testGetGuess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        int pageIndex = 1;
        int pageSize = 10;
        String url = StringUtility.format(getGuessUrl, pageIndex, pageSize, 11);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
    }

    private static final String getFreeUrl = "https://api.meiwulist.com/api_service/api/v1/content/list99Goods?pageNo={}&pageSize={}&userId={}";

    /**
     * 全场包邮
     */
    @Test
    public void testGetFree() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        int pageIndex = 1;
        int pageSize = 10;
        String url = StringUtility.format(getFreeUrl, pageIndex, pageSize, 5);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
    }

}
