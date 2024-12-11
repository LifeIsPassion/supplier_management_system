package com.shanzhu.purchase.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CkmdStock implements Serializable {
    @ApiModelProperty(value = "序号 入库id")
    private Long id;

    @ApiModelProperty(value = "商品名")
    private String shopName;

    @ApiModelProperty(value = "商品类型")
    private String shopType;

    @ApiModelProperty(value = "商品售价")
    private Long shopPrice;

    @ApiModelProperty(value = "入库数量")
    private Long shopNumber;

    @ApiModelProperty(value = "入库商品总价")
    private Long priceTotal;

    @ApiModelProperty(value = "单位规格")
    private String specs;

    @ApiModelProperty(value = "入库人")
    private String inUser;

    @ApiModelProperty(value = "供应商")
    private String shopSupplier;

    @ApiModelProperty(value = "仓库名")
    private String depository;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime date;

    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getDepository() {
        return depository;
    }

    public void setDepository(String depository) {
        this.depository = depository;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Long shopPrice) {
        this.shopPrice = shopPrice;
    }


    public Long getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Long priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getInUser() {
        return inUser;
    }

    public void setInUser(String inUser) {
        this.inUser = inUser;
    }

    public String getShopSupplier() {
        return shopSupplier;
    }

    public void setShopSupplier(String shopSupplier) {
        this.shopSupplier = shopSupplier;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(Long shopNumber) {
        this.shopNumber = shopNumber;
    }

    @Override
    public String toString() {
        return "CkmdStock{" +
                "id=" + id +
                ", shopName='" + shopName + '\'' +
                ", shopType='" + shopType + '\'' +
                ", shopPrice=" + shopPrice +
                ", shopNumber=" + shopNumber +
                ", priceTotal=" + priceTotal +
                ", specs='" + specs + '\'' +
                ", inUser='" + inUser + '\'' +
                ", shopSupplier='" + shopSupplier + '\'' +
                ", depository='" + depository + '\'' +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", remark='" + remark + '\'' +
                '}';
    }
}