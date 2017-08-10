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
        public static final String SERVER_ADDRESS = "http://123.207.253.163:8080";
    /*登录*/
    public static final String SERVER_LOGIN = SERVER_ADDRESS+"/user/login";
    /*注册*/
    public static final String SERVER_REGISTER = SERVER_ADDRESS+"/user/register";
}
