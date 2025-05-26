package com.macro.mall.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.macro.mall.domain.FileCustom;
import com.macro.mall.mapper.FileCustomMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileCustomJob implements SimpleJob {

    @Autowired
    private FileCustomMapper fileCustomMapper;

    @Override
    public void execute(ShardingContext shardingContext) {
        if (shardingContext == null) {
            log.error("ShardingContext is null!");
            return;
        }
        
        String type = shardingContext.getShardingParameter();
        doWork(type);
    }
    private void doWork(String type) {
        List<FileCustom> fileCustoms = fileCustomMapper.selectByType(type);
        log.debug("backup data size:{}",fileCustoms.size());
        for (FileCustom fileCustom : fileCustoms) {
            changeStatus(fileCustom);
        }
    }

    private void changeStatus(FileCustom fileCustom) {
        log.debug("repeat data id:{}", fileCustom.getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileCustomMapper.changeStatus(fileCustom.getId());
    }
}