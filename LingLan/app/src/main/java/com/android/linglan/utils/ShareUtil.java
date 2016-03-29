package com.android.linglan.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.android.linglan.http.Constants;
import com.android.linglan.ui.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by LeeMy on 2016/2/2 0002.
 * 友盟分享工具类
 */
public class ShareUtil {

    // 友盟分享平台的Controller,负责管理整个SDK的配置、操作等处理
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    /**
     * 以下为umeng分享
     */
    /**
     * 配置分享平台参数
     */
    public static void configPlatforms(Context context){
        // 添加新浪sso授权//设置新浪SSO handler
//        addSinaPlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform(context);

    }

    /**
     * 根据不同的平台设置不同的分享内容
     */
    public static void setShareContent(Context context, String articleORsubject, String id, String shareTitle, String imgUrl, String shareContent){// articleORsubject: 0 文章  4专题
        String shareUrl = null;
        if (Constants.ARTICLE.equals(articleORsubject)) {
            shareUrl = Constants.SERVER + "/Api/Open/article?articleid=" + id;
        } else if (Constants.SUBJECT.equals(articleORsubject)) {
            shareUrl = Constants.SERVER + "/Api/Open/special?specialid=" + id;
        }
//        String shareTitle = "分享文章的标题";//分享文章的标题
//        String shareUrl = "http://zhongyishuyou.com";//分享文章的地址
//        String imgUrl = "null";//封面图片的url地址
//        String shareContent = "summary";//新闻摘要
        UMImage shareImg;
        if (TextUtils.isEmpty(shareTitle)) {

        }
        if (TextUtils.isEmpty(shareUrl)) {
            shareUrl = "";
        }
        if(TextUtils.isEmpty(shareContent)){

        }
        if(!TextUtils.isEmpty(imgUrl)){
            shareImg = new UMImage(context, imgUrl);
        }else{
            shareImg = new UMImage(context, R.drawable.ic_launcher);//默认的图片
        }
        // 配置SSO设置免登陆
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());


        //微信分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle(shareTitle);
        weixinContent.setTargetUrl(shareUrl);
        weixinContent.setShareImage(shareImg);
        //weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareTitle);
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareImage(shareImg);
        circleMedia.setTargetUrl(shareUrl);
        mController.setShareMedia(circleMedia);

//        //新浪微博分享内容
//        SinaShareContent sinaShareContent = new SinaShareContent();
//        sinaShareContent.setShareContent("分享一篇来自“肝胆相照”的文章：" + shareTitle + shareUrl);
//        sinaShareContent.setTargetUrl(shareUrl);
//        sinaShareContent.setShareImage(shareImg);
//        mController.setShareMedia(sinaShareContent);

    }

    /**
     * 添加到友盟分享平台
     */
    public static void umengPlatforms(Activity context){
        // 添加微信平台
        addWXPlatform(context);
//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
//                SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE);
        // 是否只有已登录用户才能打开分享选择页
        mController.openShare(context, false);

    };

    public static void addWXPlatform(Context context) {
        // Weichat
        UMWXHandler wxHandler;
        wxHandler = new UMWXHandler(context, Constants.WEICHAT_APP_ID, Constants.WEICHAT_APP_KEY);
        wxHandler.addToSocialSDK();
        wxHandler.showCompressToast(false);

        UMWXHandler wxCircleHandler =
                new UMWXHandler(context, Constants.WEICHAT_APP_ID, Constants.WEICHAT_APP_KEY);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        wxCircleHandler.showCompressToast(false);
    }
}
