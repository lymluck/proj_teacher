package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.PagerSlidingTabStrip;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.StateInfo;
import study.smart.transfer_management.ui.adapter.UnCompeleteReportAdapter;
import study.smart.transfer_management.ui.fragment.MyReportFragment;
import study.smart.transfer_management.ui.fragment.UnCompeleteReportFragment;

/**
 * @author yqy
 * @date on 2018/7/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyReportActivity extends BaseActivity {
    private ViewPager pagerReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        setTitle("我发布的报告");
        pagerReport = findViewById(R.id.pager_report);
        ArrayList<UIFragment> fragments = new ArrayList<>();
        List<StateInfo> stateInfos = new ArrayList<>();
        StateInfo stateInfoSeason = new StateInfo();
        stateInfoSeason.setTitle("季度报告");
        stateInfoSeason.setType("QUARTERLY");
        stateInfos.add(stateInfoSeason);
        StateInfo stateAll = new StateInfo();
        stateAll.setTitle("总结报告");
        stateAll.setType("SUMMARY");
        stateInfos.add(stateAll);
        StateInfo stateOver = new StateInfo();
        stateOver.setTitle("结案报告");
        stateOver.setType("CLOSE_CASE");
        stateInfos.add(stateOver);
        for (int i = 0; i < stateInfos.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("stateInfos", stateInfos.get(i));
            fragments.add(MyReportFragment.getInstance(bundle));
        }
        pagerReport.setAdapter(new UnCompeleteReportAdapter(getSupportFragmentManager(), stateInfos, fragments));
        pagerReport.setOffscreenPageLimit(stateInfos.size());
        PagerSlidingTabStrip rankTabs = findViewById(R.id.tabs_report);
        rankTabs.setViewPager(pagerReport);
        pagerReport.setCurrentItem(0);
    }
}
