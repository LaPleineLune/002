package com.android.linglan.http.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/20 0020.
 */
public class SubjectDetails {
    public String favouriscancel;// 是否点赞
    public String collectiscancel;// 是否收藏
    public ArrayList<SubjectData> data;

    public class SubjectData {
        public String specialid;// 专题id
        public String contentid;// 专题内容id
        public String logo;// logo（小图）
        public String photo;// photo(详情)
        public String content_title;// 内容标题（图书为书名）
        public String contenttype;// 内容类型 0 文章 1 医案 2 图书 3视频
        public String author;// 作者名称
        public String addtime;// 添加时间

        @Override
        public String toString() {
            return "SubjectData{" +
                    "specialid='" + specialid + '\'' +
                    ", contentid='" + contentid + '\'' +
                    ", content_title='" + content_title + '\'' +
                    ", contenttype='" + contenttype + '\'' +
                    ", author='" + author + '\'' +
                    ", addtime='" + addtime + '\'' +
                    '}';
        }
    }
}
