package com.teemo.ww.ddpay.manager;

import android.content.Context;

import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.PayType;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.teemo.ww.ddpay.activity.DemoActivity;
import com.teemo.ww.ddpay.bean.Order;
import com.teemo.ww.ddpay.utils.CryptUtil;
import com.teemo.ww.ddpay.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 盒子支付管理工具类
 * 包括四种支付，以及查看签购单，打印签购单
 * Created by admin on 2016/8/11.
 */

public class IBoxPayManager {

    private static final String TAG = IBoxPayManager.class.getSimpleName();

    private Context mContext;

    /**
     * 公司后台地址，用于接收盒子后台返回信息
     */
    private String mCallbackUrl = "";

    public IBoxPayManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 刷卡支付，不存储交易信息
     *
     * @param mMD5Key     秘钥
     * @param amount      金额，精确到分
     * @param mOutTradeNo 店家自定义业务流水号（可以为空，不打印）
     * @param callBack    交易回调接口
     */
    public void paySwipCard(String mMD5Key, String amount, String mOutTradeNo, ITradeCallback callBack) {
        String orderTime = getOrderTime();

        try {
            //**********必须加入签名信息的字段***************
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
            additionalMap.put(ParcelableMap.RESV, "wwxxnn");
            additionalMap.put(ParcelableMap.ORDER_TIME, orderTime);
            additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);
            String sign = CryptUtil.getDefaultSign(Config.config, amount, mOutTradeNo, null, mMD5Key,
                    additionalMap.getMap());
            //**********必须加入签名信息的字段***************

            LogUtils.e(TAG, "WechatScan Sign = " + sign);
            //****设置打印出来的订单号*****（不需要加入签名信息的字段，放在签名完之后的map集合传入）
            if (mOutTradeNo != null)
                additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "刷卡订单号" + mOutTradeNo);//自定义第三方流水号

            //发起交易
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_CARD, amount,
                    mOutTradeNo, transactionId,
                    SignType.TYPE_MD5, sign, additionalMap, callBack);
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    //二维码扫描支付
    public void payWechatScan(String mMD5Key, String amount, String mOutTradeNo, ITradeCallback callBack) {
        String orderTime = getOrderTime();

        String transactionId = System.currentTimeMillis() + "";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.RESV, "wwxxnn");
        additionalMap.put(ParcelableMap.ORDER_TIME, orderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);

        additionalMap.put(ParcelableMap.PARTNER_ID, Config.config.getPartnerId());
        additionalMap.put(ParcelableMap.PARTNER_USERID, Config.config.getPartnerUserId());
        additionalMap.put(ParcelableMap.IBOX_MCHTNO, Config.config.getIboxMchtNo());
        additionalMap.put(ParcelableMap.ORDER_NO, mOutTradeNo);
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, mOutTradeNo, null, mMD5Key,
                additionalMap.getMap());

        LogUtils.e(TAG, "WechatScan Sign = " + sign);
        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "微信被扫的商户订单号" + mOutTradeNo);
        additionalMap.put(ParcelableMap.SUBJECT, "二维码扫描支付");

        try {
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_WEIPAY_SCAN, amount,
                    mOutTradeNo,
                    transactionId, SignType.TYPE_MD5, sign, additionalMap, callBack);
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    public void payWechatCode(String mMD5Key, String amount, String mOutTradeNo, ITradeCallback callBack) {
        String orderTime = getOrderTime();

        String transactionId = System.currentTimeMillis() + "";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.RESV, "微信主扫");
        additionalMap.put(ParcelableMap.ORDER_TIME, orderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);

        additionalMap.put(ParcelableMap.ORDER_NO, mOutTradeNo);
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, mOutTradeNo, null, mMD5Key,
                additionalMap.getMap());
        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "微信主扫的商户订单号" + mOutTradeNo);
        additionalMap.put(ParcelableMap.SUBJECT, "二维码主扫支付");

        try {
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_WEIPAY_QRCODE, amount,
                    mOutTradeNo,
                    transactionId, SignType.TYPE_MD5, sign, additionalMap, callBack);
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    public void payZhiFuBao(String mMD5Key, String amount, String mOutTradeNo, ITradeCallback callBack) {
        String orderTime = getOrderTime();

        String transactionId = System.currentTimeMillis() + "";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.ORDER_NO, mOutTradeNo);
        additionalMap.put(ParcelableMap.ORDER_TIME, orderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, mOutTradeNo, null, mMD5Key,
                additionalMap.getMap());

        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "支付宝订单" + mOutTradeNo);
        additionalMap.put(ParcelableMap.SUBJECT, "支付宝主扫支付");


        try {
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_ALIPAY, amount,
                    mOutTradeNo,
                    transactionId, SignType.TYPE_MD5, sign, additionalMap, callBack);
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    public String toYuanAmount(String amount) {
        double amountDouble = Double.parseDouble(amount) / 100;
        return String.valueOf(amountDouble);
    }

    /**
     * 获取订单时间
     *
     * @return 返回当前时间
     */
    private static String getOrderTime() {
        //获取当前订单时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 查看签购单
     *
     * @param order 订单信息
     */
    public void showTradeDetail(Order order) {
        try {
            //查看签购单
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);

            final String sign = CryptUtil.getDefaultSign(Config.config, null, order.getmOutTradeNo(),
                    order.getmCbTradeNo(), DemoActivity.mMD5Key,
                    additionalMap.getMap());
            CashboxProxy.getInstance(mContext)
                    .showTradeDetail(transactionId, order.getmOutTradeNo(),
                            order.getmCbTradeNo(),
                            SignType.TYPE_MD5, sign, additionalMap);

        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

}

