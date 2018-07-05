package study.smart.transfer_management.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.BaseFragment;
import study.smart.baselib.entity.Privileges;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.PagerSlidingTabStrip;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.StateInfo;
import study.smart.transfer_management.ui.activity.CommonSearchActivity;
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
        //根据权限来配置转案管理，一共分为三级权限
        String transferPermission = (String) SPCacheUtils.get("privileges", "");
        if (!TextUtils.isEmpty(transferPermission)) {
            Privileges privileges = JSONObject.parseObject(transferPermission, Privileges.class);
            //表示含有转案管理模块的权限
            if (privileges.isTransferCase()) {
                //判断是否有未分配中心的权限
                if (privileges.isUnallocated()) {
                    titles.add("未分配中心");
                }
                //判断是否含有已分配中心
                if (privileges.isAllocated()) {
                    titles.add("已分配中心");
                }
                //判断是否含有被驳回转案
                if (privileges.isRejected()) {
                    titles.add("被驳回转案");
                }
                //判断是否含有未分配导师
                if (privileges.isUnassigned()) {
                    titles.add("未分配导师");
                }
                //判断是否含有分配导师
                if (privileges.isAssigned()) {
                    titles.add("已分配导师");
                }
            }
        }
        for (int i = 0; i < titles.size(); i++) {
            StateInfo stateInfo = new StateInfo();
            stateInfo.setTitle(titles.get(i));
            stateInfo.setType(titles.get(i));
            Bundle bundle = new Bundle();
            bundle.putParcelable("title", stateInfo);
            fragments.add(TransferMangerListFragment.getInstance(bundle));
        }
        pagerNews.setAdapter(new XxdTransferPagerFragmentAdapter(getChildFragmentManager(), titles, fragments));
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
            toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.TRANSFER_MANAGER);
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
