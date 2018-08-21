package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.DistributionTitle;
import com.smartstudy.counselor_t.entity.RankInfo;
import com.smartstudy.counselor_t.ui.adapter.DistributionAdapter;
import com.smartstudy.counselor_t.ui.adapter.XxdTeacherRankFragmentAdapter;
import com.smartstudy.counselor_t.ui.fragment.DistributionFragment;
import com.smartstudy.counselor_t.ui.fragment.TeacherRankFragment;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.PagerSlidingTabStrip;

/**
 * @author yqy
 * @date on 2018/8/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class DistributionActivity extends BaseActivity {
    private ViewPager pagerRanks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        setTitle("转介绍");
        pagerRanks = findViewById(R.id.pager_distribution);
        ArrayList<UIFragment> fragments = new ArrayList<>();
        List<DistributionTitle> distributionTitleArrayList = new ArrayList<>();
        DistributionTitle distributionTitleMe = new DistributionTitle();
        distributionTitleMe.setTitle("转给我的");
        distributionTitleMe.setType("transfer_me");
        distributionTitleArrayList.add(distributionTitleMe);
        DistributionTitle meDistributionTitle = new DistributionTitle();
        meDistributionTitle.setTitle("我转出的");
        meDistributionTitle.setType("me_transfer");
        distributionTitleArrayList.add(meDistributionTitle);
        for (int i = 0; i < distributionTitleArrayList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("distributionTitleArrayList", distributionTitleArrayList.get(i));
            fragments.add(DistributionFragment.getInstance(bundle));
        }
        pagerRanks.setAdapter(new DistributionAdapter(getSupportFragmentManager(), distributionTitleArrayList, fragments));
        pagerRanks.setOffscreenPageLimit(distributionTitleArrayList.size());
        PagerSlidingTabStrip rankTabs = findViewById(R.id.tabs_distribution);
        rankTabs.setViewPager(pagerRanks);
        pagerRanks.setCurrentItem(0);
    }
}
