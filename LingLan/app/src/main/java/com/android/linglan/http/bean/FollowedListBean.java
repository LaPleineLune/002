package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19 0019.
 */
public class FollowedListBean implements Serializable {
    public ArrayList<FollowedListData> data;
    public static class FollowedListData implements Serializable {
        public String userid;// 关注作者id
        public String name;// 关注作者姓名
        public String about;// 关注作者简介
    }
}
