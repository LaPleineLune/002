package com.android.linglan.http;

import android.text.TextUtils;


import com.android.linglan.utils.AESCryptUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.loopj.android.http.RequestParams;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by changyizhang on 12/15/14.
 */
public class NetApi {
    public static AESCryptUtil aesCryptUtil = new AESCryptUtil();
    public static String getAppKey() {
        return SharedPreferencesUtil.getString("appkey", null);
    }

    public static String getToken() {
        return SharedPreferencesUtil.getString("token", null);
    }

//    public static String getToken() {
//        return (SharedPreferencesUtil.getString("token", null) != null) ? // token 的值为null 吗？
//                SharedPreferencesUtil.getString("token", null) : // 不为空则为token 的值
//                SharedPreferencesUtil.getString("visittoken", null);// 为空则为 visittoken 的值
//    }

    /**
     * 获取AppKey
     * secret   密钥（必须）
     * device   设备类型ios、android（必须）
     * version   版本号（必须）
     * @param callback
     * @param mac   物理地址ID
     */
    public static void getAppKey(PasserbyClient.HttpCallback callback, String mac, String mode) {
        String url = String.format(Constants.URL_APP_KEY, aesCryptUtil.encrypt(Constants.SECRET), aesCryptUtil.encrypt("android"), aesCryptUtil.encrypt(Constants.VERSION), aesCryptUtil.encrypt(mac), aesCryptUtil.encrypt(mode));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户获得验证码
     * @param callback
     * @param phone 手机号
     */
    public static void getUserCode(PasserbyClient.HttpCallback callback,String phone) {
        String url = String.format(Constants.URL_USER_CODE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(phone));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户登录
     * @param phone 手机号(post)（和username二选一）
     * @param code  验证码(post)
     * @param username  用户名(post)(和phone 二选一）
     * @param pwd   密码(post)
     * @param callback
     */
    public static void getRegisterCheckCode(PasserbyClient.HttpCallback callback, String phone, String code, String username, String pwd) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));

        if (!TextUtils.isEmpty(phone)) {
            params.put("phone", aesCryptUtil.encryptPost(phone));

            params.put("code", aesCryptUtil.encryptPost(code));
        }
        if (!TextUtils.isEmpty(username)) {
            params.put("username", aesCryptUtil.encryptPost(username));
            params.put("pwd", aesCryptUtil.encryptPost(pwd));
        }
        PasserbyClient.post(Constants.USER_LOGIN, params, callback);
//        String url = String.format(Constants.USER_LOGIN) + code +"&phone=" + registerPhone;
//        PasserbyClient.get(url, callback);
    }

