package com.camhelp.common;

/**
 * Created by storm on 2017-08-07.
 * URL路径常量
 */

public class CommonUrls {
    public static String DATABASEPATH = "/data/data/com.camhelp/database";//数据库存放路径（主要是包名）
    public static String DATABASENAME = "camhelp.db";//数据库名称

    /*交电费*/
    public static final String URL_ELECTRICITY = "http://zxzf.swpu.edu.cn/OnlinePay/login.aspx";
    /*宿舍报修*/
    public static final String URL_REPAIR = "http://hqservice.swpu.edu.cn/rsp";

    /*服务器相关路径*/

    /**服务器地址*/
    public static final String SERVER_ADDRESS = "http://123.207.253.163:8080";
    /**图片的根路径*/
    public static final String SERVER_ADDRESS_PIC = "http://123.207.253.163:8081/camhelp/";

    /**
     * 功能：登录
     * 方式：Post
     * 路径：/user/login
     */
    public static final String SERVER_LOGIN = SERVER_ADDRESS + "/user/login";

    /**
     * 功能：注册
     * 方式：Post
     * 路径：/user/register
     */
    public static final String SERVER_REGISTER = SERVER_ADDRESS + "/user/register";
    /**
     * 功能：得到所有的记录
     * 方式：get
     * 路径：/classify/list
     * */
    public static final String SERVER_COMMONLIST_ALL = SERVER_ADDRESS + "/classify/list";
    /**
     * 功能：得到某一项具体的记录
     * 方式：post
     * 路径：/classify/details
     * post：commonid
     * */
    public static final String SERVER_COMMONLIST_ONE = SERVER_ADDRESS + "/classify/details";
    /**
     * 功能：得到某一用户的信息
     * 方式：post
     * 路径：/user/find
     * post：userid
     */
    public static final String SERVER_USER_ONE= SERVER_ADDRESS + "/user/find";
    /**
     * 通过userid返回该人发布的信息
     * 方式：post
     * 路径：classify/find/publishbyuser
     * post：userid
     */
    public static final String SERVER_USER_PUBLISHED= SERVER_ADDRESS + "/user/find";

}
