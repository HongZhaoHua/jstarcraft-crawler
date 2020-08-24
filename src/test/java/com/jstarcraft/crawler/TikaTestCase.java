package com.jstarcraft.crawler;

import java.io.File;
import java.nio.file.Path;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.junit.Assert;
import org.junit.Test;

public class TikaTestCase {

    @Test
    public void test() throws Exception {
        Tika tika = new Tika();
        TikaConfig config = TikaConfig.getDefaultConfig();
        Detector[] detectors = new Detector[] { config.getDetector(), config.getMimeRepository() };

        File directory = new File(TikaTestCase.class.getClassLoader().getResource("tika").toURI());
        for (File file : directory.listFiles()) {
            Path path = file.toPath();
            for (Detector detector : detectors) {
                Metadata metadata = new Metadata();
                TikaInputStream stream = TikaInputStream.get(path, metadata);
                Assert.assertEquals(tika.detect(path), detector.detect(stream, metadata).toString());
            }
        }
    }

}
