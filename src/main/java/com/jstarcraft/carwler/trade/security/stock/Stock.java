package com.jstarcraft.carwler.trade.security.stock;

import com.jstarcraft.carwler.trade.security.Security;

public interface Stock extends Security {

    /**
     * 获取代号
     * 
     * @return
     */
    public String getSymbol();

    /**
     * 获取CUSIP
     * 
     * @return
     */
    public String getCusip();

    /**
     * 获取SEDOL
     * 
     * @return
     */
    public String getSedol();

}
