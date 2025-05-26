package com.macro.mall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RegistryCenterConfig {

    @Bean
    public static CoordinatorRegistryCenter createRegistryCenter( @Value("${zk.zk_host}") String zkHost , @Value("${zk.namespace}") String namespace){
        log.debug("zkHost:{} , namespace:{}",zkHost,namespace);
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(zkHost, namespace));
        regCenter.init();
        return regCenter;
    }
    
 }
