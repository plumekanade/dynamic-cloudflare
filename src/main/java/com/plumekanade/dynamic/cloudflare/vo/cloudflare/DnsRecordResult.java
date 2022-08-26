package com.plumekanade.dynamic.cloudflare.vo.cloudflare;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * DNS记录接口返回值
 *
 * @author kanade
 * @date 2022-08-25
 */
@Data
public class DnsRecordResult implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  private List<DnsRecordItem> result;
  private boolean success;
  private List<Object> errors;
  private List<Object> messages;
  private ResultInfo result_info;

  @Data
  public static class ResultInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer page;
    private Integer per_page;
    private Integer count;
    private Integer total_count;
    private Integer total_pages;
  }
}
