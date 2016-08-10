package com.tallty.smart_life_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.event.SelectAddress;
import com.tallty.smart_life_android.event.SetDefaultAddress;
import com.tallty.smart_life_android.model.Address;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by kang on 16/7/24.
 * 收货地址-适配器
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder>{
    private Context context;
    private ArrayList<Address> addresses;

    public AddressListAdapter(Context context, ArrayList<Address> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    @Override
    public AddressListAdapter.AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, final int position) {
        final Address address = addresses.get(position);

        holder.name.setText(address.getName());
        holder.address.setText(address.getArea()+address.getDetail());
        holder.phone.setText(address.getPhone());

        holder.checkBox.setChecked(address.isChecked());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SelectAddress(position, address));
            }
        });

        if (address.isDefaultAddress()){
            holder.isDefault.setText("默认地址");
        }else{
            holder.isDefault.setText("设为默认地址");
            holder.isDefault.setTextColor(context.getResources().getColor(R.color.global_text));
            holder.isDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置默认地址操作
                    EventBus.getDefault().post(new SetDefaultAddress(position, address));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView name;
        private TextView address;
        private TextView phone;
        private TextView isDefault;

        public AddressViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_address_check_box);
            name = (TextView) itemView.findViewById(R.id.item_address_name);
            address = (TextView) itemView.findViewById(R.id.item_address_detail);
            phone = (TextView) itemView.findViewById(R.id.item_address_phone);
            isDefault = (TextView) itemView.findViewById(R.id.item_address_default);
        }
    }
}
