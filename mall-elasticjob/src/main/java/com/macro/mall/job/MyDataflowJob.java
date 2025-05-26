package com.macro.mall.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.macro.mall.domain.FileCustom;
import com.macro.mall.mapper.FileCustomMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyDataflowJob implements DataflowJob<FileCustom>{

    @Autowired
    private FileCustomMapper fileCustomMapper;

    @Override
    public List<FileCustom> fetchData(ShardingContext shardingContext) {
        List<FileCustom> fileCustoms = fileCustomMapper.fetchData(2);
        return fileCustoms;
    }
    
    @Override
    public void processData(ShardingContext shardingContext, List<FileCustom> list) {
        log.debug("exec data size:{}",list.size());
        for (FileCustom fileCustom : list) {
            changeStatus(fileCustom);
        }

    }

    private void changeStatus(FileCustom fileCustom) {
        log.debug("repeat data id:{}",fileCustom.getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileCustomMapper.changeStatus(fileCustom.getId());

    }
}
