package com.android.linglan.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class RecommendSubjects implements Serializable{
public ArrayList<RecommendSubject> data;

    public static class RecommendSubject implements Serializable{
        public String specialid;//专题ID
        public String specialname;//专题名称
        public String description;//描述
        public String logo;// logo（小图）
        public String photo;// photo(详情)
        public String updatetime;//更新时间
        public String addtime;//创建时间
        public String orderid;//排序
        public String content_title;// 最新添加的文章标题
        public String isnew;// 是否是今日更新

        @Override
        public String toString() {
            return "RecommendSubject{" +
                    "specialid='" + specialid + '\'' +
                    ", specialname='" + specialname + '\'' +
                    ", description='" + description + '\'' +
                    ", logo='" + logo + '\'' +
                    ", photo='" + photo + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", orderid='" + orderid + '\'' +
                    '}';
        }
    }
}
