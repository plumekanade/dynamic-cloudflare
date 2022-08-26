package com.plumekanade.dynamic.cloudflare.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cloudflare配置类
 *
 * @author kanade
 * @date 2022-08-26
 */
@Data
@ConfigurationProperties(prefix = "cloudflare")
public class CloudflareConfig {
  private String zoneId;
  private String authEmail;
  private String authKey;
  private String name;
  private String ipName;
}
