package com.tallty.smart_life_android.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.utils.DpUtil;

import java.util.List;

/**
 * Created by kang on 2017/1/11.
 * 推送通知显示 - 适配器
 */

public class NotificationAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public NotificationAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        ImageView imageView = baseViewHolder.getView(R.id.push_list_image);
        if (s.isEmpty()) {
            imageView.setMaxHeight(DpUtil.dip2px(mContext, 160));
        } else {
            imageView.setMaxHeight(4000);
        }

        Glide
            .with(mContext)
            .load(s)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_error)
            .into(imageView);
    }
}
