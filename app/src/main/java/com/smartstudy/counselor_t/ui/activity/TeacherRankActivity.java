package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.RankInfo;
import com.smartstudy.counselor_t.ui.adapter.XxdTeacherRankFragmentAdapter;
import com.smartstudy.counselor_t.ui.fragment.TeacherRankFragment;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.PagerSlidingTabStrip;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherRankActivity extends BaseActivity {
    private ViewPager pagerRanks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_rank);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        setTitle("排行榜");
        pagerRanks = findViewById(R.id.pager_rank);
        ArrayList<UIFragment> fragments = new ArrayList<>();
        List<RankInfo> rankInfos = new ArrayList<>();
        RankInfo rankInfoWeek = new RankInfo();
        rankInfoWeek.setTitle("本周排行");
        rankInfoWeek.setType("weekly");
        rankInfos.add(rankInfoWeek);
        RankInfo rankInfoMonth = new RankInfo();
        rankInfoMonth.setTitle("本月排行");
        rankInfoMonth.setType("monthly");
        rankInfos.add(rankInfoMonth);
        for (int i = 0; i < rankInfos.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("rankInfos", rankInfos.get(i));
            fragments.add(TeacherRankFragment.getInstance(bundle));
        }
        pagerRanks.setAdapter(new XxdTeacherRankFragmentAdapter(getSupportFragmentManager(), rankInfos, fragments));
        pagerRanks.setOffscreenPageLimit(rankInfos.size());
        PagerSlidingTabStrip rankTabs = findViewById(R.id.tabs_ranks);
        rankTabs.setViewPager(pagerRanks);
        pagerRanks.setCurrentItem(0);
    }
}
