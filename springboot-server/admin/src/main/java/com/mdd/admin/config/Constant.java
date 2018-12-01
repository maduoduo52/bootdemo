package com.mdd.admin.config;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 14:19
 */
public class Constant {

    /**
     * 线程变量，本地请求的数据
     */
    public static InheritableThreadLocal<HeaderDto> HERDER = new InheritableThreadLocal<>();

    /**
     * 添加请求信息
     * @param headerDto
     */
    public static void set(HeaderDto headerDto){
        HERDER.set(headerDto);
    }

    /**
     * 删除请求头信息
     */
    public static void remove(){
        HERDER.remove();
    }

    /**
     * 获取
     * @return
     */
    public static HeaderDto get(){
        return HERDER.get();
    }

    /**
     * 获取emp id
     * @return
     */
    public static Long getEmpId(){
        HeaderDto dto = get();
        if(dto==null){
            return null;
        }
        return dto.getEmpId();
    }
}
