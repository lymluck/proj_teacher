package com.smartstudy.counselor_t.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.smartstudy.counselor_t.entity.RankInfo;

import java.util.List;

import study.smart.baselib.ui.base.UIFragment;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class XxdTeacherRankFragmentAdapter extends FragmentPagerAdapter {
    private List<RankInfo> rankInfos;
    private List<UIFragment> fragments;

    public XxdTeacherRankFragmentAdapter(FragmentManager fm, List<RankInfo> list, List<UIFragment> fragments) {
        super(fm);
        this.rankInfos = list;
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return rankInfos.get(position).getTitle();
    }

    @Override
    public int getCount() {
        if (rankInfos != null && rankInfos.size() > 0) {
            return rankInfos.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}

