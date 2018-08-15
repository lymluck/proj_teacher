package com.smartstudy.counselor_t.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.smartstudy.counselor_t.entity.DistributionTitle;
import com.smartstudy.counselor_t.entity.RankInfo;

import java.util.List;

import study.smart.baselib.ui.base.UIFragment;

/**
 * @author yqy
 * @date on 2018/8/14
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class DistributionAdapter  extends FragmentPagerAdapter {
    private List<DistributionTitle> distributionTitles;
    private List<UIFragment> fragments;

    public DistributionAdapter(FragmentManager fm, List<DistributionTitle> list, List<UIFragment> fragments) {
        super(fm);
        this.distributionTitles = list;
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return distributionTitles.get(position).getTitle();
    }

    @Override
    public int getCount() {
        if (distributionTitles != null && distributionTitles.size() > 0) {
            return distributionTitles.size();
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

