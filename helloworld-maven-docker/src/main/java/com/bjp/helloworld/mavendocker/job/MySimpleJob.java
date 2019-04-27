package com.bjp.helloworld.mavendocker.job;

import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ElasticJobConf(name = "MySimpleJob", cron = "0/10 * * * * ?",
        shardingItemParameters = "0=0,1=1,2=a,3=b,4=c", description = "简单任务", shardingTotalCount = 5)
public class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("jobName={};shardingItem={}", shardingContext.getJobName(), shardingContext.getShardingItem());
        log.info(shardingContext.toString());
    }
}
