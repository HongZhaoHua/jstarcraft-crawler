package com.jstarcraft.crawler.book;

import java.util.List;

import com.jstarcraft.core.utility.KeyValue;

/**
 * 微信档案
 * 
 * @author Birdy
 *
 */
public class WereadArchive {

    /** 标识 */
    private String id;
    
    /** 名称 */
    private String name;
    
    /** 笔记 */
    private List<KeyValue<String, WereadNote>> nodes;

    /**
     * 获取笔记
     * 
     * @return
     */
    public List<KeyValue<String, WereadNote>> getNodes() {
        return null;
    }

}
