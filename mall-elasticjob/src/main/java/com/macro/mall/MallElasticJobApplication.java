package com.macro.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 应用启动入口
 * Created by macro on 2018/4/26.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallElasticJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallElasticJobApplication.class, args);
    }
}
