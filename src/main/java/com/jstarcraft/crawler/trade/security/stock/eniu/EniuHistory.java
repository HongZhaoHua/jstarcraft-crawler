package com.jstarcraft.crawler.trade.security.stock.eniu;

import java.util.List;

import org.noear.snack.ONode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.selection.jsonpath.SnackJsonPathSelector;
import com.jstarcraft.core.utility.StringUtility;

import it.unimi.dsi.fastutil.objects.Object2FloatAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2FloatSortedMap;

/**
 * 亿牛历史
 * 
 * <pre>
 * https://eniu.com
 * </pre>
 * 
 * @author Birdy
 *
 */
public enum EniuHistory {

    AB_PRICE("https://eniu.com/chart/pricea/{}/t/all", "$.date", "$.price"),

    AB_PE("https://eniu.com/chart/pea/{}/t/all", "$.date", "$.pe_ttm"),

    AB_PB("https://eniu.com/chart/pba/{}/t/all", "$.date", "$.pb"),

    AB_PS("https://eniu.com/chart/psa/{}/t/all", "$.date", "$.ps"),

    AB_DY("https://eniu.com/chart/dva/{}/t/all", "$.date", "$.dv"),

    AB_DP("https://eniu.com/chart/pxla/{}", "$.chart.fhnd", "$.chart.avggx"),

    AB_ROA("https://eniu.com/chart/roea/{}/q/0", "$.date", "$.roa"),

    AB_ROE("https://eniu.com/chart/roea/{}/q/0", "$.date", "$.roe"),

    H_PRICE("https://eniu.com/chart/priceh/{}", "$.date", "$.price"),

    H_PE("https://eniu.com/chart/peh/{}", "$.date", "$.pe"),

    H_PB("https://eniu.com/chart/pbh/{}", "$.date", "$.pb"),

    H_DY("https://eniu.com/chart/dvh/{}", "$.date", "$.dv"),

    H_ROE("https://eniu.com/chart/roeh/{}", "$.date", "$.roe");

    private final String urlTemplate;

    private final SnackJsonPathSelector dateSelector;

    private final SnackJsonPathSelector dataSelector;

    private EniuHistory(String urlTemplate, String dateQuery, String dataQuery) {
        this.urlTemplate = urlTemplate;
        this.dateSelector = new SnackJsonPathSelector(dateQuery);
        this.dataSelector = new SnackJsonPathSelector(dataQuery);
    }

    public Object2FloatSortedMap<String> getHistory(RestTemplate template, String code) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(urlTemplate, code);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String content = response.getBody();
        ONode root = ONode.load(content);
        List<ONode> dates = dateSelector.selectMultiple(root);
        List<ONode> datas = dataSelector.selectMultiple(root);
        Object2FloatSortedMap<String> history = new Object2FloatAVLTreeMap<>();
        history.defaultReturnValue(Float.NaN);
        int size = dates.size();
        for (int index = 0; index < size; index++) {
            String date = dates.get(index).getString();
            float data = datas.get(index).getFloat();
            history.put(date, data);
        }
        return history;
    }

}
