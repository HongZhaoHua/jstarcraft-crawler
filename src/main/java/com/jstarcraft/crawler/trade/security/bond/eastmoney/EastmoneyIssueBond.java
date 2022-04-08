package com.jstarcraft.crawler.trade.security.bond.eastmoney;

import java.time.LocalDate;
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
import com.jstarcraft.crawler.trade.security.bond.ConvertibleBond;
import com.jstarcraft.crawler.trade.security.bond.IssueBond;
import com.jstarcraft.crawler.trade.security.stock.Stock;

import jodd.net.URLDecoder;

/**
 * 东方财富新债
 * 
 * 新债申购:https://data.eastmoney.com/kzz/default.html
 * 
 * @author Birdy
 *
 */
public class EastmoneyIssueBond implements ConvertibleBond, IssueBond {

    protected static final Logger logger = LoggerFactory.getLogger(EastmoneyIssueBond.class);

    /** 新债列表模板 */
    // https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns={column}&sortTypes={-1:降序,1:升序}&pageNumber={page}&pageSize={size}&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO
    private static final String issueUrl = URLDecoder.decode("https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns={}&sortTypes={}&pageNumber={}&pageSize={}&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO");

    private Stock conversionStock;

    private float conversionPrice;

    private float conversionValue;

    private float conversionPremium;

    @Override
    public Stock getConversionStock() {
        // TODO Auto-generated method stub
        return conversionStock;
    }

    @Override
    public float getConversionPrice() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getConversionValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getConversionPremium() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getConversionPremiumRatio() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getPutablePrice() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getCallablePrice() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public LocalDate getIssueDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LocalDate ListDate() {
        // TODO Auto-generated method stub
        return null;
    }

    public static Map<String, String> getItemsByPage(RestTemplate template, int page, int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(issueUrl, "PUBLIC_START_DATE", -1, page, size);
        url = URLDecoder.decode(url);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        List<ONode> nodes = root.get("result").get("data").ary();
        Map<String, String> items = new LinkedHashMap<>();
        for (ONode node : nodes) {
            String id = node.get("id").getString();
            String title = node.get("title").getString();
            items.put(id, title);
        }
        return items;
    }

}
