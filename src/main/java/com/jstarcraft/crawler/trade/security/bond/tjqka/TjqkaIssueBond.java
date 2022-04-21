package com.jstarcraft.crawler.trade.security.bond.tjqka;

import java.time.Duration;
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
import com.jstarcraft.crawler.exception.CrawlerException;
import com.jstarcraft.crawler.trade.security.bond.IssueBond;

import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;
import jodd.net.URLDecoder;

/**
 * 同花顺转债
 * 
 * http://data.10jqka.com.cn/ipo/bond/
 * 
 * http://data.10jqka.com.cn/ipo/kzz/
 * 
 * @author Birdy
 *
 */
public class TjqkaIssueBond implements IssueBond {

    protected static final Logger logger = LoggerFactory.getLogger(TjqkaIssueBond.class);

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 新债列表模板 */
    // http://data.10jqka.com.cn/ipo/kzz/
    private static final String issueUrl = URLDecoder.decode("http://data.10jqka.com.cn/ipo/kzz/");

    private ONode node;

    protected TjqkaIssueBond(ONode node) {
        this.node = node;
    }

    @Override
    public String getBondCode() {
        return node.get("bond_code").getString();
    }

    @Override
    public String getBondName() {
        return node.get("bond_name").getString();
    }

    @Override
    public float getFaceValue() {
        return node.get("issue_price").getFloat();
    }

    @Override
    public Duration getBondDuration() {
        return Duration.between(getBeginDate(), getEndDate());
    }

    @Override
    public Object2FloatSortedMap<LocalDate> getInterestRate() {
        throw new CrawlerException();
    }

    private LocalDate getDate(String data) {
        try {
            return LocalDate.parse(data, formatter);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    @Override
    public LocalDate getBeginDate() {
        return getDate(node.get("sub_date").getString());
    }

    @Override
    public LocalDate getEndDate() {
        return getDate(node.get("expire_date").getString());
    }

    @Override
    public String getCreditRank() {
        throw new CrawlerException();
    }

    @Override
    public LocalDate getIssueDate() {
        return getDate(node.get("sub_date").getString());
    }

    @Override
    public LocalDate getListDate() {
        return getDate(node.get("sub_date").getString());
    }

    public static Map<String, TjqkaIssueBond> getIssueBondByPage(RestTemplate template) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(issueUrl);
        url = URLDecoder.decode(url);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("list").ary();
        // TODO 获取总数
        int count = nodes.size();
        Map<String, TjqkaIssueBond> bonds = new LinkedHashMap<>();
        for (ONode node : nodes) {
            TjqkaIssueBond bond = new TjqkaIssueBond(node);
            bonds.put(bond.getBondCode(), bond);
        }
        return bonds;
    }

}
