package com.mdd.admin.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maduo
 * @date 2018/3/27 15:06
 */
@Getter
@Setter
@ToString
public final class Result {
    private int code;
    private Object data;
    private String msg;

    /**
     * 私有无参构造 不允许外部私自新加返回状态
     */
    private Result() {
    }

    /**
     * 带参数构造
     *
     * @param resultCodeEnum 返回结果枚举
     */
    public Result(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getStatus();
        this.msg = resultCodeEnum.getDesc();
    }

    /**
     * 带参数构造
     *
     * @param resultCodeEnum 返回结果编码
     * @param msg            结果描述
     */
    public Result(ResultCodeEnum resultCodeEnum, String msg, Object data) {
        this.code = resultCodeEnum.getStatus();
        this.msg = msg;
        if (null == msg || "".equals(msg)) {
            this.msg = resultCodeEnum.getDesc();
        }
        this.data = data;
    }

    /**
     * 带参数构造
     *
     * @param code 返回结果编码
     * @param msg  返回结果描述
     * @param data 结果描述
     */
    public Result(Integer code, Object data, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 无数据异常返回（code=500）
     *
     * @param msg 结果描述
     * @return
     */
    public static Result error(String msg) {
        return new Result(ResultCodeEnum.ERROR, msg, null);
    }

    public static Result error(Object data, String msg) {
        return new Result(ResultCodeEnum.ERROR, msg, data);
    }


    public static Result orderError(String msg) {
        return new Result(ResultCodeEnum.ORDER_ERROR, msg, null);
    }


    /**
     * 默认返回结果
     *
     * @return
     */
    public static Result success(Object data) {
        return new Result(ResultCodeEnum.SUCCESS, null, data);
    }

    /**
     * 有数据成功返回（code=200）
     *
     * @param data 返回数据
     * @param msg  结果描述
     * @return
     */
    public static Result success(Object data, String msg) {
        return new Result(ResultCodeEnum.SUCCESS, msg, data);
    }

    /**
     * 有数据成功返回
     *
     * @param code 返回码
     * @param data 返回数据
     * @param msg  结果描述
     * @return
     */
    public static Result success(Integer code, Object data, String msg) {
        return new Result(ResultCodeEnum.SUCCESS, msg, data);
    }

    /**
     * 无数据成功返回（code=200）
     *
     * @param msg 结果描述
     * @return
     */
    public static Result success(String msg) {
        return new Result(ResultCodeEnum.SUCCESS, msg, null);
    }

    /**
     * 参数验证失败异常返回（code=999）
     *
     * @param msg 提示信息
     * @return
     */
    public static Result validationError(String msg) {
        return new Result(ResultCodeEnum.VALIDATION_ERROR, msg, null);
    }

    /**
     * 安全校验失败
     *
     * @param msg
     * @return
     */
    public static Result safetyCheck(String msg) {
        if (msg == null) {
            return new Result(ResultCodeEnum.SAFETY_CHECK);
        } else {
            return new Result(ResultCodeEnum.SAFETY_CHECK, msg, null);
        }
    }

    /**
     * 多vo对象返回
     *
     * @param msg
     * @param objs
     * @return
     */
    public static Result successOnVo(String msg, Object... objs) {
        Result result = new Result(ResultCodeEnum.SUCCESS, msg, null);
        if (objs != null) {
            Map<String, Object> map = new HashMap<>();
            for (Object obj : objs) {
                map.put(captureName(obj.getClass().getSimpleName()), obj);
            }
            result.setData(map);
        }
        return result;
    }

    /**
     * 多vo合并为1
     *
     * @param msg
     * @param objs
     * @return
     */
    public static Result seccessMoreVo(String msg, Object... objs) {
        Result result = new Result(ResultCodeEnum.SUCCESS, msg, null);
        if (objs != null) {
            Map map = new HashMap<>();
            for (Object obj : objs) {
                Map m = JSON.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue), Map.class);
                map.putAll(m);
            }
            result.setData(map);
        }
        return result;
    }

    /**
     * 多vo合并为1
     *
     * @param objs
     * @return
     */
    public static Result seccessMoreVo(Object... objs) {
        Result result = new Result(ResultCodeEnum.SUCCESS, null, null);
        if (objs != null) {
            Map map = new HashMap<>();
            for (Object obj : objs) {
                Map m = JSON.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue), Map.class);
                map.putAll(m);
            }
            result.setData(map);
        }
        return result;
    }

    /**
     * 多VO对象返回
     *
     * @param objs
     * @return
     */
    public static Result successOnVo(Object... objs) {
        Result result = new Result(ResultCodeEnum.SUCCESS, null, null);
        if (objs != null) {
            Map<String, Object> map = new HashMap<>();
            for (Object obj : objs) {
                map.put(captureName(obj.getClass().getSimpleName()), obj);
            }
            result.setData(map);
        }
        return result;
    }


    /**
     * 首字母小写
     *
     * @param name
     * @return
     */
    private static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }

}
