package com.tallty.smart_life_android.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseFragment;

/**
 * Created by kang on 16/6/20.
 * 健康
 */
public class HealthyFragment extends BaseFragment {

    public HealthyFragment() {
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
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
    }

    @Override
    public void onClick(View v) {

    }
}