package com.mdd.admin.config.mybatisplus.mybatisplus;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/5 21:42
 */
@Data
public class EwGenJavaBD {

    private String name;

    private String mark;

    private List<Map<String,String>> fileds;
}
