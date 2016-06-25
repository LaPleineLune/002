package com.android.linglan.ui.clinical;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.AlertDialogs;
import com.android.linglan.widget.AlertDialoginter;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.xutils.image.ImageOptions;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 病历详情页
 */
public class EditClinicalPictureClassifyActivity extends BaseActivity implements AlertDialoginter {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private int alertflag = 1000;
    private int isSave;
    private String edit;

    private RelativeLayout rl_clinical_details;
    private LinearLayout ll_no_network;
    private Intent intent = null;

    private PtrClassicFrameLayout recycler_view_clinical_details;
    private RecyclerView rec_clinical_details;
    private RecyclerAdapterWithHF mAdapter;

    private ClinicalPictureClassifyAdapter clinicalPictureClassifyAdapter;
    private ArrayList<String> mSelectPath;

    private File media;
    private String type;

    private ImageOptions options = new ImageOptions.Builder().build();

    private List<File> files = new ArrayList<>();
//    private String[] categorys;//处方:1;舌象:2;患处:3;其他:4
    public List<UpdataPhotoBeen> oldUpdataPhotoBeens;
    private List<UpdataPhotoBeen> updataPhotoBeens;
    private ClinicalDetailsBean.ClinicalDetailsData clinicalDetailsData;
    private UpdataPhotoBeen updataPhotoBeen;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
//                    rl_clinical_details.setVisibility(View.VISIBLE);
//                    ll_no_network.setVisibility(View.GONE);
                    intent.putExtra("updataPhotoBeens", (Serializable) updataPhotoBeens);
                    setResult(0, intent);
                    finish();
//
                    break;
                case REQUEST_FAILURE:
//                    rl_clinical_details.setVisibility(View.GONE);
//                    ll_no_network.setVisibility(View.VISIBLE);

                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_picture_classify);

    }

    @Override
    protected void initView() {
        rl_clinical_details = (RelativeLayout) findViewById(R.id.rl_clinical_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);

//        recycler_view_clinical_details = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_clinical_details);
        rec_clinical_details = (RecyclerView) findViewById(R.id.rec_clinical_details);
    }

    @Override
    protected void initData() {
        setTitle("图片分类", "");
        isSave = 0;
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        type = 0 + "";
        Intent intent = new Intent();
        clinicalDetailsData = new ClinicalDetailsBean.ClinicalDetailsData();
        oldUpdataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
        updataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
        clinicalDetailsData.img = (ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg>) getIntent().getSerializableExtra("clinicalDetailsData.img");

        if (clinicalDetailsData.img != null && clinicalDetailsData.img.size() != 0) {
            for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg clinicalDetailsImg : clinicalDetailsData.img) {
                updataPhotoBeen = new UpdataPhotoBeen();
                updataPhotoBeen.data.categoryname = clinicalDetailsImg.categoryname;
                updataPhotoBeen.data.mediaurl = clinicalDetailsImg.mediaurl;
                updataPhotoBeen.data.mediaid  = clinicalDetailsImg.mediaid;
                updataPhotoBeen.data.categoryid  = clinicalDetailsImg.categoryid;
                LogUtil.e("idididididi = " +clinicalDetailsImg.categoryid);
                oldUpdataPhotoBeens.add(updataPhotoBeen);
            }
        }else{
            oldUpdataPhotoBeens = (List<UpdataPhotoBeen>) getIntent().getSerializableExtra("oldUpdataPhotoBeens");
        }

        updataPhotoBeens.addAll(oldUpdataPhotoBeens);

//        categorys = new String[oldUpdataPhotoBeens.size()];

        rec_clinical_details.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_details.setHasFixedSize(true);
        clinicalPictureClassifyAdapter = new ClinicalPictureClassifyAdapter(this);
//        mAdapter = new RecyclerAdapterWithHF(clinicalPictureClassifyAdapter);
        rec_clinical_details.setAdapter(clinicalPictureClassifyAdapter);
//        clinicalPictureClassifyAdapter.updateAdapter(updataPhotoBeens);
    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
        back.setOnClickListener(this);
