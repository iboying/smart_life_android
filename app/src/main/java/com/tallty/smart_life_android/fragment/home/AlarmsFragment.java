package com.tallty.smart_life_android.fragment.home;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.Engine.Engine;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.AlarmsAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.custom.CustomLoadMoreView;
import com.tallty.smart_life_android.fragment.Pop.HintDialogFragment;
import com.tallty.smart_life_android.model.Alarm;
import com.tallty.smart_life_android.model.Alarms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 电子猫眼,警报列表
 */
public class AlarmsFragment extends BaseBackFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView recyclerView;
    private TextView makeAlarmText;
    private TextView emptyMessage;
    private AlarmsAdapter adapter;
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private ArrayList<String> unreadAlarms = new ArrayList<>();
    // 加载更多
    private int current_page = 1;
    private int total_pages = 1;
    private int per_page = 10;

    public static AlarmsFragment newInstance() {
        Bundle args = new Bundle();
        AlarmsFragment fragment = new AlarmsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_alarms;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("电子猫眼");
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.alarms_list);
        makeAlarmText = getViewById(R.id.make_alarm_btn);
        emptyMessage = getViewById(R.id.empty_message);
        // 获取本地缓存的未读消息队列
        String string = sharedPre.getString(Const.UNREAD_ALARMS, Const.EMPTY_STRING);
        unreadAlarms = new ArrayList<>(Arrays.asList(string.split("@")));
    }

    @Override
    protected void setListener() {
        makeAlarmText.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        initList();
        fetchAlarms();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.make_alarm_btn:
                callThePolice();
                break;
        }
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new AlarmsAdapter(R.layout.item_home_alarm, alarms);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                showAlarmDetail(i);
            }
        });
    }

    /**
     * 查看详情
     */
    private void showAlarmDetail(int position) {
        Bundle bundle = new Bundle();
        Alarm alarm = alarms.get(position);
        // 查看详情 - 改变点击消息的状态为【已读】, 并且更新本地缓存
        if (alarm.isUnread()) {
            alarm.setUnread(false);
            adapter.notifyItemChanged(position);
            unreadAlarms.remove(alarm.getTime());
            String newString = "";
            for (String s : unreadAlarms) {
                if (!s.isEmpty())
                    newString = newString + s + "@";
            }
            SharedPreferences.Editor editor = sharedPre.edit();
            editor.putString(Const.UNREAD_ALARMS, newString);
            editor.apply();
        }

        // 传递详情数据
        ArrayList<String> images = new ArrayList<>();
        for (int j = 0; j < alarm.getImages().size(); j++) {
            images.add(alarm.getImages().get(j).get("url"));
        }
        bundle.putString(Const.PUSH_TITLE, alarm.getTitle());
        bundle.putString(Const.PUSH_TIME, alarm.getTitle());
        bundle.putStringArrayList(Const.PUSH_IMAGES, images);

        // 显示详情页面
        start(NotificationDetailFragment.newInstance(bundle));
    }

    private void fetchAlarms() {
        Engine
            .noAuthService()
            .getAlarmsHistory(current_page, per_page, shared_phone)
            .enqueue(new Callback<Alarms>() {
                @Override
                public void onResponse(Call<Alarms> call, Response<Alarms> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isPresent()) {
                            emptyMessage.setVisibility(View.INVISIBLE);
                        }
                        total_pages = response.body().getTotalPages();
                        // 设置消息的已读未读状态, 并返回列表
                        ArrayList<Alarm> data = getReadStatusAlarms(response.body().getAlarms());
                        adapter.addData(data);
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreFail();
                        emptyMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Alarms> call, Throwable t) {
                    adapter.loadMoreFail();
                    emptyMessage.setVisibility(View.VISIBLE);
                }
        });
    }

    // 充值历史记录的已读状态
    private ArrayList<Alarm> getReadStatusAlarms(ArrayList<Alarm> alarms) {
        if (unreadAlarms.size() == 0) return alarms;
        for (Alarm alarm : alarms) {
            if (unreadAlarms.contains(alarm.getTime())) alarm.setUnread(true);
        }
        return alarms;
    }

    private void callThePolice() {
        String text = "<慧生活>将为您接通社区物业电话, 点击【确定】立即拨打.";
        final HintDialogFragment hintDialog = HintDialogFragment.newInstance(text);
        hintDialog.show(getActivity().getFragmentManager(), "HintDialog");
        hintDialog.setOnHintDialogEventListener(new HintDialogFragment.OnHintDialogEventListener() {
            @Override
            public void onOk(TextView confirm_btn) {
                hintDialog.dismiss();
                if ("".equals(HomeFragment.alarmPhone)) {
                    showToast("未能获取物业电话，请稍后重试");
                } else {
                    callPhone(HomeFragment.alarmPhone);
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (current_page >= total_pages) {
                    adapter.loadMoreEnd();
                } else {
                    current_page++;
                    fetchAlarms();
                }
            }
        }, 1000);
    }
}
