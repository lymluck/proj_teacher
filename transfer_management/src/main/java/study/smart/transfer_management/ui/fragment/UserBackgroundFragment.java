package study.smart.transfer_management.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.FullyLinearLayoutManager;
import study.smart.baselib.ui.widget.HorizontalDividerItemDecoration;
import study.smart.baselib.utils.DensityUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.TransferDetailentity;
import study.smart.transfer_management.ui.adapter.AcademicExpAdapter;
import study.smart.transfer_management.ui.adapter.ActivitiesAdapter;
import study.smart.transfer_management.ui.adapter.AwarderExpAdapter;
import study.smart.transfer_management.ui.adapter.EduGroudAdapter;
import study.smart.transfer_management.ui.adapter.PersonalSkillsAdapter;
import study.smart.transfer_management.ui.adapter.ResultItemAdapter;
import study.smart.transfer_management.ui.adapter.SocialPracticeAdapter;
import study.smart.transfer_management.ui.adapter.WorkExpAdapter;

/**
 * @author yqy
 * @date on 2018/6/29
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class UserBackgroundFragment extends UIFragment {
    private LinearLayout lltyContent;
    private TransferDetailentity transferDetailentity;
    private String orderState;

    public static UserBackgroundFragment getInstance(Bundle bundle) {
        UserBackgroundFragment fragment = new UserBackgroundFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_user_backgroud, null);
    }

    @Override
    protected void initView(View rootView) {
        lltyContent = rootView.findViewById(R.id.llyt_content);
        transferDetailentity = (TransferDetailentity) getArguments().getSerializable("transferDetailentity");
        orderState = getArguments().getString("orderState");
        ImageView ivOver = rootView.findViewById(R.id.iv_over);
        if (transferDetailentity != null) {
            if (getString(R.string.closed).equals(orderState)) {
                ivOver.setVisibility(View.VISIBLE);
            } else {
                ivOver.setVisibility(View.GONE);
            }
            //成绩类型
            showResult(transferDetailentity.getResults());
            //社会实践
            showSocialPractice(transferDetailentity.getSocialPractice());
            //教育背景
            showEduBackground(transferDetailentity.getEduBackground());
            //实习/工作经历
            showWorkExp(transferDetailentity.getWorkExp());
            //课外活动
            showActivities(transferDetailentity.getActivities());
            //学术经历
            showAcademicExp(transferDetailentity.getAcademicExp());
            //获奖经历
            showAwarderExp(transferDetailentity.getAwarderExp());
            //个人技能
            showPersonalSkills(transferDetailentity.getPersonalSkills());
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    /**
     * 成绩类型展示
     *
     * @param items 列表数据集合
     */
    private void showResult(List<TransferDetailentity.Results> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("成绩类型");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initResultMorAction(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }

    /**
     * 社会实践
     *
     * @param items
     */
    private void showSocialPractice(List<TransferDetailentity.SocialPractice> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("社会实践");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initSocialPractice(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }

    /**
     * 教育背景
     *
     * @param items
     */
    private void showEduBackground(List<TransferDetailentity.EduBackground> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("教育背景");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initEduBackground(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }

    /**
     * 工作经历
     *
     * @param items
     */
    private void showWorkExp(List<TransferDetailentity.WorkExp> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("实习/工作经历");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initWorkExp(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }

    private void showActivities(List<TransferDetailentity.Activities> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("课外活动");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initActivities(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }

    private void showAcademicExp(List<TransferDetailentity.AcademicExp> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("学术经历");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initAcademicExp(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }

    private void showAwarderExp(List<TransferDetailentity.AwarderExp> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("获奖经历");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initAwarderExp(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }


    private void showPersonalSkills(List<TransferDetailentity.PersonalSkills> items) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.layout_transfer_result, null);
        RecyclerView rvResult = view.findViewById(R.id.rv_result);
        TextView tvMore = view.findViewById(R.id.tv_more);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("个人技能");
        TextView tvDataCount = view.findViewById(R.id.tv_data_count);
        tvDataCount.setText(String.format(getString(R.string.data_format), (items != null && items.size() > 0) ? items.size() + "" : 0 + ""));
        TextView tvNoData = view.findViewById(R.id.tv_no_data);
        initRecyclerView(rvResult);
        initPersonalSkills(items, rvResult, tvMore, tvNoData);
        lltyContent.addView(view, -1);
    }


    /**
     * 初始化recyclerView
     *
     * @param rv
     */
    private void initRecyclerView(RecyclerView rv) {
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(0.5f))
            .colorResId(R.color.horizontal_line_color).build());
        layoutManager = null;
        rv.setNestedScrollingEnabled(false);
    }

    /**
     * 处理事件
     *
     * @param items
     * @param rv
     * @param tvMore
     */
    private void initResultMorAction(final List<TransferDetailentity.Results> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.Results> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final ResultItemAdapter mAdapter = new ResultItemAdapter(mActivity,
                R.layout.item_transfer_result, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 处理事件
     *
     * @param items
     * @param rv
     * @param tvMore
     */
    private void initSocialPractice(final List<TransferDetailentity.SocialPractice> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.SocialPractice> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final SocialPracticeAdapter mAdapter = new SocialPracticeAdapter(mActivity,
                R.layout.item_transfer_social_practice, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initEduBackground(final List<TransferDetailentity.EduBackground> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.EduBackground> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final EduGroudAdapter mAdapter = new EduGroudAdapter(mActivity,
                R.layout.item_transfer_edubackgroud, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initWorkExp(final List<TransferDetailentity.WorkExp> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.WorkExp> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final WorkExpAdapter mAdapter = new WorkExpAdapter(mActivity,
                R.layout.item_transfer_work, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initActivities(final List<TransferDetailentity.Activities> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.Activities> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final ActivitiesAdapter mAdapter = new ActivitiesAdapter(mActivity,
                R.layout.item_transfer_activies, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }


    private void initAcademicExp(final List<TransferDetailentity.AcademicExp> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.AcademicExp> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final AcademicExpAdapter mAdapter = new AcademicExpAdapter(mActivity,
                R.layout.item_transfer_academic, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initAwarderExp(final List<TransferDetailentity.AwarderExp> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.AwarderExp> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final AwarderExpAdapter mAdapter = new AwarderExpAdapter(mActivity,
                R.layout.item_transfer_awarder, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initPersonalSkills(final List<TransferDetailentity.PersonalSkills> items, RecyclerView rv, final TextView tvMore, TextView tvNoData) {
        if (items != null && items.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            int len = items.size();
            final List<TransferDetailentity.PersonalSkills> mDatas = new ArrayList<>();
            if (len > 1) {
                mDatas.addAll(items.subList(0, 1));
                tvMore.setVisibility(View.VISIBLE);
            } else {
                mDatas.addAll(items);
                tvMore.setVisibility(View.GONE);
            }
            final PersonalSkillsAdapter mAdapter = new PersonalSkillsAdapter(mActivity,
                R.layout.item_transfer_personal_skills, mDatas);
            rv.setAdapter(mAdapter);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.clear();
                    mDatas.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    tvMore.setVisibility(View.GONE);
                }
            });
        } else {
            rv.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }
}
