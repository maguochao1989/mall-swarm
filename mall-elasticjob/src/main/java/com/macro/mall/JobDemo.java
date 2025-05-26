package com.macro.mall;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.macro.mall.job.FileCustomJob;
import com.macro.mall.job.MyElasticJob;

public class JobDemo {
    
    public static void main(String[] args) {
        // new JobScheduler(createRegistryCenter(), createJobConfiguration("springbootDemo", "0/3 * * * * ?", MyElasticJob.class)).init();
        new JobScheduler(createRegistryCenter(), createJobConfiguration("myFileCustomJobDemo", "0/3 * * * * ?", FileCustomJob.class)).init();
    }
    
    private static CoordinatorRegistryCenter createRegistryCenter() {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration("localhost:2181", "fileCustomJob"));
        regCenter.init();
        return regCenter;
    }
    
    private static LiteJobConfiguration createJobConfiguration(String jobName,String cron, Class clz) {
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 4)
                .shardingItemParameters("0=text,1=image,2=radio,3=vedio") // 设置分片参数
                .failover(true)  // 启用故障转移
                .description("文件处理作业") // 作业描述
                .build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, clz.getCanonicalName());
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
        return simpleJobRootConfig;

    }
}