//        //下拉刷新
//        recycler_view_clinical_details.setPtrHandler(new PtrDefaultHandler() {
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                page = 1;
//                getClinicalDetailsList(sort, page);
////                clinicalDetailsAdapter = new ClinicalDetailsAdapter(ClinicalDetailsActivity.this);
////                mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
////                rec_clinical_details.setAdapter(mAdapter);
//
////                getPatientDetails();
//            }
//        });
//
//        //上拉刷新
//        recycler_view_clinical_details.setOnLoadMoreListener(new OnLoadMoreListener() {
//
//            @Override
//            public void loadMore() {
//                page++;
//                getClinicalDetailsList(sort, page);
//                recycler_view_clinical_details.loadMoreComplete(true);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:

                if (updataPhotoBeens != null && updataPhotoBeens.size() != 0) {
                    for (UpdataPhotoBeen updataPhotoBeen : updataPhotoBeens) {
                        editClinicalMultiMedia(updataPhotoBeen.data.categoryid, updataPhotoBeen.data.mediaid);
                        LogUtil.e("updataPhotoBeen = " + updataPhotoBeen.data.categoryid);
//                        LogUtil.e("updataPhotoBeen.data.mediaidupdataPhotoBeen.data.mediaid = " +updataPhotoBeen.data.categoryid);
//                        LogUtil.e("updataPhotoBeen.data.mediaid = " + updataPhotoBeen.data.mediaid);
                    }
                }

                break;
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.back:
                if (isSave == 1) {
                    alertflag = 1;
                    AlertDialogs.alert(this, "提示", "退出此次编辑？", "取消", "退出");
                } else {
                    intent.putExtra("updataPhotoBeens", (Serializable) oldUpdataPhotoBeens);
                    setResult(0, intent);
                    finish();
                }

                break;
        }
    }

    @Override
    public int Altert_btleftdo() {
        switch (alertflag) {
            case 1:
                AlertDialogs.aDialog.dismiss();
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public int Altert_btrightdo() {
        switch (alertflag) {
            case 1:
                oldUpdataPhotoBeens = null;
                intent.putExtra("updataPhotoBeens", (Serializable) oldUpdataPhotoBeens);
                setResult(0, intent);
                finish();
                break;
            default:
                break;
        }
        return 0;
    }

    public class ClinicalPictureClassifyAdapter extends RecyclerView.Adapter {
        private Activity context;
//        private List<UpdataPhotoBeen> updataPhotoBeens;

        public ClinicalPictureClassifyAdapter(Activity context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.item_clinical_picture_classify, parent, false);
            return new ClinicalPictureClassifyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ClinicalPictureClassifyViewHolder) holder).bindData(position);
        }

        @Override
        public int getItemCount() {
            return updataPhotoBeens == null ? 0 :updataPhotoBeens.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


//        public void updateAdapter(List<UpdataPhotoBeen> updataPhotoBeens) {
//            this.updataPhotoBeens = updataPhotoBeens;
//            this.notifyDataSetChanged();
//        }

        class ClinicalPictureClassifyViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {
            private View rootView;
            private ImageView img_item_picture;
            private RadioButton other;
            private RadioButton prescription;
            private RadioButton tongue_picture;
            private RadioButton affected_part;
            private RadioGroup rg_classify;

            public ClinicalPictureClassifyViewHolder(View rootView) {
                super(rootView);
                initView(rootView);
            }

            private void initView(View rootView) {
                this.rootView = rootView;
                img_item_picture = (ImageView) rootView.findViewById(R.id.img_item_picture);
                prescription = (RadioButton) rootView.findViewById(R.id.prescription);
                other = (RadioButton) rootView.findViewById(R.id.other);
                tongue_picture = (RadioButton) rootView.findViewById(R.id.tongue_picture);
                affected_part = (RadioButton) rootView.findViewById(R.id.affected_part);
                rg_classify = (RadioGroup) rootView.findViewById(R.id.rg_classify);
            }

            public void bindData(final int position) {

                ImageUtil.loadImageAsync(img_item_picture, R.dimen.dp76, R.dimen.dp76, R.drawable.default_image, updataPhotoBeens.get(position).data.mediaurl, null);

                if (updataPhotoBeens.get(position).data.categoryname.equals("其他")) {
                    rg_classify.check(other.getId());
                } else if (updataPhotoBeens.get(position).data.categoryname.equals("处方")) {
                    rg_classify.check(prescription.getId());
                } else if (updataPhotoBeens.get(position).data.categoryname.equals("舌象")) {
                    rg_classify.check(tongue_picture.getId());
                } else if (updataPhotoBeens.get(position).data.categoryname.equals("患处")) {
                    rg_classify.check(affected_part.getId());
                }


                rg_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        isSave = 1;
                        int radioButtonId = group.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) findViewById(radioButtonId);
                        String radioButtonLabel = rb.getText().toString();

                        if (radioButtonLabel.trim().equals("其他")) {
                            updataPhotoBeens.get(position).data.categoryname = "其他";
                            updataPhotoBeens.get(position).data.categoryid = 4 + "";
                        } else if (radioButtonLabel.trim().equals("处方")) {
                            updataPhotoBeens.get(position).data.categoryname = "处方";
                            updataPhotoBeens.get(position).data.categoryid = 1 + "";
                        } else if (radioButtonLabel.trim().equals("舌象")) {
                            updataPhotoBeens.get(position).data.categoryname = "舌象";
                            updataPhotoBeens.get(position).data.categoryid = 2 + "";
                        } else if (radioButtonLabel.trim().equals("患处")) {
                            updataPhotoBeens.get(position).data.categoryname = "患处";
                            updataPhotoBeens.get(position).data.categoryid = 3 + "";
                        }
                    }
                });
            }

            @Override
            public void onClick(View v) {

            }
        }
    }


    //编辑图片和分类
    public void editClinicalMultiMedia(String categoryid, String mediaid) {
        NetApi.editClinicalMultiMedia(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, EditClinicalPictureClassifyActivity.this)) {
                    return;
                }
//                LogUtil.e("已上传");
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
//                handler.sendEmptyMessage(REQUEST_FAILURE);
                ToastUtil.show("上传失败");
            }
        }, categoryid, mediaid);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSave == 1) {
                alertflag = 1;
                AlertDialogs.alert(this, "提示", "退出此次编辑？", "取消", "退出");
            } else {
                intent.putExtra("updataPhotoBeens", (Serializable) oldUpdataPhotoBeens);
                setResult(0, intent);
                finish();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
