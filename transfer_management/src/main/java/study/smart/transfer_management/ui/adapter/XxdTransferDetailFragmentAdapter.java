package study.smart.transfer_management.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import study.smart.baselib.ui.base.UIFragment;

/**
 * @author yqy
 * @date on 2018/6/29
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class XxdTransferDetailFragmentAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private List<UIFragment> fragments;

    public XxdTransferDetailFragmentAdapter(FragmentManager fm, List<String> list, List<UIFragment> fragments) {
        super(fm);
        this.titles = list;
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
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

