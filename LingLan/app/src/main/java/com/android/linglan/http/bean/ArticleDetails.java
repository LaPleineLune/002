package com.android.linglan.http.bean;

import java.io.Serializable;

/**
 * Created by qingciwu on 2016/1/19 0019.
 */
public class ArticleDetails implements Serializable {
    public String favouriscancel;// 是否点赞
    public String collectiscancel;// 是否收藏
    public ArticleData data;
    public class ArticleData implements Serializable {
        public String articleid;// 文章ID（必须）
        public String title;// 标题（必须）
        public String origin_title;// 原标题
        public String authorids;// 作者ID
        public String authornames;// 作者
        public String editorids;// 编辑ID
        public String editornames;// 编辑名称
        public String proofids;// 校对ID
        public String proofnames;// 校对名称
        public String publisherid;// 发布者ID
        public String publishername;// 发布者名称
        public String from;// 来源
        public String cateid;// 分类ID
        public String catename;// 分类名称
        public String sub_cateids;// 副分类ID
        public String sub_catenames;// 副分类名称
        public String tags;// 标签
        public String photo;// logo
        public String copyright;// 版权声明
        public String digest;// 摘要
        public String content;// 内容
        public String view_level;// 查看级别
        public String isaudit;// 是否审核
        public String isrecommend;// 是否推荐
        public String ispublish;// 是否发布
        public String count_comment;// 评论次数
        public String count_favour;// 点赞次数
        public String count_collect;// 收藏次数
        public String count_view;// 查看次数
        public String status;// 状态
        public String publishtime;// 发布时间
        public String addtime;// 创建时间
        public String forum;// 期数

        @Override
        public String toString() {
            return "ArticleData{" +
                    "articleid='" + articleid + '\'' +
                    ", title='" + title + '\'' +
                    ", origin_title='" + origin_title + '\'' +
                    ", authorids='" + authorids + '\'' +
                    ", authornames='" + authornames + '\'' +
                    ", editorids='" + editorids + '\'' +
                    ", editornames='" + editornames + '\'' +
                    ", proofids='" + proofids + '\'' +
                    ", proofnames='" + proofnames + '\'' +
                    ", publisherid='" + publisherid + '\'' +
                    ", publishername='" + publishername + '\'' +
                    ", from='" + from + '\'' +
                    ", cateid='" + cateid + '\'' +
                    ", catename='" + catename + '\'' +
                    ", sub_cateids='" + sub_cateids + '\'' +
                    ", sub_catenames='" + sub_catenames + '\'' +
                    ", tags='" + tags + '\'' +
                    ", photo='" + photo + '\'' +
                    ", copyright='" + copyright + '\'' +
                    ", digest='" + digest + '\'' +
                    ", content='" + content + '\'' +
                    ", view_level='" + view_level + '\'' +
                    ", isaudit='" + isaudit + '\'' +
                    ", isrecommend='" + isrecommend + '\'' +
                    ", ispublish='" + ispublish + '\'' +
                    ", count_comment='" + count_comment + '\'' +
                    ", count_favour='" + count_favour + '\'' +
                    ", count_collect='" + count_collect + '\'' +
                    ", count_view='" + count_view + '\'' +
                    ", status='" + status + '\'' +
                    ", publishtime='" + publishtime + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", forum='" + forum + '\'' +
                    '}';
        }
    }
}
