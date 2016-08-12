package com.teemo.ww.ddpay.manager;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.PayType;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.IAuthCallback;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.iboxpay.cashbox.minisdk.model.PrintPreference;
import com.iboxpay.cashbox.minisdk.model.TradingNo;
import com.teemo.ww.ddpay.bean.Order;
import com.teemo.ww.ddpay.utils.CryptUtil;
import com.teemo.ww.ddpay.utils.LogUtils;
import com.teemo.ww.ddpay.utils.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 盒子支付管理工具类
 * 包括四种支付，以及查看签购单，打印签购单，撤销交易
 * Created by admin on 2016/8/11.
 */

public class IBoxPayManager {

    private static final String TAG = IBoxPayManager.class.getSimpleName();

    private Context mContext;

    ////TODO 请找盒子申请配置信息
    private String mAppCode = "2001740";
    private String mMerchantNo = "001440196451453";
    public static String mMD5Key = "x24aq6QZT/c=";

    /**
     * 公司后台地址，用于接收盒子后台返回信息
     */
    private String mCallbackUrl = "";

    public IBoxPayManager(Context mContext) {
        this.mContext = mContext;
    }

    private static IBoxPayManager mInstance;
    /**
     * 获取实例
     * @return 返回当前类实例
     */
    public static IBoxPayManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (IBoxPayManager.class) {
                if (mInstance == null) {
                    mInstance = new IBoxPayManager(context);
                }
            }
        }
        return mInstance;
    }

    /**初始化盒子支付配置信息*/
    public void initAppInfo() {
        //初始化配置
        PrintPreference printPreference = new PrintPreference();
        //是否显示“盒子支付签购单”页面，若隐藏则刷卡支付成功不打印，返回一张图片
        //printPreference.setDisplayIBoxPaySaleSlip(PrintPreference.SALESLIP_HIDE);
        //设置签购单商户名称
        printPreference.setMerchantName("merchantName");
        //设置签购单操作员
        printPreference.setOperatorNo("001");
        //设置签购单Title
        printPreference.setOrderTitle("订单title");
        Log.e(TAG, "initData--mMerchantNo=" + mMerchantNo);
        Config.config = new Config(mAppCode, printPreference);
        Config.config.setIboxMchtNo(mMerchantNo);

        try {
            CashboxProxy.getInstance(mContext).initAppInfo(Config.config, new IAuthCallback() {
                @Override
                public void onAuthSuccess() {
                    LogUtils.d(TAG, "authSuccess");
                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(mContext,"签到成功");
                        }
                    });
                }

                @Override
                public void onAuthFail(final ErrorMsg msg) {
                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(mContext,"签到失败");
                        }
                    });
                }
            });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
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

    /**
     * 获取订单时间
     *
     * @return 返回当前时间
     */
    private static String getOrderTime() {
        //获取当前订单时间
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 查看签购单
     *
     * @param order 订单  必须需传入mOutTradeNo，mCbTradeNo可无
     * @param mMD5Key 秘钥
     */
    public void showTradeDetail(Order order, String mMD5Key) {
        try {
            //查看签购单
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);

            final String sign = CryptUtil.getDefaultSign(Config.config, null, order.getmOutTradeNo(),
                    order.getmCbTradeNo(), mMD5Key,
                    additionalMap.getMap());
            CashboxProxy.getInstance(mContext)
                    .showTradeDetail(transactionId, order.getmOutTradeNo(),
                            order.getmCbTradeNo(),
                            SignType.TYPE_MD5, sign, additionalMap);

        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印签购单
     * @param order 订单
     * @param mMD5Key 秘钥
     */
    public void printTradeInfo(Order order, String mMD5Key) {
        try {
            //打印签购单
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);


            final String sign = CryptUtil.getDefaultSign(Config.config, null, order.getmOutTradeNo(),
                    order.getmCbTradeNo(), mMD5Key,
                    additionalMap.getMap());
            //****设置打印出来的订单号*****
            additionalMap.put(ParcelableMap.PRINT_ORDER_NO, order.getmOutTradeNo());
            //设置不跳转签购单直接打印的字段
            additionalMap.put("isPrint", "print");

            CashboxProxy.getInstance(mContext)
                    .showTradeDetail(transactionId, order.getmOutTradeNo(),
                            order.getmCbTradeNo(),
                            SignType.TYPE_MD5, sign, additionalMap);

        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * 撤销交易
     * @param order 订单 必须传入mOutTradeNo，mCbTradeNo。
     * @param mMD5Key 秘钥
     * @param callback 回调接口
     */
    public void repealOrder(Order order, String mMD5Key, ITradeCallback callback) {

        ParcelableMap additionalMap = new ParcelableMap();
        String transactionId = System.currentTimeMillis() + "";
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);

        String transAmount = toYuanAmount(order.getmAmount());
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, transAmount, order.getmOutTradeNo(),
                order.getmCbTradeNo(), mMD5Key, additionalMap.getMap());
        try {
            CashboxProxy.getInstance(mContext).cancelTrading(transAmount, transactionId, sign,
                    SignType.TYPE_MD5, new TradingNo(order.getmOutTradeNo(), order.getmCbTradeNo()),
                    additionalMap, callback);
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private String toYuanAmount(String amount) {
        if (!TextUtils.isEmpty(amount)) {
            long amountDouble = (long) (Double.parseDouble(amount) * 100);
            amount = String.valueOf(amountDouble);
            LogUtils.e(TAG,amount+"");
        }
        return amount;
    }
}
