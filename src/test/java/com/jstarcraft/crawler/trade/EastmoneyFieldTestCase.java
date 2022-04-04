package com.jstarcraft.crawler.trade;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.trade.security.bond.EastmoneyBondTestCase;

/**
 * 东方财富字段
 * 
 * @author Birdy
 *
 */
public class EastmoneyFieldTestCase {

    /**
     * 获取字段定义
     * 
     * @throws Exception
     */
    @Test
    public void testGetEastmoneyField() throws Exception {
        File file = new File(EastmoneyBondTestCase.class.getClassLoader().getResource("gridlist.txt").toURI());
        String text = FileUtils.readFileToString(file, StringUtility.CHARSET);
        Pattern pattern = Pattern.compile("\\s+title:\\s*\"(\\S+)\",\\s+key:\\s*\"(f\\S+)\",", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        Map<String, String> keyValues = new TreeMap<>((left, right) -> {
            left = left.replace("f", "");
            right = right.replace("f", "");
            return Integer.valueOf(left).compareTo(Integer.valueOf(right));
        });
        while (matcher.find()) {
            String title = matcher.group(1);
            String key = matcher.group(2);
            String value = keyValues.getOrDefault(key, title);
            if (!value.equals(title)) {
                String message = StringUtility.format("key:{},value:{},title:{}", key, value, title);
                System.out.println(message);
            }
            keyValues.put(key, value);
        }
        for (Entry<String, String> keyValue : keyValues.entrySet()) {
            String key = keyValue.getKey();
            String value = keyValue.getValue();
            String message = StringUtility.format("{}(\"{}\"),", key, value);
            System.out.println(message);
            System.out.println();
        }
    }

}
