package com.camhelp.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by storm on 2017-08-11.
 * 登录后从服务器返回的user对象
 */

public class UserVO extends DataSupport implements Serializable {

    @Override
    public String toString() {
        return "UserVO{" +
                "userID=" + userID +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", intro='" + intro + '\'' +
                ", avatar='" + avatar + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", roleID=" + roleID +
                ", bgpicture='" + bgpicture + '\'' +
                ", attentionUser='" + attentionUser + '\'' +
                ", attentionGoods='" + attentionGoods + '\'' +
                '}';
    }

    /**用户id*/
    private Integer userID;
    /**账号*/
    private String account;
    /**密码*/
    private String password;
    /**昵称
     * 0-男，1-女,-1-保密*/
    private String nickname;
    /**性别*/
    private Integer sex;
    /**个人简介*/
    private String intro;
    /**个人头像地址*/
    private String avatar;
    /**用户电话*/
    private String telephone;
    /**邮箱*/
    private String email;
    /**地址
     * 利用_进行拼接*/
    private String address;
    /**生日
     * 暂采用String*/
    private String birthday;
    /**用户角色*/
    private Integer roleID;
    /**背景图片*/
    private  String bgpicture;

    /**关注的人*/
    private String attentionUser;

    /**关注的事件*/
    private String attentionGoods;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public String getBgpicture() {
        return bgpicture;
    }

    public void setBgpicture(String bgpicture) {
        this.bgpicture = bgpicture;
    }

    public String getAttentionUser() {
        return attentionUser;
    }

    public void setAttentionUser(String attentionUser) {
        this.attentionUser = attentionUser;
    }

    public String getAttentionGoods() {
        return attentionGoods;
    }

    public void setAttentionGoods(String attentionGoods) {
        this.attentionGoods = attentionGoods;
    }
}