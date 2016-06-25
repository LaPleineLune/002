package com.android.linglan.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.ClinicalListAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.http.bean.ClinicalCollatingBean;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalClassifyPictureActivity;
import com.android.linglan.ui.clinical.ClinicalCollatingActivity;
import com.android.linglan.ui.clinical.ClinicalCreateActivity;
import com.android.linglan.ui.clinical.ClinicalManageClassifyActivity;
import com.android.linglan.ui.clinical.ClinicalPhotographActivity;
import com.android.linglan.ui.clinical.ClinicalReferenceActivity;
import com.android.linglan.ui.clinical.ClinicalSearchActivity;
import com.android.linglan.ui.clinical.TCMSearchActivity;
import com.android.linglan.ui.clinical.WeichatFlupActivity;
import com.android.linglan.ui.me.ClipPictureActivity;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.utils.CameraUtil;
import com.android.linglan.utils.FaceFileUtil;
import com.android.linglan.utils.FaceImageUtil;
import com.android.linglan.utils.FaceUIUtil;
import com.android.linglan.utils.GuideViewUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.flowlayout.FlowLayout;
import com.android.linglan.widget.flowlayout.TagAdapter;
import com.android.linglan.widget.flowlayout.TagFlowLayout;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/6 0006.
 * 临证界面
 */
public class ClinicalFragment extends BaseFragment {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_SUCCESS_NO_CLASSIFY = 2;
    protected static final int REQUEST_SUCCESS_CLASSIFY = 3;
    private static final int REQUEST_PICTURE = 9;
    private static final int REQUEST_CLIP_PIC_RESULT = 11;
    public static int ISREFRESHDATA = 0;
    public static int ISLOGIN = 0;
    public static int position;
    public static int MaxPictureNumber;

    public String collatingNum = "0";
    private GuideViewUtil mGuideViewUtil;

    private PopupWindow popupWindow;
    private View popView;

    private Intent intent = null;
    private RelativeLayout rl_clinical;
    private LinearLayout ll_no_network;
    private Button bt_clinical_create;
    private Button bt_clinical_photograph;
    private TextView tv_clinical_order;
    private PtrClassicFrameLayout recycler_view_clinical;
    private RecyclerView rec_clinical;
    private RecyclerAdapterWithHF mAdapter;
    private ClinicalAdapter clinicalAdapter;
    private File mImageFile;

    private TagFlowLayout flowlayout_classify;
    private TextView tv_clinical_classify_no_content;

    private static final String ROOT_DIR = "linglan";

    private ClinicalCollatingBean clinicalCollatingBean;
    private ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData;
    private ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList;
    private int firstpageClinicalCollatingListSize;
    private int page = 1;//页码
    private String orderid;//排序 传参数 addtime按时间排序, count_view按统计排序，""为全部
    private String cateid;//分类 传参数分类id(subjectClassifyListBeans.cateid)，传""则返回全部