    /**
     * 用户退出登录
     * @param callback
     */
    public static void getUserExit(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_USER_EXIT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 首页推荐
     * @param callback
     * @param page  页码
     */
    public static void getHomeRecommend(PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_HOME_RECOMMEND, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 推荐文章
     * http://192.168.1.117:8082/Api/Index/recommend?page=1
     * */
    public static void getRecommendArticle( PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_RECOMMEND_ARTICLE) + aesCryptUtil.encrypt(getToken()) + "&page=" + aesCryptUtil.encrypt(page);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部文章分类
     * @param callback
     */
    public static void getAllArticleClassify( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ALL_ARTICLE_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部文章分类下的文章列表
     * @param callback
     * @param cateid  分类id
     * @param page  页码
     * @param order  排序方式('addtime按时间排序' ,'count_view按统计排序')
     */
    public static void getAllArticleClassifyList( PasserbyClient.HttpCallback callback, String cateid, String page, String order) {
        String url = String.format(Constants.URL_ALL_ARTICLE_CLASSIFY_List, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(cateid), aesCryptUtil.encrypt(page), aesCryptUtil.encrypt(order));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章详情
     * 192.168.1.117:8082/Api/Article/articleInfo?articleid=1769
     * */
    public static void getDetailsArticle( PasserbyClient.HttpCallback callback,String articleid) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章详情用户写（添加）笔记
     * @param callback
     * @param articleid 文章id（必须）
     * @param notescontent  笔记内容(必须)
     */
    public static void addDetailsArticleNote( PasserbyClient.HttpCallback callback,String articleid, String notescontent) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_NOTE_WRITE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid), aesCryptUtil.encrypt(notescontent));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /* 未接 */
    /**
     * 文章详情用户预览（获取）笔记
     * @param callback
     * @param articleid 文章id（必须）
     */
    public static void getDetailsArticleNotePreview( PasserbyClient.HttpCallback callback,String articleid) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_NOTE_PREVIEW, aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章点赞
     * @param callback
     * @param articleid 文章id（必须）
     * @param iscancel 是否取消点赞，0为点赞，1为取消
     */
    public static void getDetailsArticleLike( PasserbyClient.HttpCallback callback,String articleid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_LIKE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章收藏
     * @param callback
     * @param articleid 文章id（必须）
     * @param iscancel 是否取消收藏，0为收藏，1为取消
     */
    public static void getDetailsArticleCollect( PasserbyClient.HttpCallback callback,String articleid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_COLLECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部专题
     * http://192.168.1.117:8082/Api/Special/speclist?page=1&order=addtime&cateid=null
     * */
    public static void getAllSubject( PasserbyClient.HttpCallback callback, String page, String addtime, String cateid) {
        String url = String.format(Constants.URL_ALL_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page), aesCryptUtil.encrypt(addtime), aesCryptUtil.encrypt(cateid));
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 全部专题分类列表
     * @param callback
     */
    public static void getAllSubjectClassifyList( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ALL_SUBJECT_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }


    /**
     * 推荐专题
     * http://192.168.1.117:8082/Api/Special/recspeclist
     * */
    public static void getRecommendSubject( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_RECOMMEND_SUBJECT) + aesCryptUtil.encrypt(getToken());
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 专题详情文章列表
     * 192.168.1.117:8082/Api/Special/specailcontent?specialid=1
     * */
    public static void getDetailsSubjectArticle( PasserbyClient.HttpCallback callback,String specialid,String page) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid), aesCryptUtil.encrypt(page));
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 专题详情音频列表
     * 192.168.1.117:8082/Api/Special/specailcontent?specialid=1
     * */
    public static void getDetailsFmArticle( PasserbyClient.HttpCallback callback,String specialid,String page) {
        String url = String.format(Constants.URL_DETAILS_FM_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid), aesCryptUtil.encrypt(page));
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 专题详情
     * 192.168.1.117:8082/Api/Special/specailcontent?specialid=1
     * */
    public static void getDetailsSubject( PasserbyClient.HttpCallback callback,String specialid) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid));
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }


    /**
     * 专题详情用户写（添加）笔记
     * @param callback
     * @param specialid 专题id（必须）
     * @param notescontent  笔记内容(必须)
     */
    public static void addDetailsSubjectNote( PasserbyClient.HttpCallback callback,String specialid, String notescontent) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_NOTE_WRITE) + aesCryptUtil.encrypt(getToken()) + "&specialid=" + aesCryptUtil.encrypt(specialid) +"&notescontent=" + aesCryptUtil.encrypt(notescontent);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /* 未接 */
    /**
     * 专题详情用户预览（获取）笔记
     * @param callback
     * @param specialid 专题id（必须）
     */
    public static void getDetailsSubjectNotePreview( PasserbyClient.HttpCallback callback,String specialid) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_NOTE_PREVIEW) + aesCryptUtil.encrypt(getToken()) + "&specialid=" + aesCryptUtil.encrypt(specialid);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 专题点赞
     * @param callback
     * @param specialid 文章id（必须）
     * @param iscancel 是否取消点赞，0为点赞，1为取消
     */
    public static void getDetailsSubjectLike( PasserbyClient.HttpCallback callback, String specialid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_LIKE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 专题收藏
     * @param callback
     * @param specialid 专题id（必须）
     * @param iscancel 是否取消收藏，0为收藏，1为取消
     */
    public static void getDetailsSubjectCollect(PasserbyClient.HttpCallback callback, String specialid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_COLLECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取作者详情
     * @param callback
     * @param publisherid   作者（发布者）id(必须)
     */
    public static void getDetailsAuthor(PasserbyClient.HttpCallback callback, String publisherid) {
        String url = String.format(Constants.URL_DETAILS_AUTHOR, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(publisherid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 添加关注
     * @param callback
     * @param publisherid   发布者id(必须)
     */
    public static void addFollow(PasserbyClient.HttpCallback callback, String publisherid) {
        String url = String.format(Constants.URL_ADD_FOLLOW, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(publisherid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的关注列表
     * @param callback
     * @param order   排序方式('publishtime按发布时间排序')
     */
    public static void getFollowedList(PasserbyClient.HttpCallback callback, String order) {
        String url = String.format(Constants.URL_FOLLOWED_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(order));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-文章
     * @param callback
     */
    public static void getCollectArticle(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_COLLECT_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-专题
     * @param callback
     * @param page 页码
     */
    public static void getCollectSubject(PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_COLLECT_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-混合排序全部
     * @param callback
     */
    public static void getCollectAll(PasserbyClient.HttpCallback callback,String page) {
        String url = String.format(Constants.URL_COLLECT_ALL, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }


    /**
     * 意见反馈
     * @param callback
     * @param content 反馈的内容(必须)
     */
    public static void sendFeedback(PasserbyClient.HttpCallback callback, String content, String email) {
        String url = String.format(Constants.URL_SEND_FEEDBACK, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(content), aesCryptUtil.encrypt(email));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取关于我们信息
     * @param callback
     */
    public static void getAbout(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ABOUT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     *  http://101.200.122.79/Api/Search/statistic?token=9706b8e2961c725c199ac85e4e541ba9
     * 获取全局热门搜索/历史搜索（统计）
     * @param callback
     */
    public static void getHistoryHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_STATISTIC, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索全局（统计）
     * @param callback
     * @param key 关键字
     */
    public static void getSearchAll(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_ALL, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索全局新接口（统计）
     * @param callback
     * @param key 关键字
     * @param sourcepage  0文章 4专题 5电台 -1首页
     */
    public static void getNewSearchAll(PasserbyClient.HttpCallback callback,String key,String page,String sourcepage) {
        String url = String.format(Constants.URL_SEARCH_NEW_ALL, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page), aesCryptUtil.encrypt(sourcepage));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 清除全局历史搜索
     * @param callback
     */
    public static void clearHistory(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_CLEAR_HISTORY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取专题热门搜索
     * @param callback
     */
    public static void getSubjectHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_SUBJECT_HOT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取音频热门搜索
     * @param callback
     */
    public static void getFmHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_FM_HOT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索专题
     * @param callback
     * @param key 关键字
     */
    public static void getSearchSubject(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索音频
     * @param callback
     * @param key 关键字
     */
    public static void getSearchFm(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_FM, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取文章热门搜索
     * @param callback
     */
    public static void getArticleHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ARTICLE_HOT, aesCryptUtil.encrypt(getAppKey()),aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索文章
     * @param callback
     * @param key 关键字
     */
    public static void getSearchArticle(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 文章分类排序修改
     * @param callback
     * @param cateorder  排序修改后的分类  json类型的字符串
     */
    public static void geArticleOrderEdit(PasserbyClient.HttpCallback callback,String cateorder) {
        String url = String.format(Constants.URL_ARTICLE_ORDER_EDIT) + aesCryptUtil.encrypt(getToken()) + "&cateorder=" + aesCryptUtil.encrypt(cateorder);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取地区
     * @param callback
     */
    public static void geArea(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_AREA) + aesCryptUtil.encrypt(getToken());
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 发布者发布的文章列表
     * @param callback
     * @param publisherid
     */
    public static void getUserublisheridrtiitem(PasserbyClient.HttpCallback callback,String publisherid) {
        String url = String.format(Constants.URL_PUBLISHER_ARTIITEM, aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(publisherid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户信息
     * @param callback
     */
    public static void getUserInfo(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_USER_INFO, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户信息修改
     * @param callback
     * @param alias 用户昵称(post)
     * @param name  真实姓名(post)
     * @param about  个人简介(post)
     * @param city   所在城市(post)
     * @param company 所在单位(post)
     * @param feature 个人特征
     */
    public static void getUserInfoEdit(PasserbyClient.HttpCallback callback, String alias, String name, String about, String city,String company,String feature) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));
        params.put("token", aesCryptUtil.encryptPost(getToken()));

        if (alias != null) params.put("alias", aesCryptUtil.encryptPost(alias));
        if (name != null) params.put("name", aesCryptUtil.encryptPost(name));
        if (about != null) params.put("about", aesCryptUtil.encryptPost(about));
        if (city != null) params.put("city", aesCryptUtil.encryptPost(city));
        if (company != null) params.put("company", aesCryptUtil.encryptPost(company));
        if (feature != null) params.put("feature", aesCryptUtil.encryptPost(feature));
        PasserbyClient.post(Constants.URL_USER_INFO_EDIT, params, callback);
    }

    /**
     * 上传用户头像
     * @param callback
     * @param avatar  排序修改后的分类  json类型的字符串
     */
    public static void getUserPhotoUpdate(PasserbyClient.HttpCallback callback,File avatar) {
        String url = String.format(Constants.URL_USER_PHOTO_UPLOAD);
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));
        params.put("token", aesCryptUtil.encryptPost(getToken()));
        try {
            params.put("photo", avatar, "image/x-png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PasserbyClient.post(url, params, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 检查版本更新
     * @param callback
     * @param version
     */
    public static void getCheckUpdate(PasserbyClient.HttpCallback callback,String version) {
        String url = String.format(Constants.URL_CHECK_UPDATE, aesCryptUtil.encrypt("android"), aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(version));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 服务条款
     * @param callback
     */
    public static void getAppAgreement(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_APP_AGREEMENT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 新建病历
     * @param callback
     * @param patient   患者信息（json），例如：{"patientname":"","sex":"","ageyear":"",agemonth":"","phone":""}
     * @param course    病程（json），例如：[{"feature":"病症","visittime":"就诊时间","mediaids":"多个图片以逗号隔开","pathography":{"模板ID(没有模板，默认ID为1)":"内容"，......}}]
     */
    public static void clinicalCreate(PasserbyClient.HttpCallback callback, String patient, String course) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));
        params.put("token", aesCryptUtil.encryptPost(getToken()));

        params.put("patient", aesCryptUtil.encryptPost(patient));
        params.put("course", aesCryptUtil.encryptPost(course));
        PasserbyClient.post(Constants.URL_CLINICAL_CREATE, params, callback);
    }

    /**
     * 上传病历多媒体（图片，音频，视频）
     * @param callback
     * @param media 多媒体（图片，音频，视频）流
     * @param type  多媒体类型 0图片 1音频 2视频，不传默认为0图片
     * @param categoryid    分类ID
     */
    public static void saveClinicalMultiMedia(PasserbyClient.HttpCallback callback, File media, String type, String categoryid) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));
        params.put("token", aesCryptUtil.encryptPost(getToken()));

        try {
            params.put("media", media, "image/x-png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("type", aesCryptUtil.encryptPost(type));
        params.put("categoryid", aesCryptUtil.encryptPost(categoryid));
        PasserbyClient.post(Constants.URL_SAVE_CLINICAL_MULTI_MEDIA, params, callback);
    }

    /**
     * 修改病历多媒体分类（图片，音频，视频）
     * @param callback
     * @param categoryid    分类ID
     * @param mediaid
     */
    public static void editClinicalMultiMedia(PasserbyClient.HttpCallback callback, String categoryid, String mediaid) {
        String url = String.format(Constants.URL_EDIT_CLINICAL_MULTI_MEDIA, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(categoryid), aesCryptUtil.encrypt(mediaid));
        PasserbyClient.get(url, callback);
    }

    /**
     * 删除病历多媒体（图片，音频，视频）
     * @param callback
     * @param mediaid
     */
    public static void deleteClinicalMultiMedia(PasserbyClient.HttpCallback callback, String mediaid) {
        String url = String.format(Constants.URL_DELETE_CLINICAL_MULTI_MEDIA, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(mediaid));
        PasserbyClient.get(url, callback);
    }

    /**
     * 最大上传图片数量（图片，音频，视频）
     * @param callback
     */
    public static void getMaxPictureNumber(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_MAX_PICTURE_NUMBER, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        PasserbyClient.get(url, callback);
    }
    /**
     * 图片对比
     * @param callback
     * @param category  分类（处方默认1、患处2、舌象3）
     * @param illnesscaseid  病历ID
     * @param page  分页，默认1
     */
    public static void getComparePicture(PasserbyClient.HttpCallback callback, String category, String illnesscaseid, String page) {
        String url = String.format(Constants.URL_COMPARE_PICTURE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(category), aesCryptUtil.encrypt(illnesscaseid), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 是否有未命名病历，以及未命名病历的个数
     * @param callback
     */
    public static void getClinicalCollatingNum(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_CLINICAL_COLLATING_NUM, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未命名病历列表
     * @param callback
     * @param sort  {firstvisittime:asc,patientname:desc}-------(asc升序 desc降序）（firstvisittime:首诊时间，patientname:患者姓名）
     * @param page  分页，默认1
     */
    public static void getClinicalCollatingList(PasserbyClient.HttpCallback callback, String sort, String page) {
        String url = String.format(Constants.URL_CLINICAL_COLLATING_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(sort), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 病历列表
     * @param callback
     * @param sort  {firstvisittime:asc,patientname:desc}-------(asc升序 desc降序）（firstvisittime:首诊时间，patientname:患者姓名）
     * @param page  分页，默认1
     * @param tag   标签
     */
    public static void getClinicalList(PasserbyClient.HttpCallback callback, String sort, String page, String tag,String patientname) {
        String url = String.format(Constants.URL_CLINICAL_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(sort), aesCryptUtil.encrypt(page), aesCryptUtil.encrypt(tag), aesCryptUtil.encrypt(patientname));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 病历详情-->所贴的分类
     * @param callback
     * @param illnesscaseid 病历ID
     */
    public static void getClinicalDetailsCalssify(PasserbyClient.HttpCallback callback, String illnesscaseid) {
        String url = String.format(Constants.URL_CLINICAL_DETAILS_CALSSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(illnesscaseid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 病历详情-->病人信息
     * @param callback
     * @param illnesscaseid 病历ID
     */
    public static void getPatientDetails(PasserbyClient.HttpCallback callback, String illnesscaseid) {
        String url = String.format(Constants.URL_PATIENT_DETAILS, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(illnesscaseid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 病历详情-->病程列表
     * @param callback
     * @param illnesscaseid 病历ID
     * @param sort  {visittime:asc}-------(asc升序 desc降序）（visittime:就诊时间）
     * @param page  页码
     */
    public static void getClinicalDetailsList(PasserbyClient.HttpCallback callback, String illnesscaseid, String sort, String page) {
        String url = String.format(Constants.URL_CLINICAL_DETAILS_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(illnesscaseid), aesCryptUtil.encrypt(sort), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }
    public static void addClassify2ClinicalDetails(PasserbyClient.HttpCallback callback, String illnesscaseid, String tagid) {
        String url = String.format(Constants.URL_ADD_CLINICAL_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(illnesscaseid), aesCryptUtil.encrypt(tagid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 编辑病人信息
     * @param callback
     * @param patientid 病人id
     * @param patientname   病人名字
     * @param sex   病人性别
     * @param ageyear   病人岁数
     * @param agemonth  病人出生个月
     * @param phone 病人手机
     */
    public static void savePatientDetails (PasserbyClient.HttpCallback callback, String patientid, String patientname, String sex, String ageyear, String agemonth, String phone) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));
        params.put("token", aesCryptUtil.encryptPost(getToken()));
        params.put("patientid", aesCryptUtil.encryptPost(patientid));
        params.put("patientname", aesCryptUtil.encryptPost(patientname));
        params.put("sex", aesCryptUtil.encryptPost(sex));
        params.put("ageyear", aesCryptUtil.encryptPost(ageyear));
        params.put("agemonth", aesCryptUtil.encryptPost(agemonth));
        params.put("phone", aesCryptUtil.encryptPost(phone));
        PasserbyClient.post(Constants.URL_PATIENT_DETAILS_EDIT, params, callback);
    }

    /**
     * 编辑病程
     * @param callback
     * @param courseid 病程id
     * @param course   病程内容
     */
    public static void saveDiseaseCourseInfo(PasserbyClient.HttpCallback callback, String courseid, String course) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));
        params.put("token", aesCryptUtil.encryptPost(getToken()));
        params.put("courseid", aesCryptUtil.encryptPost(courseid));
        params.put("course", aesCryptUtil.encryptPost(course));
        PasserbyClient.post(Constants.URL_COURSE_DETAILS_EDIT, params, callback);
    }

    /**
     * 添加病程
     * @param callback
     * @param illnesscaseid 病程id
     * @param course   病程内容
     */
    public static void addDiseaseCourseInfo(PasserbyClient.HttpCallback callback, String illnesscaseid, String course) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encryptPost(getAppKey()));
        params.put("token", aesCryptUtil.encryptPost(getToken()));
        params.put("illnesscaseid", aesCryptUtil.encryptPost(illnesscaseid));
        params.put("course", aesCryptUtil.encryptPost(course));
        PasserbyClient.post(Constants.URL_CLINICAL_COURSE_ADD, params, callback);
    }

    /**
     * 删除病历
     * @param callback
     * @param illnesscaseid 病历ID
     */
    public static void clinicalDelete(PasserbyClient.HttpCallback callback, String illnesscaseid) {
        String url = String.format(Constants.URL_CLINICAL_DELETE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(illnesscaseid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 删除病程
     * @param callback
     * @param courseid  病程id
     */
    public static void clinicalCourseDelete(PasserbyClient.HttpCallback callback, String courseid) {
        String url = String.format(Constants.URL_CLINICAL_COURSE_DELETE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(courseid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }


    /**
     * 获取全部模板
     * @param callback
     */
    public static void getClinicalAllMould(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_CLINICAL_ALL_MOULD, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取个人模板
     * @param callback
     */
    public static void getClinicalPersonMould(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_CLINICAL_PERSON_MOULD, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 保存个人病历模板
     * @param callback
     * @param templet   {"templetname":"主诉,现病史,诊断"}
     */
    public static void saveClinicalMould(PasserbyClient.HttpCallback callback, String templet) {
        String url = String.format(Constants.URL_SAVE_CLINICAL_MOULD, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(templet));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取分类
     * @param callback
     */
    public static void getClinicalClassify(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_CLINICAL_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取分类下的病历
     * @param callback
     * @param classifyid 分类id（必须），以逗号相连接 eg：'1,2,3'
     * @param page  分页
     */
    public static void getClassifyClinical(PasserbyClient.HttpCallback callback, String classifyid, String page) {
        String url = String.format(Constants.URL_CLASSIFY_CLINICAL, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(classifyid), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取分类下的病例个数
     * @param callback
     * @param classifyid    tagid（必须）='1,2,3'
     */
    public static void getClassifyClinicalNum(PasserbyClient.HttpCallback callback, String classifyid) {
        String url = String.format(Constants.URL_CLASSIFY_CLINICAL_NUM, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(classifyid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 新建分类
     * @param callback
     * @param tagname   分类名字，必须
     */
    public static void getClinicalCreateClassify(PasserbyClient.HttpCallback callback, String tagname) {
        String url = String.format(Constants.URL_CLINICAL_CREATE_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(tagname));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 删除分类
     * @param callback
     * @param tagid 分类id
     */
    public static void getClinicalDeleteClassify(PasserbyClient.HttpCallback callback, String tagid) {
        String url = String.format(Constants.URL_CLINICAL_DELETE_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(tagid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 编辑（改）分类
     * @param callback
     * @param tagid 分类id
     * @param tagname   分类名称
     */
    public static void getClinicalEditClassify(PasserbyClient.HttpCallback callback, String tagid, String tagname) {
        String url = String.format(Constants.URL_CLINICAL_EDIT_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(tagid), aesCryptUtil.encrypt(tagname));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 搜索
     * @param callback
     * @param key 分类id
     * @param page   分类名称
     */
    public static void getClinicalIllnesscaseearch(PasserbyClient.HttpCallback callback, String key, String page) {
        String url = String.format(Constants.URL_CLINICAL_ILLNESSCASE_SEARCH, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 临证参考
     * @param callback
     * @param page
     */
    public static void getClinicalReference(PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_CLINICAL_REFERENCE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 中医搜搜——关键字搜索
     * @param callback
     * @param key   搜索关键字
     * @param page  页码
     */
    public static void getTCMSearchKey(PasserbyClient.HttpCallback callback, String key, String page) {
        String url = String.format(Constants.URL_TCM_SEARCH_KEY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 中医搜搜详情
     * @param callback
     * @param key   关键字（必须）
     * @param page  上个页面的page
     * @param itemid    条目ID(必须），说明：上一页面列表的ID
     */
    public static void getTCMSearchDetails(PasserbyClient.HttpCallback callback, String key, String page, String itemid) {
        String url = String.format(Constants.URL_TCM_SEARCH_DETAILS, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page), aesCryptUtil.encrypt(itemid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未命名病例关联病人信息
     * @param callback
     * @param illnesscaseid  上个页面的page
     * @param nonamecaseid    条目ID(必须），说明：上一页面列表的ID
     */
    public static void getRelatePatientInfo(PasserbyClient.HttpCallback callback, String illnesscaseid, String nonamecaseid) {
        String url = String.format(Constants.URL_RELATE_PATIENT_INFO, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(illnesscaseid), aesCryptUtil.encrypt(nonamecaseid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 标记已经点选其他设备登录标志   针对code值为7的情况
     * @param callback
     */
    public static void markotherlogin(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_MARKOTHER_LOGIN, aesCryptUtil.encrypt(getAppKey()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 从后台获取app的相关提示
     * @param callback
     * @param type  什么类型的提示文字，
     *              study_more学习模块更多提示（html格式），
     *              cr_info 临症参考说明（普通文本），
     *              cr_none 临症参考无内容说明，
     *              sousou_info 中医搜搜功能说明
     */
    public static void getPrompt(PasserbyClient.HttpCallback callback, String type) {
        String url = String.format(Constants.URL_PROMPT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(type));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部音频分类列表
     * @param callback
     */
    public static void getRadioClassify(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_RADIO_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 音频分类对应的列表
     * @param callback
     * @param cateid    音频分类id
     * @param page  分页
     */
    public static void getRadioList(PasserbyClient.HttpCallback callback, String cateid, String page) {
        String url = String.format(Constants.URL_RADIO_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(cateid), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 专辑详情(头部)
     * @param callback
     * @param albumid   专辑ID
     */
    public static void getRadioSpecialInfo(PasserbyClient.HttpCallback callback, String albumid) {
        String url = String.format(Constants.URL_RADIO_SPECIAL_INFO, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(albumid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 专辑详情（列表）
     * @param callback
     * @param albumid   专辑ID
     * @param page  分页
     */
    public static void getRadioSpecialList(PasserbyClient.HttpCallback callback, String albumid, String page) {
        String url = String.format(Constants.URL_RADIO_SPECIAL_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(albumid), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 专辑收藏
     * @param callback
     * @param albumid   专辑ID
     * @param iscancel  是否取消收藏，0为收藏，1为取消
     */
    public static void getRadioSpecialCollect(PasserbyClient.HttpCallback callback, String albumid, String iscancel) {
        String url = String.format(Constants.URL_RADIO_SPECIAL_COLLECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(albumid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 音频播放详情(头部)
     * @param callback
     * @param audioid   音频ID
     */
    public static void getRadioPlayInfo(PasserbyClient.HttpCallback callback, String audioid) {
        String url = String.format(Constants.URL_RADIO_PLAY_INFO, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(audioid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 音频播放详情（列表）
     * @param callback
     * @param audioid   音频ID
     * @param page  分页
     */
    public static void getRadioPlayList(PasserbyClient.HttpCallback callback, String audioid, String page) {
        String url = String.format(Constants.URL_RADIO_PLAY_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(audioid), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 音频收藏
     * @param callback
     * @param audioid   音频ID
     * @param iscancel  是否取消收藏，0为收藏，1为取消
     */
    public static void getRadioPlayCollect(PasserbyClient.HttpCallback callback, String audioid, String iscancel) {
        String url = String.format(Constants.URL_RADIO_PLAY_COLLECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(audioid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

}
