package com.jstarcraft.crawler.mind.edrawsoft;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.noear.snack.ONode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.crawler.book.weread.WereadBook;

/**
 * 亿图脑图
 * 
 * https://mm.edrawsoft.cn/store
 * 
 * @author Birdy
 *
 */
public class EdrawsoftMind {

    protected static final Logger logger = LoggerFactory.getLogger(WereadBook.class);

    /** 脑图路径模板 */
    // https://masterapi.edrawsoft.cn/api/user/0/work/info?work_id={id}
    private static final String mindUrl = "https://masterapi.edrawsoft.cn/api/user/0/work/info?work_id={}";

    /** 内容路径模板 */
    // https://edrawcloudpubliccn.oss-cn-shenzhen.aliyuncs.com/{path}outline.json
    private static final String topicUrl = "https://edrawcloudpubliccn.oss-cn-shenzhen.aliyuncs.com/{}outline.json";

    private final RestTemplate template;

    /** 标识 */
    private String id;

    /** 标题 */
    private String title;

    /** 路径 */
    private String path;

    /** 标签 */
    private List<String> tags;

    public EdrawsoftMind(RestTemplate template, String id) {
        this.template = template;
        this.id = id;
    }

    @Deprecated
    public void update(Instant instant) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(mindUrl, id);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        ONode root = ONode.load(data);
        ONode mind = root.get("data");
        // 获取标识
        this.id = mind.get("id").getString();
        // 获取标题
        this.title = mind.get("title").getString();
        // 获取路径
        this.path = mind.get("url").getString();
        // 获取标签
        this.tags = mind.get("tags").ary().stream().map((node) -> {
            return node.getString();
        }).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public List<String> getTags() {
        return tags;
    }

    public EdrawsoftTopic getContent() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "PostmanRuntime/7.28.0");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        String url = StringUtility.format(topicUrl, path);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
        String data = response.getBody();
        if (logger.isDebugEnabled()) {
            logger.debug(JsonUtility.prettyJson(data));
        }
        EdrawsoftTopic topic = JsonUtility.string2Object(data, EdrawsoftTopic.class);
        return topic;
    }

}