    private String type;
    private String categoryid;
    private UpdataPhotoBeen updataPhotoBeen;
    private List<UpdataPhotoBeen> updataPhotoBeens;
    public static boolean isAllClassify = true;
    private String classifyIdStr = "";
    private String oldClassifyIdStr = "";
    private String allcasecon = "0";
    private String classifyNameAll = "全部病历";
    private String classifyNameNew = "全部病历";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    rl_clinical.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);

                    break;
                case REQUEST_FAILURE:
                    rl_clinical.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_SUCCESS_NO_CLASSIFY:
                    tv_clinical_classify_no_content.setVisibility(View.VISIBLE);
                    flowlayout_classify.setVisibility(View.GONE);
                    break;
                case REQUEST_SUCCESS_CLASSIFY:
                    tv_clinical_classify_no_content.setVisibility(View.GONE);
                    flowlayout_classify.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (recycler_view_clinical == null) {
            recycler_view_clinical = (PtrClassicFrameLayout) rootView.findViewById(R.id.recycler_view_clinical);
        }

        //编辑或者新建了病历，刷新页面
        if (ISREFRESHDATA == 1 && ISLOGIN == 0) {
            recycler_view_clinical.refreshComplete();
            recycler_view_clinical.setLoadMoreEnable(true);
            recycler_view_clinical.loadMoreComplete(true);
            page = 1;
            ISREFRESHDATA = 0;

            if(!SharedPreferencesUtil.getString("token","").equals("")){
                if (isAllClassify) {
                    getClinicalList(page);
                } else {
                    getClassifyClinical(classifyIdStr, page);
                }
            }else{
                getClinicalList(1);
                isAllClassify = true;
            }

            getClinicalCollatingNum();
        } else if (ISREFRESHDATA == 2) {
            //删除了病历或者病程，刷新数据，在当前位置不动
            ISREFRESHDATA = 0;
            int count = clinicalCollatingList.size();
//            LogUtil.e("病历总数量 count = " + count );
//            LogUtil.e("第一页的数量 firstpageClinicalCollatingListSize = " + firstpageClinicalCollatingListSize );
//            LogUtil.e("删除的页码 position = " + position );
            if (position == 1) {
                clinicalCollatingList.clear();
            } else {
                if (count != 0) {
                    for (int i = count - 1; i > firstpageClinicalCollatingListSize - 1 + (position - 2) * 10; i--) {
                        clinicalCollatingList.remove(i);
//                        LogUtil.e("remove掉的那些item i = " + i);
                    }
                }
            }

            if (isAllClassify) {
                getClinicalList(position);
            } else {
                getClassifyClinical(classifyIdStr, position);
            }

            collatingNum = "0";
            getClinicalCollatingNum();
        }else if (ISLOGIN == 1) {
            //登录页面过来后刷新全部病例的第一页
            ISLOGIN = 0;
            getClinicalList(1);
            isAllClassify = true;
            collatingNum = "0";
            getClinicalCollatingNum();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_clinical, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();

        popView = LayoutInflater.from(getActivity()).inflate(R.layout.popupview_clinical_order, null);

        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }


    @Override
    protected void initView() {
        rl_clinical = (RelativeLayout) rootView.findViewById(R.id.rl_clinical);
        ll_no_network = (LinearLayout) rootView.findViewById(R.id.ll_no_network);
        bt_clinical_create = (Button) rootView.findViewById(R.id.bt_clinical_create);
        bt_clinical_photograph = (Button) rootView.findViewById(R.id.bt_clinical_photograph);
        tv_clinical_order = (TextView) rootView.findViewById(R.id.tv_clinical_order);
        recycler_view_clinical = (PtrClassicFrameLayout) rootView.findViewById(R.id.recycler_view_clinical);
        rec_clinical = (RecyclerView) rootView.findViewById(R.id.rec_clinical);
    }

    @Override
    protected void initData() {
        intent = new Intent();
        allcasecon = "0";
        collatingNum = "0";
        getClinicalCollatingNum();
        getMaxPictureNumber();
        popupWindow = new PopupWindow(getActivity());
        rec_clinical.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_clinical.setHasFixedSize(true);
        page = 1;
        updataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
        type = 0 + "";
        categoryid = 0 + "";

        if (isAllClassify) {
            getClinicalList(page);
        } else {
            getClassifyClinical(classifyIdStr, page);
        }
//        getClinicalList(page);
        clinicalAdapter = new ClinicalAdapter(getActivity());
        mAdapter = new RecyclerAdapterWithHF(clinicalAdapter);
        rec_clinical.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(this);
        bt_clinical_create.setOnClickListener(this);
        tv_clinical_order.setOnClickListener(this);
        bt_clinical_photograph.setOnClickListener(this);

        //下拉刷新
        recycler_view_clinical.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.ClinicalHomepageManualRefresh);
                recycler_view_clinical.refreshComplete();
                recycler_view_clinical.setLoadMoreEnable(true);
                recycler_view_clinical.loadMoreComplete(true);
                page = 1;
                if (isAllClassify) {
                    getClinicalList(page);
                } else {
                    getClassifyClinical(classifyIdStr, page);
                }
//                getClinicalList(page);
            }
        });

        //上拉刷新
        recycler_view_clinical.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.ClinicalHomepageManualRefresh);
                page++;
                if (isAllClassify) {
                    getClinicalList(page);
                } else {
                    getClassifyClinical(classifyIdStr, page);
                }
