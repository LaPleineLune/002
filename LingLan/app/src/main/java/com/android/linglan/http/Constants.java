package com.android.linglan.http;

/**
 * Created by changyizhang on 12/10/14.
 */
public class Constants {

    public static final String FONT_SIZE = "fontsize"; // 字体大小
    public static final int HOME = 0;// 首页的搜索
    public static final int ALLSUBJECT = 1;// 全部专题页的搜索
    public static final int ALLARTICLE = 2;// 全部文章页的搜索

//    public static final String SERVER = "http://192.168.1.117:8082";//测试服务亲
    public static final String SERVER = "http://101.200.122.79";//正式服务器

    /**
     * 获取游客token
     */
    public static final String URL_VISIT_TOKEN = Constants.SERVER + "/Api/User/visittoken";

    /**
     * 用户登录
     */
    public static final String USER_LOGIN = Constants.SERVER + "/Api/User/applogin";

    /**
     * 首页推荐
     */
    public static final String URL_HOME_RECOMMEND = Constants.SERVER + "/Api/Index/recommend?token=";

    /**
     * 推荐文章
     * */
    public static final String URL_RECOMMEND_ARTICLE = Constants.SERVER + "/Api/Index/recommend?token=";

    /**
     * 全部文章分类
     */
    public static final String URL_ALL_ARTICLE_CLASSIFY = Constants.SERVER + "/Api/Article/catelist?token=";

    /**
     * 全部文章分类下的文章列表
     */
    public static final String URL_ALL_ARTICLE_CLASSIFY_List = Constants.SERVER + "/Api/Article/catearti?token=";

    /**
     * 文章详情
     */
    public static final String URL_DETAILS_ARTICLE = Constants.SERVER + "/Api/Article/articleinfo?token=";

    /**
     * 文章详情用户写（添加）笔记
     */
    public static final String URL_DETAILS_ARTICLE_NOTE_WRITE = Constants.SERVER + "/Api/ArticleNotes/addArticleNotes?token=";

    /**
     * 文章详情用户预览（获取）笔记
     */
    public static final String URL_DETAILS_ARTICLE_NOTE_PREVIEW = Constants.SERVER + "/Api/ArticleNotes/getArticleNotes?token=";

    /**
     * 文章点赞
     */
    public static final String URL_DETAILS_ARTICLE_LIKE = Constants.SERVER + "/Api/Article/articlefavour?token=";

    /**
     * 文章收藏
     */
    public static final String URL_DETAILS_ARTICLE_COLLECT = Constants.SERVER + "/Api/Article/articlecollect?token=";

    /**
    * 全部专题
    * */
    public static final String URL_ALL_SUBJECT = Constants.SERVER + "/Api/Special/speclist?token=";

    /**
     * 全部专题分类列表
     */
    public static final String URL_ALL_SUBJECT_CLASSIFY = Constants.SERVER + "/Api/Special/speccate?token=";


    /**
    * 推荐专题
    * */
    public static final String URL_RECOMMEND_SUBJECT = Constants.SERVER + "/Api/Special/recspeclist?token=";

    /**
    * 专题详情
    * */
    public static final String URL_DETAILS_SUBJECT = Constants.SERVER + "/Api/Special/specailcontent?token=";

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
    public static final String URL_DETAILS_SUBJECT_LIKE = Constants.SERVER + "/Api/Special/Specialfavour?token=";

    /**
     * 专题收藏
     */
    public static final String URL_DETAILS_SUBJECT_COLLECT = Constants.SERVER + "/Api/Special/Specialcollect?token=";

    /**
     * 获取作者详情
     */
    public static final String URL_DETAILS_AUTHOR = Constants.SERVER + "/Api/publisher/publisherinfo?token=";

    /**
     * 添加关注
     */
    public static final String URL_ADD_FOLLOW = Constants.SERVER + "/Api/publisher/addAttention?token=";

    /**
     * 获取我的关注列表
     */
    public static final String URL_FOLLOWED_LIST = Constants.SERVER + "/Api/publisher/getAttentionPublisherlist?token=";

    /**
     * 获取我的收藏-文章
     */
    public static final String URL_COLLECT_ARTICLE = Constants.SERVER + "/Api/Article/getColletArticle?token=";

    /**
     * 获取我的收藏-专题
     */
    public static final String URL_COLLECT_SUBJECT = Constants.SERVER + "/Api/Special/getColletSpecial?token=";

