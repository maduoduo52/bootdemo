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
    static final String DB_NAME = "bootdemo";
    //数据库连接
    static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME ;
    //查询所有的表
    static final String selectTable = "select table_name  from information_schema.tables  where table_schema='" + DB_NAME + "'";
    //查询数据表的字段信息
    static final String selectTableInfo = "select * from information_schema.columns where table_schema = '" + DB_NAME + "' and table_name = ";
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "lhm035730";
    //类注释 作者
    static final String author = "Maduo";
    //需要继承那个baseEntity BaseEntity或者BaseManageEntity
    static final String BaseEntity = "BaseEntity";
    //文件保存地址
    static final String filePath = "F:\\genjava\\";
    //包路径
    static final String pagbackPath = "com.mdd.admin";

    public static void main(String[] args) throws Exception {
        log.info("------查询数据库表名------start");
        Set<String> set = tables();
        log.warn("数据库表:{}", set);
        log.info("------查询数据库表名------end");
        log.info("------查询数据库表的主键属性------start");
        Map<String, String> keysMap = keys();
        log.warn("数据库表主键:{}", keysMap);
        log.info("------查询数据库表的主键属性------end");
        log.info("------查询数据库表的注释------start");
        Map<String, String> noteMap = tableNote();
        log.warn("数据库表的注释:{}", noteMap);
        log.info("------查询数据库表的注释------end");
        for (String s : set) {
            String entityName = "";
            String[] str = s.toLowerCase().split("_");
            for (int i = 0; i < str.length; i++) {
                char[] cs = str[i].toCharArray();
                cs[0] -= 32;
                entityName += String.valueOf(cs);
            }
            List<Map<String, String>> list = filed(s);
            //生成Entity
            genEntity(entityName + "Entity", list, s, keysMap.get(s), noteMap.get(s));
//            //生成dto
//            genDto(entityName+"Dto",list,noteMap.get(s));
            //生成dao
            genDao(entityName + "Dao", noteMap.get(s), entityName + "Entity");
            //生成service
            genService(entityName + "Service", noteMap.get(s), entityName + "Entity");
            //生成service impl
            genServiceImpl(entityName + "ServiceImpl", noteMap.get(s), entityName);
        }
    }

    /**
     * Entity 排除字段
     */
    static final Set<String> exceFile = new HashSet<>();

    static {
        exceFile.add("id");
        exceFile.add("addTime");
        exceFile.add("updateTime");
        exceFile.add("version");
        exceFile.add("deleteFlag");

        exceFile.add("addEmpId");
        exceFile.add("updateEmpId");

    }

    /**
     * 查询主键sql
     */
    static final String selectKey = "SELECT\n" +
            "  DISTINCT t.TABLE_NAME,\n" +
            "  t.CONSTRAINT_TYPE,\n" +
            "  c.COLUMN_NAME,\n" +
            "  c.ORDINAL_POSITION\n" +
            "FROM\n" +
            "  INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t,\n" +
            "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c,\n" +
            "  information_schema.TABLES AS ts\n" +
            "WHERE\n" +
            "  t.TABLE_NAME = c.TABLE_NAME\n" +
            "  AND t.TABLE_SCHEMA = '" + DB_NAME + "'\n" +
            "  AND t.CONSTRAINT_TYPE = 'PRIMARY KEY'";

    /**
     * 获取表的注释
     */
    static final String tableNote = "show table status";

    public static String[] genFiled(List<Map<String, String>> list, Set<String> set) {
        String str = "", plo = "";
        for (Map<String, String> map : list) {
            String columnName = map.get("columnName");
            String dataType = map.get("dataType");
            String columnComment = map.get("columnComment");
            if (!set.contains(columnName)) {
                str += "      /**\n" +
                        "       * " + columnComment + "\n" +
                        "       */\n";
                if (dataType.startsWith("varchar")) {
                    str += "      private String " + columnName + ";\n";
                } else if (dataType.startsWith("tinyint(1)")) {
                    str += "      private Boolean " + columnName + ";\n";
                } else if (dataType.startsWith("tinyint")) {
                    str += "      private Integer " + columnName + ";\n";
                } else if (dataType.startsWith("datetime")) {
                    if (!plo.contains("java.util.Date")) {
                        plo += "import java.util.Date;\n";
                    }
                    str += "      private Date " + columnName + ";\n";
                } else if (dataType.startsWith("bigint")) {
                    str += "      private Long " + columnName + ";\n";
                } else if (dataType.startsWith("float")) {
                    str += "      private Float " + columnName + ";\n";
                } else if (dataType.startsWith("double")) {
                    str += "      private Double " + columnName + ";\n";
                } else if (dataType.startsWith("text")) {
                    str += "      private String " + columnName + ";\n";
                } else if (dataType.startsWith("longText")) {
                    str += "      private String " + columnName + ";\n";
                } else if (dataType.startsWith("int")) {
                    str += "      private Integer " + columnName + ";\n";
                } else if (dataType.startsWith("decimal")) {
                    if (!plo.contains("java.math.BigDecimal")) {
                        plo += "import java.math.BigDecimal;\n";
                    }
                    str += "      private BigDecimal " + columnName + ";\n";
                } else if (dataType.startsWith("char")) {
                    str += "      private String " + columnName + ";\n";
                }
            }
        }
        return new String[]{str, plo};
    }

    /**
     * 生成service impl
     *
     * @param name
     * @param note
     * @return
     * @throws Exception
     */
    public static String genServiceImpl(String name, String note, String entityName) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String str =
                "package " + pagbackPath + ".service;\n" +
                        "import " + pagbackPath + ".model." + entityName + "Entity;\n" +
                        "import " + pagbackPath + ".dao." + entityName + "Dao;\n" +
                        "import " + pagbackPath + ".service." + entityName + "Service;\n" +
                        "import com.gimiii.server.BaseServerService;\n" +
                        "import org.springframework.stereotype.Service;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "/**\n" +
                        " * @desc " + note + " service impl \n" +
                        " * @author " + author + "  \n" +
                        " * @Create " + simpleDateFormat.format(new Date()) + "\n" +
                        " */\n" +
                        "@Service\n" +
                        "@Transactional(rollbackFor = Exception.class)\n" +
                        "public class " + name + " extends BaseServerService<" + entityName + "Dao," + entityName + "Entity> implements " + entityName + "Service{\n";
        str += "}";
        File file = new File(filePath + "service\\impl\\");
        file.mkdirs();
        File f2 = new File(file.getPath() + "\\" + name + ".java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        log.info("生成" + entityName + "service impl:路径-->{}", f2.getPath());
        return "";
    }

    /**
     * 生成service
     *
     * @param name
     * @param note
     * @return
     * @throws Exception
     */
    public static String genService(String name, String note, String entityName) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String str =
                "package " + pagbackPath + ".service;\n" +
                        "import " + pagbackPath + ".model." + entityName + ";\n" +
                        "import com.gimiii.server.BaseServerIService;\n" +
                        "/**\n" +
                        " * @desc " + note + " service \n" +
                        " * @author " + author + "  \n" +
                        " * @Create " + simpleDateFormat.format(new Date()) + "\n" +
                        " */\n" +
                        "public interface " + name + " extends BaseServerIService<" + entityName + ">{\n";
        str += "}";
        File file = new File(filePath + "service\\");
        file.mkdirs();
        File f2 = new File(file.getPath() + "\\" + name + ".java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        log.info("生成" + entityName + "service:路径-->{}", f2.getPath());
        return "";
    }

    /**
     * 生成dao
     *
     * @param name
     * @param note
     * @return
     * @throws Exception
     */
    public static String genDao(String name, String note, String entityName) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String str =
                "package " + pagbackPath + ".dao;\n" +
                        "import " + pagbackPath + ".model." + entityName + ";\n" +
                        "import com.baomidou.mybatisplus.mapper.BaseMapper;\n" +
                        "import org.apache.ibatis.annotations.Mapper;\n" +
                        "/**\n" +
                        " * @desc" + note + " DAO \n" +
                        " * @author " + author + "  \n" +
                        " * @Create " + simpleDateFormat.format(new Date()) + "\n" +
                        " */\n" +
                        "@Mapper\n" +
                        "public interface " + name + " extends BaseMapper<" + entityName + ">{\n";
        str += "}";
        File file = new File(filePath + "dao\\");
        file.mkdirs();
        File f2 = new File(file.getPath() + "\\" + name + ".java");
        FileOutputStream out = new FileOutputStream(f2);
        out.write(str.getBytes());
        out.close();
        log.info("生成" + entityName + "Dao类完成:路径-->{}", f2.getPath());
        return "";
    }

    /**
     * 生产Entity
     *
     * @param entityName
     * @param list
     * @param tableName
     * @return
     * @throws Exception
     */
    public static String genEntity(String entityName, List<Map<String, String>> list, String tableName, String idKey, String note) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String baseen = "import com.mdd.admin.config.mybatisplus.entity.BaseEntity;\n";
        String str =
                "package " + pagbackPath + ".model;\n" +
                        "import com.baomidou.mybatisplus.annotations.TableName;\n" +
                        baseen +
                        "STR" +
                        "import lombok.Data;\n" +
                        "/**\n" +
                        " * @desc" + note + " Entity\n" +
                        " * @author " + author + "  \n" +
                        " * @Create " + simpleDateFormat.format(new Date()) + "\n" +
                        " */\n" +
                        "@Data\n" +
                        "@TableName(\"" + tableName + "\")\n" +
                        "public class " + entityName + " extends " + BaseEntity + "{\n";
        //开始生产属性
        idKey = idKey == null ? "" : idKey;
        String idk = idKey.replaceAll("_", "").toLowerCase();
        String plo = "";
        for (Map<String, String> map : list) {
            String columnName = map.get("columnName");
            String dataType = map.get("dataType");
            String columnComment = map.get("columnComment");
            if (!exceFile.contains(columnName)) {
                str += "      /**\n" +
                        "       * " + columnComment + "\n" +
                        "       */\n";
                if (idk.equals(columnName.toLowerCase())) {
                    str += "      @TableId\n";
                }
                if (dataType.startsWith("varchar")) {
                    str += "      private String " + columnName + ";\n";
                } else if (dataType.startsWith("tinyint(1)")) {
                    str += "      private Boolean " + columnName + ";\n";
                } else if (dataType.startsWith("tinyint")) {
                    str += "      private Integer " + columnName + ";\n";
                } else if (dataType.startsWith("datetime")) {
                    if (!plo.contains("java.util.Date")) {
                        plo += "import java.util.Date;\n";
                    }
                    str += "      private Date " + columnName + ";\n";
                } else if (dataType.startsWith("bigint")) {
                    str += "      private Long " + columnName + ";\n";
                } else if (dataType.startsWith("float")) {
                    str += "      private Float " + columnName + ";\n";
                } else if (dataType.startsWith("double")) {
                    str += "      private Double " + columnName + ";\n";
                } else if (dataType.startsWith("text")) {
                    str += "      private String " + columnName + ";\n";
                } else if (dataType.startsWith("longText")) {
                    str += "      private String " + columnName + ";\n";
                } else if (dataType.startsWith("int")) {
                    str += "      private Integer " + columnName + ";\n";
                } else if (dataType.startsWith("decimal")) {
                    str += "      private BigDecimal " + columnName + ";\n";
                    if (!plo.contains("java.math.BigDecimal")) {
                        plo += "import java.math.BigDecimal;\n";
                    }
                } else if (dataType.startsWith("char")) {
                    str += "      private String " + columnName + ";\n";
                }

            }
        }
        str += "}";
        File file = new File(filePath + "model\\");
        file.mkdirs();
        File f2 = new File(file.getPath() + "\\" + entityName + ".java");
        FileOutputStream out = new FileOutputStream(f2);
        str = str.replaceAll("STR", plo);
        out.write(str.getBytes());
        out.close();
        log.info("生成" + entityName + "实体类完成:路径-->{}", f2.getPath());
        return "";
    }

    /**
     * 查询字段详情
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> filed(String tableName) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        //首先查询表
        ResultSet rs = stmt.executeQuery(selectTableInfo + "'" + tableName + "';");
        while (rs.next()) {
            Map<String, String> map = new HashMap<>();
            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("DATA_TYPE");
            String columnComment = rs.getString("COLUMN_COMMENT");

            String[] str = columnName.toLowerCase().split("_");
            String entityName = str[0];
            for (int i = 1; i < str.length; i++) {
                char[] cs = str[i].toCharArray();
                cs[0] -= 32;
                entityName += String.valueOf(cs);
            }
            map.put("columnName", entityName);
            map.put("dataType", dataType);
            map.put("columnComment", columnComment);
            list.add(map);
        }
        rs.close();
        stmt.close();
        conn.close();
        return list;
    }

    /**
     * 查询表列表
     *
     * @return
     * @throws Exception
     */
    public static Set<String> tables() throws Exception {
        Set<String> set = new HashSet<>();
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        //首先查询表
        ResultSet rs = stmt.executeQuery(selectTable);
        while (rs.next()) {
            String tableName = rs.getString("table_name");
            set.add(tableName);
        }
        rs.close();
        stmt.close();
        conn.close();
        return set;
    }

    /**
     * 获取主键
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> keys() throws Exception {
        Map<String, String> map = new HashMap<>();
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        //首先查询表
        ResultSet rs = stmt.executeQuery(selectKey);
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            String columnName = rs.getString("COLUMN_NAME");
            map.put(tableName, columnName);
        }
        rs.close();
        stmt.close();
        conn.close();

        return map;
    }

    /**
     * 获取表的注释
     *
     * @return
     */
    public static Map<String, String> tableNote() throws Exception {
        Map<String, String> map = new HashMap<>();
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        //首先查询表
        ResultSet rs = stmt.executeQuery(tableNote);
        while (rs.next()) {
            String tableName = rs.getString("Name");
            String columnName = rs.getString("Comment");
            map.put(tableName, columnName);
        }
        rs.close();
        stmt.close();
        conn.close();
        return map;
    }
}
