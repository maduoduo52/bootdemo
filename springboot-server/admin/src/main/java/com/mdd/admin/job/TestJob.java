package com.mdd.admin.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Maduo
 * @date 2019/11/28 14:03
 */
@Slf4j
@Component
@JobHandler("helloJob")
public class TestJob extends IJobHandler {

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("====> 测试定时任务开始");

        log.info("====> 测试定时任务结束");

        return ReturnT.SUCCESS;
    }
}
