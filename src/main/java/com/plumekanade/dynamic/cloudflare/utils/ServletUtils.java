package com.plumekanade.dynamic.cloudflare.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 请求工具类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-17 16:57:41
 */
@Slf4j
public class ServletUtils {

  private static final RequestConfig CONFIG = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
  private static final RequestConfig UN_REDIRECT_CONFIG = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setRedirectsEnabled(false).build();
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TYPE_JSON = "application/json";
  public static final Integer SUCCESS = 200;

  /**
   * 通用get
   */
  public static HttpEntity get(String url) throws Exception {
    // 创建Httpclient对象
    try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(CONFIG).build()) {
      // 创建http GET 并 发起请求
      HttpGet httpGet = new HttpGet(url);
      httpGet.setConfig(CONFIG);
      CloseableHttpResponse response = httpclient.execute(httpGet);
      // 判断返回状态是否为200
      if (SUCCESS.equals(response.getStatusLine().getStatusCode())) {
        return response.getEntity();
      }
    }
    return null;
  }

  /**
   * 通用get
   */
  public static HttpEntity getWithHeader(String url, Header header) throws Exception {
    // 创建Httpclient对象
    try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(CONFIG).build()) {
      // 创建http GET 并 发起请求
      HttpGet httpGet = new HttpGet(url);
      // 设置请求头
      httpGet.setHeader(header);
      httpGet.setConfig(CONFIG);
      CloseableHttpResponse response = httpclient.execute(httpGet);
      // 判断返回状态是否为200
      if (SUCCESS.equals(response.getStatusLine().getStatusCode())) {
        return response.getEntity();
      }
    }
    return null;
  }

  /**
   * 需要加入头部的GET请求
   */
  public static String get(String url, Header header) throws Exception {
    // 创建Httpclient对象
    try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(CONFIG).build()) {
      // 创建http GET 并 发起请求
      HttpGet httpGet = new HttpGet(url);
      // 设置请求头
      httpGet.setHeader(header);
      httpGet.setConfig(CONFIG);
      CloseableHttpResponse response = httpclient.execute(httpGet);
      // 判断返回状态是否为200
      if (SUCCESS.equals(response.getStatusLine().getStatusCode())) {
        return EntityUtils.toString(response.getEntity());
      }
    }
    return null;
  }

  /**
   * 需要加入头部的GET请求
   */
  public static String get(String url, Header[] header) throws Exception {
    // 创建Httpclient对象
    try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(CONFIG).build()) {
      // 创建http GET 并 发起请求
      HttpGet httpGet = new HttpGet(url);
      // 设置请求头
      httpGet.setHeaders(header);
      httpGet.setConfig(CONFIG);
      CloseableHttpResponse response = httpclient.execute(httpGet);
      // 判断返回状态是否为200
      if (SUCCESS.equals(response.getStatusLine().getStatusCode())) {
        return EntityUtils.toString(response.getEntity());
      }
    }
    return null;
  }

  /**
   * 华米刷步数的POST请求
   *
   * @date 2022-03-23
   */
  public static CloseableHttpResponse stepsPost(String url, List<NameValuePair> pairs, Header[] headers) throws Exception {
    try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(UN_REDIRECT_CONFIG).build()) {
      // 创建http POST 并 发起请求
      HttpPost httpPost = new HttpPost(url);
      // 设置请求头
      if (headers != null) {
        httpPost.setHeaders(headers);
      }
      // 超时设置
      httpPost.setConfig(CONFIG);
      // 设置参数
      if (pairs != null && pairs.size() > 0) {
        httpPost.setEntity(new UrlEncodedFormEntity(pairs));
      }
      return httpClient.execute(httpPost);
    }
  }

  /**
   * 需要加入头部的POST请求
   */
  public static String post(String url, String params, Header[] headers) throws Exception {
    try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(CONFIG).build()) {
      CloseableHttpResponse response = httpClient.execute(handlePreRequest(new HttpPost(url), headers, params));
      // 判断返回状态是否为200
      if (SUCCESS.equals(response.getStatusLine().getStatusCode())) {
        return EntityUtils.toString(response.getEntity());
      }
    }
    return null;
  }

  /**
   * put请求
   */
  public static String put(String url, String params, Header[] headers) throws Exception {
    try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(CONFIG).build()) {
      // 创建http POST 并 发起请求
      return EntityUtils.toString(httpClient.execute(handlePreRequest(new HttpPut(url), headers, params)).getEntity());
    }
  }

  /**
   * 请求前处理
   */
  public static <T extends HttpEntityEnclosingRequestBase> T handlePreRequest(T t, Header[] headers, String params) {
    // 设置请求头
    if (headers != null) {
      t.setHeaders(headers);
    }
    // 超时设置
    t.setConfig(CONFIG);
    // 设置参数
    if (StringUtils.hasText(params)) {
      StringEntity entity = new StringEntity(params, StandardCharsets.UTF_8);
      entity.setContentEncoding(StandardCharsets.UTF_8.toString());
      // 发送Json格式的数据请求
      t.setHeader(CONTENT_TYPE, CONTENT_TYPE_JSON);
      t.setEntity(entity);
    }
    return t;
  }

  /**
   * 普通get
   */
  public static synchronized String normalGet(String url) {
    try {
      return EntityUtils.toString(Objects.requireNonNull(get(url)));
    } catch (Exception e) {
      log.error("【Get】普通Get请求异常: ", e);
    }
    return "";
  }

  /**
   * 需要unicode转中文的get
   */
  public static synchronized Map<String, String> convertGet(String url) {
    try {
      return MapperUtils.deserialize(Objects.requireNonNull(get(url)).getContent(), new TypeReference<>() {
      });
    } catch (Exception e) {
      log.error("【Get】需要 ASCII 转中文的Get请求异常: ", e);
    }
    return new HashMap<>(0);
  }

}
