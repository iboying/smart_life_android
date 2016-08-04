package com.tallty.smart_life_android.fragment.cart;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tallty.smart_life_android.Const;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.AddressListAdapter;
import com.tallty.smart_life_android.base.BaseBackFragment;
import com.tallty.smart_life_android.event.SelectAddress;
import com.tallty.smart_life_android.event.SetDefaultAddress;
import com.tallty.smart_life_android.model.Address;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 购物车-确认订单-收货地址
 */
public class MyAddress extends BaseBackFragment {
    // 调用者
    private int from;
    // UI
    private TextView new_address_text;
    private RecyclerView recyclerView;
    private AddressListAdapter adapter;
    // 临时data
    private boolean checkeds[] = {true, false};
    private String names[] = {"Stark", "Mark"};
    private String phones[] = {"15216666666", "15217777777"};
    private String areas[] = {"XX区", "YY区"};
    private String details[] = {"XX街道XX (小区名称) XX栋XX单元XXX室", "YY街道YY (小区名称) YY栋YY单元YYY室"};
    private boolean default_address[] = {true, false};
    // 实例
    private ArrayList<Address> addresses = new ArrayList<>();
    // sharePreferences 相关
    private Address shareDefaultAddress = null;
    private int defaultAddressPosition;
    // 新增
    private Address new_address = new Address();


    public static MyAddress newInstance(int from) {
        Bundle args = new Bundle();
        args.putInt(Const.FROM, from);
        MyAddress fragment = new MyAddress();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            from = args.getInt(Const.FROM);
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_address;
    }

    @Override
    public void initToolbar(Toolbar toolbar, TextView toolbar_title) {
        toolbar_title.setText("收货地址");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        new_address_text = getViewById(R.id.new_address);
        recyclerView = getViewById(R.id.address_list);
    }

    @Override
    protected void setListener() {
        new_address_text.setOnClickListener(this);
    }

    @Override
    protected void afterAnimationLogic() {
        // 载入数据并加载列表
        loadAddressData();
    }

    private void loadAddressData() {
        // 获取数据
        for (int i = 0; i < names.length; i++){
            Address address = new Address();
            address.setChecked(checkeds[i]);
            address.setName(names[i]);
            address.setPhone(phones[i]);
            address.setArea(areas[i]);
            address.setDetail(details[i]);
            address.setDefaultAddress(default_address[i]);
            addresses.add(address);
            // 取出默认地址
            if (default_address[i] && shareDefaultAddress == null){
                shareDefaultAddress = address;
                // 记录下标
                defaultAddressPosition = i;
            }
        }

        // 载入列表
        processList();
    }

    private void processList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AddressListAdapter(context, addresses);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_address:
                startForResult(NewAddressFragment.newInstance(), REQ_CODE);
                break;
        }
    }

    /**
     * startForResult:
     * 响应NewAddressFragment的返回数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            new_address = (Address) data.getSerializable(Const.OBJECT);
            if (new_address != null) {
                addresses.add(new_address);
                adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 设置传给上个ConfirmOrderFragment的数据
     * 保存默认地址到SharedPreferences
     * 未设置默认地址,取首个地址为默认地址
     */
    private void setAddressResult(){
        Bundle bundle = new Bundle();
        setFramgentResult(RESULT_OK, bundle);
    }


    /**
     * 接收事件: SetDefaultAddress
     */
    @Subscribe
    public void onSetDefaultAddress(SetDefaultAddress event) {
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString(Const.ADDRESS_AREA, event.getAddress().getArea());
        editor.putString(Const.ADDRESS_DETAIL, event.getAddress().getDetail());
        editor.putString(Const.ADDRESS_NAME, event.getAddress().getName());
        editor.putString(Const.ADDRESS_PHONE, event.getAddress().getPhone());
        editor.commit();
        // 取消原来的默认地址
        Address cache_address = addresses.get(defaultAddressPosition);
        cache_address.setDefaultAddress(false);
        addresses.set(defaultAddressPosition, cache_address);
        adapter.notifyItemChanged(defaultAddressPosition);
        // 设置新的默认地址 && 重置 defaultAddressPosition 为新的position
        cache_address = addresses.get(event.getPosition());
        cache_address.setDefaultAddress(true);
        addresses.set(event.getPosition(), cache_address);
        adapter.notifyItemChanged(event.getPosition());
        defaultAddressPosition = event.getPosition();
    }

    /**
     * 接收事件: SelectAddress
     */
    @Subscribe
    public void onSelectAddress(SelectAddress event) {
        Address cache_address;
        // 取消原来的选中地址
        cache_address = addresses.get(defaultAddressPosition);
        cache_address.setChecked(false);
        addresses.set(defaultAddressPosition, cache_address);
        adapter.notifyItemChanged(defaultAddressPosition);
        // 设置新的选中地址 && 重置 defaultAddressPosition 为新的position
        cache_address = addresses.get(event.getPosition());
        cache_address.setChecked(true);
        addresses.set(event.getPosition(), cache_address);
        adapter.notifyItemChanged(event.getPosition());
        defaultAddressPosition = event.getPosition();

        // 把选中的地址回传给上一个页面
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.OBJECT, cache_address);
        setFramgentResult(RESULT_OK, bundle);
        if (from == Const.FROM_ORDER){
            pop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
