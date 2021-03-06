package com.tallty.smart_life_android.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.tallty.smart_life_android.App;
import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.PayEvent;
import com.tallty.smart_life_android.event.TransferDataEvent;
import com.tallty.smart_life_android.fragment.MainFragment;
import com.tallty.smart_life_android.model.CommunitiesResponse;
import com.tallty.smart_life_android.model.CommunityObject;
import com.tallty.smart_life_android.utils.GlobalUtils;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
    public static boolean isForeground = false;
    // 设备相关
    public static int windowWidth = 0;
    public static int windowHeight = 0;
    // 社区地址相关
    public static ArrayList<CommunityObject> communities = new ArrayList<>();
    public static List<String> provinces = new ArrayList<>();
    public static HashMap<String, List<String>> cities = new HashMap<>();
    public static HashMap<String, List<String>> areas = new HashMap<>();
    public static HashMap<String, List<String>> streets = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }

        // 加载图标
        Iconify.with(new FontAwesomeModule());
        PingppLog.DEBUG = true;

        // 显示推送
        Bundle bundle = getIntent().getBundleExtra(Const.HOME_BUNDLE);
        if (bundle != null) {
            EventBus.getDefault().post(new TransferDataEvent(bundle, Const.JPUSH));
        }

        // 设备数据
        setDeviceData();
    }

    private void setDeviceData() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowHeight = dm.heightPixels;
        windowWidth = dm.widthPixels;
    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        // 获取剪切板里面的数据
        processClipboardData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理分享
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                String result = data.getExtras().getString("pay_result");
                EventBus.getDefault().post(new PayEvent(result));
                Log.i(App.TAG, "结果:===》"+result);
                Log.i(App.TAG, "错误:===》"+data.getExtras().getString("error_msg"));
                Log.i(App.TAG, "额外:===》"+data.getExtras().getString("extra_msg"));
            }
        }
    }

    /**
     * 判断并处理剪切板信息
     * 标志位:
     * a: 社区活动
     * p: 超市商品
     * g: 团购商品
     */
    private void processClipboardData() {
        String body = GlobalUtils.getClipboardData(this);
        Log.i(App.TAG, body);
        List<String> urls = extractUrls(body);
        // 获取链接失败
        if (urls.size() <= 0) return;

        String url = urls.get(0);
        if (!url.isEmpty()) {
            Log.i(App.TAG, "提取的链接" + url);
            String[] cache = url.split("sl=");
            // 拆分字符串, 返回失败
            if (cache.length <= 1) return;
            String tag = cache[1].substring(0, 1);
            String id =  cache[1].substring(1, cache[1].length());
            // 标签或id为空, 返回失败
            if (tag.isEmpty() || id.isEmpty()) return ;
            // 封装数据
            Bundle bundle = new Bundle();
            bundle.putString("tag", tag);
            bundle.putString("id", id);
            EventBus.getDefault().post(new TransferDataEvent(bundle, Const.SHARE));
            // 使用后, 清空剪切板
            GlobalUtils.setClipboardData(this, "");
        }
    }

    /**
     * Returns a list with all links contained in the input
     */
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }

        return containedUrls;
    }
}
