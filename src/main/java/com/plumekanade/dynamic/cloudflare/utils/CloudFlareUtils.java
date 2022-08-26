package com.plumekanade.dynamic.cloudflare.utils;

import com.plumekanade.dynamic.cloudflare.vo.cloudflare.DnsRecordItem;
import com.plumekanade.dynamic.cloudflare.vo.cloudflare.DnsRecordResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * CloudFlare接口工具类
 *
 * @author kanade
 * @date 2022-08-25
 */
@Slf4j
public class CloudFlareUtils {
  public static String ZONE_ID;
  public static String AUTH_EMAIL;
  public static String AUTH_KEY;
  public static final String CLOUDFLARE_DOMAIN = "https://api.cloudflare.com/client/v4";

  /**
   * 获取用户的dns记录分页列表 以后有需要再加分页和页码吧
   */
  public static DnsRecordResult getDnsRecordPage() {
    try {
      String url = CLOUDFLARE_DOMAIN + "/zones/" + ZONE_ID + "/dns_records?page=1&per_page=100&order=type";
      String getData = ServletUtils.get(url, getHeaders());
      return MapperUtils.deserialize(getData, DnsRecordResult.class);
    } catch (Exception e) {
      log.error("【Cloudflare】获取DNS记录分页列表失败, 异常堆栈: ", e);
    }
    return null;
  }

  /**
   * 修改单个dns记录
   */
  public static DnsRecordItem updateDnsRecord(DnsRecordItem dnsRecordItem) {
    String id = dnsRecordItem.getId();
    dnsRecordItem.setId(null);
    String url = CLOUDFLARE_DOMAIN + "/zones/" + ZONE_ID + "/dns_records/" + id;
    try {
      String putData = ServletUtils.put(url, MapperUtils.serialize(dnsRecordItem), getHeaders());
      return MapperUtils.deserialize(putData, DnsRecordItem.class);
    } catch (Exception e) {
      log.error("【Cloudflare】修改DNS记录失败, 异常堆栈: ", e);
    }
    return null;
  }

  public static Header[] getHeaders() {
    Header[] headers = new Header[2];
    headers[0] = new BasicHeader("X-Auth-Email", AUTH_EMAIL);
    headers[1] = new BasicHeader("X-Auth-Key", AUTH_KEY);
    return headers;
  }
}

