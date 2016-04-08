package com.android.linglan.http;

import com.android.linglan.utils.AESCryptUtil;
import com.android.linglan.utils.SharedPreferencesUtil;

/**
 * Created by changyizhang on 12/10/14.
 */
public class Constants {

    /**
     * 控制log的打印
     */
    public static boolean isShow = true;

    /**
     * App密钥
     */
    public static String SECRET = "c!$X?Ss552:3`;w$-60^}h-,stz9Tu|zd+&:hIjyG#bFdxk(YN";

    /**
     * 加密
     */
    public static String SEED_16_CHARACTER = "d.7r+[6D,4[]2cQ?|!>xE6d#{]3v%d{K@q#pVbU*Vm/SRDYsIl";

    /**
     * App版本号
     */
    public static String VERSION = SharedPreferencesUtil.getString("version", "1.0.0");// 哪一版本返哪一个版本号

    //    public static final String WEICHAT_APP_ID = "wx4087d127be50227f"; // 微信(测试)
//    public static final String WEICHAT_APP_KEY = "bdeb22c7c0fe763808ccd468fb72a84a"; // 微信（测试）
    public static final String WEICHAT_APP_ID = "wxa5932d4ba421bd4c"; // 微信（正式）
    public static final String WEICHAT_APP_KEY = "388390da3f578df26b5274c0cde36954"; // 微信（正式）

    public static final String FONT_SIZE = "fontsize"; // 字体大小
    public static final int HOME = 0;// 首页的搜索
    public static final int ALLSUBJECT = 1;// 全部专题页的搜索
    public static final int ALLARTICLE = 2;// 全部文章页的搜索
    public static final String ARTICLE = "0";// 0文章
    public static final String SUBJECT = "4";// 4专题

    //    public static final String SERVER = "http://192.168.1.117:8082";//测试服务亲
//    public static final String SERVER = "http://101.200.122.79";// 外网测试服务器
//    public static final String SERVER = "http://101.200.122.79:8081";// 外网测试服务器
    public static final String SERVER = "http://syh.zhongyishuyou.com";// 外网测试服务器
//    public static final String SERVER = "http://app.zhongyishuyou.com";// 正式服务器

    /**
     * 获取AppKey
     */
    public static final String URL_APP_KEY = Constants.SERVER + "/Api/Sys/appkey?secret=%s&device=%s&version=%s&mac=%s";

    /**
     * 用户登录
     */
    public static final String USER_LOGIN = Constants.SERVER + "/Api/User/applogin";

    /**
     * 退出登录
     */
    public static final String URL_USER_EXIT = Constants.SERVER + "/Api/User/userexit?appkey=%s&token=%s";

    /**
     * 首页推荐
     */
    public static final String URL_HOME_RECOMMEND = Constants.SERVER + "/Api/Index/recommend?appkey=%s&token=%s&page=%s";

    /**
     * 推荐文章
     */
    public static final String URL_RECOMMEND_ARTICLE = Constants.SERVER + "/Api/Index/recommend?token=";

    /**
     * 全部文章分类
     */
    public static final String URL_ALL_ARTICLE_CLASSIFY = Constants.SERVER + "/Api/Article/catelist?appkey=%s&token=%s";

    /**
     * 全部文章分类下的文章列表
     */
    public static final String URL_ALL_ARTICLE_CLASSIFY_List = Constants.SERVER + "/Api/Article/catearti?appkey=%s&token=%s&cateid=%s&page=%s&order=%s";

    /**
     * 文章详情
     */
    public static final String URL_DETAILS_ARTICLE = Constants.SERVER + "/Api/Article/articleinfo?appkey=%s&token=%s&articleid=%s";

    /**
     * 文章详情用户写（添加）笔记
     */
    public static final String URL_DETAILS_ARTICLE_NOTE_WRITE = Constants.SERVER + "/Api/ArticleNotes/addArticleNotes?appkey=%s&token=%s&articleid=%s&notescontent=%s";

    /**
     * 文章详情用户预览（获取）笔记
     */
    public static final String URL_DETAILS_ARTICLE_NOTE_PREVIEW = Constants.SERVER + "/Api/ArticleNotes/getArticleNotes?token=%s&articleid=%s";

    /**
     * 文章点赞
     */
    public static final String URL_DETAILS_ARTICLE_LIKE = Constants.SERVER + "/Api/Article/articlefavour?appkey=%s&token=%s&articleid=%s&iscancel=%s";

    /**
     * 文章收藏
     */
    public static final String URL_DETAILS_ARTICLE_COLLECT = Constants.SERVER + "/Api/Article/articlecollect?appkey=%s&token=%s&articleid=%s&iscancel=%s";

    /**
     * 全部专题
     */
    public static final String URL_ALL_SUBJECT = Constants.SERVER + "/Api/Special/speclist?appkey=%s&token=%s&page=%s&addtime=%s&cateid=%s";

    /**
     * 全部专题分类列表
     */
    public static final String URL_ALL_SUBJECT_CLASSIFY = Constants.SERVER + "/Api/Special/speccate?appkey=%s&token=%s";


    /**
     * 推荐专题
     */
    public static final String URL_RECOMMEND_SUBJECT = Constants.SERVER + "/Api/Special/recspeclist?token=";

    /**
     * 专题详情
     */
    public static final String URL_DETAILS_SUBJECT = Constants.SERVER + "/Api/Special/specailcontent?appkey=%s&token=%s&specialid=%s&page=%s";

    /**
     * 专题详情用户写（添加）笔记
     */
    public static final String URL_DETAILS_SUBJECT_NOTE_WRITE = Constants.SERVER + "/Api/ArticleNotes/addArticleNotes?token=";

