# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/changyizhang/Downloads/adt-bundle-mac-x86_64-20130522/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-optimizations !method/propagation/*
#!code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
#-dontshrink
#-dontoptimize
#-dontobfuscate
-printseeds

-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Signature

-assumenosideeffects class android.util.Log{
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
    public static boolean isLoggable(java.lang.String, int);
}

-assumenosideeffects class * extends java.lang.Throwable {
    public void printStackTrace();
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.os.IInterface


-keep public class com.jingge.pulltorefreshlibrary.** { *; }    # 保持哪些类不被混淆
-keep public class com.jingge.pulltorefreshlibrary.PullToRefreshWebView$InternalWebViewSDK9
-keep public class com.umeng.** { *; }
-keep public class com.umeng.** { *; }
-keep public class prepareLingLanSocial_sdk_library_projectUnspecifiedLibrary
-keep public class prepareLingLanPopuphelperUnspecifiedLibrary

-keep public class com.squareup.picasso.** { *; }
-keep public class com.android.support.** { *; }
-keep public class com.google.code.gson.** { *; }
-keep public class com.loopj.android.** { *; }
-keep public class com.soundcloud.android.** { *; }
-keep public class com.umeng_social_sdk_res_lib.** { *; }
-keep public class com.loopj.android.http.** { *; }
-keep public class com.loopj.android.http.TextHttpResponseHandler
-keep public class com.loopj.android.http.AsyncHttpResponseHandler
-keep public class  com.android.linglan.http.** { *; }
-keep public class com.android.linglan.utils.** { *; }
-dontwarn com.loopj.android.http.**
-dontwarn com.loopj.android.http.TextHttpResponseHandler
-dontwarn com.loopj.android.http.AsyncHttpResponseHandler

-dontwarn com.jingge.pulltorefreshlibrary.**
-dontwarn com.jingge.pulltorefreshlibrary.PullToRefreshWebView$InternalWebViewSDK9

-keep class android.media.* {
    *;
}

-keep public class com.android.internal.telephony.* {
    *;
}

-keep public class android.os.storage.* {
    *;
}

-keep public class android.content.pm.* {
    *;
}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class * implements java.io.Serializable {
    *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep public class android.net.http.SslError{
    *;
}

-keep public class android.webkit.WebViewClient{
    *;
}

-keep public class android.webkit.WebChromeClient{
    *;
}

-keep public interface android.webkit.WebChromeClient$CustomViewCallback {
    *;
}

-keep public interface android.webkit.ValueCallback {
    *;
}

-keep class * implements android.webkit.WebChromeClient {
    *;
}

-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
#-keep class com.actionbarsherlock.** { *; }
#-keep interface com.actionbarsherlock.** { *; }
#-keep public class * extends com.actionbarsherlock.ActionBarSherlock
#-keep public class com.actionbarsherlock.ActionBarSherlock
-dontwarn android.support.**
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

# -keep class com.nineoldandroids.** { *; }
# -keep interface com.nineoldandroids.** { *; }
# -keep class com.nineoldandroids.** { *; }
# -keep interface com.nineoldandroids.** { *; }
-keep class com.google.code.gson.** { *; }
-keep interface com.google.code.gson.** { *; }

## UMENG sdk config
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
#-libraryjars libs/SocialSDK_QQZone_2.jar
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep public class com.huijuan.passerby.R$*{
    public static final int *;
}

-dontwarn com.squareup.okhttp.**

-keep class com.huijuan.passerby.http.bean.** { *; }
-keep class com.huijuan.passerby.webview.PasserByJsInterface { *;}