    /**
     * 获取我的收藏-全部
     */
    public static final String URL_COLLECT_ALL = Constants.SERVER + "/Api/User/collections?token=";

    /**
     * 意见反馈
     */
    public static final String URL_SEND_FEEDBACK = Constants.SERVER + "/Api/Sys/suggestion?token=";

    /**
     * 获取关于我们信息
     */
    public static final String URL_ABOUT = Constants.SERVER + "/Api/Sys/about?token=";

    /**
     * 获取全局热门搜索/历史搜索（统计）2.19
     */
    public static final String URL_STATISTIC = Constants.SERVER + "/Api/Index/statistic?token=";

    /**
     * 全局搜索2.19
     */
    public static final String URL_SEARCH_ALL = Constants.SERVER + "/Api/Index/searchall?token=";

    /**
     * 获取专题热门搜索/历史搜索（统计）
     */
    public static final String URL_SUBJECT_STATISTIC = Constants.SERVER + "/Api/Special/statistic?token=";

    /**
     * 专题搜索
     */
    public static final String URL_SEARCH_SUBJECT = Constants.SERVER + "/Api/Special/searchkey?token=";
    /**
     * 获取文章热门搜索/历史搜索（统计）
     */
    public static final String URL_ARTICLE_STATISTIC = Constants.SERVER + "/Api/Article/statistic?token=";

    /**
     * 文章搜索
     */
    public static final String URL_SEARCH_ARTICLE= Constants.SERVER + "/Api/Article/searchkey?token=";


    /**
     * 文章分类频道排序修改
     */
    public static final String URL_ARTICLE_ORDER_EDIT= Constants.SERVER + "/Api/Article/cateorder?token=";


    /**
     * 用户上传头像
     */
    public static final String URL_USER_PHOTO_UPLOAD= Constants.SERVER + "/Api/User/uploadphoto?token=";

    /**
     * 文章取消收藏
     */
    public static final String URL_ARTICLE_CANCE_COLLECT= Constants.SERVER + "/Api/Article/cancelcollect?token=";

    /**
     * 专题取消收藏
     */
    public static final String URL_SUBJECT_CANCE_COLLECT= Constants.SERVER + "/Api/Special/cancelcollect?token=";

    /**
     * 退出登录
     */
    public static final String URL_USER_EXIT = Constants.SERVER + "/Api/User/userexit?token=";

    /**
     * 获取验证码
     */
    public static final String URL_USER_CODE = Constants.SERVER + "/Api/User/code?token=";

    /**
     * 发布者发布的文章列表
     */
    public static final String URL_PUBLISHER_ARTIITEM= Constants.SERVER + "/Api/Publisher/publisherartiitem?token=";

    /**
     * 用户信息
     */
    public static final String URL_USER_INFO= Constants.SERVER + "/Api/User/userinfo?token=";

    /**
     * 用户信息修改
     */
    public static final String URL_USER_INFO_EDIT= Constants.SERVER + "/Api/User/edituser?token=";

    /**
     * 检查app版本更新
     */
    public static final String URL_CHECK_UPDATE= Constants.SERVER + "/Api/Sys/checkupdate?token=";

    /**
     * 下载app
     */
    public static final String URL_APP_DOWNLOAD= Constants.SERVER + "/Api/Sys/download?tag=android";

    /**
     * 服务条款
     */
    public static final String URL_APP_AGREEMENT= Constants.SERVER + "/Api/Sys/agreement?token=";

