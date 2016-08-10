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
import com.teemo.ww.ddpay.app.PayApplication;
import com.teemo.ww.ddpay.db.DbHelper;
import com.teemo.ww.ddpay.utils.CryptUtil;
import com.teemo.ww.ddpay.bean.Order;
import com.teemo.ww.ddpay.utils.LogUtils;
import com.teemo.ww.ddpay.utils.StringUtil;
import com.teemo.ww.ddpay.utils.ObjToFile;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PayOrderActivity extends Activity implements View.OnClickListener {

    private static final String TAG = PayOrderActivity.class.getSimpleName();
    private RelativeLayout mSwipCardRl, mWechatScanRl, mWechatCodeRl, mZhifubaoCodeRl;
    private ToggleButton mSwipBtn, mWechatScanBtn, mWechatCodeBtn, mZhifubaoCodeBtn;
    private Button mPayBtn;
    private TextView mPayAmountTv, mGoodsNameTv;

    private Context mContext;
    /**
     * 您的订单号
     */
    private String mTradeNo;
    /**
     * 钱盒交易订单号
     */
    private String mCbTradeNo;
    //订单存储
    private ObjToFile objToFile;
    private Order order;

    private String mOrderTime;
    private String mCallbackUrl="http://tpf.prepd.iboxpay.com/tpf/order/queryV2.htm/2001740";

    private PayApplication app;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PayApplication) getApplication(); // 获得CustomApplication对象
        db = app.getDb();
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
        objToFile = new ObjToFile(mContext);
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

    @Override public void onClick(View v) {
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
        @Override public void onClick(View v) {
            payOrder();
        }
    };

    private void payOrder() {
        //***********订单存储**************
        order = new Order();
        //获取当前交易时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        mOrderTime = df.format(new Date());
        order.setmDateTime(df.format(new Date()));
        order.setmGoodsName(mGoodsNameTv.getText().toString());

        //刷卡支付
        if (mSwipBtn.isSelected()) {
            paySwipCard();
        }

        //微信扫码支付
        if (mWechatScanBtn.isSelected()) {
            payWechatScan();
        }

        //微信二维码支付
        if (mWechatCodeBtn.isSelected()) {
            payWechatCode();
        }

        //支付宝二维码支付
        if (mZhifubaoCodeBtn.isSelected()) {
            payZhiFuBao();
        }
    }

    /**
     * 刷卡支付响应事件
     */
    private void paySwipCard() {
        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        //*************订单存储结束**********************

        //模拟生产一个第三方流水号
        mTradeNo = "刷卡_" + System.currentTimeMillis();
        final String amount = getAmount();

        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        try {
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
            additionalMap.put(ParcelableMap.RESV, "wwxxnn");
            additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
            additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);

            String sign = CryptUtil.getDefaultSign(Config.config, amount, mTradeNo, null,DemoActivity.mMD5Key,
                additionalMap.getMap());
            //****设置打印出来的订单号*****
            additionalMap.put(ParcelableMap.PRINT_ORDER_NO,"自定义第三方流水号");

            //发起交易
            CashboxProxy.getInstance(mContext).startTrading(PayType.TYPE_CARD, amount,
                mTradeNo, transactionId,
                SignType.TYPE_MD5, sign, additionalMap, new ITradeCallback() {
                    @Override
                    public void onTradeSuccess(ParcelableMap map) {
                        Log.e("&&&&&", "-----------------onTradeSuccess");
                        Log.d(TAG, "刷卡的交易成功");
                        tradeSuccess(map);
                    }

                    //设置了不显示盒子签购单时回调此方法。
                    @Override
                    public void onTradeSuccessWithSign(ParcelableMap map,
                        ParcelableBitmap signBitmap) {
                        Log.e("&&&&&", "onTradeSuccessWithSign");
                    }

                    @Override
                    public void onTradeFail(ErrorMsg msg) {
                        Log.d(TAG, msg.getErrorMsg() + "[" + msg.getErrorCode() + "]");

                        //*******写入文件******
                        tradeFail(msg,amount);
                        //********写入文件结束************
                    }
                });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    //二维码扫描支付
    private void payWechatScan() {
        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_WECHAT_SCAN);
        //*************订单存储结束**********************

        //模拟生产一个外部流水号
        mTradeNo = "out_" + System.currentTimeMillis();
        String transactionId = System.currentTimeMillis() + "";
        final String amount = getAmount();
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.RESV, "wwxxnn");
        additionalMap.put(ParcelableMap.PARTNER_ID, Config.config.getPartnerId());
        additionalMap.put(ParcelableMap.PARTNER_USERID,Config.config.getPartnerUserId());
        additionalMap.put(ParcelableMap.IBOX_MCHTNO, Config.config.getIboxMchtNo());
        String orderNo = "2015121716503";
        additionalMap.put(ParcelableMap.ORDER_NO, orderNo);
        additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL,mCallbackUrl);
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, mTradeNo, null,DemoActivity.mMD5Key,
            additionalMap.getMap());
        Log.e("WeChatWasScanPay  &&&&", "sign = " + sign);
        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "微信被扫的商户订单号");
        additionalMap.put(ParcelableMap.SUBJECT,"二维码扫描支付");

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
                        tradeFail(msg, amount);
                        //*************写入文件结束**********************
                    }
                });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void payWechatCode() {
        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_WECHAT_CODE);

        //模拟生产一个外部流水号
        final String outTradeNo = "out_" + System.currentTimeMillis();
        final String amount = getAmount();
        order.setmPayType(StringUtil.PAY_TYPE_WECHAT_CODE);

        String transactionId = System.currentTimeMillis() + "";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.RESV, "微信主扫");
        additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL, mCallbackUrl);
        additionalMap.put(ParcelableMap.ORDER_NO, "20170614213030");
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, outTradeNo, null,DemoActivity.mMD5Key,
            additionalMap.getMap());
        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "微信主扫的商户订单号");
        additionalMap.put(ParcelableMap.SUBJECT,"二维码主扫支付");

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
                        tradeFail(msg, amount);
                        //*******写入文件结束******
                    }
                });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void payZhiFuBao() {
        //***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_ZHIFUBAO_CODE);
        //***********订单存储**************
        //模拟生产一个外部流水号
        mTradeNo = "out_" + System.currentTimeMillis();
        String transactionId = System.currentTimeMillis() + "";
        final String amount = getAmount();

        String orderNo = "2015121716500";
        ParcelableMap additionalMap = new ParcelableMap();
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
        additionalMap.put(ParcelableMap.ORDER_NO, orderNo);
        additionalMap.put(ParcelableMap.ORDER_TIME, mOrderTime);
        additionalMap.put(ParcelableMap.CALL_BACK_URL,mCallbackUrl);
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, amount, mTradeNo, null,DemoActivity.mMD5Key,
            additionalMap.getMap());
        additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "myOrderNotest");
        additionalMap.put(ParcelableMap.SUBJECT,"支付宝主扫支付");


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
                        tradeFail(msg, amount);


                    }
                });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void tradeFail(ErrorMsg msg, String amount) {
        //*******写入文件******
        order.setmTradeStatus(msg.getErrorMsg());
        order.setmAmount(toYuanAmount(amount));
        order.setmTradeStatus(StringUtil.TRADE_STATUS_FAIL);
        order.setmCbTradeNo(mCbTradeNo);
        order.setmOutTradeNo(mTradeNo);
        objToFile.writeObject(order);

        //写入数据库
        LogUtils.i("ORDER",order.toString());
        DbHelper.getInstance().saveDb(db,order);

        //*******写入文件结束******
    }

    /**
     * 所有类型的交易成功后统一操作
     * @param map 回执信息
     */
    private void tradeSuccess(ParcelableMap map) {
        for (String key : map.getMap().keySet()) {
            Log.d(TAG, key + ":" + map.get(key));
        }
        mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
        mTradeNo = map.get(ParcelableMap.PARTNER_TRADE_NO);
        String amount = map.get(ParcelableMap.TRANS_AMOUNT);
        //*******写入文件******
        order.setmCbTradeNo(mCbTradeNo);
        order.setmOutTradeNo(mTradeNo);
        order.setmAmount(toYuanAmount(amount));
        order.setmTradeStatus(StringUtil.TRADE_STATUS_SUCCESS);
//        objToFile.writeObject(order);

        //写入数据库
        LogUtils.i("ORDER",order.toString());
        DbHelper.getInstance().saveDb(db,order);

        Intent intent = new Intent(PayOrderActivity.this, DemoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        setResult(BookOrderActivity.OK);
        finish();
        //********写入文件结束************
    }

    private void clearCheckStatus() {
        mSwipBtn.setSelected(false);
        mWechatScanBtn.setSelected(false);
        mWechatCodeBtn.setSelected(false);
        mZhifubaoCodeBtn.setSelected(false);
    }

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
