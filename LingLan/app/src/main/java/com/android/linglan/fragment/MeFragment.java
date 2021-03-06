package com.android.linglan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.ui.me.NewCollectActivity;
import com.android.linglan.ui.me.ProfileActivity;
import com.android.linglan.ui.R;
import com.android.linglan.ui.me.RadioDownloadActivity;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.ui.me.SettingActivity;
import com.android.linglan.ui.me.FeedbackActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.ShareUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.imageview.RoundedImageView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener{
    private static final int PROFILE_PANEL = 1;
    private static final int PROFILE_PANEL_NOT_LOGIN = 2;
    private View rootView;
    private final static String POSTCARD = "postcard";
    private final static String ATTEND = "attend";
    private final static String DONATE = "donate";
    private final static String COUPON = "coupon";

    private RoundedImageView avatar;// 是否是亲情会员
    private LinearLayout profile_panel,profile_panel_not_login;// 用户资料
    private LinearLayout ll_familymember;// 是否是亲情会员
//    private RelativeLayout my_followed;// 我的关注
    private RelativeLayout my_collect;// 我的收藏
    private RelativeLayout my_radio_download;// 我的下载
//    private RelativeLayout my_note;// 我的笔记
    private RelativeLayout feedback;// 意见反馈
    private RelativeLayout settings;// 设置
    private RelativeLayout share;// 推荐给朋友
    private TextView phonenum;

    @Override
    public void onResume() {
        super.onResume();
//    public void onStart() {
//        super.onStart();
        String token = SharedPreferencesUtil.getString("token", null);
        if (token != null) {
            profile_panel.setVisibility(View.VISIBLE);

            String url = SharedPreferencesUtil.getString("face", "");
            ImageUtil.loadImageAsync(avatar, url, R.drawable.avatar_default);

            String alias = SharedPreferencesUtil.getString("alias", null);
            String username =
                    // SharedPreferencesUtil.getString("alias", null) != null && !SharedPreferencesUtil.getString("alias", null).equals(""))
                    (!TextUtils.isEmpty(alias)) ? // username 的值为null 吗？
                            (alias.length() >= 9 ? alias.substring(0, 8) + "..." : alias) : // 不为空则为username 的值
                            SharedPreferencesUtil.getString("phone", null);// 为空则为 phone 的值
            phonenum.setText(username);
            if ("0".equals(SharedPreferencesUtil.getString("isfamilymember", "0"))) {
                ll_familymember.setVisibility(View.GONE);
            } else {
                ll_familymember.setVisibility(View.VISIBLE);
            }
            profile_panel_not_login.setVisibility(View.GONE);
        } else {
            profile_panel.setVisibility(View.GONE);
            profile_panel_not_login.setVisibility(View.VISIBLE);
        }
    }

    /** 此方法意思为fragment是否可见 ,可见时候加载数据 */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //fragment可见时加载数据
            String token = SharedPreferencesUtil.getString("token", null);
            if (token != null) {
                profile_panel.setVisibility(View.VISIBLE);

                String url = SharedPreferencesUtil.getString("face", "");
                ImageUtil.loadImageAsync(avatar, url, R.drawable.avatar_default);

                String alias = SharedPreferencesUtil.getString("alias", null);
                String username =
                        // SharedPreferencesUtil.getString("alias", null) != null && !SharedPreferencesUtil.getString("alias", null).equals(""))
                        (!TextUtils.isEmpty(alias)) ? // username 的值为null 吗？
                                (alias.length() >= 9 ? alias.substring(0, 8) + "..." : alias) : // 不为空则为username 的值
                                SharedPreferencesUtil.getString("phone", null);// 为空则为 phone 的值
                phonenum.setText(username);
                if ("0".equals(SharedPreferencesUtil.getString("isfamilymember", "0"))) {
                    ll_familymember.setVisibility(View.GONE);
                } else {
                    ll_familymember.setVisibility(View.VISIBLE);
                }
                profile_panel_not_login.setVisibility(View.GONE);
            } else {
                profile_panel.setVisibility(View.GONE);
                profile_panel_not_login.setVisibility(View.VISIBLE);
            }
        }else{
            //fragment不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
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
        avatar = (RoundedImageView) rootView.findViewById(R.id.avatar);
        profile_panel = (LinearLayout) rootView.findViewById(R.id.profile_panel);
        phonenum = (TextView) rootView.findViewById(R.id.phonenum);
        profile_panel_not_login = (LinearLayout) rootView.findViewById(R.id.profile_panel_not_login);
        ll_familymember = (LinearLayout) rootView.findViewById(R.id.ll_familymember);
//        my_followed = (RelativeLayout) rootView.findViewById(R.id.my_followed);
        my_collect = (RelativeLayout) rootView.findViewById(R.id.my_collect);
        my_radio_download = (RelativeLayout) rootView.findViewById(R.id.my_radio_download);
//        my_note = (RelativeLayout) rootView.findViewById(R.id.my_note);
        feedback = (RelativeLayout) rootView.findViewById(R.id.feedback);
        settings = (RelativeLayout) rootView.findViewById(R.id.settings);
        share = (RelativeLayout) rootView.findViewById(R.id.share);
    }

    @Override
    protected void initData() {
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
//        // 设置分享的内容
//        ShareUtil.setShareContent(getActivity());
    }

    @Override
    protected void setListener() {
        profile_panel_not_login.setOnClickListener(this);
        profile_panel.setOnClickListener(this);
//        my_followed.setOnClickListener(this);
        my_collect.setOnClickListener(this);
        my_radio_download.setOnClickListener(this);
//        my_note.setOnClickListener(this);
        feedback.setOnClickListener(this);
        settings.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_panel:
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.MyHome);
                startActivity(new Intent(getActivity(), ProfileActivity.class));
//                startActivityForResult(new Intent(getActivity(), ProfileActivity.class), PROFILE_PANEL);
                break;
            case R.id.profile_panel_not_login:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
//                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), PROFILE_PANEL_NOT_LOGIN);
//                finish();
                break;
//            case R.id.my_followed:
////                deleteMessage(ATTEND);
//                // followIndicator.setVisibility(View.GONE);
//                getActivity().startActivity(new Intent(getActivity(), FollowedActivity.class));
//                break;
            case R.id.my_collect:
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.MyClickCollect);
                if (SharedPreferencesUtil.getString("token", null) != null) {
                    startActivity(new Intent(getActivity(), NewCollectActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                }
                break;
            case R.id.my_radio_download:
                if (SharedPreferencesUtil.getString("token", null) != null) {
                    startActivity(new Intent(getActivity(), RadioDownloadActivity.class));
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
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.MyOpinion);
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.settings:
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.MySet);
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
