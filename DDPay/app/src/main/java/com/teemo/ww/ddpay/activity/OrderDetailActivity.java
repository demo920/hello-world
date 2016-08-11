package com.teemo.ww.ddpay.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.ParcelableBitmap;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.iboxpay.cashbox.minisdk.model.TradingNo;
import com.teemo.ww.ddpay.R;
import com.teemo.ww.ddpay.app.PayApplication;
import com.teemo.ww.ddpay.db.DbHelper;
import com.teemo.ww.ddpay.manager.IBoxPayManager;
import com.teemo.ww.ddpay.utils.CryptUtil;
import com.teemo.ww.ddpay.bean.Order;
import com.teemo.ww.ddpay.utils.StringUtil;
import com.teemo.ww.ddpay.view.TitleBar;

import org.xutils.DbManager;

public class OrderDetailActivity extends Activity {
    private static final String TAG = OrderDetailActivity.class.getSimpleName();
    private TextView mTradeStatusTv, mGoodsNameTv, mAmountTv, mOrderNumberTv, mOrderDateTv,
            mPayTypeTv;
    private Button mCheckSaleSlipBtn, mPrintSaleSlipBtn;
    private Context mContext;
    private Order order;
    private TitleBar titleBar;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PayApplication app =  (PayApplication) getApplication();
        db = app.getDb();
        setContentView(R.layout.activity_order_detail);

        initView();
        initData();
    }

    private void initData() {
        mContext = this;

        mTradeStatusTv.setText(order.getmTradeStatus());

        mGoodsNameTv.setText(order.getmGoodsName());
        mAmountTv.setText(order.getmAmount());
        mOrderNumberTv.setText(order.getmOutTradeNo());
        mOrderDateTv.setText(order.getmDateTime());
        mPayTypeTv.setText(order.getmPayType());
    }

    private void initView() {
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(StringUtil.ORDER_DETAIL);
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (order.getmTradeStatus().contains(StringUtil.TRADE_STATUS_SUCCESS)
                && !order.getmPayType().contains(StringUtil.PAY_TYPE_WECHAT_SCAN)) {//交易成功，显示撤销订单按钮（且不是微信被扫）
            titleBar.setRightBtn("撤销订单", repealOrderListener);
        }
        mTradeStatusTv = (TextView) findViewById(R.id.tv_trade_status);
        mGoodsNameTv = (TextView) findViewById(R.id.tv_goods_name);
        mAmountTv = (TextView) findViewById(R.id.tv_amount);
        mOrderNumberTv = (TextView) findViewById(R.id.tv_order_number);
        mOrderDateTv = (TextView) findViewById(R.id.tv_data_time);
        mPayTypeTv = (TextView) findViewById(R.id.tv_pay_type);

        mCheckSaleSlipBtn = (Button) findViewById(R.id.btn_check_sale_slip);
        mCheckSaleSlipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showTradeDetail();
                IBoxPayManager manager = new IBoxPayManager(mContext);
                manager.showTradeDetail(order, DemoActivity.mMD5Key);
            }
        });
        mPrintSaleSlipBtn = (Button) findViewById(R.id.btn_print_sale_slip);
        mPrintSaleSlipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                printTradeInfo();
                IBoxPayManager manager = new IBoxPayManager(mContext);
                manager.printTradeInfo(order,DemoActivity.mMD5Key);
            }
        });
    }

    private void printTradeInfo() {
        try {
            //打印签购单
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);


            final String sign = CryptUtil.getDefaultSign(Config.config, null, order.getmOutTradeNo(),
                    order.getmCbTradeNo(), DemoActivity.mMD5Key,
                    additionalMap.getMap());
            //****设置打印出来的订单号*****
            additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "打印自定义第三方流水号");
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

    private void showTradeDetail() {
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

    View.OnClickListener repealOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //撤销该订单
//            repealOrder();
            //加个转圈提示
            final ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            IBoxPayManager manager = new IBoxPayManager(mContext);
            manager.repealOrder(order,DemoActivity.mMD5Key, new ITradeCallback() {
                @Override
                public void onTradeSuccess(ParcelableMap map) {
                    dialog.dismiss();
                    Log.d(TAG, "撤销成功");
                    for (String key : map.getMap().keySet()) {
                        Log.d(TAG, key + ":" + map.get(key));
                    }
                    final String mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTradeStatusTv.setText("已撤销");
                            titleBar.setRightBtn("", null);
                            DbHelper.getInstance().updataDb(db,Order.class,"cbTradeNo",mCbTradeNo,"tradeStatus","已撤销");
                        }
                    });
                }

                @Override
                public void onTradeSuccessWithSign(ParcelableMap map,
                                                   ParcelableBitmap signBitmap) {
                    dialog.dismiss();
                }

                @Override
                public void onTradeFail(ErrorMsg msg) {
                    dialog.dismiss();
                    final String errorStr =
                            "撤销失败 - " + msg.getErrorCode() + "," + msg.getErrorMsg();
                    Log.d(TAG, errorStr);
                }
            });
        }
    };

    private void repealOrder() {
        //加个转圈提示
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ParcelableMap additionalMap = new ParcelableMap();
        String transactionId = System.currentTimeMillis() + "";
        additionalMap.put(ParcelableMap.TRANSACTION_ID, transactionId);
//        additionalMap.put(ParcelableMap.RESV, "abcd");

        String transAmount = getAmount(order.getmAmount());
        //签名
        String sign = CryptUtil.getDefaultSign(Config.config, transAmount, order.getmOutTradeNo(),
                order.getmCbTradeNo(), DemoActivity.mMD5Key, additionalMap.getMap());
        try {
            CashboxProxy.getInstance(mContext).cancelTrading(transAmount, transactionId, sign,
                    SignType.TYPE_MD5, new TradingNo(order.getmOutTradeNo(), order.getmCbTradeNo()),
                    additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            dialog.dismiss();
                            Log.d(TAG, "撤销成功");
                            for (String key : map.getMap().keySet()) {
                                Log.d(TAG, key + ":" + map.get(key));
                            }
                            final String mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTradeStatusTv.setText("已撤销");
                                    titleBar.setRightBtn("", null);
                                    DbHelper.getInstance().updataDb(db,Order.class,"cbTradeNo",mCbTradeNo,"tradeStatus","已撤销");
                                }
                            });
                        }

                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map,
                                                           ParcelableBitmap signBitmap) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            dialog.dismiss();
                            final String errorStr =
                                    "撤销失败 - " + msg.getErrorCode() + "," + msg.getErrorMsg();
                            Log.d(TAG, errorStr);
                        }
                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private String getAmount(String amount) {
        if (!TextUtils.isEmpty(amount)) {
            long amountDouble = (long) (Double.parseDouble(amount) * 100);
            amount = String.valueOf(amountDouble);
        }
        return amount;
    }
}
