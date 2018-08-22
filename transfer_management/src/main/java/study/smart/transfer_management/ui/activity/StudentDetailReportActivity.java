package study.smart.transfer_management.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.PagerSlidingTabStrip;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.transfer_management.R;
import study.smart.transfer_management.entity.StateInfo;
import study.smart.transfer_management.ui.adapter.StudentDetailReportAdapter;
import study.smart.transfer_management.ui.adapter.UnCompeleteReportAdapter;
import study.smart.transfer_management.ui.fragment.MyReportFragment;
import study.smart.transfer_management.ui.fragment.StudentDetailReportFragment;

/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentDetailReportActivity extends BaseActivity {
    private ViewPager pagerReport;
    private String name;
    private String id;

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
        name = getIntent().getStringExtra("name");
        setTitle(name + "的报告");
        id = getIntent().getStringExtra("id");
        pagerReport = findViewById(R.id.pager_report);
        ArrayList<UIFragment> fragments = new ArrayList<>();
        List<StateInfo> stateInfos = new ArrayList<>();
        StateInfo stateInfoSeason = new StateInfo();
        stateInfoSeason.setTitle(getString(R.string.season_report));
        stateInfoSeason.setType(ParameterUtils.QUARTERLY);
        stateInfos.add(stateInfoSeason);
        StateInfo stateAll = new StateInfo();
        stateAll.setTitle(getString(R.string.summary_report));
        stateAll.setType(ParameterUtils.SUMMARY);
        stateInfos.add(stateAll);
        StateInfo stateOver = new StateInfo();
        stateOver.setTitle(getString(R.string.close_report));
        stateOver.setType(ParameterUtils.CLOSE_CASE);
        stateInfos.add(stateOver);
        for (int i = 0; i < stateInfos.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("stateInfos", stateInfos.get(i));
            bundle.putString("id", id);
            fragments.add(StudentDetailReportFragment.getInstance(bundle));
        }
        pagerReport.setAdapter(new StudentDetailReportAdapter(getSupportFragmentManager(), stateInfos, fragments));
        pagerReport.setOffscreenPageLimit(stateInfos.size());
        PagerSlidingTabStrip rankTabs = findViewById(R.id.tabs_report);
        rankTabs.setViewPager(pagerReport);
        pagerReport.setCurrentItem(0);
    }
}