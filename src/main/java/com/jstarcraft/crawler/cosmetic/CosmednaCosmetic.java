package com.jstarcraft.crawler.cosmetic;

import java.time.LocalDate;
import java.util.List;

import jstarcraft.cloud.information.api.item.ProducerItem;
import jstarcraft.cloud.information.api.item.TradePlatform;

public class CosmednaCosmetic implements ProducerItem {

    /** 商品品牌 */
    protected String brand;

    /** 商品批次 */
    protected String batch;

    /** 生产日期 */
    protected LocalDate make;

    /** 过期日期 */
    protected LocalDate expire;

    public CosmednaCosmetic(String brand, String batch, LocalDate make, LocalDate expire) {
        this.brand = brand;
        this.batch = batch;
        this.make = make;
        this.expire = expire;
    }

    @Override
    public List<String> getCategories() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public TradePlatform getPlatform() {
        return null;
    }

    @Override
    public String getPicture() {
        return null;
    }

    @Override
    public Float getPrice() {
        return null;
    }

    @Override
    public String getProducer() {
        return null;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getSpecification() {
        return null;
    }

    @Override
    public LocalDate getMake() {
        return make;
    }

    @Override
    public LocalDate getExpire() {
        return expire;
    }

    public String getBatch() {
        return batch;
    }

    @Override
    public String toString() {
        return "CosmednaCosmetic [brand=" + brand + ", batch=" + batch + ", make=" + make + ", expire=" + expire + "]";
    }

}
