package com.plumekanade.dynamic.cloudflare.task;

import com.plumekanade.dynamic.cloudflare.config.CloudflareConfig;
import com.plumekanade.dynamic.cloudflare.config.TencentConfig;
import com.plumekanade.dynamic.cloudflare.utils.CloudflareUtils;
import com.plumekanade.dynamic.cloudflare.utils.MapperUtils;
import com.plumekanade.dynamic.cloudflare.utils.TencentUtils;
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
public class DnsTask implements Runnable {
  public static final String KEY = "Cloudflare";
  public static final String CRON = "0 */15 * * * ?";
  private static final DnsRecordItem V6_ITEM = new DnsRecordItem();
  public static String IP_NAME;
  private final TencentConfig tencentConfig;
  private final CloudflareConfig cloudflareConfig;

  public DnsTask(TencentConfig tencentConfig, CloudflareConfig cloudflareConfig) {
    this.tencentConfig = tencentConfig;
    this.cloudflareConfig = cloudflareConfig;
  }

  @Override
  public void run() {
    if (V6_ITEM.getId() == null) {
      DnsRecordResult dnsRecordPage = CloudflareUtils.getDnsRecordPage(cloudflareConfig);
      if (dnsRecordPage == null) {
        return;
      }
      for (DnsRecordItem dnsRecordItem : dnsRecordPage.getResult()) {
        if (dnsRecordItem.getName().contains(cloudflareConfig.getName())) {
          V6_ITEM.setId(dnsRecordItem.getId());
          V6_ITEM.setTtl(dnsRecordItem.getTtl());
          V6_ITEM.setName(dnsRecordItem.getName());
          V6_ITEM.setType(dnsRecordItem.getType());
          V6_ITEM.setContent(dnsRecordItem.getContent());
          break;
        }
      }
    }
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      boolean outFlag = false;
      while (networkInterfaces.hasMoreElements()) {
        Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          InetAddress inetAddress = inetAddresses.nextElement();
          if (inetAddress instanceof Inet6Address && inetAddress.getHostName().equals(IP_NAME)) {
            if (V6_ITEM.getContent().equals(inetAddress.getHostAddress())) {
              log.info("【DnsTask】{} 与DNS记录的IP一致, 无需修改...", V6_ITEM.getContent());
            } else {
              V6_ITEM.setContent(inetAddress.getHostAddress());
              V6_ITEM.setProxied(true);
              DnsRecordItem dnsRecordItem = CloudflareUtils.updateDnsRecord(cloudflareConfig, V6_ITEM);
              log.info("【DnsTask】Cloudflare 请求返回内容: {}", MapperUtils.serialize(dnsRecordItem));
              log.info("【DnsTask】DNSPod 请求返回内容: {}", TencentUtils.batchModifyRecord(tencentConfig, V6_ITEM.getContent()));
              log.info("【DnsTask】修改DNS记录IP为 {}", inetAddress.getHostAddress());
            }
            outFlag = true;
            break;
          }
        }
        if (outFlag) {
          break;
        }
      }
    } catch (Exception e) {
      log.error("【DnsTask】获取ip地址失败, 异常堆栈信息: ", e);
    }
  }
}
