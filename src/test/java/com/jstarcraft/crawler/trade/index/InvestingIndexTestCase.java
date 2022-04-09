package com.jstarcraft.crawler.trade.index;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jaxen.Navigator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.common.selection.xpath.JaxenXpathSelector;
import com.jstarcraft.core.common.selection.xpath.html.HtmlElementNode;
import com.jstarcraft.core.common.selection.xpath.html.HtmlNavigator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 英为财情指数单元测试
 * 
 * <pre>
 * https://api.investing.com/api/search/v2/search?q={isin}
 * https://cn.investing.com
 * https://cn.investing.com/stock-screener/
 * https://cn.investing.com/indices/{}
 * 成分股:https://cn.investing.com/indices/{}-components
 * 历史:https://cn.investing.com/indices/{}-historical-data
 * </pre>
 * 
 * @author Birdy
 *
 */
public class InvestingIndexTestCase {

}
