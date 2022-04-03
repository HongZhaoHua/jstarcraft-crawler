package com.jstarcraft.crawler.trade.security.bond;

/**
 * 东方财富转债
 * 
 * 转债列表: https://data.eastmoney.com/kzz/default.html
 * https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns=PUBLIC_START_DATE&sortTypes=-1&pageSize=50&pageNumber=1&reportName=RPT_BOND_CB_LIST&columns=ALL&quoteColumns=f2~01~CONVERT_STOCK_CODE~CONVERT_STOCK_PRICE%2Cf235~10~SECURITY_CODE~TRANSFER_PRICE%2Cf236~10~SECURITY_CODE~TRANSFER_VALUE%2Cf2~10~SECURITY_CODE~CURRENT_BOND_PRICE%2Cf237~10~SECURITY_CODE~TRANSFER_PREMIUM_RATIO%2Cf239~10~SECURITY_CODE~RESALE_TRIG_PRICE%2Cf240~10~SECURITY_CODE~REDEEM_TRIG_PRICE%2Cf23~01~CONVERT_STOCK_CODE~PBV_RATIO&source=WEB&client=WEB
 * 
 * 转债详情: https://data.eastmoney.com/kzz/detail/{}.html
 * https://datacenter-web.eastmoney.com/api/data/get?sty=ALL&st=date&sr=1&source=WEB&type=RPTA_WEB_KZZ_LS&filter=(zcode%3D%22{}%22)&p=1&ps=1000
 * 
 * @author Birdy
 *
 *
 */
public class EastmoneyConvertibleBond {

}
