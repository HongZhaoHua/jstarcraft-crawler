package com.jstarcraft.crawler.trade.index;

import org.junit.Test;
import org.noear.snack.ONode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.selection.jsonpath.SnackJsonPathSelector;
import com.jstarcraft.crawler.trade.index.funddb.FunddbIndex;

/**
 * 韭圈儿指数单元测试
 * 
 * <pre>
 * 数据源 https://funddb.cn/site/index
 * 
 * 代码源 https://github.com/akfamily/akshare/blob/master/akshare/index/zh_stock_index_csindex.py
 * </pre>
 * 
 * @author Birdy
 *
 */
public class FunddbIndexTestCase {

    @Test
    public void testCategory() {
        RestTemplate template = new RestTemplate();
        FunddbIndex.getTuplesByCategory(template, 6);
    }

    @Test
    public void testDetail() {
        RestTemplate template = new RestTemplate();
        FunddbIndex.getIndexByCode(template, "000852.SH");
    }

    @Test
    public void testHistory() {
        RestTemplate template = new RestTemplate();
        FunddbIndex.getHistoryByCode(template, "000852.SH", "pe").get();
        FunddbIndex.getHistoryByCode(template, "000852.SH", "pb").get();
        FunddbIndex.getHistoryByCode(template, "000852.SH", "dy").get();
        FunddbIndex.getHistoryByCode(template, "000852.SH", "rp").get();
    }

    /**
     * 获取成分股
     * 
     * @throws InterruptedException
     */
    @Test
    public void testConstituentStock() throws InterruptedException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("gu_code", "000009.SH");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = template.exchange("https://api.jiucaishuo.com/v2/guzhi/newtubiaodata", HttpMethod.POST, request, String.class);
        String data = response.getBody();
//        System.out.println(content);
        ONode root = ONode.load(data);
        SnackJsonPathSelector selector = new SnackJsonPathSelector("$.data.cl_many_info");
        ONode name = root.get("data").get("gu_name");
        System.out.println(name);
        int index = 0;
        for (ONode node : selector.selectMultiple(root)) {
            System.out.println(index++);
            System.out.println(node);
        }
    }

}
