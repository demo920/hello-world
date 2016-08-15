package com.teemo.ww.ddpay.app;

/**
 * Created by admin on 2016/8/9.
 */

public class IBoxPayConfig {
    ////TODO 请找盒子申请配置信息
    private String mAppCode = "2001740";
    private String mMerchantNo = "001440196451453";
    public String mMD5Key = "x24aq6QZT/c=";

    public String getmAppCode() {
        return mAppCode;
    }

    public void setmAppCode(String mAppCode) {
        this.mAppCode = mAppCode;
    }

    public String getmMerchantNo() {
        return mMerchantNo;
    }

    public void setmMerchantNo(String mMerchantNo) {
        this.mMerchantNo = mMerchantNo;
    }

    public String getmMD5Key() {
        return mMD5Key;
    }

    public void setmMD5Key(String mMD5Key) {
        this.mMD5Key = mMD5Key;
    }
}
