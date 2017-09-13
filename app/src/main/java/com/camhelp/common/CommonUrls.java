package com.camhelp.common;

/**
 * Created by storm on 2017-08-07.
 * URL路径常量
 */

public class CommonUrls {
    public static String DATABASEPATH = "/data/data/com.camhelp/database";//数据库存放路径（主要是包名）
    public static String DATABASENAME = "camhelp.db";//数据库名称
    public static String FILEPROVIDER_PACKAGE_NAME = "com.camhelp.fileprovider";//图片的包名

    /*交电费*/
    public static final String URL_ELECTRICITY = "http://zxzf.swpu.edu.cn/OnlinePay/login.aspx";
    /*宿舍报修*/
    public static final String URL_REPAIR = "http://hqservice.swpu.edu.cn/rsp";

    /*服务器相关路径*/

    /**
     * 服务器地址
     */
    public static final String SERVER_ADDRESS = "http://39.108.174.87:8080";
    /**
     * 图片的根路径
     */
    public static final String SERVER_ADDRESS_PIC = "http://39.108.174.87:8081/camhelp/";

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
     */
    public static final String SERVER_COMMONLIST_ALL = SERVER_ADDRESS + "/classify/list";

    /**
     * 功能：得到某一项具体的记录
     * 方式：post
     * 路径：/classify/details
     * post：commonid
     */
    public static final String SERVER_COMMONLIST_ONE = SERVER_ADDRESS + "/classify/details";

    /**
     * 功能：得到某一用户的信息
     * 方式：post
     * 路径：/find/user
     * post：userid
     */
    public static final String SERVER_USER_ONE = SERVER_ADDRESS + "/user/find/user";

    /**
     * 通过userid返回该人发布的信息
     * 方式：post
     * 路径：classify/find/publishbyuser
     * post：userid
     */
    public static final String SERVER_USER_PUBLISHED = SERVER_ADDRESS + "/classify/find/publishbyuser";

    /**
     * 通过用户id得到用户关注的人的列表
     * 方式：post
     * 路径：/user/attentionuser
     * post：userid
     */
    public static final String SERVER_MINE_FOCUS = SERVER_ADDRESS + "/user/attentionuser";

    /**
     * 发布
     * 方式：post
     * 路径：/classify/publish
     */
    public static final String SERVER_PUBLISH = SERVER_ADDRESS + "/classify/publish";

    /**
     * 更新头像
     * 方式：post
     * 路径：/user/update/avatar
     * 参数：
     * id：userid
     * avatar：picFile
     */
    public static final String SERVER_USER_UPDATE_AVATAR = SERVER_ADDRESS + "/user/update/avatar";

    /**
     * 更新背景图
     * 方式：post
     * 路径：/user/update/bgpicture
     * 参数：
     * id：userid
     * avatar：picFile
     */
    public static final String SERVER_USER_UPDATE_BG = SERVER_ADDRESS + "/user/update/bgpicture";

    /**
     * 发布评论
     * 方式：post
     * 路径：/comment/addcomment
     *
     */
    public static final String SERVER_COMMENT_ADD = SERVER_ADDRESS + "/comment/addcomment";

    /**
     * 得到用户头像
     * 方式：get
     * 路径：/img/getuseravatar/{id}
     */
    public static final String SERVER_USER_AVATAR = SERVER_ADDRESS + "/img/getuseravatar/";

    /**
     * 得到用户背景图
     * 方式：get
     * 路径：/user/showbg/{id}
     */
    public static final String SERVER_USER_BG = SERVER_ADDRESS + "/user/showbg/";


    /*分类获取*/
    /**
     * 分类获取列表
     * 方式：post
     * 采用分页显示 传参数 page:第几页   size：每一页的大小
     * 路径：
     *      关注：search/attentionuserpublish
     *      最新：search/thenews
     *      最热：search/thehotest
     *      活动：search/activity
     *      问题：search/problem
     *      失物：search/lostthing
     *      捡物：search/findthing
     *      新鲜事：search/freshthings
     *      表白墙：search/wall
     * */
    public static final String SERVER_CATEGORY_FOCUS = SERVER_ADDRESS + "/search/attentionuserpublish/";
    public static final String SERVER_CATEGORY_THENEWS = SERVER_ADDRESS + "/search/thenews/";
    public static final String SERVER_CATEGORY_THEHOTEST = SERVER_ADDRESS + "/search/thehotest/";
    public static final String SERVER_CATEGORY_ACTIVITY = SERVER_ADDRESS + "/search/activity/";
    public static final String SERVER_CATEGORY_PROBLEM = SERVER_ADDRESS + "/search/problem/";
    public static final String SERVER_CATEGORY_LOSTTHING = SERVER_ADDRESS + "/search/lostthing/";
    public static final String SERVER_CATEGORY_FINDTHING = SERVER_ADDRESS + "/search/findthing/";
    public static final String SERVER_CATEGORY_FRESHTHINGS = SERVER_ADDRESS + "/search/freshthings/";
    public static final String SERVER_CATEGORY_WALL = SERVER_ADDRESS + "/search/wall/";

    /**
     * 搜索
     * 方式：post
     * 采用分页显示 传参数 page:第几页   size：每一页的大小
     * 路径：search/data
     */
    public static final String SERVER_SEARCH = SERVER_ADDRESS + "/search/data/";

}
