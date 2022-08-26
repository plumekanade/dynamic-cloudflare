package com.plumekanade.dynamic.cloudflare.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 动态定时任务
 *
 * @author kanade
 * @date 2022-07-20
 */
@Slf4j
@Configuration
@EnableScheduling
public class DynamicTask {
  public static final Map<String, ScheduledFuture<?>> FUTURE_MAP = new HashMap<>();
  private final ThreadPoolTaskScheduler poolScheduler;

  public DynamicTask(ThreadPoolTaskScheduler poolScheduler) {
    poolScheduler.setPoolSize(10);
    this.poolScheduler = poolScheduler;
  }

  /**
   * 添加/重启定时任务
   */
  public void startTask(String futureKey, Runnable runnable, String cron) {
    stopTask(futureKey);
    DynamicTask.FUTURE_MAP.put(futureKey, poolScheduler.schedule(runnable, new CronTrigger(cron)));
    log.info("【定时任务】启动/重启 {} - {} 定时任务完成...", futureKey, cron);
  }

  /**
   * 停止定时任务
   */
  public void stopTask(String futureKey) {
    ScheduledFuture<?> scheduledFuture = FUTURE_MAP.remove(futureKey);
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
    }
  }
}
