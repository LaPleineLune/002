package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/18 0018.
 */
public class RegisterBean implements Serializable {
    public String code;
    public String msg;
    public RegisterData data;
    public static class RegisterData implements Serializable {
        public String userid;// 用户id
        public String phone;// 手机号（登录用）
        public String username;// 用户名（登录用）
        public String status;// 状态
        public String isadmin;// 是否是超级用户
        public String rolenames;// 角色名称
        public String token;// token

        public String face;// 用户头像
        public String alias;// 用户昵称
        public String name;// 真实姓名
        public String about;// 用户简介
        public String city;// 所在地区
        public String company;// 所在公司
        public String feature;// 个人特征
        public String isapprove;// 是否是认证作者
        public String isfamilymember;// 是否是亲情会员 1是亲情会员  0不是亲情会员
        public String addtime;// 添加时间
        public String login_time;// 登录时间

    }
}
