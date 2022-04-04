package com.jstarcraft.crawler.trade.security;

/**
 * 东方财富字段
 * 
 * http://quote.eastmoney.com/center/js/gridlist.js
 * http://quote.eastmoney.com/center/js/gridlist3.js
 * http://quote.eastmoney.com/newstatic/js/old/quote.js
 * http://quote.eastmoney.com/newstatic/js/cyb/quote_cyb.js
 * http://quote.eastmoney.com/newstatic/js/cyb.js
 * http://quote.eastmoney.com/newstatic/build/bond.js
 * 
 * @author Birdy
 *
 */
public enum EastmoneyField {

    f2("最新价格"),

    f3("涨跌幅"),

    f4("涨跌额"),

    f5("成交量"),

    f6("成交额"),

    f7("振幅"),

    f8("换手率"),

    f9("市盈率"),

    f10("量比"),

    f11("涨跌幅(5m)"),

    f12("代码"),

    f13("交易所"),

    f14("名称"),

    f15("最高价格"),

    f16("最低价格"),

    f17("开盘价格"),

    f18("收盘价格"),

    f20("总市值"),

    f21("流通市值"),

    f22("涨跌速"),

    f23("市净率"),

    f24("涨跌幅(60d)"),

    f25("涨跌幅(ytd)"),

    f26("发行日期"),

    f28("昨结"),

    f30("现量"),

    f31("现汇买入价格"),

    f32("现汇卖出价格"),

    f33("委比"),

    f34("买盘(外盘)"),

    f35("卖盘(内盘)"),

    f45("净利润"),

    f62("主力净流入"),

    f104("上涨家数"),

    f105("下跌家数"),

    f108("持仓量"),

    f115("市盈率"),

    f124("更新时间"),

    f128("领涨股"),

    f136("涨跌幅"),

    f142("现钞买入价格"),

    f143("现钞卖出价格"),

    f161("行权价格"),

    f162("剩余日"),

    f163("日增"),

    f184("主力净占比"),

    f186("最新价格"),

    f187("涨跌幅"),

    f188("溢价(A/H)%"),

    f189("比价(A/H)"),

    f191("代码"),

    f193("名称"),

    f196("最新价格"),

    f197("涨跌幅"),

    f199("比价(A/B)"),

    f201("B股代码"),

    f203("名称"),

    f204("主力最大股"),

    f207("领跌股"),

    f211("买量"),

    f212("卖量"),

    f213("N股代码"),

    f217("涨跌额"),

    f218("涨跌幅"),

    f219("折合美元"),

    f220("折合港元"),

    f222("涨跌幅"),

    f223("排名"),

    f225("排名"),

    f226("排行变化"),

    f227("纯债价值"),

    f229("最新价格"),

    f230("涨跌幅"),

    f232("正股代码"),

    f234("正股名称"),

    f235("转股价格"),

    f236("转股价值"),

    f237("转股溢价率"),

    f238("纯债溢价率"),

    f239("回售价格"),

    f240("强赎价格"),

    f241("到期价格"),

    f242("转股日期"),

    f243("申购日期"),

    f249("波动率"),

    f250("折溢价率"),

    f339("最新"),

    f340("名称"),

    f341("涨跌幅"),

    f342("涨跌额"),

    f344("成交量"),

    f345("持仓量"),

    f346("波动率"),

    f347("折溢价率");

    private final String name;

    private EastmoneyField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
