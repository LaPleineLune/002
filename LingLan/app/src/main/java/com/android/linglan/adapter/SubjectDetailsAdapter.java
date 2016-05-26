package com.android.linglan.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.SubjectDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/3/23 0023.
 */
public class SubjectDetailsAdapter extends RecyclerView.Adapter {
    static final int VIEW_SUBJECT_TITLE = 0;
    static final int VIEW_SUBJECT_LIST = 1;
    private Activity context;
    private SubjectDetailsListAdapter subjectDetailsListAdapter;
    private String description;
    private String photo;
    private ArrayList<SubjectDetailsBean.SubjectData.SubjectList> subjectData;

    public SubjectDetailsAdapter(Activity context, String description, String photo) {
        this.description = description;
        this.photo = photo;
        this.context = context;
    }

    public void upDateAdapter(ArrayList<SubjectDetailsBean.SubjectData.SubjectList> subjectData) {
        this.subjectData = subjectData;
        notifyDataSetChanged();
    }

    // 专题详情的头部
    private View createSubjectTitleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_details_title, parent, false);
        return view;
    }

    //专题详情文章列表
    private View createSubjectListView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_details_list, parent, false);
        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_SUBJECT_TITLE:
                view = createSubjectTitleView(parent);
                break;
            case VIEW_SUBJECT_LIST:
                view = createSubjectListView(parent);
                break;
            default:
                break;
        }
        return new SubjectDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SubjectDetailsViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class SubjectDetailsViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ImageView img_subject_details;
        private TextView subject_description;

        private TagFlowLayout mFlowLayout;
        private RelativeLayout rl_history_search;
        private LinearLayout ll_no_history_search,ll_history_search;
        private View rootView;

        public SubjectDetailsViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            img_subject_details = (ImageView) rootView.findViewById(R.id.img_subject_details);
            subject_description = (TextView) rootView.findViewById(R.id.subject_description);
//            mFlowLayout = (TagFlowLayout) rootView.findViewById(R.id.id_flowlayout);
//            ll_no_history_search = (LinearLayout) rootView.findViewById(R.id.ll_no_history_search);
//            ll_history_search = (LinearLayout) rootView.findViewById(R.id.ll_history_search);
//            rl_history_search = (RelativeLayout) rootView.findViewById(R.id.rl_history_search);
        }

        TextView tv;
        private void bindData(int index) {
            switch (index) {
                case VIEW_SUBJECT_TITLE:
                    if (!TextUtils.isEmpty(description)) {
                        subject_description.setText(description);
                    } else {
                        subject_description.setVisibility(View.GONE);
                    }

                    try {// R.dimen.dp125    R.drawable.subject_details
                        ImageUtil.loadImageAsync(img_subject_details, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, R.drawable.default_image, photo, null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    break;
                case VIEW_SUBJECT_LIST:

                    RecyclerView rec_history_search = (RecyclerView) rootView.findViewById(R.id.rec_subject_details_list);
                    rec_history_search.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_history_search.setHasFixedSize(true);

                    subjectDetailsListAdapter = new SubjectDetailsListAdapter(context, subjectData);
                    rec_history_search.setAdapter(subjectDetailsListAdapter);
                    break;
                default:
                    break;
            }


        }

        @Override
        public void onClick(View v) {

        }
    }
}
