package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/2 0002.
 */
public class ProfileBean implements Serializable {
    public String code;
//    public ArrayList<ProfileData> data;
    public ProfileData data;
    public class ProfileData implements Serializable {
        public String userid;// 用户ID
        public String face;// 头像
        public String alias;// 用户昵称
        public String name;// 真实姓名
        public String about;// 个人简介
        public String city;// 所在城市
        public String company;// 工作单位
        public String feature;// 个人特长
//       "绑定其他平台"
    }
}
