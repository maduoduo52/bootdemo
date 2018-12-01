package com.mdd.admin.config.mybatisplus;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 14:25
 */
@Slf4j
public class MddStdOutImpl implements Log {

    public MddStdOutImpl(String clazz) {
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void error(String s, Throwable throwable) {
        log.error("{}-->{}",s,throwable);
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void debug(String s) {
        if(s!=null && (s.startsWith("==>  Preparing") || s.startsWith("==> Parameters") || s.startsWith("<==      Total"))){
            log.info("{}",s);
        }
    }

    @Override
    public void trace(String s) {
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }
}
