package com.jstarcraft.crawler.trade.security.bond.essence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.noear.snack.ONode;
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
import com.jstarcraft.crawler.trade.security.IssueSecurity;

/**
 * 安信新债
 * 
 * 新债日历:https://www.essence.com.cn/service/sharesandbonds
 * 
 * @author Birdy
 *
 */
public class EssenceIssueBond implements IssueSecurity {

    protected static final Logger logger = LoggerFactory.getLogger(EssenceIssueBond.class);

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 新债列表模板 */
    // https://www.essence.com.cn/api/newbonds?pageNo={page}&pageSize={size}
    private static final String issueUrl = "https://www.essence.com.cn/api/newbonds?pageNo={}&pageSize={}";

    private ONode node;

    protected EssenceIssueBond(ONode node) {
        this.node = node;
    }

    @Override
    public String getSecurityCode() {
        return node.get("securityCode").getString();
    }

    @Override
    public String getSecurityName() {
        return node.get("securityName").getString();
    }

    private LocalDate getDate(String data) {
        try {
            return LocalDate.parse(data, formatter);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    @Override
    public LocalDate getIssueDate() {
        return getDate(node.get("issueDate").getString());
    }

    @Override
    public LocalDate getListDate() {
        return getDate(node.get("listDate").getString());
    }

    public static Map<String, EssenceIssueBond> getIssueBondsByPage(RestTemplate template, int page, int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        headers.add(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(issueUrl, page, size);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("data").ary();
        // TODO 获取总数
        int count = root.get("total").getInt();
        Map<String, EssenceIssueBond> bonds = new LinkedHashMap<>();
        for (ONode node : nodes) {
            EssenceIssueBond bond = new EssenceIssueBond(node);
            bonds.put(bond.getSecurityCode(), bond);
        }
        return bonds;
    }

}
