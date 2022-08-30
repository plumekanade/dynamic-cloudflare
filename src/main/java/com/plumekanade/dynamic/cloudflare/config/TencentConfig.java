package com.plumekanade.dynamic.cloudflare.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * DNSPod配置类
 *
 * @author kanade
 * @date 2022-08-30
 */
@Data
@ConfigurationProperties(prefix = "tencent")
public class TencentConfig {
  private Long appId;
  private String secretId;
  private String secretKey;
  private String domainId;
  private Long[] recordIds;
}
