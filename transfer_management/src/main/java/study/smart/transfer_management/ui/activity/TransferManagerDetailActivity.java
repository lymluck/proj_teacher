package study.smart.transfer_management.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.smartstudy.annotation.Route;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.entity.Privileges;
import study.smart.baselib.listener.OnSendMsgDialogClickListener;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.PagerSlidingTabStrip;
import study.smart.baselib.ui.widget.dialog.AppBasicDialog;
import study.smart.baselib.ui.widget.dialog.DialogCreator;
import study.smart.baselib.ui.widget.dialog.LoadingDialog;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.OptionSuccess;
import study.smart.transfer_management.entity.TransferDetailentity;
import study.smart.transfer_management.mvp.contract.TransferManagerDetailContract;
import study.smart.transfer_management.mvp.presenter.TransferManagerDetailPresenter;
import study.smart.transfer_management.ui.adapter.XxdTransferDetailFragmentAdapter;
import study.smart.transfer_management.ui.fragment.ContractInformationFragment;
import study.smart.transfer_management.ui.fragment.UserBackgroundFragment;

/**
 * @author yqy
 * @date on 2018/6/27
 * @describe 详情界面
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerDetailActivity extends BaseActivity<TransferManagerDetailContract.Presenter> implements TransferManagerDetailContract.View {
    //结案按钮
    private TextView tvOver;
    //驳回按钮
    private TextView tvTurnDown;
    //分配中心
    private TextView tvDistributionCenter;
    //中间分割线
    private View vLine;
    private LinearLayout llBottom;
    private String modelName;
    private String orderState;

    private LinearLayout llNoDistributio;
    //重启
    private TextView tvReboot;

    private String id;

    private AppBasicDialog overDialog;

    private AppBasicDialog rebootDialog;

    private AppBasicDialog turnDownDialog;

    private List<TransferDetailentity.Meta.CenterList> centerLists = new ArrayList<>();

    private List<TransferDetailentity.Meta.SoftTeachers> softTeachers = new ArrayList<>();

    private List<TransferDetailentity.Meta.HardTeachers> hardTeachers = new ArrayList<>();

    private OptionsPickerView unallocatedOptionPicker;

    private OptionsPickerView teacherOptionPicker;

    private ViewPager pagerNews;

    private PagerSlidingTabStrip newTabs;

    private LinearLayout llService;

    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_manager_detail);

    }

    @Override
    public TransferManagerDetailContract.Presenter initPresenter() {
        return new TransferManagerDetailPresenter(this);
    }

    @Override
    public void initView() {
        loadingDialog = new LoadingDialog(this, "正在处理...", R.drawable.ic_dialog_loading);
        modelName = getIntent().getStringExtra("model");
        orderState = getIntent().getStringExtra("order_state");
        id = getIntent().getStringExtra("id");
        pagerNews = rootView.findViewById(R.id.pager_news);
        newTabs = rootView.findViewById(R.id.tabs_news);
        tvOver = findViewById(R.id.tv_over);
        tvTurnDown = findViewById(R.id.tv_turn_down);
        tvDistributionCenter = findViewById(R.id.tv_distribution_center);
        vLine = findViewById(R.id.v_line);
        llBottom = findViewById(R.id.ll_bottom);
        llNoDistributio = findViewById(R.id.ll_no_distributio);
        tvReboot = findViewById(R.id.tv_reboot);
        llService = findViewById(R.id.ll_service);
        //权限判断,判断下面按钮的显示与隐藏
        bottomControl();
        //获取个人信息
        getTransferDetail();

    }

    public void getTransferDetail() {
        if (getString(R.string.un_allocate_center).equals(modelName)) {
            presenter.getUnallocated(id);
        } else if (getString(R.string.allocate_center).equals(modelName)) {
            presenter.getAllocated(id);
        } else if (getString(R.string.turn_down_case).equals(modelName)) {
            presenter.getRejectedCenter(id);
        } else if (getString(R.string.un_allocate_teacher).equals(modelName)) {
            presenter.getUnallocatedCoachl(id);
        } else {
            presenter.getAllocatedCoachl(id);
        }
    }

    public void bottomControl() {
        String transferPerssion = (String) SPCacheUtils.get("privileges", "");
        //因为模块权限前面已经判定过了，这里就不重复判定了
        Privileges privileges = JSONObject.parseObject(transferPerssion, Privileges.class);
        //如果是分为配中心的模块
        if (privileges != null) {
            llBottom.setVisibility(View.VISIBLE);
            if (getString(R.string.un_allocate_center).equals(modelName)) {
                //判断订单状态,未分配中心状态
                if (getString(R.string.un_allocate_center).equals(orderState)) {
                    llNoDistributio.setVisibility(View.VISIBLE);
                    tvReboot.setVisibility(View.GONE);
                    //判断是否含有结案权限
                    if (privileges.isUnallocatedClose()) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }
                    //判断是否有驳回权限
                    if (privileges.isUnallocatedRejected()) {
                        tvTurnDown.setVisibility(View.VISIBLE);
                    } else {
                        tvTurnDown.setVisibility(View.GONE);
                    }
                    //判断是否含有分配权限
                    if (privileges.isUnallocatedAllocateCenter()) {
                        tvDistributionCenter.setVisibility(View.VISIBLE);
                        tvOver.setTextColor(Color.parseColor("#58646E"));
                        tvTurnDown.setTextColor(Color.parseColor("#58646E"));
                    } else {
                        tvDistributionCenter.setVisibility(View.GONE);
                        //当不存在分配中心的时候，结案和驳回的字体颜色需要更改
                        tvOver.setTextColor(Color.parseColor("#078CF1"));
                        tvTurnDown.setTextColor(Color.parseColor("#078CF1"));
                    }
                    //判断中间分割线是否存在，当且仅当结案根驳回都存在的时候，分割线存在
                    if (privileges.isUnallocatedClose() && privileges.isUnallocatedRejected()) {
                        vLine.setVisibility(View.VISIBLE);
                    } else {
                        vLine.setVisibility(View.GONE);
                    }
                } else {
                    //已结案状态,判断是否含有重启权限
                    llNoDistributio.setVisibility(View.GONE);
                    if (privileges.isUnallocatedReopen()) {
                        tvReboot.setVisibility(View.VISIBLE);
                    } else {
                        tvReboot.setVisibility(View.GONE);
                    }
                }
            } else if (getString(R.string.allocate_center).equals(modelName)) {
                if (getString(R.string.closed).equals(orderState)) {
                    //结案可以看到重启按钮
                    llNoDistributio.setVisibility(View.GONE);
                    if (privileges.isAllocatedReopen()) {
                        tvReboot.setVisibility(View.VISIBLE);
                    } else {
                        tvReboot.setVisibility(View.GONE);
                    }
                } else {
                    llNoDistributio.setVisibility(View.VISIBLE);
                    tvReboot.setVisibility(View.GONE);
                    //服务状态和选导师状态
                    //结案权限
                    if (privileges.isAllocatedClose()) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }
                    //已分配中心不显示驳回按钮
                    tvTurnDown.setVisibility(View.GONE);
                    vLine.setVisibility(View.GONE);
                    //判断是否含有分配权限
                    if (privileges.isAllocatedAssigned()) {
                        if (getString(R.string.in_service).equals(orderState)) {
                            tvDistributionCenter.setText(R.string.redistribution_teacher);
                        } else {
                            tvDistributionCenter.setText(R.string.distribution_teacher);
                        }
                        tvDistributionCenter.setVisibility(View.VISIBLE);
                        tvOver.setTextColor(Color.parseColor("#58646E"));
                        tvTurnDown.setTextColor(Color.parseColor("#58646E"));
                    } else {
                        tvDistributionCenter.setVisibility(View.GONE);
                        //当不存在分配中心的时候，结案和驳回的字体颜色需要更改
                        tvOver.setTextColor(Color.parseColor("#078CF1"));
                        tvTurnDown.setTextColor(Color.parseColor("#078CF1"));
                    }
                }
            } else if (getString(R.string.turn_down_case).equals(modelName)) {
                if (getString(R.string.closed).equals(orderState)) {
                    llNoDistributio.setVisibility(View.GONE);
                    if (privileges.isRejectedReopen()) {
                        tvReboot.setVisibility(View.VISIBLE);
                    } else {
                        tvReboot.setVisibility(View.GONE);
                    }
                } else {
                    //判断是否有驳回按钮
                    llNoDistributio.setVisibility(View.VISIBLE);
                    tvReboot.setVisibility(View.GONE);
                    if (privileges.isRejectedRejected()) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }

                    //判断是否有结案权限
                    if (privileges.isRejectedClose()) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }

                    //判断是否含有分配权限
                    if (privileges.isRejectedAllocateCenter()) {
                        tvDistributionCenter.setVisibility(View.VISIBLE);
                        tvOver.setTextColor(Color.parseColor("#58646E"));
                        tvTurnDown.setTextColor(Color.parseColor("#58646E"));
                    } else {
                        tvDistributionCenter.setVisibility(View.GONE);
                        //当不存在分配中心的时候，结案和驳回的字体颜色需要更改
                        tvOver.setTextColor(Color.parseColor("#078CF1"));
                        tvTurnDown.setTextColor(Color.parseColor("#078CF1"));
                    }

                    if (privileges.isRejectedRejected() && privileges.isRejectedClose()) {
                        vLine.setVisibility(View.VISIBLE);
                    } else {
                        vLine.setVisibility(View.GONE);
                    }
                }
            } else if (getString(R.string.un_allocate_teacher).equals(modelName)) {
                //未分配导师的权限
                if (getString(R.string.closed).equals(orderState)) {
                    llNoDistributio.setVisibility(View.GONE);
                    if (privileges.isUnassignedReopen()) {
                        tvReboot.setVisibility(View.VISIBLE);
                    } else {
                        tvReboot.setVisibility(View.GONE);
                    }
                } else {
                    //判断是否有驳回按钮
                    llNoDistributio.setVisibility(View.VISIBLE);
                    tvReboot.setVisibility(View.GONE);
                    if (privileges.isUnassignedRejected()) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }

                    //判断是否有结案权限
                    if (privileges.isUnassignedClose()) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }

                    //判断是否含有分配权限
                    if (privileges.isUnassignedAssigned()) {
                        tvDistributionCenter.setText(getString(R.string.distribution_teacher));
                        tvDistributionCenter.setVisibility(View.VISIBLE);
                        tvOver.setTextColor(Color.parseColor("#58646E"));
                        tvTurnDown.setTextColor(Color.parseColor("#58646E"));
                    } else {
                        tvDistributionCenter.setVisibility(View.GONE);
                        //当不存在分配中心的时候，结案和驳回的字体颜色需要更改
                        tvOver.setTextColor(Color.parseColor("#078CF1"));
                        tvTurnDown.setTextColor(Color.parseColor("#078CF1"));
                    }

                    if (privileges.isUnassignedRejected() && privileges.isUnassignedClose()) {
                        vLine.setVisibility(View.VISIBLE);
                    } else {
                        vLine.setVisibility(View.GONE);
                    }
                }
            } else {
                //已分配导师
                //驳回按钮不显示
                tvTurnDown.setVisibility(View.GONE);
                vLine.setVisibility(View.VISIBLE);
                if (getString(R.string.closed).equals(orderState)) {
                    llNoDistributio.setVisibility(View.GONE);
                    if (privileges.isAssignedReopen()) {
                        tvReboot.setVisibility(View.VISIBLE);
                    } else {
                        tvReboot.setVisibility(View.GONE);
                    }
                } else {
                    //判断是否有结案权限
                    llNoDistributio.setVisibility(View.VISIBLE);
                    tvReboot.setVisibility(View.GONE);
                    if (privileges.isAssignedClose()) {
                        tvOver.setVisibility(View.VISIBLE);
                    } else {
                        tvOver.setVisibility(View.GONE);
                    }

                    //判断是否含有分配权限
                    if (privileges.isAssignedAssigned()) {
                        if (getString(R.string.in_service).equals(orderState)) {
                            tvDistributionCenter.setText(getString(R.string.redistribution_teacher));
                        } else {
                            tvDistributionCenter.setText(getString(R.string.distribution_teacher));
                        }
                        tvDistributionCenter.setVisibility(View.VISIBLE);
                    } else {
                        tvDistributionCenter.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            llBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        tvReboot.setOnClickListener(this);
        tvOver.setOnClickListener(this);
        tvTurnDown.setOnClickListener(this);
        tvDistributionCenter.setOnClickListener(this);
        topdefaultLeftbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_over) {
            //结案
            if (overDialog == null) {
                overDialog = DialogCreator.createAppBasicDialog(this, "", "确定结案吗？",
                    getString(R.string.sure), getString(R.string.cancle), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i1 = v.getId();
                            if (i1 == R.id.positive_btn) {
                                if (loadingDialog != null && !loadingDialog.isShowing()) {
                                    loadingDialog.show();
                                }
                                if (getString(R.string.un_allocate_center).equals(modelName)) {
                                    presenter.closeTransferCase(id);
                                } else if (getString(R.string.allocate_center).equals(modelName)) {
                                    presenter.closeTransferAllocatedCenter(id);
                                } else if (getString(R.string.turn_down_case).equals(modelName)) {
                                    presenter.closeRejectCenter(id);
                                } else if (getString(R.string.un_allocate_teacher).equals(modelName)) {
                                    presenter.closeUnallocatedCoac(id);
                                } else {
                                    presenter.closeAllocatedCoac(id);
                                }
                                overDialog.dismiss();
                            } else {
                                overDialog.dismiss();
                            }
                        }
                    });
                ((TextView) overDialog.findViewById(R.id.dialog_info)).setGravity(Gravity.CENTER);
                WindowManager.LayoutParams p = overDialog.getWindow().getAttributes();
                p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
                overDialog.getWindow().setAttributes(p);
                overDialog.show();
            } else {
                overDialog.show();
            }
        } else if (i == R.id.tv_turn_down) {
            //驳回转案
            if (turnDownDialog == null) {
                turnDownDialog = DialogCreator.createTransferTurnDownDialog(this, new OnSendMsgDialogClickListener() {
                    @Override
                    public void onPositive(String word) {
                        if (TextUtils.isEmpty(word)) {
                            ToastUtils.shortToast("请输入驳回理由");
                            return;
                        }
                        if (loadingDialog != null && !loadingDialog.isShowing()) {
                            loadingDialog.show();
                        }
                        if (getString(R.string.un_allocate_center).equals(modelName)) {
                            presenter.rejectTransferCase(id, word);
                        } else if (getString(R.string.turn_down_case).equals(modelName)) {
                            presenter.rejectRejectCenter(id, word);
                        } else {
                            presenter.rejectUnallocatedCoach(id, word);
                        }
                        turnDownDialog.dismiss();
                    }

                    @Override
                    public void onNegative() {
                        turnDownDialog.dismiss();
                    }
                });
                turnDownDialog.show();
            } else {
                turnDownDialog.show();
            }

        } else if (i == R.id.tv_reboot) {
            if (rebootDialog == null) {
                rebootDialog = DialogCreator.createAppBasicDialog(this, "", "确定重启吗？",
                    getString(R.string.sure), getString(R.string.cancle), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i1 = v.getId();
                            if (i1 == R.id.positive_btn) {
                                //重启转案
                                if (loadingDialog != null && !loadingDialog.isShowing()) {
                                    loadingDialog.show();
                                }
                                if (getString(R.string.un_allocate_center).equals(modelName)) {
                                    presenter.reopenTransferCase(id);
                                } else if (getString(R.string.allocate_center).equals(modelName)) {
                                    presenter.reopenAllocatedCenter(id);
                                } else if (getString(R.string.turn_down_case).equals(modelName)) {
                                    presenter.reopenRejectCenter(id);
                                } else if (getString(R.string.un_allocate_teacher).equals(modelName)) {
                                    presenter.reopenUnallocatedCoac(id);
                                } else {
                                    presenter.reopenAllocatedCoac(id);
                                }
                            }
                        }
                    });
                ((TextView) rebootDialog.findViewById(R.id.dialog_info)).setGravity(Gravity.CENTER);
                WindowManager.LayoutParams p = rebootDialog.getWindow().getAttributes();
                p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
                rebootDialog.getWindow().setAttributes(p);
                rebootDialog.show();
            } else {
                rebootDialog.show();
            }

        } else if (i == R.id.topdefault_leftbutton) {
            finish();
        } else {
            if (getString(R.string.un_allocate_center).equals(modelName) || getString(R.string.turn_down_case).equals(modelName)) {
                if (unallocatedOptionPicker != null) {
                    unallocatedOptionPicker.show();
                }
            } else if (teacherOptionPicker != null) {
                teacherOptionPicker.show();
            }
        }
    }

    @Override
    public void showAllocatedTransferDetail(TransferDetailentity transferDetailentity) {
        if (transferDetailentity != null) {
            if (getString(R.string.allocate_teacher).equals(modelName) || (getString(R.string.allocate_center).equals(modelName) && "服务中".equals(orderState))) {
                llService.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_center_name)).setText(transferDetailentity.getCenterName());
                ((TextView) findViewById(R.id.tv_hard_teacher)).setText(transferDetailentity.getHardTeacherName());
                ((TextView) findViewById(R.id.tv_soft_teacher)).setText(transferDetailentity.getSoftTeacherName());
            } else {
                llService.setVisibility(View.GONE);
            }

            if (getString(R.string.turn_down_case).equals(modelName)) {
                findViewById(R.id.ll_reject).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_reject_center)).setText(transferDetailentity.getCenterName());
                final TextView tvState = findViewById(R.id.tv_state);
                final LinearLayout llDetail = findViewById(R.id.ll_detail);
                ((TextView) findViewById(R.id.tv_reject_person)).setText(transferDetailentity.getRejector());
                ((TextView) findViewById(R.id.tv_reject_time)).setText(transferDetailentity.getRejectedTime());
                ((TextView) findViewById(R.id.tv_reject_reason)).setText(transferDetailentity.getRejectReason());
                tvState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (llDetail.getVisibility() == View.GONE) {
                            llDetail.setVisibility(View.VISIBLE);
                            tvState.setText("收缩");
                            Drawable img = getResources().getDrawable(R.drawable.ic_blue_arrow_down);
                            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                            tvState.setCompoundDrawables(img, null, null, null);
                        } else {
                            llDetail.setVisibility(View.GONE);
                            tvState.setText("展开");
                            Drawable img = getResources().getDrawable(R.drawable.ic_blue_arraw_up);
                            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                            tvState.setCompoundDrawables(img, null, null, null);
                        }
                    }
                });
            } else {
                findViewById(R.id.ll_reject).setVisibility(View.GONE);
            }
            //获取数据后，创建fragment,fragment公用activity里面的数据
            ArrayList<UIFragment> fragments = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            titles.add("合同及基本信息");
            titles.add("用户背景信息");
            for (int i = 0; i < titles.size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putString("orderState", orderState);
                bundle.putSerializable("transferDetailentity", transferDetailentity);
                if ("合同及基本信息".equals(titles.get(i))) {
                    fragments.add(ContractInformationFragment.getInstance(bundle));
                } else {
                    fragments.add(UserBackgroundFragment.getInstance(bundle));
                }
            }
            pagerNews.setAdapter(new XxdTransferDetailFragmentAdapter(getSupportFragmentManager(), titles, fragments));
            pagerNews.setOffscreenPageLimit(titles.size());
            newTabs.setViewPager(pagerNews);
            pagerNews.setCurrentItem(0);

            if (transferDetailentity.getUser() != null) {
                topdefaultCentertitle.setText(transferDetailentity.getUser().getName());
            }

            //对分配中心数据行赋值
            if (transferDetailentity.getMeta() != null) {
                if (transferDetailentity.getMeta().getCenterList() != null) {
                    centerLists.clear();
                    centerLists.addAll(transferDetailentity.getMeta().getCenterList());
                    //分配中心
                    initUnallocatedOptionPicker();
                }

                //软实力老师
                if (transferDetailentity.getMeta().getSoftTeachers() != null) {
                    softTeachers.clear();
                    softTeachers.addAll(transferDetailentity.getMeta().getSoftTeachers());
                }

                if (transferDetailentity.getMeta().getHardTeachers() != null) {
                    hardTeachers.clear();
                    hardTeachers.addAll(transferDetailentity.getMeta().getHardTeachers());
                    initTeacherOptionPicker();
                }

            }
        }
    }

    @Override
    public void assignTransferCaseSuccess() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        ToastUtils.shortToast("分配成功");
        EventBus.getDefault().post(new OptionSuccess());
        finish();
    }

    @Override
    public void rejectTransferCaseSuccess() {
        ToastUtils.shortToast("驳回成功");
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        EventBus.getDefault().post(new OptionSuccess());
        finish();
    }

    @Override
    public void reopenTransferCaseSuccess() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        ToastUtils.shortToast("重启成功");
        EventBus.getDefault().post(new OptionSuccess());
        finish();
    }

    @Override
    public void closeTranseferCaseSuccess() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        ToastUtils.shortToast("结案成功");
        EventBus.getDefault().post(new OptionSuccess());
        finish();
    }


    private void initTeacherOptionPicker() {
        //首先判断是否有软实力和硬实力的老师
        if (softTeachers == null || softTeachers.size() == 0) {
            ToastUtils.shortToast("没有软实力老师可以进行分配");
            return;
        }
        if (hardTeachers == null || hardTeachers.size() == 0) {
            ToastUtils.shortToast("没有硬实力老师可以进行分配");
            return;
        }

        final List<String> softTeacher = new ArrayList<>();
        final List<String> hardTeacher = new ArrayList<>();
        for (TransferDetailentity.Meta.SoftTeachers softTeachers : softTeachers) {
            softTeacher.add(softTeachers.getValue());
        }

        for (TransferDetailentity.Meta.HardTeachers hardTeachers : hardTeachers) {
            hardTeacher.add(hardTeachers.getValue());
        }

        teacherOptionPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (loadingDialog != null && !loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
                if (getString(R.string.in_service).equals(orderState)) {
                    presenter.assigunTransferCaseTeacherAgain(id, hardTeachers.get(options1).getId(), softTeachers.get(option2).getId());
                } else {
                    presenter.assigunTransferCaseTeacher(id, hardTeachers.get(options1).getId(), softTeachers.get(option2).getId());
                }
            }
        })
            .setLayoutRes(R.layout.dialog_teacher_custom, new CustomListener() {
                @Override
                public void customLayout(View v) {
                    TextView tvSure = v.findViewById(R.id.btnSubmit);
                    TextView tvCancel = v.findViewById(R.id.btnCancel);
                    TextView tvTitle = v.findViewById(R.id.tvTitle);
                    tvCancel.setText(R.string.cancle);
                    tvCancel.setTextColor(getResources().getColor(R.color.app_text_color));
                    tvSure.setText(R.string.sure);
                    tvSure.setTextColor(getResources().getColor(R.color.app_main_color));
                    tvTitle.setTextSize(20);
                    tvTitle.setTextColor(getResources().getColor(R.color.app_text_color2));
                    if (getString(R.string.in_service).equals(orderState)) {
                        tvTitle.setText(getString(R.string.redistribution_teacher));
                    } else {
                        tvTitle.setText(getString(R.string.distribution_teacher));
                    }
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            teacherOptionPicker.dismiss();
                        }
                    });

                    tvSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            teacherOptionPicker.returnData();
                            teacherOptionPicker.dismiss();
                        }
                    });
                }

            })
            .setDecorView((RelativeLayout) findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
            .setContentTextSize(20)//滚轮文字大小
            .setTextColorCenter(Color.parseColor("#292929"))//设置选中文本的颜色值
            .setLineSpacingMultiplier(1.8f)//行间距
            .setDividerColor(getResources().getColor(R.color.transparent))//设置分割线的颜色
            .setSelectOptions(0)//设置选择的值
            .build();
        teacherOptionPicker.findViewById(R.id.rv_topbar).setBackgroundColor(Color.parseColor("#ffffff"));
        teacherOptionPicker.setNPicker(hardTeacher, softTeacher, null);
    }

    //初始化地址选择器
    private void initUnallocatedOptionPicker() {
        //首先判断是否有数据
        if (centerLists != null && centerLists.size() > 0) {
            List<String> centerListName = new ArrayList<>();
            for (TransferDetailentity.Meta.CenterList centerList : centerLists) {
                centerListName.add(centerList.getValue());
            }
            unallocatedOptionPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    if (loadingDialog != null && !loadingDialog.isShowing()) {
                        loadingDialog.show();
                    }
                    presenter.assignTransferCase(id, centerLists.get(options1).getId());
                }
            })
                .setLayoutRes(R.layout.dialog_teacher_custom, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvSure = v.findViewById(R.id.btnSubmit);
                        TextView tvCancel = v.findViewById(R.id.btnCancel);
                        TextView tvTitle = v.findViewById(R.id.tvTitle);
                        tvCancel.setText(R.string.cancle);
                        tvCancel.setTextColor(getResources().getColor(R.color.app_text_color));
                        tvSure.setText(R.string.sure);
                        tvSure.setTextColor(getResources().getColor(R.color.app_main_color));
                        tvTitle.setTextSize(20);
                        tvTitle.setTextColor(getResources().getColor(R.color.app_text_color2));
                        tvTitle.setText("分配中心");
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                unallocatedOptionPicker.dismiss();
                            }
                        });

                        tvSure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                unallocatedOptionPicker.returnData();
                                unallocatedOptionPicker.dismiss();
                            }
                        });
                    }
                })
                .setDecorView((RelativeLayout) findViewById(R.id.activity_rootview))//必须是RelativeLayout，不设置setDecorView的话，底部虚拟导航栏会显示在弹出的选择器区域
                .setContentTextSize(20)//滚轮文字大小
                .setTextColorCenter(Color.parseColor("#292929"))//设置选中文本的颜色值
                .setLineSpacingMultiplier(1.8f)//行间距
                .setDividerColor(getResources().getColor(R.color.transparent))//设置分割线的颜色
                .setSelectOptions(0)//设置选择的值
                .build();
            unallocatedOptionPicker.findViewById(R.id.ll_title).setVisibility(View.GONE);
            unallocatedOptionPicker.findViewById(R.id.rv_topbar).setBackgroundColor(Color.parseColor("#ffffff"));
            unallocatedOptionPicker.setPicker(centerListName);//添加数据
        } else {
            ToastUtils.shortToast(getString(R.string.have_no_centetr));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (overDialog != null) {
            overDialog.dismiss();
            overDialog = null;
        }
        if (rebootDialog != null) {
            rebootDialog.dismiss();
            rebootDialog = null;
        }
        if (turnDownDialog != null) {
            turnDownDialog.dismiss();
            turnDownDialog = null;
        }
        if (centerLists != null) {
            centerLists.clear();
            centerLists = null;
        }
        if (softTeachers != null) {
            softTeachers.clear();
            softTeachers = null;
        }
        if (hardTeachers != null) {
            hardTeachers.clear();
            hardTeachers = null;
        }
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
