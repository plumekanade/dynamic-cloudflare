package com.plumekanade.dynamic.cloudflare.task;

import com.plumekanade.dynamic.cloudflare.utils.CloudFlareUtils;
import com.plumekanade.dynamic.cloudflare.utils.MapperUtils;
import com.plumekanade.dynamic.cloudflare.vo.cloudflare.DnsRecordItem;
import com.plumekanade.dynamic.cloudflare.vo.cloudflare.DnsRecordResult;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Cloudflare定时任务
 *
 * @author kanade
 * @date 2022-08-26
 */
@Slf4j
public class CloudFlareTask implements Runnable {
  public static final String KEY = "Cloudflare";
  public static final String CRON = "0 0,30 * * * ?";
  private static final DnsRecordItem V6_ITEM = new DnsRecordItem();
  public static String NAME;
  public static String IP_NAME;

  @Override
  public void run() {
    if (V6_ITEM.getId() == null) {
      DnsRecordResult dnsRecordPage = CloudFlareUtils.getDnsRecordPage();
      if (dnsRecordPage == null) {
        return;
      }
      for (DnsRecordItem dnsRecordItem : dnsRecordPage.getResult()) {
        if (dnsRecordItem.getName().contains(NAME)) {
          V6_ITEM.setId(dnsRecordItem.getId());
          V6_ITEM.setName(dnsRecordItem.getName());
          V6_ITEM.setContent(dnsRecordItem.getContent());
          V6_ITEM.setTtl(dnsRecordItem.getTtl());
          break;
        }
      }
    }
    log.info("【CloudflareTask】DNS记录: {}", MapperUtils.serialize(V6_ITEM));
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          InetAddress inetAddress = inetAddresses.nextElement();
          if (inetAddress instanceof Inet6Address && inetAddress.getHostName().equals(IP_NAME)) {
            V6_ITEM.setContent(inetAddress.getHostAddress());
            DnsRecordItem dnsRecordItem = CloudFlareUtils.updateDnsRecord(V6_ITEM);
            log.info("【CloudflareTask】修改DNS记录 {} 的IP为 {}, 请求结果: {}", V6_ITEM.getName(), inetAddress.getHostAddress(), dnsRecordItem != null);
            break;
          }
        }
      }
    } catch (Exception e) {
      log.error("【CloudflareTask】获取ip地址失败, 异常堆栈信息: ", e);
    }
  }
}
