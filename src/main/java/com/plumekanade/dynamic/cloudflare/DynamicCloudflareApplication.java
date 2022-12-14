package com.plumekanade.dynamic.cloudflare;

import com.plumekanade.dynamic.cloudflare.config.CloudflareConfig;
import com.plumekanade.dynamic.cloudflare.config.TencentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CloudflareConfig.class, TencentConfig.class})
public class DynamicCloudflareApplication {

  public static void main(String[] args) {
    SpringApplication.run(DynamicCloudflareApplication.class, args);
  }

}
