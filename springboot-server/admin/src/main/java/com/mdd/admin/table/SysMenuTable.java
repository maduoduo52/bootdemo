package com.mdd.admin.table;

/**
 * @desc用户 - 菜单表
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
public class SysMenuTable {
  /**
   * 菜单名称
   */
  public static final String NAME = "name";
  /**
   * 类型  MENU 菜单  BUTTON 按钮（按钮包括权限）
   */
  public static final String TYPE = "type";
  /**
   * 状态  1 启用 0禁用
   */
  public static final String STATUS = "status";
  /**
   * 图标
   */
  public static final String ICON = "icon";
  /**
   * 父节点
   */
  public static final String PARENT = "parent";

  /**
   * 排序
   */
  public static final String ORDER_KEY = "order_key";

  /**
   * 父节点集合
   */
  public static final String PARENTS = "parents";
  /**
   * 是否为根节点
   */
  public static final String ROOT_FLAG = "root_flag";
  /**
   * url 访问地址  主要为前端地址
   */
  public static final String URL = "url";
  /**
   * 权限地址  后台java接口访问地址
   */
  public static final String AUTHORITY_URL = "authority_url";
  /**
   * 权限按钮  此按钮IDS来控制 界面按钮或者字段显示与否
   */
  public static final String AUTHORITY_BUTTON = "authority_button";
}
