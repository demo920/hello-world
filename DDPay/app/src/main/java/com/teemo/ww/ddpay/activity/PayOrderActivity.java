package com.teemo.ww.ddpay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.PayType;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.ParcelableBitmap;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.teemo.ww.ddpay.R;
import com.teemo.ww.ddpay.bean.Order;
import com.teemo.ww.ddpay.db.DbHelper;
import com.teemo.ww.ddpay.manager.IBoxPayManager;
import com.teemo.ww.ddpay.utils.CryptUtil;
import com.teemo.ww.ddpay.utils.LogUtils;
import com.teemo.ww.ddpay.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PayOrderActivity extends Activity implements View.OnClickListener {

    private static final String TAG = PayOrderActivity.class.getSimpleName();
    private RelativeLayout mSwipCardRl, mWechatScanRl, mWechatCodeRl, mZhifubaoCodeRl;
    private ToggleButton mSwipBtn, mWechatScanBtn, mWechatCodeBtn, mZhifubaoCodeBtn;
    private Button mPayBtn;
    private TextView mPayAmountTv, mGoodsNameTv;

    private Context mContext;
    private Order order;

    /**
     * 第三方外部流水号
     */
    private String mTradeNo;

    /**
     * 钱盒交易回执订单号，交易成功生成
     */
    private String mCbTradeNo;

    /**
     * 订单时间
     */
    private String mOrderTime;

    /**
     * 公司后台地址
     */
    ////TODO
    private String mCallbackUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);

        initView();

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String amount = intent.getStringExtra(StringUtil.PAY_AMOUNT);
        String goodsName = intent.getStringExtra(StringUtil.GOODS_NAME);
        mPayAmountTv.setText(amount);
        mGoodsNameTv.setText(goodsName);
        //存储对象类
