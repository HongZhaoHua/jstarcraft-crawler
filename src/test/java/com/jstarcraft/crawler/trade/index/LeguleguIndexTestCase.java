package com.jstarcraft.crawler.trade.index;

import org.junit.Test;

import com.jstarcraft.core.common.security.SecurityUtility;
import com.jstarcraft.core.utility.StringUtility;

public class LeguleguIndexTestCase {

    @Test
    public void testToken() {
        // TODO 乐咕乐股的token根据日期生成
        byte[] bytes = "2022-04-29".getBytes(StringUtility.CHARSET);
        String md5 = SecurityUtility.byte2Hex(SecurityUtility.signatureMd5(bytes));
        System.out.println(md5);
    }

}
