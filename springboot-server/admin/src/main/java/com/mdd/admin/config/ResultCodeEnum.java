package com.mdd.admin.config;

/**
 * 返回结果状态枚举
 *
 * @author Maduo
 * @Description 返回结果状态枚举
 * @create 2018-04-25
 **/
public enum ResultCodeEnum {
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    VALIDATION_ERROR(999, "参数校验失败"),
    SAFETY_CHECK(100, "安全校验失败"),
    ORDER_ERROR(201, "存在未完成订单信息"),
    JXL_ACCESS(202, "请从新发起任务"),
    JXL_INPUT_CODE(203, "请输入短信验证码"),
    JXL_SKIP(401, "跳过抓取"),
    ;
    /**
     * 状态值
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String desc;

    ResultCodeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 获取状态编码
     *
     * @return
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 获取结果描述
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }
}