//        objToFile = new ObjToFile(mContext);
    }

    private void initView() {
        mContext = this;
        mSwipCardRl = (RelativeLayout) findViewById(R.id.rl_swip);
        mWechatScanRl = (RelativeLayout) findViewById(R.id.rl_wechat_scan);
        mWechatCodeRl = (RelativeLayout) findViewById(R.id.rl_wechat_code);
        mZhifubaoCodeRl = (RelativeLayout) findViewById(R.id.rl_zhifubao_code);

        mPayBtn = (Button) findViewById(R.id.btn_pay);
        mPayAmountTv = (TextView) findViewById(R.id.tv_amount);

        //all toggle
        mSwipBtn = (ToggleButton) findViewById(R.id.rbtn_pay_type_selected_swip);
        mWechatScanBtn = (ToggleButton) findViewById(R.id.rbtn_pay_type_selected_wechat_scan);
        mWechatCodeBtn = (ToggleButton) findViewById(R.id.rbtn_pay_type_selected_wechat_code);
        mZhifubaoCodeBtn = (ToggleButton) findViewById(R.id.rbtn_pay_type_selected_zhifubao_code);
        //
        mGoodsNameTv = (TextView) findViewById(R.id.tv_goods_name);
        mSwipBtn.setSelected(true);

        mSwipCardRl.setOnClickListener(this);
        mWechatScanRl.setOnClickListener(this);
        mWechatCodeRl.setOnClickListener(this);
        mZhifubaoCodeRl.setOnClickListener(this);

        mPayBtn.setOnClickListener(payOrderClickListener);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        clearCheckStatus();

        switch (resId) {
            case R.id.rl_swip:
                mSwipBtn.setSelected(true);
                break;

            case R.id.rl_wechat_scan:
                mWechatScanBtn.setSelected(true);
                break;

            case R.id.rl_wechat_code:
                mWechatCodeBtn.setSelected(true);
                break;

            case R.id.rl_zhifubao_code:
                mZhifubaoCodeBtn.setSelected(true);
                break;
        }
    }

    private View.OnClickListener payOrderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            payOrder();
        }
    };

    private void payOrder() {

        //交易金额
        String amount = getAmount();
        String mMD5Key = DemoActivity.mMD5Key;

        //***********订单存储**************
        order = new Order();
        order.setmAmount(toYuanAmount(amount));
        //获取当前交易时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        mOrderTime = df.format(new Date());
        order.setmDateTime(df.format(new Date()));
        order.setmGoodsName(mGoodsNameTv.getText().toString());
        //***********订单存储结束**************

        final IBoxPayManager pm = new IBoxPayManager(mContext);

        //刷卡支付
        if (mSwipBtn.isSelected()) {
//            paySwipCard(amount, mMD5Key);

            /********使用工具类来发起支付*/
            //模拟生产一个第三方流水号
            String outTradeNo = "刷卡_" + System.currentTimeMillis();
            order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
            order.setmOutTradeNo(outTradeNo);

            pm.paySwipCard(mMD5Key,amount, outTradeNo, new ITradeCallback() {
                @Override
                public void onTradeSuccess(ParcelableMap parcelableMap) {
                    LogUtils.d(TAG,"工具类，刷卡交易成功");
                    tradeSuccess(parcelableMap);
                }

                @Override
                public void onTradeSuccessWithSign(ParcelableMap parcelableMap, ParcelableBitmap parcelableBitmap) {

                }

                @Override
                public void onTradeFail(ErrorMsg errorMsg) {
                    LogUtils.d(TAG,"工具类，刷卡交易失败");

                }
            });
        }

        //微信扫码支付
        if (mWechatScanBtn.isSelected()) {
//            payWechatScan(amount, mMD5Key);

            /********使用工具类来发起支付*/
            //模拟生产一个第三方流水号
            String outTradeNo = "微信扫码_" + System.currentTimeMillis();
            order.setmPayType(StringUtil.PAY_TYPE_WECHAT_SCAN);
            order.setmOutTradeNo(outTradeNo);

            pm.payWechatScan(mMD5Key, amount, outTradeNo, new ITradeCallback() {
                @Override
                public void onTradeSuccess(ParcelableMap parcelableMap) {
                    LogUtils.d(TAG,"工具类，微信扫码支付成功");
                    tradeSuccess(parcelableMap);
                }

                @Override
                public void onTradeSuccessWithSign(ParcelableMap parcelableMap, ParcelableBitmap parcelableBitmap) {

                }

                @Override
                public void onTradeFail(ErrorMsg errorMsg) {
                    LogUtils.d(TAG,"工具类，微信扫码支付失败");

                }
            });

        }

        //微信二维码支付
        if (mWechatCodeBtn.isSelected()) {
//            payWechatCode(amount, mMD5Key);

            /********使用工具类来发起支付*/
            //模拟生产一个第三方流水号
            String outTradeNo = "微信二维码_" + System.currentTimeMillis();
            order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
            order.setmOutTradeNo(outTradeNo);

            pm.payWechatCode(mMD5Key, amount, outTradeNo, new ITradeCallback() {
                @Override
                public void onTradeSuccess(ParcelableMap parcelableMap) {
                    LogUtils.d(TAG,"工具类，微信二维码支付成功");
                    tradeSuccess(parcelableMap);
                }

                @Override
                public void onTradeSuccessWithSign(ParcelableMap parcelableMap, ParcelableBitmap parcelableBitmap) {

                }

                @Override
                public void onTradeFail(ErrorMsg errorMsg) {
                    LogUtils.d(TAG,"工具类，微信二维码支付失败");

                }
            });
        }

        //支付宝二维码支付
        if (mZhifubaoCodeBtn.isSelected()) {
//            payZhiFuBao(amount, mMD5Key);

            /********使用工具类来发起支付*/
            //模拟生产一个第三方流水号
            String outTradeNo = "支付宝_" + System.currentTimeMillis();
            order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
            order.setmOutTradeNo(outTradeNo);

            pm.payZhiFuBao(mMD5Key, amount, outTradeNo, new ITradeCallback() {
                @Override
                public void onTradeSuccess(ParcelableMap parcelableMap) {
                    LogUtils.d(TAG,"工具类，支付宝成功");
                    tradeSuccess(parcelableMap);
                }

                @Override
                public void onTradeSuccessWithSign(ParcelableMap parcelableMap, ParcelableBitmap parcelableBitmap) {

                }

                @Override
                public void onTradeFail(ErrorMsg errorMsg) {
                    LogUtils.d(TAG,"工具类，支付宝失败");

                }
            });
        }
    }

    /**
     * 刷卡支付响应事件
     *
     * @param amount
     */
    private void paySwipCard(String amount, String mMD5Key) {

        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        //***********订单存储结束***********

        //模拟生产一个第三方流水号
        mTradeNo = "刷卡_" + System.currentTimeMillis();

        try {

            //**********必须加入签名信息的字段***************
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
            additionalMap.put(ParcelableMap.RESV, "wwxxnn");
            additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
            additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);
            String sign = CryptUtil.getDefaultSign(Config.config, amount, mTradeNo, null, mMD5Key,
                    additionalMap.getMap());
            //**********必须加入签名信息的字段***************

            //****设置打印出来的订单号*****（不需要加入签名信息的字段，放在签名完之后的map集合传入）
            additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "自定义第三方流水号");

            //发起交易
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_CARD, amount,
                    mTradeNo, transactionId,
                    SignType.TYPE_MD5, sign, additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            Log.d(TAG, "刷卡的交易成功");
                            tradeSuccess(map);
                        }

                        //设置了不显示盒子签购单时回调此方法。多返回一张交易订单图片bitmap
                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map,
                                                           ParcelableBitmap signBitmap) {
                            Log.d(TAG, "刷卡的交易成功---onTradeSuccessWithSign");
                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            Log.d(TAG, msg.getErrorMsg() + "[" + msg.getErrorCode() + "]");

                            //*******写入文件******
                            tradeFail(msg);
                            //********写入文件结束************
                        }
                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    //二维码扫描支付
    private void payWechatScan(String amount, String mMD5Key) {
        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_WECHAT_SCAN);
        //*************订单存储结束**********************

        //模拟生产一个外部流水号
        mTradeNo = "out_" + System.currentTimeMillis();

        String transactionId = System.currentTimeMillis() + "";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.RESV, "wwxxnn");
        additionalMap.put(ParcelableMap.PARTNER_ID, Config.config.getPartnerId());
        additionalMap.put(ParcelableMap.PARTNER_USERID, Config.config.getPartnerUserId());
        additionalMap.put(ParcelableMap.IBOX_MCHTNO, Config.config.getIboxMchtNo());
        //TODO 订单
        SimpleDateFormat df = new SimpleDateFormat ("yyyyMMddHHmmssZ");
        String orderNo = "WechatScan" + df.format(new Date());
        LogUtils.i(TAG,"微信二维码扫描"+orderNo);
