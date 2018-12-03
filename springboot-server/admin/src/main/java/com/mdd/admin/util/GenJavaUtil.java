package com.mdd.admin.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Desc: java 代码生成工具
 * @Author Maduo
 * @Create 2018/12/1 18:57
 */
@Slf4j
public class GenJavaUtil {

    //数据库名称
    static String DB_NAME = "bootdemo";
    //数据库连接
    static String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME+"?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
    // 数据库的用户名与密码，需要根据自己的设置
    static String USER = "root";
    static String PASS = "lhm035730";

    //包名
    static String packName = "com.mdd.admin";

    //文件生成路径
    static String filePath = "F:"+File.separator+"gmiii_demo"+File.separator+"gimiii-springboot" + File.separator +"src"+File.separator+
            "main"+File.separator+"java"+File.separator+"com"+File.separator+"gimiii"+File.separator;
    //zengliming
    static String author = "Maduo";

    static String date  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    static String tableName="sys_user";

    public static void main(String[] args)throws Exception {
        List<EwGenJavaBD> genJavaBDS = init();
        System.out.println();
        String [] strs = tableName.split(",");
        for (EwGenJavaBD genJavaBD : genJavaBDS) {
            boolean flag = false;
            if(!StringUtils.isEmpty(tableName)){
                for (String str : strs) {
                    if(str.equals(genJavaBD.getName())){
                        flag = true;
                        break;
                    }
                }
            }else{
                flag = true;
            }

            if(flag){
                System.out.println("======开始创建"+genJavaBD.getName()+" final 静态类");
                String finalPack = genFinal(genJavaBD);
                System.out.println("=====开始创建"+genJavaBD.getName()+" entity");
                String entityPack = entity(genJavaBD,finalPack);
                System.out.println("=====开始创建"+genJavaBD.getName()+" dao");
                String daoPack = dao(genJavaBD,entityPack);
                System.out.println("=====开始创建"+genJavaBD.getName()+" service ");
                String service = service(genJavaBD,entityPack);
                System.out.println("=====开始创建"+genJavaBD.getName()+" service impl");
                String serviceImpl = serviceImpl(genJavaBD,entityPack,service,daoPack);
                System.out.println("=====开始创建"+genJavaBD.getName()+" DTO");
                String dto = dto(genJavaBD);
            }
        }
    }

    /**
     * 生成但表对应的类文件
     * @param tableName
     * @param comment
     * @throws Exception
     */
    public static void createSingleTableClass(String tableName,String comment) throws Exception{
        //获取数据库相关信息
        EwGenJavaBD genJavaBD = new EwGenJavaBD();
        genJavaBD.setName(tableName);
        genJavaBD.setMark(comment);
        System.out.println("===========查询表（"+tableName+"）字段详情===================");
        List<Map<String,String>> fileds =  filed(tableName);
        genJavaBD.setFileds(fileds);

        //生成类
        System.out.println("======开始创建"+genJavaBD.getName()+" final 静态类");
        String finalPack = genFinal(genJavaBD);

        System.out.println("=====开始创建"+genJavaBD.getName()+" entity");
        String entityPack = entity(genJavaBD,finalPack);

        System.out.println("=====开始创建"+genJavaBD.getName()+" dao");
        String daoPack = dao(genJavaBD,entityPack);

        System.out.println("=====开始创建"+genJavaBD.getName()+" service ");
        String service = service(genJavaBD,entityPack);

        System.out.println("=====开始创建"+genJavaBD.getName()+" service impl");
        String serviceImpl = serviceImpl(genJavaBD,entityPack,service,daoPack);

        System.out.println("=====开始创建"+genJavaBD.getName()+" DTO");
        String dto = dto(genJavaBD);
    }


