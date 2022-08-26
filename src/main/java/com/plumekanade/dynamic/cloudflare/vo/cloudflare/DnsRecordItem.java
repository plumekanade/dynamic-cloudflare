package com.plumekanade.dynamic.cloudflare.vo.cloudflare;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * DNS记录对象
 *
 * @author kanade
 * @date 2022-08-25
 */
@Data
public class DnsRecordItem implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  private String id;
  private String zone_id;
  // 顶级域名的名称 -> xxx.com/xxx.cn/....
  private String zone_name;
  // dns记录名称（子域名全称）
  private String name;
  /**
   * dns记录解析类型
   * A指向ipv4
   * AAA指向ipv6
   * CAA可以为名称和仅允许使用特定主机名颁发证书
   * CERT具有证书类型和通过算法加密的公钥
   * CNAME是目标的别名
   * 详细参考Cloudflare的DNS记录设置......
   */
  private String type;
  // 子域名指向的内容, 如: ipv4、ipv6, 依据解析类型不同填入不同数据
  private String content;
  // 是否可代理
  private boolean proxiable;
  // 代理状态
  private boolean proxied;
  // ttl 60~86400 1为自动
  private Integer ttl;
  // 是否锁定
  private boolean locked;
  // 元数据
  private Meta meta;
  // 创建时间 yyyy-MM-ddTHH:mm:ss.SSS???Z
  private String created_on;
  // 修改时间 yyyy-MM-ddTHH:mm:ss.SSS???Z
  private String modified_on;

  @Data
  public static class Meta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 自动添加
    private boolean add_added;
    // 自app管理?
    private boolean managed_by_apps;
    private boolean managed_by_argo_tunnel;
    private String source;
  }
}