//                getClinicalList(page);
                recycler_view_clinical.loadMoreComplete(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.bt_clinical_create:
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.ClinicalNewMedicalHistory);
                if (NetApi.getToken() != null) {
                    intent.setClass(getActivity(), ClinicalCreateActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), RegisterActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.bt_clinical_photograph:
                intent.setClass(getActivity(), ClinicalPhotographActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_clinical_order:
                showPopup(v);
                break;
        }
    }

    private boolean verifyPermission(String permission) {
        return CameraUtil.isCameraEnable()
                && PackageManager.PERMISSION_GRANTED == getActivity().checkCallingOrSelfPermission(permission);
    }

    private void takePhoto() {

        if (!verifyPermission("android.permission.CAMERA")) {
            ToastUtil.show("摄像头打开失败，请检查设备并开放权限");
            return;
        }

        mImageFile = FaceFileUtil.getImageFile();
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "head.jpg")));

        startActivityForResult(intent, REQUEST_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQUEST_PICTURE:// 拍照
                    takePhotoResult();
                    break;
                default:
                    break;
            }
        }
    }

    private void takePhotoResult() {
        //图片是否需要旋转
        mImageFile = new File(Environment.getExternalStorageDirectory()
                + "/head.jpg");

        int degree = FaceImageUtil.getBitmapDegree(mImageFile.getAbsolutePath());
        if (degree != 0) {
            Bitmap bitmap = FaceImageUtil.getScaledBitmap(mImageFile.getAbsolutePath(), FaceUIUtil.getScreenWidth(), FaceUIUtil.getScreenHeight());
            bitmap = FaceImageUtil.rotateBitmapByDegree(bitmap, degree);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(mImageFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(getActivity(),
                ClinicalClassifyPictureActivity.class);
        intent.putExtra(ClipPictureActivity.TAG_URL, mImageFile.getAbsolutePath());
        startActivityForResult(intent, REQUEST_CLIP_PIC_RESULT);
    }

    private void showPopup(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        popView.measure(w, h);

        int height = popView.getMeasuredHeight();
        int width = popView.getMeasuredWidth();
        popupWindow = new PopupWindow(popView, width, (int) height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        int x = getResources().getDimensionPixelSize(R.dimen.dp14);
        int y = getResources().getDimensionPixelSize(R.dimen.dp14);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - v.getWidth() + x, location[1] + v.getHeight() - y);

    }

    private void getClinicalList(final int page) {
        NetApi.getClinicalList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("page = " + page + "   getClinicalList=" + result);
                recycler_view_clinical.refreshComplete();
                recycler_view_clinical.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    recycler_view_clinical.loadMoreComplete(false);
                    if (HttpCodeJugementUtil.code == 1 && page == 1) {
                        recycler_view_clinical.loadPage1MoreComplete();
                        clinicalAdapter.upDateAdapterTitle(classifyNameAll);
                        if (page == 1) {
                            clinicalAdapter.upDateAdapter(null, null);
                        }
                    }
                    return;
                }

                classifyNameAll = "全部病历";
                clinicalAdapter.upDateAdapterTitle(classifyNameAll);

                clinicalCollatingBean = JsonUtil.json2Bean(result, ClinicalCollatingBean.class);

                if (clinicalCollatingBean.data.list == null || (clinicalCollatingBean.data.list).size() == 0) {
                    if (page != 1) {
                        recycler_view_clinical.loadMoreComplete(false);
                    }
                }

//                if(clinicalCollatingBean.data.list == null || clinicalCollatingBean.data.list.size() == 0){
//                    recycler_view_clinical.loadMoreComplete(false);
//                }

                clinicalCollatingData = clinicalCollatingBean.data;
                if (page == 1) {
                    clinicalCollatingList = clinicalCollatingBean.data.list;
                    firstpageClinicalCollatingListSize = clinicalCollatingBean.data.list.size();
                    if (firstpageClinicalCollatingListSize <= 9) {
                        recycler_view_clinical.loadPage1MoreComplete();
                    } else {
                        recycler_view_clinical.loadMoreComplete(true);
                    }
                } else {
                    clinicalCollatingList.addAll(clinicalCollatingBean.data.list);
                }

                if (clinicalCollatingData != null && clinicalCollatingList.size() != 0) {
                    allcasecon = clinicalCollatingData.casecon;
                    clinicalAdapter.upDateAdapter(clinicalCollatingData, clinicalCollatingList);
                } else {
                    clinicalAdapter.upDateAdapter(clinicalCollatingData, null);
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
//                LogUtil.e(message);
                recycler_view_clinical.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, null, page + "", null, null);
    }

    private void getClassifyClinical(String classifyid, final int page) {
        NetApi.getClassifyClinical(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClassifyClinical=" + result);
                recycler_view_clinical.refreshComplete();
                recycler_view_clinical.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    recycler_view_clinical.loadMoreComplete(false);
                    if (HttpCodeJugementUtil.code == 1) {
                        classifyIdStr = oldClassifyIdStr;
                        clinicalAdapter.upDateAdapterTitle(classifyNameAll);
                    }
                    return;
                }
                classifyNameAll = classifyNameNew;
                clinicalCollatingBean = JsonUtil.json2Bean(result, ClinicalCollatingBean.class);

//                if (clinicalCollatingBean.data.list == null || (clinicalCollatingBean.data.list).size() == 0 || clinicalCollatingBean.data.list.size() <= 9) {
//                    if (page == 1) {
//                        recycler_view_clinical.loadPage1MoreComplete();
//                    } else {
//                        recycler_view_clinical.loadMoreComplete(false);
//                    }
//                }

                if(clinicalCollatingBean.data.list == null || clinicalCollatingBean.data.list.size() == 0){
                    recycler_view_clinical.loadMoreComplete(false);
                }

                clinicalCollatingData = clinicalCollatingBean.data;
                if (page == 1) {
                    clinicalCollatingList = clinicalCollatingBean.data.list;
                    firstpageClinicalCollatingListSize = clinicalCollatingBean.data.list.size();
                    if (firstpageClinicalCollatingListSize <= 9) {
                        recycler_view_clinical.loadPage1MoreComplete();
                    } else {
                        recycler_view_clinical.loadMoreComplete(false);
                    }
                } else {
                    clinicalCollatingList.addAll(clinicalCollatingBean.data.list);
                }

                if (clinicalCollatingData != null && clinicalCollatingList.size() != 0) {
                    clinicalAdapter.upDateAdapter(clinicalCollatingData, clinicalCollatingList);
                } else {
                    clinicalAdapter.upDateAdapter(clinicalCollatingData, null);
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
//                LogUtil.e(message);
                recycler_view_clinical.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, classifyid, page + "");
    }

    private void getClinicalCollatingNum() {
        NetApi.getClinicalCollatingNum(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalCollatingNum" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result);
                    collatingNum = json.getJSONObject("data").getString("unnamedcaseflag");
                    LogUtil.e("未命名病历的个数=" + collatingNum);
                    clinicalAdapter.CollatingNum(collatingNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void getAllClassify() {
        isAllClassify = true;
        page = 1;
        getClinicalList(page);
    }

    public void getClassify(String classifyid) {
        oldClassifyIdStr = classifyIdStr;
        classifyIdStr = classifyid;
        isAllClassify = false;
        page = 1;
        getClassifyClinical(classifyid, page);
    }

    /**
     * Created by LeeMy on 2016/4/8 0008.
     * 临症首页的Adapter
     */
    public class ClinicalAdapter extends RecyclerView.Adapter {
        static final int VIEW_CLINICAL_TITLE = 0;
        static final int VIEW_CLINICAL_LIST = 1;
        private Activity context;
        private ClinicalListAdapter clinicalListAdapter;
        private ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData;
        private ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList;
        private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
        private String collatingNum = "0";
        private String classifyName = "全部病历";

        private PopupWindow popupWindow;
        private View popView;

        public ClinicalAdapter(Activity context) {
            this.context = context;
        }

        public void CollatingNum(String collatingNum) {
            this.collatingNum = collatingNum;
            notifyDataSetChanged();
        }

        public void upDateAdapterTitle(String classifyName) {
            this.classifyName = classifyName;
            notifyDataSetChanged();
        }

        public void upDateAdapter(ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData, ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList) {
            this.clinicalCollatingData = clinicalCollatingData;
            this.clinicalCollatingList = clinicalCollatingList;
            notifyDataSetChanged();
        }

        // 临证的头部
        private View createClinicalTitleView(ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_title, parent, false);
            popView = LayoutInflater.from(context).inflate(R.layout.popupview_clinical_classify, null);
            return view;
        }

        // 临证列表
        private View createClinicalListView(ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_list, parent, false);
            return view;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case VIEW_CLINICAL_TITLE:
                    view = createClinicalTitleView(parent);
                    break;
                case VIEW_CLINICAL_LIST:
                    view = createClinicalListView(parent);
                    break;
                default:
                    break;
            }
            return new ClinicalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ClinicalViewHolder) holder).bindData(position);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class ClinicalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private int classifyFlag1 = 0;
            private int classifyFlag2 = 0;
            private int positionFlag1 = -1;
            private int positionFlag2 = -1;
            private String tagid1 = "0";
            private String tagid2 = "0";
            private String classifyNum = "0";

            private View rootView;
            private Intent intent = null;

            private Button btn_search;
            private TextView tv_clinical_reference;
            private TextView tv_weichat_flup;
            private TextView tv_tcm_search;
            private TextView tv_clinical_classify_type;
            private TextView tv_clinical_casecon;
            private View view_collating;
            private LinearLayout ll_search;
            private LinearLayout ll_clinical_collating;
            private LinearLayout ll_clinical_classify;

            private LinearLayout ll_pop_clinical_classify;
            private LinearLayout ll_clinical_classify_title;
            private RelativeLayout rl_pop_clinical_classify;
            private TextView tv_classify_num;
            private TextView tv_classify_all_num;
            private TextView tv_classify_ok;
            private TextView tv_classify1;
            private TextView tv_classify2;
            private TextView tv_all_classify;
            //            private TagFlowLayout flowlayout_classify;
//            private TextView tv_clinical_classify_no_content;
            private TextView tv;
            private TextView tv_manage_classify;

            private RecyclerView rec_clinical_list;

            private EditText filter_edit;

            public ClinicalViewHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                initView(rootView);
            }

            private void initView(View rootView) {
                popupWindow = new PopupWindow(context);
                btn_search = (Button) rootView.findViewById(R.id.btn_search);
                tv_clinical_reference = (TextView) rootView.findViewById(R.id.tv_clinical_reference);
                tv_weichat_flup = (TextView) rootView.findViewById(R.id.tv_weichat_flup);
                tv_tcm_search = (TextView) rootView.findViewById(R.id.tv_tcm_search);
                tv_clinical_classify_type = (TextView) rootView.findViewById(R.id.tv_clinical_classify_type);
                tv_clinical_casecon = (TextView) rootView.findViewById(R.id.tv_clinical_casecon);
                view_collating = rootView.findViewById(R.id.view_collating);
                ll_search = (LinearLayout) rootView.findViewById(R.id.ll_search);
                ll_clinical_collating = (LinearLayout) rootView.findViewById(R.id.ll_clinical_collating);
                ll_clinical_classify = (LinearLayout) rootView.findViewById(R.id.ll_clinical_classify);

                ll_pop_clinical_classify = (LinearLayout) popView.findViewById(R.id.ll_pop_clinical_classify);
                ll_clinical_classify_title = (LinearLayout) popView.findViewById(R.id.ll_clinical_classify_title);
                rl_pop_clinical_classify = (RelativeLayout) popView.findViewById(R.id.rl_pop_clinical_classify);
                tv_classify_num = (TextView) popView.findViewById(R.id.tv_classify_num);
                tv_classify_all_num = (TextView) popView.findViewById(R.id.tv_classify_all_num);
                tv_classify_ok = (TextView) popView.findViewById(R.id.tv_classify_ok);
                tv_classify1 = (TextView) popView.findViewById(R.id.tv_classify1);
                tv_classify2 = (TextView) popView.findViewById(R.id.tv_classify2);
                tv_classify1.setText("");
                tv_classify2.setText("");
                tagid1 = "0";
                tagid2 = "0";
                tv_all_classify = (TextView) popView.findViewById(R.id.tv_all_classify);
                flowlayout_classify = (TagFlowLayout) popView.findViewById(R.id.flowlayout_classify);
                tv_manage_classify = (TextView) popView.findViewById(R.id.tv_manage_classify);
                tv_clinical_classify_no_content = (TextView) popView.findViewById(R.id.tv_clinical_classify_no_content);

                rec_clinical_list = (RecyclerView) rootView.findViewById(R.id.rec_clinical_list);

                filter_edit = (EditText) rootView.findViewById(R.id.filter_edit);
            }

            private void bindData(int index) {
                intent = new Intent();
                switch (index) {
                    case VIEW_CLINICAL_TITLE:
                        if(clinicalCollatingData != null && clinicalCollatingData.tagnames != null && !clinicalCollatingData.tagnames.equals("")){
                            tv_clinical_classify_type.setText(clinicalCollatingData.tagnames);
                        }else{
                            tv_clinical_classify_type.setText(classifyName);
                        }
                        btn_search.setOnClickListener(this);
                        tv_clinical_reference.setOnClickListener(this);
                        tv_weichat_flup.setOnClickListener(this);
                        tv_tcm_search.setOnClickListener(this);
                        ll_search.setOnClickListener(this);
                        if (!collatingNum.equals("0")) {
                            ll_clinical_collating.setVisibility(View.VISIBLE);
                            view_collating.setVisibility(View.VISIBLE);
                        } else {
                            ll_clinical_collating.setVisibility(View.GONE);
                            view_collating.setVisibility(View.GONE);
                        }
                        if (clinicalCollatingData != null && clinicalCollatingData.casecon != null) {
                            tv_clinical_casecon.setText(clinicalCollatingData.casecon + "个");
                        } else {
                            tv_clinical_casecon.setText("0个");
                        }
                        ll_clinical_collating.setOnClickListener(this);
                        ll_clinical_classify.setOnClickListener(this);

                        ll_pop_clinical_classify.setOnClickListener(this);
                        tv_classify_ok.setOnClickListener(this);
                        tv_classify1.setOnClickListener(this);
                        tv_classify2.setOnClickListener(this);
                        tv_manage_classify.setOnClickListener(this);

                        tv_all_classify.setText("全部病历  " + allcasecon);
                        tv_all_classify.setOnClickListener(this);

                        filter_edit.setOnClickListener(this);

                        flowlayout_classify.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                            @Override
                            public boolean onTagClick(View view, int position, com.android.linglan.widget.flowlayout.FlowLayout parent) {
//                            if (ll_clinical_classify_title.isFocused())
                                ll_clinical_classify_title.setVisibility(View.GONE);

                                if (!rl_pop_clinical_classify.isFocused())
                                    rl_pop_clinical_classify.setVisibility(View.VISIBLE);
                                if (positionFlag1 == -1 || positionFlag2 == -1) {
                                    if ((positionFlag1 != position && positionFlag2 != position) && ((classifyFlag1 == 0 && classifyFlag2 == 0) || (classifyFlag1 == 0 && classifyFlag2 == 1))) {
                                        tv_classify1.setVisibility(View.VISIBLE);
//                                    tv_classify1.setText(hotsearch[position]);
//                                    tv_classify1.setText(clinicalClassifyData.get(position).tagname);
                                        tv_classify1.setText(data.get(position).tagname);
                                        tagid1 = data.get(position).tagid;
                                        classifyFlag1 = 1;
                                        positionFlag1 = position;
                                        if (classifyFlag1 == 0 && classifyFlag2 == 1) {
//                                            tagid = tagid1 + "," + tagid2;
                                            getClassifyClinicalNum(tagid1 + "," + tagid2);
//                                            tv_classify_num.setText("2个");
                                        } else {
//                                            tagid = tagid1;
                                            getClassifyClinicalNum(tagid1);
//                                            tv_classify_num.setText("1个");
                                        }
                                    } else if ((positionFlag2 != position && positionFlag1 != position) && (classifyFlag1 == 1 && classifyFlag2 == 0)) {
                                        tv_classify2.setVisibility(View.VISIBLE);
//                                    tv_classify2.setText(hotsearch[position]);
//                                    tv_classify2.setText(clinicalClassifyData.get(position).tagname);
                                        tv_classify2.setText(data.get(position).tagname);
                                        tagid2 = data.get(position).tagid;
                                        classifyFlag2 = 1;
                                        positionFlag2 = position;
//                                        tv_classify_num.setText("2个");
//                                        tagid = tagid1 + "," + tagid2;
                                        getClassifyClinicalNum(tagid1 + "," + tagid2);
                                    } else if (classifyFlag1 == 1 && classifyFlag2 == 1) {
                                        ToastUtil.show("最多只能选择两个标签");
                                    } else if ((positionFlag2 == -1 && positionFlag1 == position) || (positionFlag1 == -1 && positionFlag2 == position)) {
                                        ToastUtil.show("请选择不同标签");
                                    }
                                } else {
                                    if (classifyFlag1 == 1 && classifyFlag2 == 1) {
                                        ToastUtil.show("最多只能选择两个标签");
                                    } else {
                                        ToastUtil.show("请选择不同标签");
                                    }
                                }
//                            ToastUtil.show(hotsearch[position]);
//                view.setBackgroundResource(R.drawable.bg_bottom_textview);

                                return true;
                            }
                        });

                        break;
                    case VIEW_CLINICAL_LIST:
                        rec_clinical_list.setLayoutManager(new SyLinearLayoutManager(context));
                        rec_clinical_list.setHasFixedSize(true);
                        if (clinicalCollatingData != null) {
                            clinicalListAdapter = new ClinicalListAdapter(context, clinicalCollatingData, clinicalCollatingList);
                        } else {
                            clinicalListAdapter = new ClinicalListAdapter(context, null, null);
                        }
                        rec_clinical_list.setAdapter(clinicalListAdapter);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_search:
                        Intent intent1 = new Intent(context, ClinicalSearchActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.btn_search:
//                    ToastUtil.show("我是搜索");
                        String key = filter_edit.getText().toString().trim();
                        if (key != null && key.length() != 0) {
                            Intent intent = new Intent(context, ClinicalSearchActivity.class);
                            intent.putExtra("key", key);
                            context.startActivity(intent);
                        } else {
                            ToastUtil.show("请输入搜索内容");
                        }

                        break;
                    case R.id.tv_clinical_reference:
                        MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.ClinicalSelectReference);
                        intent.setClass(context, ClinicalReferenceActivity.class);
                        context.startActivity(intent);
                        break;
                    case R.id.tv_weichat_flup:
                        intent.setClass(context, WeichatFlupActivity.class);
                        context.startActivity(intent);
                        break;
                    case R.id.tv_tcm_search:
                        MobclickAgent.onEvent(context, UmengBuriedPointUtil.ClinicalSelectSoso);
                        intent.setClass(context, TCMSearchActivity.class);
                        context.startActivity(intent);
                        break;
                    case R.id.ll_clinical_collating:
                        intent.setClass(context, ClinicalCollatingActivity.class);
