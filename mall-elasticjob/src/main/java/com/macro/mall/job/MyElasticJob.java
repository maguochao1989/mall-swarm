package com.macro.mall.job;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyElasticJob implements SimpleJob {
    
    @Override
    public void execute(ShardingContext context) {
        System.out.println("jobName:" + context.getJobName() + "，shardingTotalCount:" + context.getShardingTotalCount() + "，shardingItem:" + context.getShardingItem() + "，shardingParameter:" + context.getShardingParameter());
        log.debug("已经被调用了");
    }
}