//        String orderNo = "2015121716503"+System.currentTimeMillis();
        additionalMap.put(ParcelableMap.ORDER_NO, orderNo);
        additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, mTradeNo, null, mMD5Key,
                additionalMap.getMap());

        Log.e("WeChatWasScanPay  &&&&", "sign = " + sign);
        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "微信被扫的商户订单号");
        additionalMap.put(ParcelableMap.SUBJECT, "二维码扫描支付");

        try {
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_WEIPAY_SCAN, amount,
                    mTradeNo,
                    transactionId, SignType.TYPE_MD5, sign, additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            Log.e("WeChatWasScanPay", "-----------------onTradeSuccess");
                            Log.d(TAG, "微信被扫的交易成功");
                            tradeSuccess(map);
                        }

                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map,
                                                           ParcelableBitmap signBitmap) {
                            Log.e("WeChatWasScanPay", "-----------------onTradeSuccessWithSign");
                        }

                        @Override
                        public void onTradeFail(final ErrorMsg msg) {
                            Toast.makeText(PayOrderActivity.this,
                                    msg.getErrorMsg() + "[" + msg.getErrorCode() + "]", Toast.LENGTH_SHORT)
                                    .show();

                            //*******写入文件******
                            tradeFail(msg);
                            //*************写入文件结束**********************
                        }
                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void payWechatCode(String amount, String mMD5Key) {
        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_WECHAT_CODE);

        //模拟生产一个外部流水号
        final String outTradeNo = "out_" + System.currentTimeMillis();

        String transactionId = System.currentTimeMillis() + "";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.RESV, "微信主扫");
        additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);
        //TODO 订单
        String orderNo = "20170614213030"+System.currentTimeMillis();
        additionalMap.put(ParcelableMap.ORDER_NO, orderNo);
