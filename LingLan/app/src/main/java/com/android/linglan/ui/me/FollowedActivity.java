package com.android.linglan.ui.me;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.FollowedAuthorFragment;
import com.android.linglan.fragment.FollowedTeamFragment;
import com.android.linglan.fragment.FollowedUpdateFragment;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/1/6 0006.
 * 我的关注
 */
public class FollowedActivity extends BaseActivity implements View.OnClickListener {
    private TextView followed_update;
    private TextView followed_author;
    private TextView followed_team;

    private FragmentManager fragmentManager;
    private FollowedUpdateFragment followedUpdateFragment;
    private FollowedAuthorFragment followedAuthorFragment;
    private FollowedTeamFragment followedTeamFragment;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_follwed);
    }

    @Override
    protected void initView() {
        followed_update = (TextView) findViewById(R.id.followed_update);
        followed_author = (TextView) findViewById(R.id.followed_author);
        followed_team = (TextView) findViewById(R.id.followed_team);

    }

    @Override
    protected void initData() {
        setTitle("我的关注", "");
        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
    }

    @Override
    protected void setListener() {
        followed_update.setOnClickListener(this);
        followed_author.setOnClickListener(this);
        followed_team.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.followed_update:
                setTabSelection(0);
                break;
            case R.id.followed_author:
                setTabSelection(1);
                break;
            case R.id.followed_team:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        followed_update.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));
        followed_author.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));
        followed_team.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));

    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */

    private void hideFragments(FragmentTransaction transaction) {
        if (followedUpdateFragment != null) {
            transaction.hide(followedUpdateFragment);
        }
        if (followedAuthorFragment != null) {
            transaction.hide(followedAuthorFragment);
        }
        if (followedTeamFragment != null) {
            transaction.hide(followedTeamFragment);
        }
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                followed_update.setTextColor(getResources().getColor(R.color.text_color_fragment_title));
                if (followedUpdateFragment == null) {
                    followedUpdateFragment = new FollowedUpdateFragment();
                    transaction.add(R.id.followed_content, followedUpdateFragment, "tag1");
                } else {
                    transaction.show(followedUpdateFragment);
                }
                break;
            case 1:
                followed_author.setTextColor(getResources().getColor(R.color.text_color_fragment_title));
                if (followedAuthorFragment == null) {
                    followedAuthorFragment = new FollowedAuthorFragment();
                    transaction.add(R.id.followed_content, followedAuthorFragment, "tag2");
                } else {
                    transaction.show(followedAuthorFragment);
                }
                break;
            case 2:
                followed_team.setTextColor(getResources().getColor(R.color.text_color_fragment_title));
                if (followedTeamFragment == null) {
                    followedTeamFragment = new FollowedTeamFragment();
                    transaction.add(R.id.followed_content, followedTeamFragment, "tag3");
                } else {
                    transaction.show(followedTeamFragment);
                }
                break;
        }
        transaction.commit();
    }
}