    /**
     * 将数据库的表名处理成类名
     * @param tableName
     * @return
     */
    public static String handleTableName(String tableName){
        String strs [] =  tableName.toLowerCase().split("_|-");
        String entityName ="";
        for (int i = 0; i <strs.length ; i++) {
            char[] cs=strs[i].toCharArray();
            cs[0]-=32;
            entityName+=String.valueOf(cs);
        }
        return entityName;
    }

    /**
     * 根据数据库表名获取对应包的类名
     * @param tableName
     * @return
     */
    public static String getClassAllName(String tableName,ClassType classType){
        String className = handleTableName(tableName);
        String classNameAllPath = null;
        switch (classType){
            case TABLE:
                classNameAllPath = packName+".table."+className+"Table";
                break;
            case ENTITY:
                classNameAllPath = packName+".model."+className+"Entity";
                break;
            case DAO:
                classNameAllPath = packName+".dao."+className+"Dao";
                break;
            case SERVICE:
                classNameAllPath = packName+".service."+className+"Service";
                break;
            case SERVICEIMP:
                classNameAllPath = packName+".service.impl."+className+"ServiceImpl";
                break;
            case DTO:
                classNameAllPath = packName+".dto.business.DTO"+className;
                break;
            case CONTROLLER:
                classNameAllPath = packName+".controller"+className+"Controller";
        }
        return classNameAllPath;
    }

    public enum ClassType {
        ENTITY, DAO, SERVICE, SERVICEIMP, DTO, TABLE, CONTROLLER
    }

