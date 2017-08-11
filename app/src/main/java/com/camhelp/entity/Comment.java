package com.camhelp.entity;

import java.util.Date;

/**
 * Created by storm on 2017-08-11.
 * 评论实体类
 */

public class Comment {
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

    // @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createtime;

    //private String createtime;

    public Comment() {
    }
}