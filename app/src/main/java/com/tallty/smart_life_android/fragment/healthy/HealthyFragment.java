package com.tallty.smart_life_android.fragment.healthy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseLazyMainFragment;
import com.tallty.smart_life_android.event.TabSelectedEvent;
import com.tallty.smart_life_android.fragment.MainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by kang on 16/6/20.
 * 健康
 */
public class HealthyFragment extends BaseLazyMainFragment {


    public static HealthyFragment newInstance() {
        Bundle args = new Bundle();

        HealthyFragment fragment = new HealthyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_healthy;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("智慧健康");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 订阅事件:
     * Tab Healthy按钮被重复点击时执行的操作
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position == MainFragment.HEALTHY)
            Log.d("tab-reselected", "智慧健康被重复点击了");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
