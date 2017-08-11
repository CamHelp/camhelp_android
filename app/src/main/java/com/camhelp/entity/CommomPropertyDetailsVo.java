package com.camhelp.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by storm on 2017-08-11.
 * 查看具体详情实体类
 */

public class CommomPropertyDetailsVo {
    private Integer userID;

    private String nickname;

    /**
     * 个人头像地址
     */
    private String avatar;

    private Integer commonid;

    /**
     * 分类的type
     * 1-活动
     * 2-问题
     * 3-失物
     * 4-寻物
     */
    private Integer categoryType;

    /**
     * 标题
     */
    private String commonTitle;
    /**
     * 简介
     */
    private String commonIntro;
    /**
     * 内容
     */
    private String commonContent;
    /**
     * 图片1
     */
    private String commonPic1;
    /**
     * 图片2
     */
    private String commonPic2;
    /**
     * 图片3
     */
    private String commonPic3;
    /**
     * 图片4
     */
    private String commonPic4;
    /**
     * 创建时间
     */
    private Date createtime;
    /**
     * 关注数
     */
    private int praisenum;
    /**
     * 浏览量
     */
    private int browsenum;

    /**发布者ID*/
    // private int userId;

    /**
     * 活动开始时间
     */
    private String expstartTime;
    /**
     * 活动结束时间
     */
    private String expendTime;

    /**
     * 问题类型
     */
    private Integer proType;

    /**
     * 物品-联系方式
     */
    private String goodscontact;

    //评论的显示
    private List<Comment> commentList;

}