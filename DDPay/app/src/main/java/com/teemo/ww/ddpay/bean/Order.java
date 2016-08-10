package com.teemo.ww.ddpay.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 订单信息实例
 * Created by George on 16/2/26.
 */
@Table(name = "Order")
public class Order implements Serializable {
    public static final long serialVersionUID = 42L;

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "goodName")
    /**商品名*/
    private String mGoodsName;

    @Column(name = "amount")
    /**商品金额*/
    private String mAmount;

    /**商品交易时间*/
    @Column(name = "dateTime")
    private String mDateTime;

    /**交易状态*/
    @Column(name = "tradeStatus")
    private String mTradeStatus;

    /**商家自定义交易流水号*/
    @Column(name = "outTradeNo")
    private String mOutTradeNo;

    /**交易流水号*/
    @Column(name = "cbTradeNo")
    private String mCbTradeNo;

    /**支付类型，刷卡支付-微信扫码支付-微信二维码支付-支付宝二维码支付*/
    @Column(name = "payType")
    private String mPayType;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmPayType() {
        return mPayType;
    }

    public void setmPayType(String mPayType) {
        this.mPayType = mPayType;
    }

    public String getmOutTradeNo() {
        return mOutTradeNo;
    }

    public void setmOutTradeNo(String mOutTradeNo) {
        this.mOutTradeNo = mOutTradeNo;
    }

    public String getmCbTradeNo() {
        return mCbTradeNo;
    }

    public void setmCbTradeNo(String mCbTradeNo) {
        this.mCbTradeNo = mCbTradeNo;
    }

    public String getmGoodsName() {
        return mGoodsName;
    }

    public void setmGoodsName(String mGoodsName) {
        this.mGoodsName = mGoodsName;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmTradeStatus() {
        return mTradeStatus;
    }

    public void setmTradeStatus(String mTradeStatus) {
        this.mTradeStatus = mTradeStatus;
    }

    @Override public String toString() {
        return "{" +
            "\"mGoodsName\":\"" + mGoodsName + '\"' +
            ", \"mAmount\":\"" + mAmount + '\"' +
            ", \"mDateTime\":\"" + mDateTime + '\"' +
            ", \"mTradeStatus\":\"" + mTradeStatus + '\"' +
            ", \"mOutTradeNo\":\"" + mOutTradeNo + '\"' +
            ", \"mCbTradeNo\":\"" + mCbTradeNo + '\"' +
            ", \"mPayType\":\"" + mPayType + '\"' +
            '}';
    }
}
