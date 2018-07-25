package study.smart.transfer_management.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import study.smart.baselib.ui.base.UIFragment;
import study.smart.transfer_management.entity.StateInfo;

/**
 * @author yqy
 * @date on 2018/7/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyReportAdapter extends FragmentPagerAdapter {
    private List<StateInfo> titles;
    private List<UIFragment> fragments;

    public MyReportAdapter(FragmentManager fm, List<StateInfo> list, List<UIFragment> fragments) {
        super(fm);
        this.titles = list;
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getTitle();
    }

    @Override
    public int getCount() {
        if (titles != null && titles.size() > 0) {
            return titles.size();
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



