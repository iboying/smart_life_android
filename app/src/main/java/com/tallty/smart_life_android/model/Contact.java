package com.tallty.smart_life_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/7/24.
 * 收货地址
 */

public class Contact implements Serializable {

    @SerializedName("id")
    @Expose
    private int id = 0;

    @SerializedName("name")
    @Expose
    private String name;

    // 电话
    @SerializedName("phone")
    @Expose
    private String phone;

    // 省
    @SerializedName("province")
    @Expose
    private String province;

    // 市
    @SerializedName("city")
    @Expose
    private String city;

    // 区
    @SerializedName("area")
    @Expose
    private String area;
    // 街道
    @SerializedName("street")
    @Expose
    private String street;
    // 社区
    @SerializedName("community")
    @Expose
    private String community;
    // 详细地址
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("is_default")
    @Expose
    private boolean isDefault;

    @SerializedName("order")
    @Expose
    private int order;

    // UI属性
    private boolean checked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
