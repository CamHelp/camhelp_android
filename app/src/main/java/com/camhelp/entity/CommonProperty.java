package com.camhelp.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jupiter
 * @description Goods、Problem、Experience的共同属性合并
 * @create 2017-08-08-8:43
 */

public class CommonProperty extends DataSupport implements Serializable {

    @Override
    public String toString() {
        return "CommonProperty{" +
                "commonid=" + commonid +
                ", categoryType=" + categoryType +
                ", commonTitle='" + commonTitle + '\'' +
                ", commonIntro='" + commonIntro + '\'' +
                ", commonContent='" + commonContent + '\'' +
                ", commonPic1='" + commonPic1 + '\'' +
                ", commonPic2='" + commonPic2 + '\'' +
                ", commonPic3='" + commonPic3 + '\'' +
                ", commonPic4='" + commonPic4 + '\'' +
                ", createtime=" + createtime +
                ", praisenum=" + praisenum +
                ", browsenum=" + browsenum +
                ", userId=" + userId +
                ", expstartTime='" + expstartTime + '\'' +
                ", expendTime='" + expendTime + '\'' +
                ", proType=" + proType +
                ", goodscontact='" + goodscontact + '\'' +
                '}';
    }

    public void CommonProperty(){

    }

    //id
    private int commonid;

    /**分类的type
     * 1-活动
     * 2-问题
     * 3-失物
     * 4-寻物*/
    private  int categoryType;

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
    private Date createtime;
    /**关注数*/
    private int praisenum;
    /**浏览量*/
    private int browsenum;

    /**发布者ID*/
     private int userId;

    /**活动开始时间*/
    private String expstartTime;
    /**活动结束时间*/
    private String expendTime;

    /**问题类型*/
    private String proType;

    /**物品-联系方式*/
    private  String goodscontact;



    public int getCommonid() {
        return commonid;
    }

    public void setCommonid(int commonid) {
        this.commonid = commonid;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getGoodscontact() {
        return goodscontact;
    }

    public void setGoodscontact(String goodscontact) {
        this.goodscontact = goodscontact;
    }
}