    /**
     * 城市列表
     */
    public static final String URL_CITY= Constants.SERVER + "/Api/Sys/city?token=";






//    /**
//     * Develop server configuration.
//     */
  /*public static final String SERVER = "http://219.239.236.49/";
  public static final String M_SEVER = "http://219.239.236.49/";
  public static String STHREEKEY = "&_mLw83yunHangaoUkgluiaOi06Gaoc20_";
  public static String SUSERKEY = "&_SpAwNUjgkIugkeIi345_";
  public static String SSTARTINGUPKEY = "&_TcidjochgUjgKijD2_";
  public static final String ENCRYPT_KEY = "a12UiEf5";
  public static final String ENCRYPT_KEY1 = "_mylwbwxygcspawn_";
  public static final String ENCRYPT_KEY2 = "rt8xGR0BaGTiS677GGzby";*/
//    /**
//     * Release server configuration.
//     */
////    public static final String SERVER = "http://app.warmler.com/"; // Release url.
//    public static final String M_SEVER = "http://m.warmler.com/";
//    public static String STHREEKEY = "&_UyrdQ19MiBcdwT3857_";
//    public static String SUSERKEY = "&_ElementPottiUjgkIugkeIiGxcW_";
//    public static String SSTARTINGUPKEY = "&_NcwU87Miqz5F38Nd4_";
//    public static final String ENCRYPT_KEY = "B42DcVW8";
//    public static final String ENCRYPT_KEY1 = "_whhwxbelehyb_";
//    public static final String ENCRYPT_KEY2 = "U2FsdGVkX18PIDNkYM3iQ";
//
//    // public static String SENCODING = "UTF-8";
//    // public static String SPCODE = "01011004"; // wandoujia
//    public static String SPCODE = "01011002"; // QQ
//    public static String MEHOD = "2";
//    public static String INVITE_NAME;
//    public static String javascriptUrl;
//    public static List<String> verifyUrls;
//    // appid
//    public static final String APP_ID = "wx5f3253a1416afedf";// yum
//
//    // 商户号
//    public static final String MCH_ID = "1227475802";// yum
//
//    public static final String API_KEY = "wanghusenlin2015wanghusenlin2015";// yum
//
//    public static final String WEI_XIN = "1";
//    public static final String ALI_PAY = "2";
//
//    public static final String URL_START_INFO = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=start&action=index";
//
//    public static final String URL_LOGIN_BY_MOBILE = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=user&act=login";
//
//    public static final String URL_UPLOAD_DEVICE_INFO = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=device&act=index";
//
//    public static final String URL_VERIFY_TOKEN = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=user&act=checkToken";
//
//    public static final String URL_DETAIL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=project&act=index&pid=%s";
//
//    public static final String URL_PAY_RESULT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=payback&act=showCard&pid=";
//
//    public static final String URL_WEBVIEW = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=project&act=projectDetail&pid=%s";
//
//    public static final String URL_MESSAGE_NUMBER = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=cachenum&act=getNumber";
//
//    public static final String URL_MOVED_STORY = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=energy&act=list&lastid=%s&direction=%s";
//
//    public static final String URL_COUPON_LIST = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=coupon&act=findList";
//
//    public static final String URL_MORE_LOVE = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=tag&act=list";
//
//    public static final String URL_PROJECT_LIST_TAG = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=tag&act=item&tagid=%s&page=%d";
//
//    public static final String URL_CATEGORY = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=cate&act=cate";
//
//    public static final String URL_CATEGORY_PROJECT_LIST = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=cate&act=cateItem&cateid=%s&page=%d";
//
//    public static final String URL_PROJECT_SEARCH = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=search&act=index&title=%s&page=%d";
//
//    public static final String URL_ORGNIZATION_LIST = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=project&act=projectOrg&oid=%s";
//
//    public static final String URL_ADD_COMMENT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=comment&act=add";
//
//    public static final String URL_COMMENT_LIST = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=comment&act=list&pid=%s&page=%d";
//
//    public static final String URL_COUPON_BANNER = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=coupon&act=adList";
//
//    public static final String URL_COUPON_COUNT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=ticket&act=getTicketCount";
//
//    public static final String URL_COUPON_HOT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=coupon&act=hotList&page=";
//
//    public static final String URL_COUPON_ALL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=coupon&act=allList&page=";
//
//    public static final String URL_COUPON_DETAIL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=coupon&act=detail&itemid=%s";
//
//    public static final String URL_ME_COUPON_DETAIL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userCoupon&act=detail&couponid=%s";
//
//    public static final String URL_HAVE_COUPON_USED = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userCoupon&act=setUsed";
//
//    public static final String URL_HAVE_COUPON_USED_MORE = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userCoupon&act=setUsedMore";
//
//    public static final String URL_PROJECT_CATEGORY = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=index&act=cate&page=";
//
//    public static final String URL_COUPON_GET = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=receiveCoupon&act=receive";
//
//    public static final String URL_MY_COUPON_DETAIL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userCoupon&act=detail&couponid=%s";
//
//    public static final String URL_FOLLOW_PROJECT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=attend&act=add";
//
//    public static final String URL_POSTER = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=index&act=index&page=";
//
//    public static final String URL_PROJECT_EVENT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=project&act=projectEvent&pid=%s&page=%d";
//
//    public static final String URL_PROJECT_MORE = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=index&act=cateList&cateid=";
//
//    public static final String URL_MY_ATTENTION_COUNT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=profilenum&act=index";
//
//  /*public static final String URL_MY_MESSAGE = SERVER
//      + "mobile/dynamic.php?mod=mob&ctl=cachenum&act=getNumber";*/
//
//    public static final String URL_CLEAR_MESSAGE_NOTIFICATION = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=cachenum&act=clearNumber&type=";
//
//    public static final String URL_MY_COLLECT_LIST = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=attend&act=list&offset=";
//
//    public static final String URL_MY_COUPON = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userCoupon&act=cate&page=%s&type=%s";
//
//    public static final String URL_CANCEL_COLLECTIONS = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=attend&act=cancel&pids=";
//
//    public static final String URL_MY_VOUCHER_DETAIL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userCoupon&act=detail&couponid=";
//
//    public static final String URL_MY_ALBUM_LIST = SERVER
//            + "/mobile/dynamic.php?mod=mob&ctl=postcard&act=list&page=";
//
//    public static final String URL_DONATE = SERVER
//            + "/mobile/dynamic.php?mod=mob&&ctl=donate&act=add&devid=%s&pcode=%s&version=%s&paynum=%s&method=%s";
//
//    public static final String URL_SNS_LOGIN = SERVER
//            + "/mobile/dynamic.php?mod=mob&ctl=userOpenPlatform&act=login";
//
//    public static final String URL_MYCOLLECTLIST = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=attend&act=list&offset=";
//
//    public static final String URL_MYCOLLECTLISTDEL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=attend&act=cancel&pids=";
//
//    public static final String URL_UPDATE_USER_INFO = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=user&act=updateUserInfo";
//
//    public static final String URL_AVATAR_UPLOAD = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=user&act=uploadHead";
//
//    public static final String URL_UPDATE_NICKNAME = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=user&act=updateNickname";
//
//    public static final String URL_UPDATE_NICKNAME_IN_PAYBACK = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=payback&act=changeNickname";
//
//    public static final String URL_VOUCHERDETAILS = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userCoupon&act=detail&couponid=";
//
//    public static final String URL_PAYMENT_PROTOCOL = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=project&act=orgProtocol&oid=";
//
//    public static final String URL_SHARE_CONTENT =
//            SERVER
//                    + "/mobile/dynamic.php?mod=mob&ctl=share&act=share&type=%d&platid=%d&pid=%s&eventid=%s&cardid=%s&urlid=%s";
//
//    public static final String URL_REPORT_SHARE_RESULT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=share&act=addShare";
//
//    public static final String URL_CHECK_AD_ON_START = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=initial&act=index";
//
//    public static final String URL_THEME_COLOR = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=initial&act=index";
//
//    public static final String URL_QUERY_PAY_RESULT = SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=donate&act=check&pid=";
//
//    public static final String URL_UPDATE_INFO = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=update&act=index";
//
//    public static final String URL_BIND_INVITE_CODE = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=user&act=bindInvite";
//
//    public static final String URL_SNS_BIND = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userOpenPlatform&act=bind";
//
//    public static final String URL_GET_BIND_PLATFORM = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=userOpenPlatform&act=getOpenplatformList";
//
//    public static final String URL_BIND_PHONE = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=user&act=bindMobile&category=";
//
//    public static final String URL_GET_DONATE_RECORD_INFO = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&&ctl=donate&act=list&page=";
//
//    public static final String URL_DONATE_LIST = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=donate&act=record&pay=%s&page=%s";
//
//    public static final String URL_DONATE_AGAIN = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=donate&act=pay&orderid=%s&token=%s";
//
//    public static final String URL_DONATE_RECORD_MESSAGE = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&&ctl=donate&act=num";
//
//    public static final String URL_WEIXIN_PAY = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&&ctl=weixin&act=add";
//
//    public static final String URL_WEIXIN_PAY_AGAIN = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&&ctl=weixin&act=pay";
//
//    public static final String URL_DOANTE_WAIT_NUM = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=donate&act=numfail";
//
//    public static final String URL_PAY_SUCCESS_INFO = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=payback&act=showPartner&pid=";
//
//    public static final String URL_ENCRYPT_CODE = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=mb&act=index";
//
//    public static final String URL_DONATE_GOAL = Constants.SERVER
//            + "mobile/dynamic.php?mod=mob&ctl=project&act=fundraise&pid=%s&page=%s";
}
