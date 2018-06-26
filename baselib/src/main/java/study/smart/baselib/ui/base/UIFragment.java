package study.smart.baselib.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import study.smart.baselib.mvp.base.BasePresenter;

/**
 * @author yqy
 * @date on 2018/2/6
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public abstract class UIFragment<P extends BasePresenter>  extends Fragment implements View.OnClickListener {
    // 标志位，标志已经初始化完成。
    protected boolean isPrepared;
    protected P presenter;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    public Activity mActivity;
    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //按钮点击切换时复写该方法加上isPrepared=true
        //viewPager+fragment切换时不加
        presenter = initPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 初始化视图和数据
        rootView = getLayoutView();
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView(rootView);
        initEvent();
        return rootView;
    }

    protected abstract View getLayoutView();

    protected abstract void initView(View rootView);

    protected abstract void initEvent();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
        //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
        //统计页面
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    public abstract P initPresenter();

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
    }


    @Override
    public void onClick(View v) {
    }

}
