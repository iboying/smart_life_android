package com.tallty.smart_life_android.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.tallty.smart_life_android.utils.ToastUtil;

/**
 * Created by kang on 16/6/15.
 * 公用基础Activity
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected SharedPreferences sharedPre;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置样式为无actionBar样式
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 基础数据
        sharedPre = getSharedPreferences("SmartLife", Activity.MODE_PRIVATE);
        // 加载图标
        Iconify.with(new FontAwesomeModule());
        // 设置布局
        initLayout();
        // 引用组件
        initView(savedInstanceState);
        // 设置监听器
        setListener();
        // 添加业务逻辑
        processLogic(savedInstanceState);
    }

    /**
     * 返回当前Activity布局文件的id
     */
    protected abstract void initLayout();

    /**
     * 初始化布局以及View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 全局查找View
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     * 需要处理点击事件时，重写该方法
     */
    public void onClick(View v) {
    }

    /**
     * 显示Toast
     */
    public void showToast(String text) {
        ToastUtil.show(text);
    }
}
