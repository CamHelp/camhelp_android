package com.camhelp.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by storm on 2017-08-11.
 * 查看具体详情实体类
 */

public class CommomPropertyDetailsVo extends DataSupport implements Serializable {
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

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCommonid() {
        return commonid;
    }

    public void setCommonid(Integer commonid) {
        this.commonid = commonid;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getCommonTitle() {
        return commonTitle;
    }

    public void setCommonTitle(String commonTitle) {
        this.commonTitle = commonTitle;
    }

    public String getCommonIntro() {
        return commonIntro;
    }

    public void setCommonIntro(String commonIntro) {
        this.commonIntro = commonIntro;
    }

    public String getCommonContent() {
        return commonContent;
    }

    public void setCommonContent(String commonContent) {
        this.commonContent = commonContent;
    }

    public String getCommonPic1() {
        return commonPic1;
    }

    public void setCommonPic1(String commonPic1) {
        this.commonPic1 = commonPic1;
    }

    public String getCommonPic2() {
        return commonPic2;
    }

    public void setCommonPic2(String commonPic2) {
        this.commonPic2 = commonPic2;
    }

    public String getCommonPic3() {
        return commonPic3;
    }

    public void setCommonPic3(String commonPic3) {
        this.commonPic3 = commonPic3;
    }

    public String getCommonPic4() {
        return commonPic4;
    }

    public void setCommonPic4(String commonPic4) {
        this.commonPic4 = commonPic4;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public int getBrowsenum() {
        return browsenum;
    }

    public void setBrowsenum(int browsenum) {
        this.browsenum = browsenum;
    }

    public String getExpstartTime() {
        return expstartTime;
    }

    public void setExpstartTime(String expstartTime) {
        this.expstartTime = expstartTime;
    }

    public String getExpendTime() {
        return expendTime;
    }

    public void setExpendTime(String expendTime) {
        this.expendTime = expendTime;
    }

    public Integer getProType() {
        return proType;
    }

    public void setProType(Integer proType) {
        this.proType = proType;
    }

    public String getGoodscontact() {
        return goodscontact;
    }

    public void setGoodscontact(String goodscontact) {
        this.goodscontact = goodscontact;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}