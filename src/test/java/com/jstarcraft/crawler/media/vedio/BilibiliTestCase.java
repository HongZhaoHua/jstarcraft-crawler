package com.jstarcraft.crawler.media.vedio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jaxen.Navigator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.selection.css.JsoupCssSelector;
import com.jstarcraft.core.common.selection.xpath.JaxenXpathSelector;
import com.jstarcraft.core.common.selection.xpath.html.HtmlElementNode;
import com.jstarcraft.core.common.selection.xpath.html.HtmlNavigator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 哔哩哔哩单元测试
 * 
 * https://www.freesion.com/article/66861140089/
 * 
 * @author Birdy
 *
 */
public class BilibiliTestCase {

    protected static final Logger logger = LoggerFactory.getLogger(BilibiliTestCase.class);

    private static final JsoupCssSelector pageSelector = new JsoupCssSelector("span.cur-page");

    private static final Navigator navigator = HtmlNavigator.getInstance();

    private static final JaxenXpathSelector<HtmlElementNode> scriptSelector = new JaxenXpathSelector<>("//script[not(@*)]", navigator);

    private static void mergeFile(File videoFile, File audioFile, File saveFile) {
        String command = StringUtility.format("ffmpeg -n -loglevel quiet -i \"{}\" -i \"{}\" -c copy \"{}\"", videoFile.getAbsolutePath(), audioFile.getAbsolutePath(), saveFile.getAbsoluteFile());
        System.out.println(command);// 打印一下命令
        try {
            FileUtils.deleteQuietly(saveFile);
            Runtime runtime = Runtime.getRuntime();// 获取cmd窗口
            Process process = runtime.exec(command);// 执行合并命令
            while (process.isAlive()) {
                System.out.println("文件合并中");
                Thread.sleep(1000L);
            }
            System.out.println("文件合并完");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void downloadFile(String refererUrl, String fromUrl, File toFile) {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.REFERER, refererUrl);
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        URI uri = URI.create(fromUrl);
        ResponseEntity<Resource> response = template.exchange(uri, HttpMethod.GET, request, Resource.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] buffer = new byte[1024];
            int readBytesLength;
            try (InputStream input = response.getBody().getInputStream()) {
                FileUtils.deleteQuietly(toFile);
                FileUtils.createParentDirectories(toFile);
                try (FileOutputStream output = new FileOutputStream(toFile);) {
                    while ((readBytesLength = input.read(buffer)) != -1) {
                        output.write(buffer, 0, readBytesLength);
                        System.out.println("文件写入中");
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                    System.out.println("写入中断");
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                System.out.println("创建文件夹失败");
            }
            System.out.println("文件写入完成");
        }
    }

    private static void downloadMedia(RestTemplate template, String url, String title, String part) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        headers.add(HttpHeaders.ACCEPT, "text/html");
        headers.add(HttpHeaders.REFERER, "www.bilibili.com");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        Document document = Jsoup.parse(data);
        HtmlElementNode root = new HtmlElementNode(document);
        List<HtmlElementNode> elements = scriptSelector.selectMultiple(root);
        Element element = (Element) elements.get(0).getValue();
        String script = element.html();
        script = script.replaceAll("window.__playinfo__=([\\s\\S]*)", "$1");
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(script));
        }
        ONode node = ONode.load(script);
        String videoUrl = node.get("data").get("dash").get("video").ary().get(0).get("baseUrl").getString();
        String videoPath = StringUtility.format("bilibili/{}/{}_video.mp4", title, part);
        File videoFile = new File(videoPath);
        // 下载视频文件
        downloadFile(url, videoUrl, videoFile);

        String audioUrl = node.get("data").get("dash").get("audio").ary().get(0).get("baseUrl").getString();
        String audioPath = StringUtility.format("bilibili/{}/{}_audio.mp3", title, part);
        File audioFile = new File(audioPath);
        // 下载音频文件
        downloadFile(url, audioUrl, audioFile);

        String savePath = StringUtility.format("bilibili/{}/{}.mp4", title, part);
        File saveFile = new File(savePath);
        // 合并媒体文件
        mergeFile(videoFile, audioFile, saveFile);
    }

    @Test
    public void testDownload() {
        // RestTemplate支持Gzip
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        RestTemplate template = new RestTemplate(factory);
        String pageUrl = "https://www.bilibili.com/video/BV1Zb411b7QW/?p=1";
        downloadMedia(template, pageUrl, "收纳", "1");
    }

    @Test
    public void testDownloads() {
        // RestTemplate支持Gzip
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        RestTemplate template = new RestTemplate(factory);
        String bvid = "BV1Zb411b7QW";
        String bilibiliUrl = "https://www.bilibili.com/video/{}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        headers.add(HttpHeaders.ACCEPT, "text/html");
        headers.add(HttpHeaders.REFERER, "www.bilibili.com");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(bilibiliUrl, bvid);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
//        if (logger.isDebugEnabled()) {
//            logger.debug(XmlUtility.prettyHtml(data));
//        }
        Document document = Jsoup.parse(data);
        HtmlElementNode root = new HtmlElementNode(document);
        String size = pageSelector.selectSingle(document.root()).text();
        List<HtmlElementNode> elements = scriptSelector.selectMultiple(root);
        Element element = (Element) elements.get(1).getValue();
        String script = element.html();
        script = script.replaceAll("window.__INITIAL_STATE__=([\\s\\S]*);\\(function[\\s\\S]*\\(\\)\\);", "$1");
//        if (logger.isDebugEnabled()) {
//            logger.debug(JsonUtility.prettyJson(script));
//        }
        ONode node = ONode.load(script);
        String title = node.get("videoData").get("title").getString();
        int index = 0;
        String pageUrl = "https://www.bilibili.com/video/{}/?p={}";
        for (ONode page : node.get("videoData").get("pages").ary()) {
            String part = page.get("part").getString();
            int duration = page.get("duration").getInt();
            downloadMedia(template, StringUtility.format(pageUrl, bvid, index + 1), title, part);
            index++;
        }
    }

}
