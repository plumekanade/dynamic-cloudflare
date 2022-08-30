package com.plumekanade.dynamic.cloudflare.config;

import com.plumekanade.dynamic.cloudflare.task.DnsTask;
import com.plumekanade.dynamic.cloudflare.task.DynamicTask;
import com.plumekanade.dynamic.cloudflare.utils.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author kanade
 * @date 2022-08-26
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class AppInit implements CommandLineRunner {

  private final DynamicTask dynamicTask;
  private final TencentConfig tencentConfig;
  private final CloudflareConfig cloudflareConfig;

  @Override
  public void run(String... args) throws Exception {
    // 网络驱动的IP名称
    DnsTask.IP_NAME = cloudflareConfig.getIpName();

    // dns定时任务
    dynamicTask.startTask(DnsTask.KEY, new DnsTask(tencentConfig, cloudflareConfig), DnsTask.CRON);
    log.info(MapperUtils.serialize(tencentConfig));
  }
}
