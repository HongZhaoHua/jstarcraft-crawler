package com.jstarcraft.crawler.trade.security.bond.eastmoney;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

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
import com.jstarcraft.core.common.selection.regular.RegularSelector;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.trade.security.bond.ConvertibleBond;
import com.jstarcraft.crawler.trade.security.bond.IssueBond;
import com.jstarcraft.crawler.trade.security.stock.Stock;

import it.unimi.dsi.fastutil.objects.Object2FloatRBTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;
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

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ONode node;

    /** 转换股票 */
    private Stock conversionStock;

    protected EastmoneyIssueBond(ONode node) {
        this.node = node;
    }

    @Override
    public String getBondCode() {
        return node.get("SECURITY_CODE").getString();
    }

    @Override
    public String getBondName() {
        return node.get("SECURITY_NAME_ABBR").getString();
    }

    @Override
    public float getFaceValue() {
        return node.get("PAR_VALUE").getFloat();
    }

    @Override
    public Duration getBondDuration() {
        return Duration.between(getBeginDate(), getEndDate());
    }

    private static final RegularSelector interestSelector = new RegularSelector("第([\\S]*?)年([\\d\\.]+?)%", 0, 0);

    @Override
    public Object2FloatSortedMap<LocalDate> getInterestRate() {
        Object2FloatSortedMap<LocalDate> keyValues = new Object2FloatRBTreeMap<>();
        String text = node.get("INTEREST_RATE_EXPLAIN").getString();
        Matcher matcher = interestSelector.matchContent(text);
        while (matcher.find()) {
            // TODO 中文数字转阿拉伯数字
            String year = matcher.group(1);
            String interest = matcher.group(2);
        }
        return null;
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
        return getDate(node.get("VALUE_DATE").getString());
    }

    @Override
    public LocalDate getEndDate() {
        return getDate(node.get("CEASE_DATE").getString());
    }

    @Override
    public String getCreditRank() {
        return node.get("RATING").getString();
    }

    @Override
    public Stock getConversionStock() {
        return conversionStock;
    }

    @Override
    public float getConversionPrice() {
        return node.get("TRANSFER_PRICE").getFloat();
    }

    @Override
    public float getConversionValue() {
        return node.get("TRANSFER_VALUE").getFloat();
    }

    @Override
    public float getConversionPremium() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getConversionPremiumRatio() {
        return node.get("TRANSFER_PREMIUM_RATIO").getFloat();
    }

    @Override
    public LocalDate getConversionDate() {
        return getDate(node.get("TRANSFER_START_DATE").getString());
    }

    @Override
    public float getPutablePrice() {
        return node.get("RESALE_TRIG_PRICE").getFloat();
    }

    @Override
    public float getCallablePrice() {
        return node.get("REDEEM_TRIG_PRICE").getFloat();
    }

    @Override
    public LocalDate getIssueDate() {
        return getDate(node.get("PUBLIC_START_DATE").getString());
    }

    @Override
    public LocalDate getListDate() {
        return getDate(node.get("LISTING_DATE").getString());
    }

    /** 新债列表模板 */
    // https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns={column}&sortTypes={-1:降序,1:升序}&pageNumber={page}&pageSize={size}&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO
    private static final String issueUrl = URLDecoder.decode("https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns={}&sortTypes={}&pageNumber={}&pageSize={}&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO");

    public static Map<String, EastmoneyIssueBond> getIssueBondsByPage(RestTemplate template, int page, int size) {
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
        // TODO 获取总数
        int count = root.get("result").get("count").getInt();
        Map<String, EastmoneyIssueBond> bonds = new LinkedHashMap<>();
        for (ONode node : nodes) {
            EastmoneyIssueBond bond = new EastmoneyIssueBond(node);
            bonds.put(bond.getBondCode(), bond);
        }
        return bonds;
    }

}
