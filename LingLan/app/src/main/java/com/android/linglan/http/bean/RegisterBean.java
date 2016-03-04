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
        public String phone;// 电话
        public String code;// 验证码
        public String username;// 用户名字
        public String pwd;// 用户登录密码
        public String status;// 状态
        public String isadmin;// 是否是超级用户
        public String roleids;//
        public String admin_roleids;//
        public String addtime;// 添加时间
        public String login_time;// 登录时间
        public String token;// token

        @Override
        public String toString() {
            return "RegisterData{" +
                    "userid='" + userid + '\'' +
                    ", phone='" + phone + '\'' +
                    ", code='" + code + '\'' +
                    ", username='" + username + '\'' +
                    ", pwd='" + pwd + '\'' +
                    ", status='" + status + '\'' +
                    ", isadmin='" + isadmin + '\'' +
                    ", roleids='" + roleids + '\'' +
                    ", admin_roleids='" + admin_roleids + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", login_time='" + login_time + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }
}
