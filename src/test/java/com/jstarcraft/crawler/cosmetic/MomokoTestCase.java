package com.jstarcraft.crawler.cosmetic;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;

public class MomokoTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(MomokoTestCase.class);

    public static final String brandUrl = "https://cosmetic.momoko.hk/js/script.js";

    public static final String batchUrl = "https://cosmetic.momoko.hk/calculate.php";

    @Test
    public void testGetBatch() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        headers.add(HttpHeaders.ORIGIN, "https://cosmetic.momoko.hk");
        headers.add(HttpHeaders.REFERER, "https://cosmetic.momoko.hk");
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("brand", "S6");
        parameters.add("code", "9294GN");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = template.exchange(batchUrl, HttpMethod.POST, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
    }

}
