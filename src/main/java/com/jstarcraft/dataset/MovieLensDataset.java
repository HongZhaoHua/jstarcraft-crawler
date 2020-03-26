package com.jstarcraft.dataset;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.DataSpace;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.core.utility.StringUtility;

/**
 * MovieLens数据集
 * 
 * <pre>
 * TODO 暂时依赖于Spring的装配机制,考虑自定义一套轻量级的装配机制.
 * </pre>
 * 
 * @author Birdy
 *
 */
public class MovieLensDataset {

    private final static Logger logger = LoggerFactory.getLogger(MovieLensDataset.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.US);

    @Bean
    URL movieLensURL() {
        URL url = MovieLensDataset.class.getClassLoader().getResource("ml-100k.zip");
        return url;
    }

    @Bean
    DataSpace movieLensDataSpace() {
        // 定义数据空间
        Map<String, Class<?>> qualityDifinitions = new HashMap<>();
        Map<String, Class<?>> quantityDifinitions = new HashMap<>();
        // 用户数据属性
        qualityDifinitions.put("user", int.class);
        quantityDifinitions.put("age", int.class);
        qualityDifinitions.put("gender", String.class);
        qualityDifinitions.put("occupation", String.class);
        qualityDifinitions.put("zip", String.class);

        // 物品数据属性
        qualityDifinitions.put("movie", int.class);
        qualityDifinitions.put("title", String.class);
        quantityDifinitions.put("release", int.class);
        qualityDifinitions.put("genre", int.class);
        quantityDifinitions.put("factor", float.class);

        // 得分数据属性
        quantityDifinitions.put("rating", float.class);
        quantityDifinitions.put("timestamp", int.class);

        DataSpace dataSpace = new DataSpace(qualityDifinitions, quantityDifinitions);
        return dataSpace;
    }

    @Bean
    DataModule movieLensUserDataModule(URL movieLensURL, DataSpace movieLensDataSpace) throws Exception {
        TreeMap<Integer, String> configuration = new TreeMap<>();
        configuration.put(1, "user");
        configuration.put(2, "age");
        configuration.put(3, "gender");
        configuration.put(4, "occupation");
        configuration.put(5, "zip");
        DataModule dataModule = movieLensDataSpace.makeDenseModule("movieLensUser", configuration, 1000000);
        String path = movieLensURL.getPath();
        try (ZipFile zipFile = new ZipFile(path)) {
            ZipEntry zipTerm = zipFile.getEntry("ml-100k/u.user");
            try (InputStream termStream = zipFile.getInputStream(zipTerm); InputStreamReader streamReader = new InputStreamReader(termStream); BufferedReader bufferReader = new BufferedReader(streamReader)) {
                try (CSVParser parser = new CSVParser(bufferReader, CSVFormat.DEFAULT.withDelimiter('|'))) {
                    Iterator<CSVRecord> iterator = parser.iterator();
                    while (iterator.hasNext()) {
                        CSVRecord datas = iterator.next();
                        try {
                            // 用户标识
                            int user = Integer.parseInt(datas.get(0));
                            // 用户年龄
                            int age = Integer.parseInt(datas.get(1));
                            // 用户性别
                            String gender = datas.get(2);
                            // 用户职业
                            String occupation = datas.get(3);
                            // 用户邮编
                            String zip = datas.get(4);
                        } catch (Exception exception) {
                            logger.error(StringUtility.format("分析用户[{}]错误", datas.get(0)), exception);
                        }
                    }
                }
            }
        }
        return dataModule;
    }

    @Bean
    QualityAttribute movieLensGenreDataAttribute(URL movieLensURL, DataSpace movieLensDataSpace) throws Exception {
        QualityAttribute dataAttribute = movieLensDataSpace.getQualityAttribute("genre");
        String path = movieLensURL.getPath();
        try (ZipFile zipFile = new ZipFile(path)) {
            ZipEntry zipTerm = zipFile.getEntry("ml-100k/u.genre");
            try (InputStream termStream = zipFile.getInputStream(zipTerm); InputStreamReader streamReader = new InputStreamReader(termStream); BufferedReader bufferReader = new BufferedReader(streamReader)) {
                try (CSVParser parser = new CSVParser(bufferReader, CSVFormat.DEFAULT.withDelimiter('|'))) {
                    Iterator<CSVRecord> iterator = parser.iterator();
                    while (iterator.hasNext()) {
                        CSVRecord datas = iterator.next();
                        try {

                        } catch (Exception exception) {
                            logger.error(StringUtility.format("分析体裁[{}]错误", datas.get(0)), exception);
                        }
                    }
                }
            }
        }
        return dataAttribute;
    }

    @Bean
    DataModule movieLensItemDataModule(URL movieLensURL, DataSpace movieLensDataSpace, QualityAttribute movieLensGenreDataAttribute) throws Exception {
        TreeMap<Integer, String> configuration = new TreeMap<>();
        configuration.put(1, "movie");
        configuration.put(2, "title");
        configuration.put(3, "release");
        configuration.put(3 + movieLensGenreDataAttribute.getSize(), "factor");
        DataModule dataModule = movieLensDataSpace.makeSparseModule("movieLensItem", configuration, 1000000);
        String path = movieLensURL.getPath();
        try (ZipFile zipFile = new ZipFile(path)) {
            ZipEntry zipTerm = zipFile.getEntry("ml-100k/u.item");
            try (InputStream termStream = zipFile.getInputStream(zipTerm); InputStreamReader streamReader = new InputStreamReader(termStream); BufferedReader bufferReader = new BufferedReader(streamReader)) {
                try (CSVParser parser = new CSVParser(bufferReader, CSVFormat.DEFAULT.withDelimiter('|'))) {
                    Iterator<CSVRecord> iterator = parser.iterator();
                    while (iterator.hasNext()) {
                        CSVRecord datas = iterator.next();
                        try {

                        } catch (Exception exception) {
                            logger.error(StringUtility.format("分析物品[{}]错误", datas.get(0)), exception);
                        }
                    }
                }
            }
        }
        return dataModule;
    }

    @Bean
    DataModule movieLensScoreDataModule(URL movieLensURL, DataSpace movieLensDataSpace, DataModule movieLensUserDataModule, DataModule movieLensItemDataModule) throws Exception {
        TreeMap<Integer, String> configuration = new TreeMap<>();
        configuration.put(1, "user");
        configuration.put(2, "movie");
        configuration.put(3, "rating");
        configuration.put(4, "timestamp");
        DataModule dataModule = movieLensDataSpace.makeDenseModule("movieLensScore", configuration, 1000000);
        String path = movieLensURL.getPath();
        try (ZipFile zipFile = new ZipFile(path)) {
            ZipEntry zipTerm = zipFile.getEntry("ml-100k/u.data");
            try (InputStream termStream = zipFile.getInputStream(zipTerm); InputStreamReader streamReader = new InputStreamReader(termStream); BufferedReader bufferReader = new BufferedReader(streamReader)) {
                try (CSVParser parser = new CSVParser(bufferReader, CSVFormat.DEFAULT.withDelimiter('\t'))) {
                    Iterator<CSVRecord> iterator = parser.iterator();
                    while (iterator.hasNext()) {
                        CSVRecord datas = iterator.next();
                        try {

                        } catch (Exception exception) {
                            logger.error(StringUtility.format("分析评价[{}]错误", datas.get(0)), exception);
                        }
                    }
                }
            }
        }
        return dataModule;
    }

}
