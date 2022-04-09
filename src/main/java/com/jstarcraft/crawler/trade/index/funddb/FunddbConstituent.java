package com.jstarcraft.crawler.trade.index.funddb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 韭圈儿成分股票
 * 
 * @author Birdy
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunddbConstituent {

    private String id;

    private String code;

    private String name;

    private float weight;

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

}
