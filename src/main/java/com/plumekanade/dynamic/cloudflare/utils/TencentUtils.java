package com.plumekanade.dynamic.cloudflare.utils;

import com.plumekanade.dynamic.cloudflare.config.TencentConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.ModifyRecordBatchRequest;
import com.tencentcloudapi.dnspod.v20210323.models.ModifyRecordBatchResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * DNSPod工具类
 *
 * @author kanade
 * @date 2022-08-30
 */
@Slf4j
public class TencentUtils {

//  public static void main(String[] args) {
//    try{
//      // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
//      // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
//      Credential cred = new Credential("SecretId", "SecretKey");
//      // 实例化要请求产品的client对象
//      DnspodClient client = new DnspodClient(cred, "");
//      // 实例化一个请求对象,每个接口都会对应一个request对象
//      ModifyRecordRequest req = new ModifyRecordRequest();
//      req.setDomainId();
//      req.setRecordType("AAAA");
//      req.setRecordLine("默认");
//      req.setValue("");
//      req.setRecordId();
//      // 返回的resp是一个ModifyRecordResponse的实例，与请求对象对应
//      ModifyRecordResponse resp = client.ModifyRecord(req);
//      // 输出json格式的字符串回包
//      System.out.println(ModifyRecordResponse.toJsonString(resp));
//    } catch (TencentCloudSDKException e) {
//      log.error("【DNSPod】异常堆栈: ", e);
//    }
//  }

  /**
   * 批量修改dns记录
   */
  public static String batchModifyRecord(TencentConfig tencentConfig, String ipv6) {
    try{
      // 实例化一个认证对象
      Credential cred = new Credential(tencentConfig.getSecretId(), tencentConfig.getSecretKey());
      // 实例化要请求产品的client对象
      DnspodClient client = new DnspodClient(cred, "");
      // 实例化一个请求对象,每个接口都会对应一个request对象
      ModifyRecordBatchRequest req = new ModifyRecordBatchRequest();
      req.setRecordIdList(tencentConfig.getRecordIds());
      req.setChange("value");
      req.setChangeTo(ipv6);
      // 发起请求
      ModifyRecordBatchResponse resp = client.ModifyRecordBatch(req);
      return ModifyRecordBatchResponse.toJsonString(resp);
    } catch (TencentCloudSDKException e) {
      log.error("【DNSPod】批量修改DNS记录失败, 腾讯SDK相关异常: {}", e.toString());
    } catch (Exception e) {
      log.error("【DNSPod】批量修改DNS记录失败, 未知异常: ", e);
    }
    return null;
  }
}