//        additionalMap.put(ParcelableMap.ORDER_NO, "20170614213030");
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, outTradeNo, null, mMD5Key,
                additionalMap.getMap());
        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "微信主扫的商户订单号");
        additionalMap.put(ParcelableMap.SUBJECT, "二维码主扫支付");

        try {
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_WEIPAY_QRCODE, amount,
                    outTradeNo,
                    transactionId, SignType.TYPE_MD5, sign, additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            Log.e("WeChat", "-----------------onTradeSuccess");
                            Log.d(TAG, "微信主扫交易成功");
                            tradeSuccess(map);
                        }

                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map,
                                                           ParcelableBitmap signBitmap) {
                            Log.e("WeChat", "-----------------onTradeSuccessWithSign");
                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            Log.e("WeChat", "-----------------onTradeFail--msg=" + msg);
                            //*******写入文件******
                            tradeFail(msg);
                            //*******写入文件结束******
                        }
                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void payZhiFuBao(String amount, String mMD5Key) {
        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_ZHIFUBAO_CODE);
        //***********订单存储**************

        //模拟生产一个外部流水号
        mTradeNo = "out_" + System.currentTimeMillis();
        String orderNo = "2015121716500"+System.currentTimeMillis();

        String transactionId = System.currentTimeMillis() + "";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.ORDER_NO, orderNo);
        additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, mTradeNo, null, mMD5Key,
                additionalMap.getMap());

        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "myOrderNotest");
        additionalMap.put(ParcelableMap.SUBJECT, "支付宝主扫支付");


        try {
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_ALIPAY, amount,
                    mTradeNo,
                    transactionId, SignType.TYPE_MD5, sign, additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            Log.e("MiniCashBoxLog", "-----------------onTradeSuccess");
                            Log.d(TAG, "支付宝主扫交易成功");
                            tradeSuccess(map);
                        }

                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map,
                                                           ParcelableBitmap signBitmap) {
                            Log.d("MiniCashBoxLog", "-----------------onTradeSuccessWithSign");
                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            Log.d("MiniCashBoxLog", "-----------------onTradeFail--msg=" + msg);
                            tradeFail(msg);


                        }
                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void tradeFail(ErrorMsg msg) {
        //*******写入文件******
        order.setmTradeStatus(msg.getErrorMsg());
        order.setmTradeStatus(StringUtil.TRADE_STATUS_FAIL);
        order.setmCbTradeNo(mCbTradeNo);
        order.setmOutTradeNo(mTradeNo);
//        objToFile.writeObject(order);

        //写入数据库
        LogUtils.i("ORDER", order.toString());
        DbHelper.getInstance().saveDb(order);

        //*******写入文件结束******
    }

    /**
     * 所有类型的交易成功后统一操作
     *
     * @param map 回执信息
     */
    private void tradeSuccess(ParcelableMap map) {
        for (String key : map.getMap().keySet()) {
            Log.d(TAG, key + ":" + map.get(key));
        }
        mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
//        mTradeNo = map.get(ParcelableMap.PARTNER_TRADE_NO);
//        String amount = map.get(ParcelableMap.TRANS_AMOUNT);

        //*******写入文件******
        order.setmCbTradeNo(mCbTradeNo);
//        order.setmOutTradeNo(mTradeNo);
//        order.setmAmount(toYuanAmount(amount));
        order.setmTradeStatus(StringUtil.TRADE_STATUS_SUCCESS);
//        objToFile.writeObject(order);

        //写入数据库
        LogUtils.i("ORDER", order.toString());
        DbHelper.getInstance().saveDb(order);
        //********写入文件结束************

        Intent intent = new Intent(PayOrderActivity.this, DemoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        setResult(BookOrderActivity.OK);
        finish();
    }

    private void clearCheckStatus() {
        mSwipBtn.setSelected(false);
        mWechatScanBtn.setSelected(false);
        mWechatCodeBtn.setSelected(false);
        mZhifubaoCodeBtn.setSelected(false);
    }

    /**
     * 获取交易金额
     */
    private String getAmount() {
        String amount = mPayAmountTv.getText().toString();
        if (!TextUtils.isEmpty(amount)) {
            long amountDouble = (long) (Double.parseDouble(amount) * 100);
            amount = String.valueOf(amountDouble);
        }
        return amount;
    }

    private String toYuanAmount(String amount) {
        double amountDouble = Double.parseDouble(amount) / 100;
        return String.valueOf(amountDouble);
    }
}
