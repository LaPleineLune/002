package com.android.linglan.utils;

/**
 * Created by LeeMy on 2016/5/27 0027.
 * 友盟埋点的值
 */
public class UmengBuriedPointUtil {
    // http://dev.umeng.com/analytics/android-doc/integration?spm=0.0.0.0.gD2z43#3_3_1
    // 用法：MobclickAgent.onEvent(this,"Forward");
    // MobclickAgent.onEvent(this, UmengBuriedPointUtil.ManualStudy);

    public static String ManualStudy = "manual_study";// 主动进入学习次数
    public static String StudyHomepageManualRefresh = "study_homepage_manual_refresh";// 学习首页主动刷新次数
    public static String StudySelectArticle = "study_select_character";// 学习进入文章数
    public static String StudySelectSubject = "study_select_subject";// 学习进入专题数
    public static String StudySelectMore = "study_select_more";// 学习进入更多数
    public static String StudySelectSearch = "study_select_search";// 学习进入搜索数
    public static String StudyClickCharacter = "study_click_character";// 学习内点击文章详情数
    public static String StudySubjectContent = "study_subject_content";// 学习内点击专题详情数  进入专题详情次数
    public static String StudySearchClickSearch = "study_search_click_search";// 学习内搜索次数
    public static String StudySearchSubject = "study_search_subject";// 学习内搜索出专题次数
    public static String StudySearchCharacter = "study_search_character";// 学习内搜索出文章次数
    public static String StudySubjectClickLike = "study_subject_click_like";// 学习内专题点赞次数
    public static String StudySubjectClickCollect = "study_subject_click_collect";// 学习内专题收藏次数
    public static String StudySubjectClickShareCircle = "study_subiect_click_share-circle";// 学习内专题分享次数
    public static String StudyCharacterClickLike = "study_character_click_like";// 学习内文章点赞次数
    public static String StudyCharacterClickCollect = "study_character_click_collect";// 学习内文章收藏次数
    public static String StudyCharacterClickShareFriend = "study_character_click_share-friend";// 学习内文章分享到微信个人
    public static String StudyCharacterClickShareCircle = "study_character_click_share-circle";// 学习内文章分享到微信朋友圈

    public static String ManualClinical = "manual_clinical";// 主动进入临证次数
    public static String ClinicalHomepageManualRefresh = "clinical_homepage_manual_refresh";// 临证首页主动刷新次数
    public static String ClinicalSelectReference = "clinical_select_reference";// 点击进入临证参考次数
    public static String ClinicalSelectSoso = "clinical_select_soso";// 点击进入中医搜搜次数
    public static String ClinicalReferenceSearch = "clinical_reference_search";// 临证参考搜索次数
//    public static String ClinicalReferenceClickCharacter = "clinical_reference_click_character";// 临证参考进入文章详情次数 1
    public static String ClinicalReferenceClickExplain = "clinical_reference_click_explain";// 临证参考页点击说明次数（问号）
    public static String ClinicalSosoSearch = "clinical_soso_search";// 中医搜搜点击搜索次数
    public static String ClinicalSelectSoso2 = "clinical_select_soso2";// 点击进入中医搜搜第2页面
    public static String ClinicalSearchMedicalHistory = "clinical_search_medical-history";// 临证搜索病历次数
    public static String ClinicalNewMedicalHistory = "clinical_new_medical-history";// 临证点击新建病历按钮次数
    public static String ClinicalNewMedicalHistorySave = "clinical_new_medical-history_save";// 临证新建病历点击保存次数
    public static String ClinicalNewChangeTemplate = "clinical_new_change-template";// 临证新建病历点击模版次数 (暂不接)
    public static String ClinicalClassCheck = "clinical_class_check";// 临证分类点击确定查看次数
    public static String ClinicalClassEdit = "clinical_class_edit";// 临证分类管理分类
    public static String ClinicalUnnamedRelevance = "clinical_unnamed_relevance";// 临证未命名病历点击关联按钮
    public static String ClinicalUnnamedRelevanceSucceed = "clinical_unnamed_relevance_succeed";// 临证未命名病历关联成功
    public static String ClinicalUnnamedDelete = "clinical_unnamed_delete";// 临证未命名病历删除
    public static String ClinicalMedicalHistoryAdd = "clinical_medicalhistory_add";// 临证病历点击添加病程按钮次数
    public static String ClinicalMedicalHistoryAddSucceed = "clinical_medicalhistory_add_succeed";// 临证病历成功添加病程
    public static String ClinicalMedicalHistoryPhotoContrast = "clinical_medicalhistory_photo-contrast";// 临证病历图片对比按钮点击次数
    public static String ClinicalMedicalHistoryClass = "clinical_medicalhistory_class";// 临证病历详情点击病例分类按钮次数
    public static String ClinicalMedicalHistoryClassSucceed = "clinical_medicalhistory_class_succeed";// 临证病历详情成功分配病历分类
    public static String ClinicalMedicalHistorySort = "clinical_medicalhistory_sort";// 临证病历详情病程排序点击数
    public static String ClinicalMedicalHistoryDelete = "clinical_medicalhistory_delete";// 临证病历详情删除病历次数
    public static String ClinicalMedicalHistoryClickExample = "clinical_medicalhistory_click-example";// 临证点击查看示例病历
    public static String ClinicalMedicalHistoryDeleteExample = "clinical_medicalhistory_delete-example";// 临证成功删除示例病例

    public static String ManualMy = "manual_my";// 主动进入我的次数
    public static String MyHome = "my_home";// 个人中心点击次数
    public static String MyHomeIcon = "my_home_icon";// 成功更换头像次数
    public static String MyHomeNickname = "my_home_nickname";// 成功保存用户昵称次数
    public static String MyHomeName = "my_home_nickname";// 成功保存真实姓名次数
    public static String MyHomeMessage = "my_home_message";// 成功保存个人简介次数
    public static String MyHomeCity = "my_home_city";// 成功保存所在城市次数
    public static String MyHomeCompany = "my_home_company";// 成功保存工作单位次数
    public static String MyClickCollect = "my_click_collect";// 我的点击进入我的收藏次数
    public static String MySet = "my_set";// 我的点击进入设置次数
    public static String MyWipeCache = "my_wipe-cache";// 点击清理缓存次数
    public static String MyFont = "my_font";// 我的成功调整字体大小次数
    public static String MyUpdate = "my_update";// 检查更新点击次数
    public static String MyAbout = "my_about";// 关于我们点击次数
    public static String MyExit = "my_exit";// 退出登录次数
    public static String MyOpinion = "my_opinion";// 意见反馈点击次数
    public static String MyOpinionSubmit = "my_opinion_submit";// 意见反馈成功提交次数

}