    /**
     * 生成静态类
     * @param genJavaBD
     * @return
     * @throws Exception
     */
    public static String genFinal(EwGenJavaBD genJavaBD)throws Exception{
        // 类名大写
        String className = handleTableName(genJavaBD.getName());
        String str = "package "+packName+".table;\n" +
                "\n" +
                "/**\n" +
                " * @description "+genJavaBD.getMark()+"\n"+
                " * @Author "+author+"\n" +
                " * @Date "+date+"\n" +
                " */\n" +
                "public class "+className+"Table {\n" ;
        for (Map<String, String> m : genJavaBD.getFileds()) {
            str+="  /**\n" +
                 "   * "+m.get("columnComment")+"\n" +
                 "   */\n";
            str+="  public static final String "+m.get("column").toUpperCase() +" = "+ "\""+m.get("column")+"\";\n";
        }
        str+="}\n";
        File file = new File(filePath+"table"+File.separator);
        file.mkdirs();
        File f2 = new File(file.getPath()+File.separator+className+"Table.java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        return getClassAllName(genJavaBD.getName(),ClassType.TABLE);
    }

    /**
     * 生成entity
     * @param genJavaBD
     * @param finalPack
     * @return
     * @throws Exception
     */
    public static String entity(EwGenJavaBD genJavaBD,String finalPack)throws Exception{
        // 类名大写
        String className = handleTableName(genJavaBD.getName());
        String plo = "";
        String str = "package "+packName+".model;\n" +
                "import com.baomidou.mybatisplus.annotations.TableName;\n" +
                "import com.baomidou.mybatisplus.annotations.TableField;\n"+
                "import com.gimiii.config.mybatisplus.entity.BaseEntity;\n" +
                "import lombok.Data;\n" +
                "import "+finalPack+";\n"+
                "STR\n" +
                "\n" +
                "/**\n" +
                " * @description "+genJavaBD.getMark()+"\n"+
                " * @Author "+author+"\n" +
                " * @Date "+date+"\n" +
                " */\n" +
                "@Data\n" +
                "@TableName(\""+genJavaBD.getName()+"\")\n"+
                "public class "+className+"Entity extends BaseEntity{\n" ;
        for (Map<String, String> m : genJavaBD.getFileds()) {
            str+="  /**\n" +
                    "   * "+m.get("columnComment")+"\n" +
                    "   */\n";
            str+="  @TableField("+className+"Table."+m.get("column").toUpperCase()+")\n";
            String dataType = m.get("dataType");
            String columnName = m.get("columnName");
            if(dataType.startsWith("tinyint(1)")){
                str+="  private Boolean "+columnName+";\n";
            }else if(dataType.startsWith("tinyint")){
                str+="  private Integer "+columnName+";\n";
            }else if(dataType.startsWith("datetime") || dataType.startsWith("date") || dataType.startsWith("timestamp")){
                if(!plo.contains("java.util.Date")){
                    plo+="import java.util.Date;\n";
                }
                str+="  private Date "+columnName+";\n";
            }else if(dataType.startsWith("bigint")){
                str+="  private Long "+columnName+";\n";
            }else if(dataType.startsWith("float")){
                str+="  private Float "+columnName+";\n";
            }else if(dataType.startsWith("double")){
                str+="  private Double "+columnName+";\n";
            }else if(dataType.startsWith("int")){
                str+="  private Integer "+columnName+";\n";
            }else if(dataType.startsWith("decimal")){
                if(!plo.contains("java.math.BigDecimal")){
                    plo+="import java.math.BigDecimal;\n";
                }
                str+="  private BigDecimal "+columnName+";\n";
            }else{
                str+="  private String "+columnName+";\n";
            }
        }
        str+="}\n";
        str = str.replaceAll("STR",plo);
        File file = new File(filePath+"model"+File.separator);
        file.mkdirs();
        File f2 = new File(file.getPath()+File.separator+className+"Entity.java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        return getClassAllName(genJavaBD.getName(),ClassType.ENTITY);
    }

    /**
     * 生成DTO
     * @param genJavaBD
     * @return
     * @throws Exception
     */
    public static String dto(EwGenJavaBD genJavaBD)throws Exception{
        // 类名大写
        String className = handleTableName(genJavaBD.getName());
        String plo = "";
        String str = "package "+packName+".dto.business;\n" +
                "import com.gimiii.dto.PageDTO;\n" +
                "import java.io.Serializable;\n" +
                "import lombok.Data;\n" +
                "STR\n" +
                "\n" +
                "/**\n" +
                " * @description "+genJavaBD.getMark()+"\n"+
                " * @Author "+author+"\n" +
                " * @Date "+date+"\n" +
                " */\n" +
                "@Data\n" +
                "public class "+className+"DTO extends PageDTO implements Serializable{\n" ;
        for (Map<String, String> m : genJavaBD.getFileds()) {
            str+="  /**\n" +
                    "   * "+m.get("columnComment")+"\n" +
                    "   */\n";
            String dataType = m.get("dataType");
            String columnName = m.get("columnName");
            if(dataType.startsWith("tinyint(1)")){
                str+="  private Boolean "+columnName+";\n";
            }else if(dataType.startsWith("tinyint")){
                str+="  private Integer "+columnName+";\n";
            }else if(dataType.startsWith("datetime") || dataType.startsWith("date") || dataType.startsWith("timestamp")){
                if(!plo.contains("java.util.Date")){
                    plo+="import java.util.Date;\n";
                }
                str+="  private Date "+columnName+";\n";
            }else if(dataType.startsWith("bigint")){
                str+="  private Long "+columnName+";\n";
            }else if(dataType.startsWith("float")){
                str+="  private Float "+columnName+";\n";
            }else if(dataType.startsWith("double")){
                str+="  private Double "+columnName+";\n";
            }else if(dataType.startsWith("int")){
                str+="  private Integer "+columnName+";\n";
            }else if(dataType.startsWith("decimal")){
                if(!plo.contains("java.math.BigDecimal")){
                    plo+="import java.math.BigDecimal;\n";
                }
                str+="  private BigDecimal "+columnName+";\n";
            }else{
                str+="  private String "+columnName+";\n";
            }
        }
        str+="}\n";
        str = str.replaceAll("STR",plo);
        File file = new File(filePath+File.separator+"dto"+File.separator+"business"+File.separator);
        file.mkdirs();
        File f2 = new File(file.getPath()+File.separator+className+"DTO.java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        return getClassAllName(genJavaBD.getName(),ClassType.DTO);
    }

    /**
     * 生成dao
     * @param genJavaBD
     * @param entityPack
     * @return
     * @throws Exception
     */
    public static String dao(EwGenJavaBD genJavaBD,String entityPack)throws Exception{
        // 类名大写
        String className = handleTableName(genJavaBD.getName());

        String str ="package "+packName+".dao;\n" +
                "\n" +
                "import com.baomidou.mybatisplus.mapper.BaseMapper;\n" +
                "import "+entityPack+";\n" +
                "import org.apache.ibatis.annotations.Mapper;\n" +
                "\n" +
                "/**\n" +
                " * @description "+genJavaBD.getMark()+"\n"+
                " * @Author "+author+"\n" +
                " * @Date "+date+"\n" +
                " */\n" +
                "@Mapper\n" +
                "public interface "+className+"Dao extends BaseMapper<"+className+"Entity> {\n" +
                "}";
        File file = new File(filePath+"dao"+File.separator);
        file.mkdirs();
        File f2 = new File(file.getPath()+File.separator+className+"Dao.java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        return getClassAllName(genJavaBD.getName(),ClassType.DAO);
    }

    /**
     * 生成service
     * @param genJavaBD
     * @param entityPack
     * @return
     * @throws Exception
     */
    public static String service(EwGenJavaBD genJavaBD,String entityPack)throws Exception{
        // 类名大写
        String className = handleTableName(genJavaBD.getName());

        String str ="package "+packName+".service;\n" +
                "\n" +
                "import com.gimiii.config.mybatisplus.mybatisplus.service.BaseServerIService;\n" +
                "import "+entityPack+";\n" +
                "\n" +
                "/**\n" +
                " * @description "+genJavaBD.getMark()+"\n"+
                " * @Author "+author+"\n" +
                " * @Date "+date+"\n" +
                " */\n" +
                "public interface "+className+"Service extends BaseServerIService<"+className+"Entity> {\n" +
                "}";
        File file = new File(filePath+"service"+File.separator);
        file.mkdirs();
        File f2 = new File(file.getPath()+File.separator+className+"Service.java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        return getClassAllName(genJavaBD.getName(),ClassType.SERVICE);
    }

    /**
     * 生成 service impl
     * @param genJavaBD
     * @param entityPack
     * @param servicePack
     * @param daoPack
     * @return
     * @throws Exception
     */
    public static String serviceImpl(EwGenJavaBD genJavaBD,String entityPack,String servicePack,String daoPack)throws Exception{
        // 类名大写
        String className = handleTableName(genJavaBD.getName());

        String str ="package "+packName+".service.impl;\n" +
                "\n" +
                "import "+daoPack+";\n" +
                "import "+entityPack+";\n" +
                "import "+servicePack+";\n" +
                "import com.gimiii.config.mybatisplus.mybatisplus.service.impl.BaseServerServiceImpl;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "\n" +
                "/**\n" +
                " * @description "+genJavaBD.getMark()+"\n"+
                " * @Author "+author+"\n" +
                " * @Date "+date+"\n" +
                " */\n" +
                "@Service\n" +
                "@Transactional(rollbackFor = Exception.class)\n"+
                "public class "+className+"ServiceImpl extends BaseServerServiceImpl<"+className+"Dao,"+className+"Entity> implements "+className+"Service{\n" +
                "}";
        File file = new File(filePath+"service"+File.separator+"impl"+File.separator);
        file.mkdirs();
        File f2 = new File(file.getPath()+File.separator+className+"ServiceImpl.java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        return getClassAllName(genJavaBD.getName(),ClassType.SERVICEIMP);
    }

    /**
     * 生成controller
     * @param genJavaBD
     * @param entityPack
     * @param servicePack
     * @return
     * @throws Exception
     */
    public static String controller(EwGenJavaBD genJavaBD,String entityPack,String servicePack)throws Exception{
        // 类名大写
        String className = handleTableName(genJavaBD.getName());

        String str ="package "+packName+".controller;\n" +
                "\n" +
                "import "+entityPack+";\n" +
                "import "+servicePack+";\n" +
                "import com.gimiii.ew.mybatisplus.controller.BaseController;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "\n" +
                "/**\n" +
                " * @description "+genJavaBD.getMark()+"\n"+
                " * @Author "+author+"\n" +
                " * @Date "+date+"\n" +
                " */\n" +
                "@RestController\n" +
                "@RequestMapping(value = \""+className.substring(0, 1).toLowerCase() + className.substring(1)+"\")\n"+
                "public class "+className+"Controller extends BaseController<"+className+"ServiceImpl,"+className+"Entity> {\n" +
                "}";
        File file = new File(filePath+"controller");
        file.mkdirs();
        File f2 = new File(file.getPath()+File.separator+className+"Controller.java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        return getClassAllName(genJavaBD.getName(),ClassType.CONTROLLER);
    }

    /**
     * 初始化加载
     * @return
     */
    public static List<EwGenJavaBD> init()throws Exception{
        List<EwGenJavaBD> list = new ArrayList<>();
        System.out.println("初始化数据:==================================");
        //step 1 ：查询表列表
        System.out.println("查询表数据 表名以及备注 :==================================1");
        Map<String,String> tableNote = tableNote();
        System.out.println(JSON.toJSONString(tableNote,true));
        //表集合
        Set<String> table  = tableNote.keySet();
        for (String s : table) {
            EwGenJavaBD ewGenJavaBD = new EwGenJavaBD();
            ewGenJavaBD.setName(s);
            ewGenJavaBD.setMark(tableNote.get(s));
            System.out.println("===========查询表（"+s+"）字段详情===================");
            List<Map<String,String>> fileds =  filed(s);
            ewGenJavaBD.setFileds(fileds);
            list.add(ewGenJavaBD);
        }

        return list;
    }


    /**
     * 获取表的注释
     * @return
     */
    public static Map<String,String> tableNote()throws Exception{
        Map<String,String> map = new HashMap<>();
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        //首先查询表
        ResultSet rs = stmt.executeQuery("show table status");
        while(rs.next()){
            String tableName = rs.getString("Name");
            String columnName = rs.getString("Comment");
            map.put(tableName,columnName);
        }
        rs.close();
        stmt.close();
        conn.close();
        return map;
    }


    /**
     * 查询字段详情
     * @param tableName
     * @return
     * @throws Exception
     */
    public static List<Map<String,String>> filed(String tableName)throws Exception{
        List<Map<String,String>> list  = new ArrayList<>();
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        //首先查询表
        ResultSet rs = stmt.executeQuery("select * from information_schema.columns where table_schema = '"+DB_NAME+"' and table_name = "+"'"+tableName+"';");
        while(rs.next()){
            Map<String,String> map = new HashMap<>();
            String columnName = rs.getString("COLUMN_NAME");
            if(!("id,add_time,update_time,version,delete_flag,add_emp_id,update_emp_id" +
                    ",data_company,data_dept,data_emp,data_emp_group").contains(columnName)){
                String dataType = rs.getString("DATA_TYPE");
                String columnComment = rs.getString("COLUMN_COMMENT");
                map.put("column",columnName);
                String [] str = columnName.toLowerCase().split("_");
                String entityName = str[0];
                for (int i = 1; i <str.length ; i++) {
                    char[] cs=str[i].toCharArray();
                    cs[0]-=32;
                    entityName+=String.valueOf(cs);
                }
                map.put("columnName",entityName);
                map.put("dataType",dataType);
                map.put("columnComment",columnComment);
                list.add(map);
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        return list;
    }
}
