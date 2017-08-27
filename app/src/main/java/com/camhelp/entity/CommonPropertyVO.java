package com.camhelp.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by storm on 2017-08-11.
 * 首页展示列表返回的每一项实体
 */

public class CommonPropertyVO extends DataSupport implements Serializable {
    @Override
    public String toString() {
        return "CommonPropertyVO{" +
                "userID=" + userID +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", commonid=" + commonid +
                ", categoryType=" + categoryType +
                ", commonTitle='" + commonTitle + '\'' +
                ", commonPic1='" + commonPic1 + '\'' +
                ", commonPic2='" + commonPic2 + '\'' +
                ", commonPic3='" + commonPic3 + '\'' +
                ", commonPic4='" + commonPic4 + '\'' +
                ", createtime=" + createtime +
                ", praisenum=" + praisenum +
                '}';
    }

    private Integer userID;

    private String nickname;

    /**个人头像地址*/
    private String avatar;

    private Integer commonid;

    /**分类的type
     * 1-活动
     * 2-问题
     * 3-失物
     * 4-寻物*/
    private  Integer categoryType;

    /**标题*/
    private String commonTitle;

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
    private int praisenum;

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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }
}
