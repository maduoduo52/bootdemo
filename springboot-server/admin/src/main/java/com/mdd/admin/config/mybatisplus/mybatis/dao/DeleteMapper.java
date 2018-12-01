package com.mdd.admin.config.mybatisplus.mybatis.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Maduo
 * @Create 2018/11/25 14:25
 */
@Mapper
public interface DeleteMapper {

    /**
     * 逻辑删除
     * @param tableName 表名
     * @param operationUid 操作人ID
     * @param map 删除条件
     * @return
     */
    @Update("<script>" +
            "update `${tableName}` set update_time = now(),delete_flag = 1 " +
            "   <if test='operationUid!=null and operationUid!=\"\"'>" +
            "       , update_emp_id = #{operationUid} "+
            "   </if>"+
            "   <where> "+
            "       <foreach collection=\"param.keys\" item=\"k\" separator=\",\">   "+
            "           and ${k}  = #{param[${k}]}  "+
            "       </foreach> "+
            "   </where> "+
            "</script>")
    int deleteByMap(@Param("tableName") String tableName,
                    @Param("operationUid") Long operationUid,
                    @Param("param") Map<String, Object> map);


    /**
     * 根据IDS 进行删除
     * @param tableName 表名
     * @param operationUid 操作人ID
     * @param idKey 主键
     * @param idList id集合
     * @return
     */
    @Update("<script>" +
            "update `${tableName}` set update_time = now(),delete_flag = 1 " +
            "   <if test='operationUid!=null and operationUid!=\"\"'>" +
            "       , update_emp_id = #{operationUid} "+
            "   </if>"+
            "   <where> "+
            "       `${idKey}` "+
            "       <foreach  collection=\"idList\" item=\"id\" open=\"in (\" close=\")\" separator=\",\">   "+
            "#{id}"+
            "       </foreach> "+
            "   </where> "+
            "</script>")
    int deleteBatchIds(@Param("tableName") String tableName,
                       @Param("operationUid") Long operationUid,
                       @Param("idKey") String idKey,
                       @Param("idList") List<? extends Serializable> idList);


    /**
     * 删除
     * @param map 参数
     * @return
     */
    @Update("${gw.sql}")
    int delete(@Param("gw") Map map);


    /**
     * 物理删除
     * @return
     */
    @Delete("${gw.sql}")
    int physicsDelete(@Param("gw") Map map);


    /**
     * 物理删除
     * @param tableName  表名
     * @param map
     * @return
     */
    @Delete("<script>" +
            "delete from  `${tableName}` " +
            "   <where> "+
            "       <foreach collection=\"param.keys\" item=\"k\" separator=\",\">   "+
            "           and ${k}  = #{param[${k}]}  "+
            "       </foreach> "+
            "   </where> "+
            "</script>")
    int physicsDeleteByMap(@Param("tableName") String tableName,
                           @Param("param") Map<String, Object> map);



    /**
     * 根据IDS 进行删除
     * @param tableName 表名
     * @param idKey 主键
     * @param idList id集合
     * @return
     */
    @Update("<script>" +
            "delete from  `${tableName}` " +
            "   <where> "+
            "       `${idKey}` "+
            "       <foreach  collection=\"idList\" item=\"id\" open=\"in (\" close=\")\" separator=\",\">   "+
            "#{id}"+
            "       </foreach> "+
            "   </where> "+
            "</script>")
    int physicsDeleteBatchIds(@Param("tableName") String tableName,
                              @Param("idKey") String idKey,
                              @Param("idList") List<? extends Serializable> idList);


}
