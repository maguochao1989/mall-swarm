package com.macro.mall.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.macro.mall.job.FileCustomJob;
import com.macro.mall.job.MyDataflowJob;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@Configuration
public class JobConfig {

    @Autowired
    private DataSource dataSource;

    // @Autowired
    // CoordinatorRegistryCenter coordinatorRegistryCenter;

    // @Bean(initMethod = "init")
    // public SpringJobScheduler springJobScheduler(MyElasticJob myElasticJob) {
    //     JobEventConfiguration jobEventConfiguration = new JobEventRdbConfiguration(dataSource);
    //     return new SpringJobScheduler(myElasticJob, 
    //                         coordinatorRegistryCenter, 
    //                         createJobConfiguration("springbootDemo", "0/3 * * * * ?", MyElasticJob.class,1,null,false),
    //                         jobEventConfiguration);
    // }


    private static CoordinatorRegistryCenter createRegistryCenter(String zkhost,String namespace) {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(zkhost, namespace));
        regCenter.init();
        return regCenter;
    }

    // @Bean(initMethod = "init")
    // public SpringJobScheduler springJobScheduler(FileCustomJob job) {
    //     return new SpringJobScheduler(job, 
    //                         createRegistryCenter("localhost:2181", "fileCustomJob"), 
    //                         createJobConfiguration("fileCustomJobDemo", "0/3 * * * * ?", FileCustomJob.class,4,"0=text,1=image,2=radio,3=vedio",false));
    // }



    @Bean(initMethod = "init")
    public SpringJobScheduler springJobScheduler(MyDataflowJob job) {
        JobEventConfiguration jobEventConfiguration = new JobEventRdbConfiguration(dataSource);
        return new SpringJobScheduler(job, 
                            createRegistryCenter("localhost:2181", "myDataflowJob"),
                            createJobConfiguration("myDataflowJob", "0/3 * * * * ?", MyDataflowJob.class, 1, null, true),
                            jobEventConfiguration);
    }
    
    private static LiteJobConfiguration createJobConfiguration(String jobName, String cron, Class<?> clz,int shardingTotalCount,String shardingParam,boolean isDataflow) {
        JobCoreConfiguration.Builder builder =  JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount);  
        if(!StringUtils.isEmpty(shardingParam)){
            builder.shardingItemParameters(shardingParam);
        }

        JobCoreConfiguration simpleCoreConfig = builder.failover(true).description("文件处理作业").build();

        JobTypeConfiguration jobTypeConfiguration = null ;
        if(isDataflow){
            jobTypeConfiguration = new DataflowJobConfiguration(simpleCoreConfig, clz.getCanonicalName(), true);
        }else
            jobTypeConfiguration = new SimpleJobConfiguration(simpleCoreConfig, clz.getCanonicalName());
        return LiteJobConfiguration.newBuilder(jobTypeConfiguration).build();
    }

}
