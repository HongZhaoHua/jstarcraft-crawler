package com.jstarcraft.dataset;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Test;

import com.jstarcraft.core.utility.StringUtility;

public class ZipDatasetTestCase {

    @Test
    public void test() throws Exception {
        URL url = ZipDatasetTestCase.class.getClassLoader().getResource("ml-100k.zip");
        String path = url.getPath();
        try (ZipFile zip = new ZipFile(path)) {
            ZipEntry term = zip.getEntry("ml-100k/u.data");
            try (InputStream termStream = zip.getInputStream(term); InputStreamReader streamReader = new InputStreamReader(termStream, StringUtility.CHARSET); BufferedReader bufferReader = new BufferedReader(streamReader)) {
                String line;
                while ((line = bufferReader.readLine()) != null) {
                    System.out.println(line.toString());
                }
            }
        }
    }

}
