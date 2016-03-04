package com.android.linglan.http;

import android.text.TextUtils;


import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.loopj.android.http.RequestParams;


import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by changyizhang on 12/15/14.
 */
public class NetApi {
    public static String getToken() {
        return (SharedPreferencesUtil.getString("token", null) != null) ? // token 的值为null 吗？
                SharedPreferencesUtil.getString("token", null) : // 不为空则为token 的值
                SharedPreferencesUtil.getString("visittoken", null);// 为空则为 visittoken 的值
    }

    /**
     * 获取游客token
     * @param callback
     */
    public static void getVisitToken(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_VISIT_TOKEN);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户获得验证码
     * @param callback
     * @param phone 手机号
     */
    public static void getUserCode(PasserbyClient.HttpCallback callback,String phone) {
        String url = String.format(Constants.URL_USER_CODE) + getToken() + "&phone=" + phone;
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
//        params.put("token", token);
        if (!TextUtils.isEmpty(phone)) {
            params.put("phone", phone);
            params.put("code", code);
        }
        if (!TextUtils.isEmpty(username)) {
            params.put("username", username);
            params.put("pwd", pwd);
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
        String url = String.format(Constants.URL_USER_EXIT) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 首页推荐
     * @param callback
     * @param page  页码
     */
    public static void getHomeRecommend(PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_HOME_RECOMMEND) + getToken() + "&page=" + page;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 推荐文章
     * http://192.168.1.117:8082/Api/Index/recommend?page=1
     * */
    public static void getRecommendArticle( PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_RECOMMEND_ARTICLE) + getToken() + "&page=" + page;
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部文章分类
     * @param callback
     */
    public static void getAllArticleClassify( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ALL_ARTICLE_CLASSIFY) + getToken();
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
        String url = String.format(Constants.URL_ALL_ARTICLE_CLASSIFY_List) + getToken() + "&cateid=" + cateid +"&page=" + page + "&order=" + order;
//        RequestParams params = new RequestParams();
//        params.put("cateid", cateid);
//        params.put("page", page);
//        PasserbyClient.get(url, callback, params);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章详情
     * 192.168.1.117:8082/Api/Article/articleInfo?articleid=1769
     * */
    public static void getDetailsArticle( PasserbyClient.HttpCallback callback,String articleid) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE) + getToken() + "&articleid=" + articleid;
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
        String url = String.format(Constants.URL_DETAILS_ARTICLE_NOTE_WRITE) + getToken() + "&articleid=" + articleid + "&notescontent=" + notescontent;
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
        String url = String.format(Constants.URL_DETAILS_ARTICLE_NOTE_PREVIEW) + getToken() + "&articleid=" + articleid;
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
        String url = String.format(Constants.URL_DETAILS_ARTICLE_LIKE) + getToken() + "&articleid=" + articleid + "&iscancel=" + iscancel;
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
        String url = String.format(Constants.URL_DETAILS_ARTICLE_COLLECT) + getToken() + "&articleid=" + articleid + "&iscancel=" + iscancel;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部专题
     * http://192.168.1.117:8082/Api/Special/speclist?page=1&order=addtime&cateid=null
     * */
    public static void getAllSubject( PasserbyClient.HttpCallback callback, String page, String addtime, String cateid) {
        String url = String.format(Constants.URL_ALL_SUBJECT) + getToken() + "&page=" + page + "&addtime=" + addtime + "&cateid=" + cateid;
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 全部专题分类列表
     * @param callback
     */
    public static void getAllSubjectClassifyList( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ALL_SUBJECT_CLASSIFY) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }


    /**
     * 推荐专题
     * http://192.168.1.117:8082/Api/Special/recspeclist
     * */
    public static void getRecommendSubject( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_RECOMMEND_SUBJECT) + getToken();
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 专题详情
     * 192.168.1.117:8082/Api/Special/specailcontent?specialid=1
     * */
    public static void getDetailsSubject( PasserbyClient.HttpCallback callback,String specialid) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT) + getToken() + "&specialid=" + specialid;
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /* 未接 */
    /**
     * 专题详情用户写（添加）笔记
     * @param callback
     * @param specialid 专题id（必须）
     * @param notescontent  笔记内容(必须)
     */
    public static void addDetailsSubjectNote( PasserbyClient.HttpCallback callback,String specialid, String notescontent) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_NOTE_WRITE) + getToken() + "&specialid=" + specialid +"&notescontent=" + notescontent;
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
        String url = String.format(Constants.URL_DETAILS_SUBJECT_NOTE_PREVIEW) + getToken() + "&specialid=" + specialid;
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
        String url = String.format(Constants.URL_DETAILS_SUBJECT_LIKE) + getToken() + "&specialid=" + specialid + "&iscancel=" + iscancel;
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
        String url = String.format(Constants.URL_DETAILS_SUBJECT_COLLECT) + getToken() + "&specialid=" + specialid + "&iscancel=" + iscancel;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取作者详情
     * @param callback
     * @param publisherid   作者（发布者）id(必须)
     */
    public static void getDetailsAuthor(PasserbyClient.HttpCallback callback, String publisherid) {
        String url = String.format(Constants.URL_DETAILS_AUTHOR) + getToken() + "&publisherid=" + publisherid;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 添加关注
     * @param callback
     * @param publisherid   发布者id(必须)
     */
    public static void addFollow(PasserbyClient.HttpCallback callback, String publisherid) {
        String url = String.format(Constants.URL_ADD_FOLLOW) + getToken() + "&publisherid=" + publisherid;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的关注列表
     * @param callback
     * @param order   排序方式('publishtime按发布时间排序')
     */
    public static void getFollowedList(PasserbyClient.HttpCallback callback, String order) {
        String url = String.format(Constants.URL_FOLLOWED_LIST) + getToken() + "&order=" + order;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-文章
     * @param callback
     */
    public static void getCollectArticle(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_COLLECT_ARTICLE) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-专题
     * @param callback
     */
    public static void getCollectSubject(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_COLLECT_SUBJECT) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-混合排序全部
     * @param callback
     */
    public static void getCollectAll(PasserbyClient.HttpCallback callback,String page) {
        String url = String.format(Constants.URL_COLLECT_ALL) + getToken() + "&page=" + page;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }


    /**
     * 意见反馈
     * @param callback
     * @param content 反馈的内容(必须)
     */
    public static void sendFeedback(PasserbyClient.HttpCallback callback, String content, String email) {
        String url = String.format(Constants.URL_SEND_FEEDBACK) + getToken() +"&content=" + content + "&email=" + email;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取关于我们信息
     * @param callback
     */
    public static void getAbout(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ABOUT) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     *  http://101.200.122.79/Api/Search/statistic?token=9706b8e2961c725c199ac85e4e541ba9
     * 获取全局热门搜索/历史搜索（统计）
     * @param callback
     */
    public static void getHistoryHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_STATISTIC) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 关键字搜索全局（统计）
     * @param callback
     * @param key 关键字
     */
    public static void getSearchAll(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_ALL) + getToken() + "&key=" + key + "&page=" + page;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 获取专题热门搜索/历史搜索（统计）
     * @param callback
     */
    public static void getSubjectHistoryHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_SUBJECT_STATISTIC) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 关键字搜索专题
     * @param callback
     * @param key 关键字
     */
    public static void getSearchSubject(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_SUBJECT) + getToken() + "&key=" + key + "&page=" + page;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 获取文章热门搜索/历史搜索（统计）
     * @param callback
     */
    public static void getArticleHistoryHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ARTICLE_STATISTIC) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 关键字搜索文章
     * @param callback
     * @param key 关键字
     */
    public static void getSearchArticle(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_ARTICLE) + getToken() + "&key=" + key + "&page=" + page;
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
        String url = String.format(Constants.URL_ARTICLE_ORDER_EDIT) + getToken() + "&cateorder=" + cateorder;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试  未写入页面
     * 上传用户头像
     * @param callback
     * @param photo  排序修改后的分类  json类型的字符串
     */
    public static void getUserPhotoUpdate(PasserbyClient.HttpCallback callback,File photo,String type) {
        String url = String.format(Constants.URL_USER_PHOTO_UPLOAD) + getToken() + "&photo=" + photo+ "&type=" + type;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 文章取消收藏
     * @param callback
     * @param articleid  文章id
     */
    public static void getArticleCancleCollect(PasserbyClient.HttpCallback callback,String articleid) {
        String url = String.format(Constants.URL_ARTICLE_CANCE_COLLECT) + getToken() + "&articleid=" + articleid;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 专题取消收藏
     * @param callback
     * @param specialid  专题id
     */
    public static void getSubjectCancleCollect(PasserbyClient.HttpCallback callback,String specialid) {
        String url = String.format(Constants.URL_SUBJECT_CANCE_COLLECT) + getToken() + "&specialid=" + specialid;
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
        String url = String.format(Constants.URL_PUBLISHER_ARTIITEM) + getToken() + "&publisherid=" + publisherid;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户信息
     * @param callback
     */
    public static void getUserInfo(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_USER_INFO) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 用户信息修改
     * @param alias 用户昵称(post)
     * @param name  真实姓名(post)
     * @param about  个人简介(post)
     * @param city   所在城市(post)
     * @param company 所在单位(post)
     * @param feature 个人特征
     * @param callback
     */
    public static void getUserInfoEdit(PasserbyClient.HttpCallback callback, String alias, String name, String about, String city,String company,String feature) {
        RequestParams params = new RequestParams();
//        params.put("token", token);
//        if (!TextUtils.isEmpty(phone)) {
        params.put("alias", alias);
        params.put("name", name);
//        }
//        if (!TextUtils.isEmpty(username)) {
        params.put("about", about);
        params.put("city", city);
        params.put("company", company);
        params.put("feature", feature);
//        }
        PasserbyClient.post(Constants.URL_USER_INFO_EDIT + getToken(), params, callback);
//        String url = String.format(Constants.USER_LOGIN) + code +"&phone=" + registerPhone;
//        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试  写入了AppUpdaterUtil类
     * 检查版本更新
     * @param callback
     * @param version
     */
    public static void getCheckUpdate(PasserbyClient.HttpCallback callback,String version) {//  + "&tag=android"
        String url = String.format(Constants.URL_CHECK_UPDATE) + getToken() + "&version=" + version;
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试  写入了AppUpdaterUtil类
     * 下载app
     * @param callback
     */
    public static void getAppDownLoad(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_APP_DOWNLOAD);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 服务条款
     * @param callback
     */
    public static void getAppAgreement(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_APP_AGREEMENT) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 获取城市列表
     * @param callback
     */
    public static void getCity(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_CITY) + getToken();
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }





 /* public static void fetchEncryptCode(PasserbyClient.HttpCallback callback){
    RequestParams params = new RequestParams();
    String time = PasserbyClient.time;
    long currentTime = TimeUtil.getTimeSystemFormate();
    if (TextUtils.isEmpty(time)){
      time = String.valueOf(currentTime);
    }
    params.add("t", time);
    String data = (Long.parseLong(time) - 1) + "_"
            + GloableParams.versionName + "_"
            + Constants.SPCODE;
    String key = null;
    try {
      key = DesUtil.encode(Constants.ENCRYPT_KEY1, data).trim();
    } catch (Exception e) {
      e.printStackTrace();
    }
    params.add("k", key);
    params.add("token", SharedPreferencesUtil.getString("token", ""));
    PasserbyClient.post(Constants.URL_ENCRYPT_CODE, params, callback);
  }*/

 /* public static void fetchStartInfo(PasserbyClient.HttpCallback callback) {
    String url = Constants.URL_START_INFO + "&devid=" + GloableParams.deviceID + "&pcode="
        + Constants.SPCODE + "&version="
        + GloableParams.versionName;
    PasserbyClient.get(url, false, callback);
  }*/

/*  public static void loginByMobile(String mobile, String password,
      PasserbyClient.HttpCallback callback) {
    String sign = Md5Util.md5(
            "login=" + mobile + "&pass=" + password + "&version="
                    + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    RequestParams params = new RequestParams();
    params.put("login", mobile);
    params.put("sign", sign);
    params.put("pass", password);
    PasserbyClient.post(Constants.URL_LOGIN_BY_MOBILE, params, callback);
  }*/

 /* public static void uploadDeviceInfo() {
    String uid, regid, name, resolution, brand, model, os, osversion, imei, macaddress, devtoken, ext, sign, version;
    uid = SharedPreferencesUtil.getString("uid", "");
    regid = SharedPreferencesUtil.getString("regid", "");
    name = "";
    int accessType = NetworkUtil.getAccessType();
    int operation = NetworkUtil.getOperationType();
    resolution = GloableParams.winHeight + "x" + GloableParams.winWidth;
    brand = "";
    model = android.os.Build.MODEL.replace(" ", "");
    os = "android";
    osversion = android.os.Build.VERSION.RELEASE;
    imei = TelephonyUtil.getIMEI();
    macaddress = TelephonyUtil.getMacAddress();
    if (macaddress == null) {
      macaddress = "";
    }
    devtoken = "";
    ext = "";
    sign = Md5Util.md5(
        "accesstype=" + accessType + "&brand=" + brand + "&devid="
            + GloableParams.deviceID + "&devtoken=" + devtoken
            + "&imei=" + imei + "&macaddress=" + macaddress
            + "&model=" + model + "&name=" + name + "&operation=" + operation + "&os=" + os
            + "&osversion=" + osversion + "&resolution="
            + resolution + "&uid=" + uid
            + Constants.SSTARTINGUPKEY).toLowerCase();
    String url = Constants.URL_UPLOAD_DEVICE_INFO + "&sign="
        + sign + "&accesstype=" + accessType + "&operation=" + operation + "&brand=" + brand
        + "&devtoken=" + devtoken
        + "&imei=" + imei + "&macaddress=" + macaddress + "&model="
        + model + "&name=" + name + "&os=" + os + "&osversion="
        + osversion + "&resolution=" + resolution + "&uid=" + uid + "&regid=" + regid
        + "&ext=" + ext;

    PasserbyClient.get(url, null);
  }*/

/*  public static void getCouponDetail(String itemid, PasserbyClient.HttpCallback callback) {
    String url = String.format(Constants.URL_COUPON_DETAIL, itemid);
    PasserbyClient.get(url, callback);
  }*/

 /* public static void getMeCouponDetail(String itemid, PasserbyClient.HttpCallback callback) {
    String url = String.format(Constants.URL_ME_COUPON_DETAIL, itemid);
    PasserbyClient.get(url, callback);
  }
*/
 /* public static void getCouponState(String couponid, PasserbyClient.HttpCallback callback) {
    RequestParams params = new RequestParams();
    params.add("couponids", couponid);
    params.add("token", SharedPreferencesUtil
        .getString("token", ""));
    PasserbyClient.post(Constants.URL_HAVE_COUPON_USED_MORE, params, callback);
  }*/

/*  public static void getGetCoupon(String itemid, PasserbyClient.HttpCallback callback) {
    String url = Constants.URL_COUPON_GET;
    RequestParams params = new RequestParams();
    params.add("itemid", itemid);
    params.add("token", SharedPreferencesUtil
        .getString("token", ""));
    PasserbyClient.post(url, params, callback);
  }*/

  /*public static void getVoucherDetails(String id, PasserbyClient.HttpCallback callback) {
    String url = Constants.URL_VOUCHERDETAILS + id;
    PasserbyClient.get(url, callback);
  }*/

 /* public static void verifyToken(PasserbyClient.HttpCallback callback) {
    PasserbyClient.get(Constants.URL_VERIFY_TOKEN, callback);
  }*/

//  public static void getHomePoster(int page,
//      PasserbyClient.HttpCallback callback) {
//    PasserbyClient.get(Constants.URL_POSTER + page, callback);
//  }
//
//  public static void getHomeProjects(int page, PasserbyClient.HttpCallback callback) {
//    PasserbyClient.get(Constants.URL_PROJECT_CATEGORY + page, callback);
//  }

//  public static String getProjectWebUrl(String pid) {
//    return PasserbyClient.appendDefaultGetParams(String.format(Constants.URL_WEBVIEW, pid));
//  }

//    public static void getProjectDetail(String pid, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_DETAIL, callback, pid);
//    }
//
//    public static void getOrganizationList(String organizationId,
//                                           PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_ORGNIZATION_LIST, callback, organizationId);
//    }

 /* public static String getDonateUrl(int payCount) {
    return String.format(Constants.URL_DONATE, GloableParams.deviceID, Constants.SPCODE,
            GloableParams.versionName, payCount, Constants.MEHOD);
  }*/

//    public static String getDonateAgainUrl(String orderId)
//    {
//        return String.format(Constants.URL_DONATE_AGAIN, orderId,
//                SharedPreferencesUtil.getString("token", ""));
//    }

/*
  public static byte[] getDonatePostData(String projectId, int payMethod, int price) {
    return EncodingUtils.getBytes(PasserbyClient.appendDefaultGetParams(String.format(
            "pid=%s&method=%d&price=%d", projectId, payMethod, price)), "UTF-8");
  }
*/

/*  public static byte[] getPostToken() {
    return EncodingUtils.getAsciiBytes("token=" + SharedPreferencesUtil.getString("token", ""));
  }*/

//    public static void getSpecialListByCategoryId(String categoryId,
//                                                  PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_PROJECT_MORE + categoryId, callback);
//    }

//    public static void getSpecialListByCategoryId(String categoryId, int page,
//                                                  PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_PROJECT_MORE + categoryId + "&page=" + page, callback);
//    }

//    public static void getProjectListByTagId(String tagId, int page,
//                                             PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_PROJECT_LIST_TAG, callback, tagId, page);
//    }
//
//    public static void getProjectListBySearching(String keyword, int page,
//                                                 PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_PROJECT_SEARCH, callback, keyword, page);
//    }
//
//    public static void getPayResult(String pid, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_PAY_RESULT + pid, callback);
//    }
//
//    public static void followProject(String pid, PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("pid", pid);
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        PasserbyClient.post(Constants.URL_FOLLOW_PROJECT, params, callback);
//    }

 /* public static void snsBind(SHARE_MEDIA platform, Bundle snsParams, Map<String, Object> info,
      PasserbyClient.HttpCallback callback) {
    String accessToken = snsParams.getString("access_token");
    if (TextUtils.isEmpty(accessToken)) {
      accessToken = snsParams.getString("access_key");
    }
    String openId = (String) snsParams.get("openid");
    if (TextUtils.isEmpty(openId)) {
      openId = snsParams.getString("uid");
    }
    int platId = 0;
    switch (platform) {
      case SINA:
        platId = 1;
        break;
      case WEIXIN:
        platId = 2;
        break;
      case QQ:
        platId = 3;
        break;
    }
    String expiresIn = snsParams.getString("expires_in");
    String avatar = (String) info.get("profile_image_url");
    if (TextUtils.isEmpty(avatar)) {
      avatar = (String) info.get("headimgurl");
    }
    String sign = Md5Util.md5(
        "access_token=" + accessToken + "&expiretime=" + expiresIn + "&openid="
            + openId + Constants.STHREEKEY)
        .toLowerCase();
    RequestParams params = new RequestParams();
    params.put("token", SharedPreferencesUtil.getString("token", ""));
    params.put("platid", platId);
    params.put("access_token", accessToken);
    params.put("openid", openId);
    params.put("expiretime", expiresIn);
    params.put("sign", sign);
    params.put("headpath", avatar);
    PasserbyClient.post(Constants.URL_SNS_BIND, params, callback);
  }*/

    /* public static void snsLogin(SHARE_MEDIA platform, Bundle snsParams, Map<String, Object> info,
         PasserbyClient.HttpCallback callback) {
       String accessToken = snsParams.getString("access_token");
       if (TextUtils.isEmpty(accessToken)) {
         accessToken = snsParams.getString("access_key");
       }

       String openId = (String) snsParams.get("openid");
       if (TextUtils.isEmpty(openId)) {
         openId = snsParams.getString("uid");
       }
       String avatar = (String) info.get("profile_image_url");
       if (TextUtils.isEmpty(avatar)) {
         avatar = (String) info.get("headimgurl");
       }
       String expiresIn = snsParams.getString("expires_in");
       String sign = Md5Util.md5(
           "access_token=" + accessToken + "&expiretime=" + expiresIn + "&openid="
               + openId + Constants.STHREEKEY)
           .toLowerCase();
       int platId = 0;

       switch (platform) {
         case SINA:
           platId = 1;
           break;
         case WEIXIN:
           platId = 2;
           break;
         case QQ:
           platId = 3;
           break;
       }
       RequestParams params = new RequestParams();
       params.put("platid", platId);
       params.put("access_token", accessToken);
       params.put("openid", openId);
       params.put("expiretime", expiresIn);
       params.put("sign", sign);
       params.put("headpath", avatar);
       SharedPreferencesUtil.saveString("openId", openId);
       PasserbyClient.post(Constants.URL_SNS_LOGIN, params, callback);
     }
   */
//    public static void getMyCouponList(int page, int type,  PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(String.format(Constants.URL_MY_COUPON, String.valueOf(page), String.valueOf(type)), callback);
//    }
//
//    public static void getAllCouponList(int page, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_COUPON_ALL + page, callback);
//    }
//
//    public static void getHotCouponList(int page, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_COUPON_HOT + page, callback);
//    }
//
//    public static void getCouponBanner(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_COUPON_BANNER, callback);
//    }
//
//    public static void getProjectEvents(String pid, int page, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_PROJECT_EVENT, callback, pid, page);
//    }
//
//    public static void getMyAlums(int page, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_MY_ALBUM_LIST + page, callback);
//    }
//
//    public static void getMyAttentionCount(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_MY_ATTENTION_COUNT, callback);
//    }

  /*
   * public static void getMyUpdateStatus(PasserbyClient.HttpCallback callback) {
   * PasserbyClient.get(Constants.URL_MY_MESSAGE, callback);
   * }
   */

//    public static void getMoreLoveProjects(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_MORE_LOVE, callback);
//    }

 /* public static void getJPushResult(String regId, PasserbyClient.HttpCallback callback) {
    String uid, name, accesstype, resolution, brand, model, os, osversion, imei, macaddress, devtoken, ext, sign, version;
    uid = "0";
    name = "";
    accesstype = NetworkUtil.getNetworkTypeName(PasserbyApplication.getsApplicationContext());
    resolution = GloableParams.winHeight + "x" + GloableParams.winWidth;
    brand = "";
    model = android.os.Build.MODEL.replace(" ", "");
    os = "android";
    osversion = android.os.Build.VERSION.RELEASE;
    imei = TelephonyUtil.getIMEI();
    macaddress = TelephonyUtil.getMacAddress();
    if (macaddress == null) {
      macaddress = "";
    }
    devtoken = "";
    ext = "";
    sign = Md5Util.md5(
        "accesstype=" + accesstype + "&brand=" + brand + "&devid="
            + GloableParams.deviceID + "&devtoken=" + devtoken
            + "&imei=" + imei + "&macaddress=" + macaddress
            + "&model=" + model + "&name=" + name + "&os=" + os
            + "&osversion=" + osversion + "&resolution="
            + resolution + "&uid=" + uid
            + Constants.SSTARTINGUPKEY).toLowerCase();

    String uri = Constants.URL_UPLOAD_DEVICE_INFO + "&sign="
        + sign + "&accesstype=" + accesstype + "&brand=" + brand
        + "&devtoken=" + devtoken
        + "&imei=" + imei + "&macaddress=" + macaddress + "&model="
        + model + "&name=" + name + "&os=" + os + "&osversion="
        + osversion + "&resolution=" + resolution + "&uid=" + uid
        + "&ext=" + ext
        + "&regId=" + regId;
    RequestParams params = new RequestParams();

    PasserbyClient.post(uri, params, callback);
  }
*/
 /* public static void getRegisterCodeResult(String registerPhone,
      PasserbyClient.HttpCallback callback) {
    String category = "register";
    String sign = Md5Util.md5("login=" + registerPhone + "&version="
        + GloableParams.versionName + Constants.SUSERKEY).toLowerCase();
    String url = Constants.SERVER
        + "mobile/dynamic.php?mod=mob&ctl=user&act=sendCode"
        + "&login=" + registerPhone + "&sign=" + sign + "&category="
        + category;
    PasserbyClient.get(url, callback);
  }*/

 /* public static void getBindCodeResult(String registerPhone,
      PasserbyClient.HttpCallback callback) {
    String category = "bind";
    String sign = Md5Util.md5("login=" + registerPhone + "&version="
        + GloableParams.versionName + Constants.SUSERKEY).toLowerCase();
    String url = Constants.SERVER
        + "mobile/dynamic.php?mod=mob&ctl=user&act=sendCode"
        + "&login=" + registerPhone + "&sign=" + sign + "&category="
        + category;
    PasserbyClient.get(url, callback);
  }*/

  /*public static void changeBindingMobile(String registerPhone,
      PasserbyClient.HttpCallback callback) {
    String category = "modify";
    String sign = Md5Util.md5("login=" + registerPhone + "&version="
        + GloableParams.versionName + Constants.SUSERKEY).toLowerCase();
    String url = Constants.SERVER
        + "mobile/dynamic.php?mod=mob&ctl=user&act=sendCode"
        + "&login=" + registerPhone + "&sign=" + sign + "&category="
        + category;
    PasserbyClient.get(url, callback);
  }*/

 /* public static void forgetPasswordBySendingCode(String registerPhone,
      PasserbyClient.HttpCallback callback) {
    String category = "forget";
    String sign = Md5Util.md5("login=" + registerPhone + "&version="
        + GloableParams.versionName + Constants.SUSERKEY).toLowerCase();
    String url = Constants.SERVER
        + "mobile/dynamic.php?mod=mob&ctl=user&act=sendCode"
        + "&login=" + registerPhone + "&sign=" + sign + "&category="
        + category;
    PasserbyClient.get(url, callback);
  }*/

 /* public static void getRegisterCheckCode(String code, String registerPhone,
      PasserbyClient.HttpCallback callback) {
    String category = "register";
    String sign = Md5Util.md5(
        "login=" + registerPhone + "&version="
            + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    String url = Constants.SERVER
        + "mobile/dynamic.php?mod=mob&ctl=user&act=checkCode"
        + "&login=" + registerPhone
        + "&category=" + category
        + "&code=" + code
        + "&sign=" + sign;
    PasserbyClient.get(url, callback);
  }*/

  /*public static void checkCodeInBindingMobile(String code, String registerPhone,
      PasserbyClient.HttpCallback callback) {
    String category = "bind";
    String sign = Md5Util.md5(
        "login=" + registerPhone + "&version="
            + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    String url = Constants.SERVER
        + "mobile/dynamic.php?mod=mob&ctl=user&act=checkCode"
        + "&login=" + registerPhone
        + "&category=" + category
        + "&code=" + code
        + "&sign=" + sign;
    PasserbyClient.get(url, callback);
  }*/

 /* public static void checkCodeInChangingBindingMobile(String code, String registerPhone,
      PasserbyClient.HttpCallback callback) {
    String category = "modify";
    String sign = Md5Util.md5(
        "login=" + registerPhone + "&version="
            + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    String url = Constants.SERVER
        + "mobile/dynamic.php?mod=mob&ctl=user&act=checkCode"
        + "&login=" + registerPhone
        + "&category=" + category
        + "&code=" + code
        + "&sign=" + sign;
    PasserbyClient.get(url, callback);
  }*/

 /* public static void getForgetCheckCode(String code, String phoneNumber,
      PasserbyClient.HttpCallback callback
      ) {
    String category = "forget";
    String sign = Md5Util.md5(
        "login=" + phoneNumber + "&version="
            + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    String url = Constants.SERVER + "/mobile/dynamic.php?mod=mob&ctl=user&act=checkCode"
        + "&login=" + phoneNumber
        + "&category=" + category
        + "&code=" + code
        + "&sign=" + sign;
    PasserbyClient.get(url, callback);
  }
*/
 /* public static void getPhoneNumberCheck(String phoneNumber, PasserbyClient.HttpCallback callback) {
    String sign = Md5Util.md5(
        "login=" + phoneNumber + "&version="
            + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    String url = Constants.SERVER + "/mobile/dynamic.php?mod=mob&ctl=user&act=checkMobile"
        + "&login="
        + phoneNumber
        + "&sign="
        + sign;
    PasserbyClient.get(url, callback);
  }*/

 /* public static void getRegisterResult(String phoneNumber, String password, String repassword,
      PasserbyClient.HttpCallback callback) {
    String sign = Md5Util.md5(
        "login=" + phoneNumber + "&version="
            + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    String uri = Constants.SERVER + "mobile/dynamic.php?mod=mob&ctl=user&act=addUser";
    RequestParams params = new RequestParams();
    params.put("login", phoneNumber);
    params.put("pass", password);
    params.put("repass", repassword);
    params.put("sign", sign);
    PasserbyClient.post(uri, params, callback);
  }*/

  /*public static void getForgetPassWordResult(String phoneNumber, String password,
      String repassword,
      PasserbyClient.HttpCallback callback) {
    String sign = Md5Util.md5(
        "login=" + phoneNumber + "&version="
            + GloableParams.versionName + Constants.SUSERKEY)
        .toLowerCase();
    String uri = Constants.SERVER + "mobile/dynamic.php?mod=mob&ctl=user&act=forgetPass";
    RequestParams params = new RequestParams();
    params.put("login", phoneNumber);
    params.put("pass", password);
    params.put("repass", repassword);
    params.put("sign", sign);
    PasserbyClient.post(uri, params, callback);
  }*/

//    public static void setPasswordInBindingMobile(String phoneNumber, String password,
//                                                  String repassword,
//                                                  PasserbyClient.HttpCallback callback) {
//        String uri = Constants.SERVER + "mobile/dynamic.php?mod=mob&ctl=user&act=bindMobile";
//        RequestParams params = new RequestParams();
//        params.put("mobile", phoneNumber);
//        params.put("pass", password);
//        params.put("repass", repassword);
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        PasserbyClient.post(uri, params, callback);
//    }
//
//    public static void changeBind(String phoneNumber, String password, String repassword,
//                                  PasserbyClient.HttpCallback callback) {
//        String category = "modify";
//        RequestParams params = new RequestParams();
//        params.put("mobile", phoneNumber);
//        params.put("pass", password);
//        params.put("repass", repassword);
//        params.put("category", category);
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        PasserbyClient.post(Constants.URL_BIND_PHONE, params, callback);
//    }

//  public static void getFollowedList(String offset, String page,
//      PasserbyClient.HttpCallback callback) {
//    String url = Constants.URL_MYCOLLECTLIST + offset + "&page=" + page;
//    PasserbyClient.get(url, callback);
//  }

//    public static void getSupportList(String page,
//                                      PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_GET_DONATE_RECORD_INFO + page, callback);
//    }
//
//    public static void getDonateRecordInfo(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_DONATE_RECORD_MESSAGE, callback);
//    }
//
//    public static void getCategoryList(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_CATEGORY, callback);
//    }
//
//    public static void getThemeColor(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_THEME_COLOR, callback);
//    }
//
//    public static void getCancelFollowed(String pid, PasserbyClient.HttpCallback callback) {
//        String url = Constants.URL_MYCOLLECTLISTDEL + pid;
//        PasserbyClient.get(url, callback);
//    }
//
//
//    public static void addComment(String pid, String content, PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("content", content);
//        params.put("pid", pid);
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        PasserbyClient.post(Constants.URL_ADD_COMMENT, params, callback);
//    }
//
//    public static void getCheckPhoneNumber(String pwd, PasserbyClient.HttpCallback callback) {
//        String token = SharedPreferencesUtil.getString(
//                "token", "");
//        String url = Constants.SERVER
//                + "mobile/dynamic.php?mod=mob&ctl=user&act=verifyPass";
//        RequestParams params = new RequestParams();
//        params.put("oldpass", pwd);
//        params.put("token", token);
//        PasserbyClient.post(url, params, callback);
//    }
//
//    public static void changePassword(String oldpass, String pass, String repass,
//                                      PasserbyClient.HttpCallback callback) {
//        String url = Constants.SERVER + "mobile/dynamic.php?mod=mob&ctl=user&act=modifyPass";
//        String token = SharedPreferencesUtil.getString(
//                "token", "");
//        RequestParams params = new RequestParams();
//        params.put("oldpass", oldpass);
//        params.put("pass", pass);
//        params.put("repass", repass);
//        params.put("token", token);
//        PasserbyClient.post(url, params, callback);
//    }
//
//
//    public static void getFeedBack(String content, String email, PasserbyClient.HttpCallback callback) {
//        String url = Constants.SERVER
//                + "mobile/dynamic.php?mod=mob&ctl=feedback&act=add";
//        RequestParams params = new RequestParams();
//        params.put("token", SharedPreferencesUtil.getString("token", ""));
//        params.put("email", email);
//        params.put("content", content);
//        PasserbyClient.post(url, params, callback);
//    }
//
//    public static void getUpdateInfo(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_UPDATE_INFO, callback);
//    }
//
//    public static void getMovedStory(String lastId, String direction,
//                                     PasserbyClient.HttpCallback callback) {
//        String url = String.format(Constants.URL_MOVED_STORY, lastId, direction);
//        PasserbyClient.get(url, callback);
//    }
//
//    public static void getCommentList(String pid, int page, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(String.format(Constants.URL_COMMENT_LIST, pid, page), callback);
//    }
//
//    public static void getMessageCount(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_MESSAGE_NUMBER, callback);
//    }
//
//    public static void getCouponsAtDiscovery(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_COUPON_LIST, callback);
//    }
//
//
//    public static void getCouponCount(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_COUPON_COUNT, callback);
//    }
//
//    public static void getProjectListByCategoryId(String categoryId, int page,
//                                                  PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_CATEGORY_PROJECT_LIST, callback, categoryId, page);
//    }
//
//    public static void updateUserInfo(String gender, String year, String month, String day,
//                                      PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        if (!TextUtils.isEmpty(gender)) {
//            params.put("sex", gender);
//        }
//        if (!TextUtils.isEmpty(year)) {
//            params.put("birthyear", year);
//        }
//        if (!TextUtils.isEmpty(month)) {
//            params.put("birthmonth", month);
//        }
//        if (!TextUtils.isEmpty(day)) {
//            params.put("birthday", day);
//        }
//
//        PasserbyClient.post(Constants.URL_UPDATE_USER_INFO, params, callback);
//    }
//
//    public static void clearMyMessage(String type, PasserbyClient.HttpCallback callback) {
//        String url = Constants.URL_CLEAR_MESSAGE_NOTIFICATION + type;
//        PasserbyClient.get(url, callback);
//    }
//
//    public static void uploadAvatar(File avatar, PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        try {
//            params.put("headphoto", avatar, "image/x-png");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        PasserbyClient.post(Constants.URL_AVATAR_UPLOAD, params, callback);
//    }
//
//    public static void updateNickname(String nickname, PasserbyClient.HttpCallback callback) {
//        if (TextUtils.isEmpty(nickname)) {
//            ToastUtil.show("请输入昵称!");
//            return;
//        }
//
//        RequestParams params = new RequestParams();
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        params.put("nickname", nickname);
//
//        PasserbyClient.post(Constants.URL_UPDATE_NICKNAME, params, callback);
//    }
//
//    public static void updateAddress(String province, String city, String district,
//                                     PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        params.put("country", "中国");
//        params.put("province", province);
//        params.put("city", city);
//        params.put("district", district);
//        PasserbyClient.post(Constants.URL_UPDATE_USER_INFO, params, callback);
//    }

//  public static String getPaymentProtocolUrl(String orgnizationId) {
//    return PasserbyClient.appendDefaultGetParams(Constants.URL_PAYMENT_PROTOCOL + orgnizationId);
//  }

//    public static void updateNicknameInPayBack(String pid, String cardTitle, String cardBrief,
//                                               String cardLogo, String nickname, PasserbyClient.HttpCallback callback) {
//        if (TextUtils.isEmpty(nickname)) {
//            ToastUtil.show("请输入昵称!");
//            callback.onFailure("Empty nickname");
//            return;
//        }
//
//        RequestParams params = new RequestParams();
//
//        params.put("pid", pid);
//        params.put("title", cardTitle);
//        params.put("brief", cardBrief);
//        params.put("logo", cardLogo);
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        params.put("nickname", nickname);
//
//        PasserbyClient.post(Constants.URL_UPDATE_NICKNAME_IN_PAYBACK, params, callback);
//    }

 /* public static void getShareContent(ShareType type, Platform platform, String pid, String eventId,
      String cardId, String urlId, PasserbyClient.HttpCallback callback) {
    PasserbyClient.get(Constants.URL_SHARE_CONTENT, callback, type.ordinal(), platform != null
                    ? platform.ordinal()
                    : null,
            pid, eventId, cardId, urlId);
  }
*/
 /* public static void reportShareResult(String pid, ShareType type, Platform platform, int status) {
    RequestParams params = new RequestParams();
    params.put("token", SharedPreferencesUtil
        .getString("token", ""));
    params.put("pid", pid);
    params.put("type", type.ordinal());
    params.put("platid", platform.ordinal());
    params.put("status", status);
    PasserbyClient.post(Constants.URL_REPORT_SHARE_RESULT, params, null);
  }*/

/*  public static void showAdOnStart(final Activity activity) {
    PasserbyClient.get(Constants.URL_CHECK_AD_ON_START, new PasserbyClient.HttpCallback() {
      @Override
      public void onSuccess(String result) {
        BootJump bootJump = JsonUtil.json2Bean(result, BootJump.class);
        if (bootJump != null && bootJump.jump != null) {
          String url = bootJump.jump.url;
          if (!TextUtils.isEmpty(url)) {
            // url += "&uid=" + SharedPreferencesUtil.getString("uid", "");
            WebViewActivity.showWebView(activity, null, url, getPostToken(), null);
            activity.overridePendingTransition(R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom);
          }
        }*/

       /* ThemeManager.fetchThemeCallback.onSuccess(result);
        try {
          String contact = new JSONObject(result).optString("contact");
          SharedPreferencesUtil.saveString("contact", contact);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        HomeDialogManager.homeMessageCallback.onSuccess(result);
        HomeDialogManager.showDialog(activity);
      }

      @Override
      public void onFailure(String message) {

      }
    });
  }*/

 /* public static void queryPayResult(String pid, PasserbyClient.HttpCallback callback) {
    PasserbyClient.get(Constants.URL_QUERY_PAY_RESULT + pid, callback);
  }

  public static void bindInviteCode(String invitefrom, PasserbyClient.HttpCallback callback) {
    RequestParams params = new RequestParams();
    params.put("invitefrom", invitefrom);
    params.put("token", SharedPreferencesUtil
        .getString("token", ""));
    PasserbyClient.post(Constants.URL_BIND_INVITE_CODE, params, callback);
  }
*/
  /*public static void checkSnsBindingStatus(PasserbyClient.HttpCallback callback) {
    RequestParams params = new RequestParams();
    params.put("token", SharedPreferencesUtil
        .getString("token", ""));
    PasserbyClient.post(Constants.URL_GET_BIND_PLATFORM, params, callback);
  }*/

 /* public static void getDonateList(String pay, String page, PasserbyClient.HttpCallback callback) {
    String url = String.format(Constants.URL_DONATE_LIST, pay, page);
    PasserbyClient.get(url, callback);
  }*/

//    public static void weixinPayInfo(String pid, String method, String payNum,
//                                     PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("pid", pid);
//        params.put("method", method);
//        params.put("paynum", payNum);
//        params.put("token", SharedPreferencesUtil.getString("token", ""));
//        PasserbyClient.post(Constants.URL_WEIXIN_PAY, params, callback);
//    }
//
//    public static void weixinPayAgain(String orderId, PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("orderid", orderId);
//        params.put("token", SharedPreferencesUtil.getString("token", ""));
//        PasserbyClient.post(Constants.URL_WEIXIN_PAY_AGAIN, params, callback);
//    }
//
//    public static void requestAsJumpOnStart(String id, String serveUrl,
//                                            PasserbyClient.HttpCallback callback) {
//        RequestParams params = new RequestParams();
//        params.put("initialid", id);
//        params.put("token", SharedPreferencesUtil.getString("token", ""));
//        PasserbyClient.post(serveUrl, params, callback);
//    }
//
//    public static void getWaitDoanteNum(PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_DOANTE_WAIT_NUM, callback);
//    }
//
//    public static void paySuccessData(String pid, PasserbyClient.HttpCallback callback) {
//        PasserbyClient.get(Constants.URL_PAY_SUCCESS_INFO + pid, callback);
//    }

  /*public static String getPaySDKUrl(String url, String pid) {
    String completeUrl =
        url + "&devid=" + GloableParams.deviceID + "&pcode="
            + Constants.SPCODE + "&version="
            + GloableParams.versionName + "&token=" + SharedPreferencesUtil.getString("token", "")
            + "&pid=" + pid + "&orderId=%s" + "&method=%s";
    return completeUrl;
  }*/

//    public static void test(PasserbyClient.HttpCallback callback){
//        String url = "http://219.239.236.49/mobile/dynamic.php?mod=mob&ctl=mb&act=xedni";
//        PasserbyClient.get(url, callback);
//    }
//
//    public static void uploadImage(File avatar, String url, PasserbyClient.HttpCallback callback){
//        RequestParams params = new RequestParams();
//        params.put("token", SharedPreferencesUtil
//                .getString("token", ""));
//        try {
//            params.put("file", avatar, "image/x-png");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        PasserbyClient.post(url, params, callback);
//    }
//
//    public static void getDonateGoal(String pid, int page, PasserbyClient.HttpCallback callback){
//        PasserbyClient.get(String.format(Constants.URL_DONATE_GOAL, pid, String.valueOf(page)), callback);
//    }


}