    /**
     * 专题详情用户预览（获取）笔记
     */
    public static final String URL_DETAILS_SUBJECT_NOTE_PREVIEW = Constants.SERVER + "/Api/ArticleNotes/getArticleNotes?token=";

    /**
     * 专题点赞
     */
    public static final String URL_DETAILS_SUBJECT_LIKE = Constants.SERVER + "/Api/Special/specialfavour?appkey=%s&token=%s&specialid=%s&iscancel=%s";

    /**
     * 专题收藏
     */
    public static final String URL_DETAILS_SUBJECT_COLLECT = Constants.SERVER + "/Api/Special/specialcollect?appkey=%s&token=%s&specialid=%s&iscancel=%s";

    /**
     * 获取作者详情
     */
    public static final String URL_DETAILS_AUTHOR = Constants.SERVER + "/Api/publisher/publisherinfo?appkey=%s&token=%s&publisherid=%s";

    /**
     * 添加关注
     */
    public static final String URL_ADD_FOLLOW = Constants.SERVER + "/Api/publisher/addAttention?appkey=%s&token=%s&publisherid=%s";

    /**
     * 获取我的关注列表
     */
    public static final String URL_FOLLOWED_LIST = Constants.SERVER + "/Api/publisher/getAttentionPublisherlist?appkey=%s&token=%s&order=%s";

    /**
     * 获取我的收藏-文章
     */
    public static final String URL_COLLECT_ARTICLE = Constants.SERVER + "/Api/Article/getColletArticle?appkey=%s&token=%s";

    /**
     * 获取我的收藏-专题
     */
    public static final String URL_COLLECT_SUBJECT = Constants.SERVER + "/Api/Special/getColletSpecial?appkey=%s&token=%s&page=%s";

    /**
     * 获取我的收藏-全部
     */
    public static final String URL_COLLECT_ALL = Constants.SERVER + "/Api/User/collections?appkey=%s&token=%s&page=%s";

    /**
     * 意见反馈
     */
    public static final String URL_SEND_FEEDBACK = Constants.SERVER + "/Api/Sys/suggestion?appkey=%s&token=%s&content=%s&email=%s";

    /**
     * 获取关于我们信息
     */
    public static final String URL_ABOUT = Constants.SERVER + "/Api/Sys/about?appkey=%s&token=%s";

    /**
     * 获取全局热门搜索/历史搜索（统计）2.19
     */
    public static final String URL_STATISTIC = Constants.SERVER + "/Api/Index/statistic?appkey=%s&token=%s";

    /**
     * 全局搜索2.19
     */
    public static final String URL_SEARCH_ALL = Constants.SERVER + "/Api/Index/searchall?appkey=%s&token=%s&key=%s&page=%s";

    /**
     * 清除全局历史搜索
     */
    public static final String URL_CLEAR_HISTORY = Constants.SERVER + "/Api/Index/clearhistory?appkey=%s&token=%s";

    /**
     * 获取专题热门搜索/历史搜索（统计）
     */
    public static final String URL_SUBJECT_STATISTIC = Constants.SERVER + "/Api/Special/statistic?appkey=%s&token=%s";

    /**
     * 专题搜索
     */
    public static final String URL_SEARCH_SUBJECT = Constants.SERVER + "/Api/Special/searchkey?appkey=%s&token=%s&key=%s&page=%s";

    /**
     * 获取文章热门搜索/历史搜索（统计）
     */
    public static final String URL_ARTICLE_STATISTIC = Constants.SERVER + "/Api/Article/statistic?token=%s";

    /**
     * 文章搜索
     */
    public static final String URL_SEARCH_ARTICLE = Constants.SERVER + "/Api/Article/searchkey?appkey=%s&token=%s&key=%s&page=%s";

    /**
     * 文章分类频道排序修改
     */
    public static final String URL_ARTICLE_ORDER_EDIT = Constants.SERVER + "/Api/Article/cateorder?token=";

    /**
     * 用户上传头像
     */
    public static final String URL_USER_PHOTO_UPLOAD = Constants.SERVER + "/Api/User/uploadphoto";

    /**
     * 获取验证码
     */
    public static final String URL_USER_CODE = Constants.SERVER + "/Api/User/code?appkey=%s&token=%s&phone=%s";

    /**
     * 发布者发布的文章列表
     */
    public static final String URL_PUBLISHER_ARTIITEM = Constants.SERVER + "/Api/Publisher/publisherartiitem?token=%s&publisherid=%s";

    /**
     * 用户信息
     */
    public static final String URL_USER_INFO = Constants.SERVER + "/Api/User/userinfo?appkey=%s&token=%s";

    /**
     * 用户信息修改
     */
    public static final String URL_USER_INFO_EDIT = Constants.SERVER + "/Api/User/edituser";

    /**
     * 检查app版本更新
     */
    public static final String URL_CHECK_UPDATE = Constants.SERVER + "/Api/Sys/checkupdate?tag=%s&appkey=%s&token=%s&version=%s";

    /**
     * 下载app
     */
    public static final String URL_APP_DOWNLOAD = Constants.SERVER + "/Api/Sys/download?tag=" + new AESCryptUtil().encrypt("android");

    /**
     * 服务条款
     */
    public static final String URL_APP_AGREEMENT = Constants.SERVER + "/Api/Sys/agreement?appkey=%s&token=%s";

    /**
     * 城市列表
     */
    public static final String URL_CITY = Constants.SERVER + "/Api/Sys/city?token=";

    /**
     * 地区列表
     */
    public static final String URL_AREA = Constants.SERVER + "/Api/Sys/area?token=";
}
