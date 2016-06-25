package com.android.linglan.ui.clinical;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ProgressUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.AlertDialogs;
import com.android.linglan.widget.AlertDialoginter;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 病历详情页
 */
public class ClinicalPictureClassifyActivity extends BaseActivity implements AlertDialoginter {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private int alertflag = 1000;

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
    int i;

    private UpdataPhotoBeen updataPhotoBeen;
    private List<UpdataPhotoBeen> updataPhotoBeens;

    private ImageOptions options = new ImageOptions.Builder().build();

    private List<File> files = new ArrayList<>();
    private String[] categorys;//处方:1;舌象:2;患处:3;其他:4
    public List<UpdataPhotoBeen> oldUpdataPhotoBeens;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ProgressUtil.dismiss();
//                    rl_clinical_details.setVisibility(View.VISIBLE);
//                    ll_no_network.setVisibility(View.GONE);
                    intent.putExtra("updataPhotoBeens", (Serializable) updataPhotoBeens);
                    setResult(0, intent);
                    finish();
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

        recycler_view_clinical_details = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_clinical_details);
        rec_clinical_details = (RecyclerView) findViewById(R.id.rec_clinical_details);
    }

    @Override
    protected void initData() {
        setTitle("图片分类", "");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        type = 0 + "";
        Intent intent = new Intent();

        mSelectPath = getIntent().getStringArrayListExtra("imgruls");

        categorys = new String[mSelectPath.size()];
        File file;
        for (String path : mSelectPath) {
            file = new File(path);
            files.add(file);
        }

        rec_clinical_details.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_details.setHasFixedSize(true);
        clinicalPictureClassifyAdapter = new ClinicalPictureClassifyAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(clinicalPictureClassifyAdapter);
        rec_clinical_details.setAdapter(mAdapter);
        clinicalPictureClassifyAdapter.updateAdapter(mSelectPath);
//        recycler_view_clinical_details.setOffsetToKeepHeaderWhileLoading(-1);
        updataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
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
                ProgressUtil.show(ClinicalPictureClassifyActivity.this, "图片上传中，请稍后...");
                updataPhotoBeens.clear();
//                i = 0;
                for (int i = 0; i < files.size(); i++) {
                    if (categorys[i] == null || categorys[i].equals("")) {
                        saveClinicalMultiMedia(files.get(i), type, 4 + "");
                    } else {
                        saveClinicalMultiMedia(files.get(i), type, categorys[i]);
                    }
                }

                break;
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.back:
                alertflag = 1;
                AlertDialogs.alert(this, "提示", "退出此次编辑？", "取消", "退出");
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
                intent.putExtra("updataPhotoBeens", (Serializable) updataPhotoBeens);
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
//        private ArrayList<String> mSelectPath;

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

            return mSelectPath != null ? mSelectPath.size() : 0;

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public void updateAdapter(ArrayList<String> mSelectPath) {
//            this.mSelectPath = mSelectPath;
            this.notifyDataSetChanged();
        }

        class ClinicalPictureClassifyViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {
            private View rootView;
            private ImageView img_item_picture;
            private RadioButton other;
            private RadioButton prescription;
            private RadioButton tongue_picture;
            private RadioButton affected_part;
            private RadioGroup rg_classify;
            private ArrayList<Bitmap> photoes;

            public ClinicalPictureClassifyViewHolder(View rootView) {
                super(rootView);
                initView(rootView);
            }

            private void initView(View rootView) {
                this.rootView = rootView;
                photoes = new ArrayList<>();
                img_item_picture = (ImageView) rootView.findViewById(R.id.img_item_picture);
                prescription = (RadioButton) rootView.findViewById(R.id.prescription);
                other = (RadioButton) rootView.findViewById(R.id.other);
                tongue_picture = (RadioButton) rootView.findViewById(R.id.tongue_picture);
                affected_part = (RadioButton) rootView.findViewById(R.id.affected_part);
                rg_classify = (RadioGroup) rootView.findViewById(R.id.rg_classify);
            }

            public void bindData(final int position) {
                x.image().bind(img_item_picture, mSelectPath.get(position), options);
//                photoes.clear();
//                for(String path : mSelectPath){
//                    LogUtil.e("pathpath-----------------:::" + path);
//                    Bitmap bm = GetLocalOrNetBitmap(path);
//                    Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 213, 213);
//                    photoes.add(thumbnail);
//                }
//                img_item_picture.setImageBitmap(photoes.get(position));
//                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 213, 213);
//                photoes.add(thumbnail);

//                ImageUtil.loadImageAsync(img_item_picture, R.dimen.dp76, R.dimen.dp76, R.drawable.default_image, mSelectPath.get(position), null);
                rg_classify.check(other.getId());

                rg_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int radioButtonId = group.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) findViewById(radioButtonId);
                        String radioButtonLabel = rb.getText().toString();

                        if (radioButtonLabel.trim().equals("其他")) {
                            categorys[position] = 4 + "";
                        } else if (radioButtonLabel.trim().equals("处方")) {
                            categorys[position] = 1 + "";
                        } else if (radioButtonLabel.trim().equals("舌象")) {
                            categorys[position] = 2 + "";
                        } else if (radioButtonLabel.trim().equals("患处")) {
                            categorys[position] = 3 + "";
                        }
                    }
                });
            }

                public  Bitmap GetLocalOrNetBitmap(String url)
                {
                    Bitmap bitmap = null;
                    InputStream in = null;
                    BufferedOutputStream out = null;
                    try
                    {
                        in = new BufferedInputStream(new URL(url).openStream(), 2*1024);
                        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                        out = new BufferedOutputStream(dataStream, 2*1024);
                        copy(in, out);
                        out.flush();
                        byte[] data = dataStream.toByteArray();
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        data = null;
                        return bitmap;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                }

            private  void copy(InputStream in, OutputStream out)
                    throws IOException {
                byte[] b = new byte[2*1024];
                int read;
                while ((read = in.read(b)) != -1) {
                    out.write(b, 0, read);
                }
            }



            @Override
            public void onClick(View v) {

            }
        }
    }

    //上传图片和分类
    public void saveClinicalMultiMedia(File media, String type, final String categoryid) {
        NetApi.saveClinicalMultiMedia(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalPictureClassifyActivity.this)) {
                    return;
                }
                updataPhotoBeen = JsonUtil.json2Bean(result, UpdataPhotoBeen.class);
//                LogUtil.e(updataPhotoBeen.data.toString());
                updataPhotoBeens.add(updataPhotoBeen);
                if (files.size() == updataPhotoBeens.size()) {
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                }
            }

            @Override
            public void onFailure(String message) {
//                handler.sendEmptyMessage(REQUEST_FAILURE);
                ProgressUtil.dismiss();
                ToastUtil.show(message);
            }
        }, media, type, categoryid);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//                ToastUtil.show("我要出来弹窗");
            alertflag = 1;
            AlertDialogs.alert(this, "提示", "退出此次编辑？", "取消", "退出");
            //                    clinicalCreate(patient, course);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
