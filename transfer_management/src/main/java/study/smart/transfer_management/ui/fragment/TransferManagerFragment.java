package study.smart.transfer_management.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.BaseFragment;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.activity.CommonSearchActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.PagerSlidingTabStrip;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.StateInfo;
import study.smart.transfer_management.ui.adapter.XxdTransferPagerFragmentAdapter;

/**
 * @author yqy
 * @date on 2018/6/26
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerFragment extends UIFragment {
    private TextView topRightMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_transfer_manager, null);
    }

    @Override
    protected void initView(View rootView) {
        RelativeLayout newsTop = rootView.findViewById(R.id.top_news);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) newsTop.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight(mActivity);
            newsTop.setLayoutParams(params);
            newsTop.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);
        }
        ((TextView) rootView.findViewById(R.id.topdefault_centertitle)).setText("转案管理");
        topRightMenu = rootView.findViewById(R.id.topdefault_rightmenu);
        topRightMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_blue, 0, 0, 0);
        topRightMenu.setVisibility(View.VISIBLE);
        ImageView topLeftBtn = rootView.findViewById(R.id.topdefault_leftbutton);
        topLeftBtn.setVisibility(View.GONE);
        ViewPager pagerNews = rootView.findViewById(R.id.pager_news);
        ArrayList<UIFragment> fragments = new ArrayList<>();

        List<String> titles = new ArrayList<>();
        titles.add("已分配中心");
        titles.add("未分配中心");

        List<String> eventIds = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            eventIds.add(titles.get(i));
            StateInfo stateInfo = new StateInfo();
            stateInfo.setTitle(titles.get(i));
            stateInfo.setType(titles.get(i));
            Bundle bundle = new Bundle();
            bundle.putParcelable("title", stateInfo);
            fragments.add(TransferMangerListFragment.getInstance(bundle));
        }
        pagerNews.setAdapter(new XxdTransferPagerFragmentAdapter(getChildFragmentManager(), titles, eventIds, fragments));
        pagerNews.setOffscreenPageLimit(4);
        PagerSlidingTabStrip newTabs = rootView.findViewById(R.id.tabs_news);
        newTabs.setViewPager(pagerNews);
        pagerNews.setCurrentItem(0);
    }

    @Override
    protected void initEvent() {
        topRightMenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.topdefault_rightmenu) {
            Intent toSearch = new Intent(mActivity, CommonSearchActivity.class);
            toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.NEWS_FLAG);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(toSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    topRightMenu, "btn_tr").toBundle());
            } else {
                startActivity(toSearch);
            }
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
