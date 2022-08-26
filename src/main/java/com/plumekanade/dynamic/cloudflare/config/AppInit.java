package com.plumekanade.dynamic.cloudflare.config;

import com.plumekanade.dynamic.cloudflare.task.CloudFlareTask;
import com.plumekanade.dynamic.cloudflare.task.DynamicTask;
import com.plumekanade.dynamic.cloudflare.utils.CloudFlareUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author kanade
 * @date 2022-08-26
 */
@Configuration
@AllArgsConstructor
public class AppInit implements CommandLineRunner {

  private final DynamicTask dynamicTask;
  private final CloudflareConfig cloudflareConfig;

  @Override
  public void run(String... args) throws Exception {
    CloudFlareUtils.ZONE_ID = cloudflareConfig.getZoneId();
    CloudFlareUtils.AUTH_EMAIL = cloudflareConfig.getAuthEmail();
    CloudFlareUtils.AUTH_KEY = cloudflareConfig.getAuthKey();
    CloudFlareTask.NAME = cloudflareConfig.getName();
    CloudFlareTask.IP_NAME = cloudflareConfig.getIpName();
    dynamicTask.startTask(CloudFlareTask.KEY, new CloudFlareTask(), CloudFlareTask.CRON);
  }
}