//                    intent.putExtra("collatingNum", collatingNum);
                        context.startActivity(intent);
                        break;
                    case R.id.ll_clinical_classify:
                        if (NetApi.getToken() != null) {
                            ll_clinical_classify.setFocusable(false);
                            ll_clinical_classify.setClickable(false);
                            tv_classify1.setText("");
                            tv_classify2.setText("");
                            tagid1 = "0";
                            tagid2 = "0";
//                            getClinicalClassify(v);
                            classifyNameAll = tv_clinical_classify_type.getText().toString();
                            showPopup(v);
                        } else {
                            intent.setClass(getActivity(), RegisterActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.tv_manage_classify:
                        MobclickAgent.onEvent(context, UmengBuriedPointUtil.ClinicalClassEdit);
                        intent.setClass(context, ClinicalManageClassifyActivity.class);
                        context.startActivity(intent);
                        popupWindow.dismiss();
                        ll_clinical_classify.setFocusable(true);
                        ll_clinical_classify.setClickable(true);
                        break;
                    case R.id.ll_pop_clinical_classify:
                        popupWindow.dismiss();
                        ll_clinical_classify.setFocusable(true);
                        ll_clinical_classify.setClickable(true);
                        break;
                    case R.id.tv_classify1:
                        tv_classify1.setVisibility(View.GONE);
                        tv_classify1.setText("");
                        tagid1 = "0";
                        classifyFlag1 = 0;
                        positionFlag1 = -1;
//                        tv_classify_num.setText("1个");
                        if (classifyFlag1 == 0 && classifyFlag2 == 0) {
                            ll_clinical_classify_title.setVisibility(View.VISIBLE);
                            rl_pop_clinical_classify.setVisibility(View.GONE);
                        } else {
                            getClassifyClinicalNum(tagid2);
                        }
                        break;
                    case R.id.tv_classify2:
                        tv_classify2.setVisibility(View.GONE);
                        tv_classify2.setText("");
                        tagid2 = "0";
                        classifyFlag2 = 0;
                        positionFlag2 = -1;
//                        tv_classify_num.setText("1个");
                        if (classifyFlag1 == 0 && classifyFlag2 == 0) {
                            ll_clinical_classify_title.setVisibility(View.VISIBLE);
                            rl_pop_clinical_classify.setVisibility(View.GONE);
                        } else {
                            getClassifyClinicalNum(tagid1);
                        }
                        break;
                    case R.id.tv_classify_ok:
                        MobclickAgent.onEvent(context, UmengBuriedPointUtil.ClinicalClassCheck);
                        if (classifyNum.equals("0")) {
                            ToastUtil.show("选择的分类没有病历，请重新选择");
                        } else {
                            popupWindow.dismiss();
                            ll_clinical_classify.setFocusable(true);
                            ll_clinical_classify.setClickable(true);
                            String tagid = "";

                            if (!tv_classify1.getText().toString().equals("") && !tv_classify2.getText().toString().equals("")) {
                                classifyName = tv_classify1.getText().toString() + " + " + tv_classify2.getText().toString();
                                tv_clinical_classify_type.setText(tv_classify1.getText().toString() + " + " + tv_classify2.getText().toString());
                                tagid = tagid1 + "," + tagid2;
                            } else {
                                classifyName = tv_classify1.getText().toString() + tv_classify2.getText().toString();
                                tv_clinical_classify_type.setText(tv_classify1.getText().toString() + tv_classify2.getText().toString());
                                if (!tagid1.equals("0")) {
                                    tagid = tagid1;
                                } else if (!tagid2.equals("0")) {
                                    tagid = tagid2;
                                }
                            }
                            classifyNameNew = classifyName;
                            getClassify(tagid);
                        }
                        break;
                    case R.id.tv_all_classify:
                        popupWindow.dismiss();
                        ll_clinical_classify.setFocusable(true);
                        ll_clinical_classify.setClickable(true);
//                        tv_clinical_classify_type.setText("全部病历");
                        classifyNameAll = "全部病历";
                        getAllClassify();
                        break;
                    case R.id.filter_edit:

                        break;
                }
            }

            private void showPopup(View v) {
                getClinicalClassify();
                ll_clinical_classify_title.setVisibility(View.VISIBLE);
                rl_pop_clinical_classify.setVisibility(View.GONE);
                tv_classify1.setVisibility(View.GONE);
                tagid1 = "0";
                tagid2 = "0";
                classifyFlag1 = 0;
                classifyFlag2 = 0;
                positionFlag1 = -1;
                positionFlag2 = -1;
                tv_classify2.setVisibility(View.GONE);
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                popView.measure(w, h);

                int height = popView.getMeasuredHeight();
                int width = popView.getMeasuredWidth();
                popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

//            popupWindow = new PopupWindow(popView, width, (int) height);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                int[] location = new int[2];
                v.getLocationOnScreen(location);

                int x = context.getResources().getDimensionPixelSize(R.dimen.dp14);
                int y = context.getResources().getDimensionPixelSize(R.dimen.dp14);

//            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - v.getWidth() + x, location[1] + v.getHeight() - y);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, getStatusBarHeight());

            }

            public int getStatusBarHeight() {
                int result = 0;
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = context.getResources().getDimensionPixelSize(resourceId);
                }
                return result;
            }

            final LayoutInflater mInflater = LayoutInflater.from(context);
            final List<ClinicalClassifyBean.ClinicalClassifyData> data = new ArrayList<ClinicalClassifyBean.ClinicalClassifyData>();

            private void getClinicalClassify() {
                NetApi.getClinicalClassify(new PasserbyClient.HttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        LogUtil.e("getClinicalClassify=" + result);
                        if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
                            ll_clinical_classify.setFocusable(true);
                            ll_clinical_classify.setClickable(true);
                            if (HttpCodeJugementUtil.code == 1) {
                                tv_classify_all_num.setText("共0个分类");
                                handler.sendEmptyMessage(REQUEST_SUCCESS_NO_CLASSIFY);
                            }
                            return;
                        }

                        handler.sendEmptyMessage(REQUEST_SUCCESS_CLASSIFY);
                        ClinicalClassifyBean clinicalClassifyBean = JsonUtil.json2Bean(result, ClinicalClassifyBean.class);

                        clinicalClassifyData = clinicalClassifyBean.data;

                        data.clear();
                        for (ClinicalClassifyBean.ClinicalClassifyData datas : clinicalClassifyData) {
                            if (!datas.userid.equals("0")) {
                                data.add(datas);
                            }
                        }
                        tv_classify_all_num.setText("共" + (data != null ? data.size() : 0) + "个分类");
//                    showPopup(v);

                        flowlayout_classify.setAdapter(new TagAdapter(data) {// clinicalClassifyData
                            @Override
                            public View getView(FlowLayout parent, int position, Object o) {
                                LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.item_clinical_classify,
                                        flowlayout_classify, false);
                                tv = (TextView) ll.findViewById(R.id.tv_classify_context);
                                tv.setText(data.get(position).tagname
                                        + "  " + data.get(position).cont);
                                tv.setTextSize(16);
                                Resources resource = context.getResources();
                                ColorStateList csl = resource.getColorStateList(R.color.french_grey);//no_text_color_fragment_title
                                tv.setTextColor(csl);
                                return ll;
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        ll_clinical_classify.setFocusable(true);
                        ll_clinical_classify.setClickable(true);
                    }
                });
            }

            private void getClassifyClinicalNum(String classifyid) {
                NetApi.getClassifyClinicalNum(new PasserbyClient.HttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        LogUtil.e("getClassifyClinicalNum=" + result);
                        if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
                            return;
                        }

                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            classifyNum = data;
                            tv_classify_num.setText(data + "个");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        tv_classify_num.setText("2个");
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                }, classifyid);
            }

        }
    }

    private void getMaxPictureNumber() {
        NetApi.getMaxPictureNumber(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    return;
                }
                try {
                    JSONObject data = new JSONObject(result);
                    MaxPictureNumber = data.getInt("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                ToastUtil.show("MaxPictureNumber = " + MaxPictureNumber);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}
