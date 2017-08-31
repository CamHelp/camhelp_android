package com.camhelp.entity;

import java.util.Date;

/**
 * Created by storm on 2017-08-11.
 * 评论实体类
 */

public class Comment {
    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", commonId=" + commonId +
                ", fromuserId=" + fromuserId +
                ", touserId=" + touserId +
                ", mappercommonid=" + mappercommonid +
                ", commenttext='" + commenttext + '\'' +
                ", commentpath='" + commentpath + '\'' +
                ", tonickname='" + tonickname + '\'' +
                ", fromnickname='" + fromnickname + '\'' +
                ", fromuseravatar='" + fromuseravatar + '\'' +
                ", createtime=" + createtime +
                '}';
    }

    private Integer commentId;//id
    //事件id
    private Integer commonId;
    //发送者
    private Integer fromuserId;
    //接受者
    private Integer touserId;

    //评论事件的id
    private Integer mappercommonid;

    private String commenttext;

    private String commentpath;

    private String tonickname;

    private String fromnickname;

    private String fromuseravatar;

    private String createtime;

    public Comment() {
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCommonId() {
        return commonId;
    }

    public void setCommonId(Integer commonId) {
        this.commonId = commonId;
    }

    public Integer getFromuserId() {
        return fromuserId;
    }

    public void setFromuserId(Integer fromuserId) {
        this.fromuserId = fromuserId;
    }

    public Integer getTouserId() {
        return touserId;
    }

    public void setTouserId(Integer touserId) {
        this.touserId = touserId;
    }

    public Integer getMappercommonid() {
        return mappercommonid;
    }

    public void setMappercommonid(Integer mappercommonid) {
        this.mappercommonid = mappercommonid;
    }

    public String getCommenttext() {
        return commenttext;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }

    public String getCommentpath() {
        return commentpath;
    }

    public void setCommentpath(String commentpath) {
        this.commentpath = commentpath;
    }

    public String getTonickname() {
        return tonickname;
    }

    public void setTonickname(String tonickname) {
        this.tonickname = tonickname;
    }

    public String getFromnickname() {
        return fromnickname;
    }

    public void setFromnickname(String fromnickname) {
        this.fromnickname = fromnickname;
    }

    public String getFromuseravatar() {
        return fromuseravatar;
    }

    public void setFromuseravatar(String fromuseravatar) {
        this.fromuseravatar = fromuseravatar;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}