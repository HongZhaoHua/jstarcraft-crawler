package com.jstarcraft.crawler.trade.index;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.tuple.MapTuple;
import com.jstarcraft.crawler.trade.index.funddb.FunddbConstituent;
import com.jstarcraft.crawler.trade.index.funddb.FunddbIndex;

/**
 * 韭圈儿指数单元测试
 * 
 * @author Birdy
 *
 */
public class FunddbIndexTestCase {

    @Test
    public void testCategory() {
        RestTemplate template = new RestTemplate();
        Map<String, MapTuple> tuples = FunddbIndex.getTuplesByCategory(template, 0);
        Assert.assertEquals(159, tuples.size());
    }

    @Test
    public void testDetail() {
        RestTemplate template = new RestTemplate();
        FunddbIndex index = FunddbIndex.getIndexByCode(template, "000300.SH");
        Assert.assertEquals("000300.SH", index.getIndexCode());
        LocalDate date = LocalDate.of(2020, 6, 1);
        Assert.assertEquals(3971.34F, index.getIndexValue().getMeasureHistory().getFloat(date), 0F);
        Assert.assertTrue(1.35F == index.getPrice2Book().getMeasureHistory().getFloat(date));
        Assert.assertTrue(12.23F == index.getPrice2Earn().getMeasureHistory().getFloat(date));
        Assert.assertTrue(2.27F == index.getDividendYield().getMeasureHistory().getFloat(date));
        Assert.assertTrue(5.43F == index.getRiskPremium().getMeasureHistory().getFloat(date));
        List<FunddbConstituent> constituents = index.getIndexConstituents();
        Assert.assertEquals(300, constituents.size());
    }

}
