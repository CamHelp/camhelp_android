package com.camhelp.entity;

/**
 * @author Jupiter
 * @description Goods、Problem、Experience的共同属性合并
 * @create 2017-08-08-8:43
 */
public class CommonProperty {

    //id
    private Integer id;

    /**分类的type
     * 1-活动
     * 2-问题
     * 3-失物
     * 4-寻物*/
    private  Integer categoryType;

    /**标题*/
    private String commonTitle;
    /**简介*/
    private String commonIntro;
    /**内容*/
    private String commonContent;
    /**图片1*/
    private String commonPic1;
    /**图片2*/
    private String commonPic2;
    /**图片3*/
    private String commonPic3;
    /**图片4*/
    private String commonPic4;
    /**创建时间*/
    private String createtime;
    /**关注数*/
    private String praisenum;
    /**浏览量*/
    private String browsenum;

    /**发布者ID*/
    private int userId;

    /**活动开始时间*/
    private String expstartTime;
    /**活动结束时间*/
    private String expendTime;

    /**问题类型*/
    private Integer proType;

}
