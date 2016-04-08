package com.android.linglan.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.linglan.http.Constants;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changyizhang on 12/21/14.
 */
public class
        UmengSnsUtil {

  private static boolean shareInit;
//  private static String SINA_FOLLOW_UID = "5371124553";

  private static UMSocialService loginController = UMServiceFactory
      .getUMSocialService("com.umeng.login");

//  private static UMSocialService shareController = UMServiceFactory
//      .getUMSocialService("com.umeng.share");

  public static void init(Context context) {
    // Weichat
    UMWXHandler wxHandler =
        new UMWXHandler(context, Constants.WEICHAT_APP_ID, Constants.WEICHAT_APP_KEY);
    wxHandler.addToSocialSDK();
    wxHandler.showCompressToast(false);

    UMWXHandler wxCircleHandler =
        new UMWXHandler(context, Constants.WEICHAT_APP_ID, Constants.WEICHAT_APP_KEY);
    wxCircleHandler.setToCircle(true);
    wxCircleHandler.addToSocialSDK();
    wxCircleHandler.showCompressToast(false);

    // QQ
//    UMQQSsoHandler qqSsoHandler =
//        new UMQQSsoHandler((android.app.Activity) context, Configs.QQ_ZONE_APP_ID,
//            Configs.QQ_ZONE_APP_KEY);
//    qqSsoHandler.addToSocialSDK();

    // QZone
//    QZoneSsoHandler qZoneSsoHandler =
//        new QZoneSsoHandler((android.app.Activity) context, Configs.QQ_ZONE_APP_ID,
//            Configs.QQ_ZONE_APP_KEY);
//    qZoneSsoHandler.addToSocialSDK();

    // SSO for sina weibo
    loginController.getConfig().setSsoHandler(new SinaSsoHandler());
  }

  public static UMSocialService getAuthController() {
    return loginController;
  }

  public static void authorize(Context context, SHARE_MEDIA platform,
      SocializeListeners.UMAuthListener listener) {
    loginController.doOauthVerify(context, platform, listener);
  }

  public static void logout(Context context, SHARE_MEDIA platform,
      SocializeListeners.SocializeClientListener listener) {
    loginController.deleteOauth(context, platform, listener);
  }

  public static void getUserInfo(final Context context,
      final SHARE_MEDIA platform, SocializeListeners.UMDataListener listener) {
    loginController.getPlatformInfo(context, platform,
            listener);
  }

  /*public static void addCustomPlatforms(Context context) {
    init(context);

    shareController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
        SHARE_MEDIA.SINA);
    shareController.openShare((Activity) context, false);
  }*/

//  public static void configShare(Context context, final String[] choosePlatforms) {
//    init(context);
//   /* shareController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
//            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);*/
//    shareController.getConfig().setPlatforms(getShareFromName(choosePlatforms));
//    shareController.getConfig().setPlatformOrder(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
//            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
//   // shareInit = true;
//  }
//
//
//  public static void postShare(Context context, final ShareType type, Platform platform,
//                               String pid, String eventId,
//                               String cardId, final String[] choosePlatforms) {
//    postShare(context, type, null, pid, eventId, cardId, null, choosePlatforms);
//  }
//
//  public static void postShare(final Context context, final ShareType type, Platform platform,
//      final String pid, String eventId,
//      String cardId, String urlId, final String[] choosePlatforms) {
//    ProgressUtil.show(context, "");
//    NetApi.getShareContent(type, platform, pid, eventId, cardId, urlId, new PasserbyClient.HttpCallback() {
//      @Override
//      public void onSuccess(String result) {
//        ShareContent shareContent = JsonUtil.json2Bean(result, ShareContent.class);
//        if (shareContent == null) {
//          ToastUtil.show("分享失败!");
//        } else {
//          SnsUtil.postShare(context, pid, type, shareContent, choosePlatforms);
//        }
//        ProgressUtil.dismiss();
//      }
//
//      @Override
//      public void onFailure(String message) {
//        ProgressUtil.dismiss();
//        ToastUtil.show("分享失败!");
//      }
//    });
//  }
//
//  private static void postShare(final Context context, final String pid, final ShareType type, ShareContent content,
//                                 String[] choosePlatforms) {
//    if (!shareInit) {
//      configShare(context, choosePlatforms);
//    }
//    if (choosePlatforms == null || choosePlatforms.length == 0){
//      choosePlatforms =  new String[]{"1", "2", "3", "4", "5"};
//    }
//    if (content != null && content.sharedata != null) {
//      Platform platform;
//      ShareContent.ShareItem shareItem;
//      BaseShareContent shareContent;
//
//      //for (Map.Entry<Integer, ShareContent.ShareItem> entry : content.sharedata.entrySet()) {
//      for (String entry: choosePlatforms)
//        if (entry != null) {
//         // platform = Platform.values()[entry.getKey()];
//          platform = Platform.values()[Integer.parseInt(entry)];
//          shareContent = getShareContent(platform);
//          if (shareContent != null) {
//            //shareItem = entry.getValue();
//            int index = Integer.parseInt(entry);
//            shareItem = content.sharedata.get(index);
//            if (TextUtils.isEmpty(shareItem.title)) {
//              shareItem.title = shareItem.brief;
//            }
//            if (TextUtils.isEmpty(shareItem.brief)) {
//              shareItem.brief = shareItem.title;
//            }
//            shareContent.setTitle(shareItem.title);
//            if (Platform.WEIBO == platform) {
//              shareItem.brief += shareItem.link;
//            }
//            shareContent.setShareContent(shareItem.brief);
//            if(!TextUtils.isEmpty(shareItem.logo)){
//              shareContent.setShareImage(new UMImage(context, shareItem.logo));
//            }
//            shareContent.setTargetUrl(shareItem.link);
//            shareController.setShareMedia(shareContent);
//          }
//        }
//
//    }
//
//    shareController.openShare((Activity) context, new SocializeListeners.SnsPostListener() {
//      @Override
//      public void onStart() {
//
//      }
//
//      @Override
//      public void onComplete(final SHARE_MEDIA share_media, int eCode, SocializeEntity socializeEntity) {
//        int status;
//
//        if (eCode == 200) {
//          ToastUtil.show("分享成功.");
//          getUserInfo(context, share_media, new SocializeListeners.UMDataListener() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onComplete(int status, Map<String, Object> info) {
//              snsFollow(context, share_media);
//              if (PayResultFragment.jumpTo) {
//                new PayResultFragment().jump();
//              }
//            }
//          });
//
//          status = 1;
//        } else {
//          PayResultFragment.jumpTo = false;
//          String eMsg = "";
//          if (eCode == -101) {
//            eMsg = "没有授权";
//          }
//          ToastUtil.show("分享失败[" + eCode + "] " +
//                  eMsg);
//          status = 2;
//        }
//
//        NetApi.reportShareResult(pid, type, getSharePlatform(share_media), status);
//      }
//    });
//  }

  /*public static void setShareContent(Context context, String content, String title,
      Map<String, String> map, String imageUrl) {
    QQShareContent qqShareContent = new QQShareContent();
    if (!TextUtils.isEmpty(content)) {
      qqShareContent.setShareContent(content);
    }
    if (!TextUtils.isEmpty(title)) {
      qqShareContent.setTitle(title);
    }
    if (!TextUtils.isEmpty(imageUrl)) {
      qqShareContent.setShareImage(new UMImage(context, imageUrl));
    }
    if (map != null) {
      qqShareContent.setTargetUrl(map.get("qq"));
    }
    shareController.setShareMedia(qqShareContent);
    // sina
    SinaShareContent sinaShareContent = new SinaShareContent();
    if (!TextUtils.isEmpty(content) && map != null) {
      sinaShareContent.setShareContent("#路人甲# " + title + ":" + content + map.get("sina"));
    } else if (!TextUtils.isEmpty(content)) {
      sinaShareContent.setShareContent("#路人甲# " + title + ":" + content);
    }
    if (!TextUtils.isEmpty(title)) {
      sinaShareContent.setTitle(title);
    }
    if (!TextUtils.isEmpty(imageUrl)) {
      sinaShareContent.setShareImage(new UMImage(context, imageUrl));
    }
    if (map != null) {
      sinaShareContent.setTargetUrl(map.get("sina"));
    }
    shareController.setShareMedia(sinaShareContent);
    // weixin
    WeiXinShareContent weixinContent = new WeiXinShareContent();
    if (!TextUtils.isEmpty(content)) {
      weixinContent.setShareContent(content);
    }
    if (!TextUtils.isEmpty(title)) {
      weixinContent.setTitle(title);
    }
    if (!TextUtils.isEmpty(imageUrl)) {
      weixinContent.setShareImage(new UMImage(context, imageUrl));
    }

    if (map != null) {
      weixinContent.setTargetUrl(map.get("weichat"));
    }
    shareController.setShareMedia(weixinContent);

    //
    QZoneShareContent qzone = new QZoneShareContent();
    if (!TextUtils.isEmpty(content)) {
      qzone.setShareContent(content);
    }
    if (!TextUtils.isEmpty(title)) {
      qzone.setTitle(title);
    }
    if (!TextUtils.isEmpty(imageUrl)) {
      qzone.setShareImage(new UMImage(context, imageUrl));
    }

    if (map != null) {
      qzone.setTargetUrl(map.get("qq"));
    }
    shareController.setShareMedia(qzone);

    // 设置朋友圈分享的内容
    CircleShareContent circleMedia = new CircleShareContent();
    if (!TextUtils.isEmpty(content)) {
      circleMedia.setShareContent(content);
    }
    if (!TextUtils.isEmpty(title)) {
      circleMedia.setTitle(title);
    }
    if (!TextUtils.isEmpty(imageUrl)) {
      circleMedia.setShareImage(new UMImage(context, imageUrl));
    }
    if (map != null) {
      circleMedia.setTargetUrl(map.get("weichat"));
    }
    shareController.setShareMedia(circleMedia);

  }*/

//  public static BaseShareContent getShareContent(Platform type) {
//    BaseShareContent content = null;
//
//    switch (type) {
//      case WEIBO:
//        content = new SinaShareContent();
//        break;
//      case WEICHAT:
//        content = new WeiXinShareContent();
//        break;
//      case QQ:
//        content = new QQShareContent();
//        break;
//      case QQ_ZONE:
//        content = new QZoneShareContent();
//        break;
//      case WEICHAT_MOMENTS:
//        content = new CircleShareContent();
//        break;
//    }
//
//    return content;
//  }
//
//  public static Platform getSharePlatform(SHARE_MEDIA media) {
//    Platform platform = null;
//
//    switch (media) {
//      case SINA:
//        platform = Platform.WEIBO;
//        break;
//      case WEIXIN:
//        platform = Platform.WEICHAT;
//        break;
//      case QQ:
//        platform = Platform.QQ;
//        break;
//      case QZONE:
//        platform = Platform.QQ_ZONE;
//        break;
//      case WEIXIN_CIRCLE:
//        platform = Platform.WEICHAT_MOMENTS;
//        break;
//    }
//
//    return platform;
//  }
//
//  private static SHARE_MEDIA[] getShareFromName(String[] names) {
//    List<SHARE_MEDIA> list = new ArrayList<SHARE_MEDIA>();
//    if (names == null || names.length == 0) {
//      list.add(SHARE_MEDIA.SINA);
//      list.add(SHARE_MEDIA.WEIXIN);
//      list.add(SHARE_MEDIA.QQ);
//      list.add(SHARE_MEDIA.QZONE);
//      list.add(SHARE_MEDIA.WEIXIN_CIRCLE);
//      return list.toArray(new SHARE_MEDIA[list.size()]);
//    }
//
//    for (String name:names){
//      SHARE_MEDIA platform = null;
//      switch (Integer.parseInt(name) - 1) {
//        case 0:
//          platform = SHARE_MEDIA.SINA;
//          break;
//        case 1:
//          platform = SHARE_MEDIA.WEIXIN;
//          break;
//        case 2:
//          platform = SHARE_MEDIA.QQ;
//          break;
//        case 3:
//          platform = SHARE_MEDIA.QZONE;
//          break;
//        case 4:
//          platform = SHARE_MEDIA.WEIXIN_CIRCLE;
//          break;
//      }
//      if(platform != null){
//        list.add(platform);
//      }
//    }
//    return list.toArray(new SHARE_MEDIA[list.size()]);
//  }
//
//  private static void snsFollow(final Context context, SHARE_MEDIA share_media) {
//    if (SHARE_MEDIA.SINA.equals(share_media)) {
//      shareController.follow(context, share_media,
//          new SocializeListeners.MulStatusListener() {
//            @Override
//            public void onStart() {}
//
//            @Override
//            public void onComplete(MultiStatus multiStatus, int st,
//                SocializeEntity entity) {
//              if (st == 200) {
//                Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
//              }
//            }
//          }, new String[] {String.valueOf(UmengSnsUtil.SINA_FOLLOW_UID)});
//    }
//  }
//
//  public interface JumpToPaySuccess{
//    void jump();
//  }
}
