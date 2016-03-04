package com.android.linglan.ui.me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by LeeMy on 2016/1/22 0022.
 * 修改用户昵称，用户真实姓名，用户工作单位
 */
public class ChangeNameActivity extends BaseActivity {
    private String changeNameTitle;
    private String name;
    private EditText change_name;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_change_name);

    }

    @Override
    protected void initView() {
        change_name = (EditText) findViewById(R.id.change_name);

    }

    @Override
    protected void initData() {
        changeNameTitle = getIntent().getStringExtra("nameFlag");
        name = getIntent().getStringExtra("name");
        setTitle(changeNameTitle, "保存");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        if (name == null || name.equals("")) {
//            if (changeNameTitle.equals("用户昵称")) {
//                change_name.setHint("请输入用户昵称");
//            } else if (changeNameTitle.equals("真实姓名")) {
//                change_name.setHint("请输入真实姓名");
//            } else if (changeNameTitle.equals("工作单位")) {
//                change_name.setHint("请输入工作单位");
//            }
            change_name.setHint("请输入" + changeNameTitle);
        } else {
//            if (changeNameTitle.equals("用户昵称")) {
//                change_name.setHint("请输入用户昵称");
//            } else if (changeNameTitle.equals("真实姓名")) {
//                change_name.setHint("请输入真实姓名");
//            } else if (changeNameTitle.equals("工作单位")) {
//                change_name.setHint("请输入工作单位");
//            }
            change_name.setHint("请输入" + changeNameTitle);
            change_name.setText(name);
            change_name.setSelection(name.length());
        }
    }

    @Override
    protected void setListener() {
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = change_name.getText().toString().trim();
                if (!text.isEmpty()) {
                    if (changeNameTitle.equals("用户昵称")) {
                        getUserInfoEdit(text, null, null);
                    } else if (changeNameTitle.equals("真实姓名")) {
                        getUserInfoEdit(null, text, null);
                    } else if (changeNameTitle.equals("工作单位")) {
                        getUserInfoEdit(null, null, text);
                    }
                } else {
                    ToastUtil.show("请输入" + changeNameTitle);
                }
            }
        });
    }

    /**
     * 用户信息修改
     * @param alias 用户昵称(post)
     * @param name  真实姓名(post)
     */
    private void getUserInfoEdit(final String alias, final String name, final String company){
        NetApi.getUserInfoEdit(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                ToastUtil.show("保存成功");
                Intent intent = new Intent();
                if (alias != null) {
                    intent.putExtra("nickname", alias);
                } else if (name != null) {
                    intent.putExtra("userName", name);
                } else if (company != null) {
                    intent.putExtra("companyName", company);
                }
                setResult(RESULT_OK, intent);
                finish();

            }

            @Override
            public void onFailure(String message) {

            }
        }, alias, name, null, null, company, null);
    }
}
