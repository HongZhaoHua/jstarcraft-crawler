package com.jstarcraft.crawler.trade.index;

import java.util.List;

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

/**
 * 韭圈儿单元测试
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
public class FunddbTestCase {

    @Test
    public void testCategory() throws InterruptedException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = template.exchange("https://api.jiucaishuo.com/v2/guzhi/showcategory", HttpMethod.GET, request, String.class);
        String content = response.getBody();
        ONode root = ONode.load(content);
        SnackJsonPathSelector selector = new SnackJsonPathSelector("$.data.right_list");
        for (ONode node : selector.selectContent(root)) {
            System.out.println(node);
        }
    }

    @Test
    public void testIndex() throws InterruptedException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("gu_code", "399441.SZ");
        parameters.add("pe_category", "pb");
        parameters.add("year", -1);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = template.exchange("https://api.jiucaishuo.com/v2/guzhi/newtubiaolinedata", HttpMethod.POST, request, String.class);
        String content = response.getBody();
        System.out.println(content);
        ONode root = ONode.load(content);
        ONode average = root.get("data").get("ping_pe");
        SnackJsonPathSelector selector = new SnackJsonPathSelector("$.data.tubiao.series[?(@.name == '市净率')].data");
        List<ONode> nodes = selector.selectContent(root);
        for (ONode node : nodes) {
            System.out.println(node.ary().size());
        }
        System.out.println(nodes.size());
    }
    
    /**
     * 获取成分股
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
        String content = response.getBody();
//        System.out.println(content);
        ONode root = ONode.load(content);
        SnackJsonPathSelector selector = new SnackJsonPathSelector("$.data.cl_many_info");
        ONode name = root.get("data").get("gu_name");
        System.out.println(name);
        int index = 0;
        for (ONode node : selector.selectContent(root)) {
            System.out.println(index++);
            System.out.println(node);
        }
    }

}
