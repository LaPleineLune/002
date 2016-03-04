package com.android.linglan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.MainActivity;
import com.android.linglan.ui.me.CollectActivity;
import com.android.linglan.ui.me.FollowedActivity;
import com.android.linglan.ui.me.NewCollectActivity;
import com.android.linglan.ui.me.NoteActivity;
import com.android.linglan.ui.me.ProfileActivity;
import com.android.linglan.ui.R;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.ui.me.SettingActivity;
import com.android.linglan.ui.me.FeedbackActivity;
import com.android.linglan.utils.ShareUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener{
    private View rootView;
    private final static String POSTCARD = "postcard";
    private final static String ATTEND = "attend";
    private final static String DONATE = "donate";
    private final static String COUPON = "coupon";

    private LinearLayout profile_panel,profile_panel_not_login;// 用户资料
//    private RelativeLayout my_followed;// 我的关注
    private RelativeLayout my_collect;// 我的收藏
//    private RelativeLayout my_note;// 我的笔记
    private RelativeLayout feedback;// 意见反馈
    private RelativeLayout settings;// 设置
    private RelativeLayout share;// 推荐给朋友
    private TextView phonenum;


    @Override
    public void onResume() {
        super.onResume();
        String token = SharedPreferencesUtil.getString("token", null);
        if (token != null) {
            profile_panel.setVisibility(View.VISIBLE);
            String username =
                    (SharedPreferencesUtil.getString("username", null) != null && !SharedPreferencesUtil.getString("username", null).equals("")) ? // username 的值为null 吗？
                            SharedPreferencesUtil.getString("username", null) : // 不为空则为username 的值
                            SharedPreferencesUtil.getString("phone", null);// 为空则为 phone 的值
            phonenum.setText(username);
            profile_panel_not_login.setVisibility(View.GONE);
        } else {
            profile_panel.setVisibility(View.GONE);
            profile_panel_not_login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_me, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        profile_panel = (LinearLayout) rootView.findViewById(R.id.profile_panel);
        phonenum = (TextView) rootView.findViewById(R.id.phonenum);
        profile_panel_not_login = (LinearLayout) rootView.findViewById(R.id.profile_panel_not_login);
//        my_followed = (RelativeLayout) rootView.findViewById(R.id.my_followed);
        my_collect = (RelativeLayout) rootView.findViewById(R.id.my_collect);
//        my_note = (RelativeLayout) rootView.findViewById(R.id.my_note);
        feedback = (RelativeLayout) rootView.findViewById(R.id.feedback);
        settings = (RelativeLayout) rootView.findViewById(R.id.settings);
        share = (RelativeLayout) rootView.findViewById(R.id.share);

//        avatar = (ImageView) rootView.findViewById(R.id.avatar);
//        nickname = (TextView) rootView.findViewById(R.id.nickname);
//        noSupportingPrompt = (TextView) rootView.findViewById(R.id.no_supporting_prompt);
//        noCouponPrompt = (TextView) rootView.findViewById(R.id.no_coupon_prompt);
//        myFollowedCount = (TextView) rootView.findViewById(R.id.followed_count);
//        mySupportedCount = (TextView) rootView.findViewById(R.id.support_count);
//        myCouponsCount = (TextView) rootView.findViewById(R.id.coupon_count);
//        myAlbumCount = (TextView) rootView.findViewById(R.id.album_count);
//        supportPics = rootView.findViewById(R.id.supported_pics);
//        couponPics = rootView.findViewById(R.id.coupon_pics);
//
//        supportPic1 = (ImageView) rootView.findViewById(R.id.support_pic1);
//        supportPic2 = (ImageView) rootView.findViewById(R.id.support_pic2);
//        couponPic1 = (ImageView) rootView.findViewById(R.id.coupon_pic1);
//        couponPic2 = (ImageView) rootView.findViewById(R.id.coupon_pic2);
//
//        followIndicator = rootView.findViewById(R.id.followed_indicator);
//        supportIndicator = rootView.findViewById(R.id.support_indicator);
//        couponIndicator = rootView.findViewById(R.id.coupon_indicator);
//        albumIndicator = rootView.findViewById(R.id.album_indicator);
//
//
//        rootView.findViewById(R.id.my_supporting).setOnClickListener(this);
//        rootView.findViewById(R.id.my_coupon).setOnClickListener(this);
//        rootView.findViewById(R.id.my_album).setOnClickListener(this);
//
//
//
//        rootView.findViewById(R.id.test).setVisibility(View.GONE);
//        rootView.findViewById(R.id.test).setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        setTitle("我的", "");
//        String userid = SharedPreferencesUtil.getString("userid", null);
//        if (userid != null && !userid.equals("")) {
        String token = SharedPreferencesUtil.getString("token", null);
        if (token != null) {
            profile_panel.setVisibility(View.VISIBLE);
            String username =
                    (SharedPreferencesUtil.getString("username", null) != null) ? // username 的值为null 吗？
                    SharedPreferencesUtil.getString("username", null) : // 不为空则为username 的值
                    SharedPreferencesUtil.getString("phone", null);// 为空则为 phone 的值
            phonenum.setText(username);
            profile_panel_not_login.setVisibility(View.GONE);
        } else {
            profile_panel.setVisibility(View.GONE);
            profile_panel_not_login.setVisibility(View.VISIBLE);
        }
        //设置打印友盟log日志
        com.umeng.socialize.utils.Log.LOG = true;
        //关闭友盟toast提示
        ShareUtil.mController.getConfig().closeToast();
        // 配置需要分享的相关平台
        ShareUtil.configPlatforms(getActivity());
        // 设置分享的内容
        ShareUtil.setShareContent(getActivity());
    }

    @Override
    protected void setListener() {
        profile_panel_not_login.setOnClickListener(this);
        profile_panel.setOnClickListener(this);
//        my_followed.setOnClickListener(this);
        my_collect.setOnClickListener(this);
//        my_note.setOnClickListener(this);
        feedback.setOnClickListener(this);
        settings.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_panel:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.profile_panel_not_login:
//                AuthenticationActivity.show(getActivity());
                startActivity(new Intent(getActivity(), RegisterActivity.class));
//                finish();
                break;
//            case R.id.my_followed:
////                deleteMessage(ATTEND);
//                // followIndicator.setVisibility(View.GONE);
//                getActivity().startActivity(new Intent(getActivity(), FollowedActivity.class));
//                break;
            case R.id.my_collect:
//                deleteMessage(DONATE);
                // supportIndicator.setVisibility(View.GONE);
                if (SharedPreferencesUtil.getString("token", null) != null) {
                    startActivity(new Intent(getActivity(), NewCollectActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                }
                break;
//            case R.id.my_note:
//                // deleteMessage(COUPON);
//                // couponIndicator.setVisibility(View.GONE);
//                startActivity(new Intent(getActivity(), NoteActivity.class));
//                break;
            case R.id.feedback:
//                deleteMessage(POSTCARD);
                // albumIndicator.setVisibility(View.GONE);
//                Intent intent = new Intent();
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
//                intent.putExtra("title", getString(R.string.album));
//                startActivity(intent);
                break;
            case R.id.settings:
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.share:
//                ToastUtil.show("我是分享");
                ShareUtil.umengPlatforms(getActivity());
//                SnsUtil.postShare(getActivity(), ShareType.SETTING, null, null, null, null, null);
                break;
//            case R.id.test:
////                getActivity().startActivity(new Intent(getActivity(), TestActivity.class));
//                break;
            default:
                break;
        }
    }
}
