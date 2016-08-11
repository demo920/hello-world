package com.teemo.ww.ddpay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.callback.GetCashboxProxyCallback;
import com.iboxpay.cashbox.minisdk.callback.IAuthCallback;
import com.iboxpay.cashbox.minisdk.callback.IFetchCardInfoCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.M1CardPWType;
import com.iboxpay.cashbox.minisdk.model.PrintPreference;
import com.teemo.ww.ddpay.R;
import com.teemo.ww.ddpay.app.PayApplication;
import com.teemo.ww.ddpay.bean.Order;
import com.teemo.ww.ddpay.db.DbHelper;
import com.teemo.ww.ddpay.utils.LogUtils;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DemoActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "打印机 客户端";

    private Button mGetSnBtn, mGetPlainTextCardNo,mGetM1CardNo;

    private Context mContext;

    private Button mPayBtn, mMyOrderBtn;
    ////TODO 请找盒子申请配置信息
    private String mAppCode = "2001740";
    private String mMerchantNo = "001440196451453";
    public static String mMD5Key = "x24aq6QZT/c=";

    private PrintPreference printPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        mContext = this;
        setContentView(R.layout.activity_demo);
        initView();
//        initData();
    }

    private void initData() {
        //初始化配置
        printPreference = new PrintPreference();
        //是否显示“盒子支付签购单”页面
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
            CashboxProxy.getInstance(this).initAppInfo(Config.config, new IAuthCallback() {
                @Override
                public void onAuthSuccess() {
                    Log.d(TAG, "authSuccess");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                "签到成功", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onAuthFail(final ErrorMsg msg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                "签到失败:" + msg.getErrorCode() + " ," + msg.getErrorMsg(),
                                Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mGetSnBtn = (Button) findViewById(R.id.btn_getSn);
        mGetSnBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String sn = CashboxProxy.getBoxSn(DemoActivity.this);
                Toast.makeText(DemoActivity.this, "SN=" + sn, Toast.LENGTH_SHORT).show();
            }
        });

        mPayBtn = (Button) findViewById(R.id.btn_pay);
        mMyOrderBtn = (Button) findViewById(R.id.btn_my_order);
        mGetPlainTextCardNo = (Button) findViewById(R.id.btn_getPlainTextCardNo);

        //获取M1卡号
        mGetM1CardNo = (Button) findViewById(R.id.btn_getM1CardNo);
        mGetM1CardNo.setOnClickListener(this);

        mPayBtn.setOnClickListener(this);
        mMyOrderBtn.setOnClickListener(this);
        mGetPlainTextCardNo.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override public void onClick(View v) {
        Intent intent = new Intent();
        int resId = v.getId();

        switch (resId) {
            //下单
            case R.id.btn_pay:

//                //获取当前交易时间
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                String mOrderTime = df.format(new Date());
//                Order order = new Order();
//                order.setmDateTime(mOrderTime);
//                order.setmGoodsName("大宝剑");
//                //写入数据库
//                LogUtils.i("ORDER",order.toString());
//                DbHelper.getInstance().saveDb(order);

                intent.setClass(mContext, BookOrderActivity.class);
                startActivity(intent);
                break;

            //我的订单
            case R.id.btn_my_order:
//                //读取数据库
//                LogUtils.i("ORDER","读取数据库");
//                List<Order> orders = DbHelper.getInstance().readDbList();
//                LogUtils.i("ORDER",orders.size()+",第一个是："+orders.get(0).toString());

                intent.setClass(mContext, OrderListActivity.class);
                startActivity(intent);
                break;

            //获取明文“会员”卡号
            case R.id.btn_getPlainTextCardNo:
                fetchCardInfo();
                break;

            //获取M1卡 卡号
            case R.id.btn_getM1CardNo:

                PayApplication app =  (PayApplication) getApplication();
                DbManager db = app.getDb();
                DbHelper.getInstance().updataDb(db,Order.class,"amount","552.0","tradeStatus","交易撤销了");
//                fetchM1CardInfo();
                break;
        }
    }

    private void fetchCardInfo() {

        CashboxProxy.getInstance(mContext).fetchCardInfo(new IFetchCardInfoCallback() {
            @Override
            public void onFetchCardInfoSuccess(final String cardInfo) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoActivity.this, cardInfo, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFetchCardInfoFail(final ErrorMsg msg) {
                Log.e("MiniCashBoxLog", "fetchCardInfo----Fail-----msg=" + msg.getErrorCode());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoActivity.this, "刷卡失败，请重新刷卡", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void fetchM1CardInfo(){
        try {
            CashboxProxy.getInstance(mContext).fetchM1CardInfo(56, "A22BBBCA", M1CardPWType.TYPE_A,"FFFFFFFFFFFF",
                new IFetchCardInfoCallback() {
                    @Override public void onFetchCardInfoSuccess(final String cardInfo) {
                        Log.e("MiniCashBoxLog", "M1----succ-----cardInfo=" + cardInfo);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DemoActivity.this, cardInfo, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override public void onFetchCardInfoFail(final ErrorMsg msg) {
                        Log.e("MiniCashBoxLog", "M1----Fail-----msg=" + msg.getErrorMsg());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DemoActivity.this, msg.getErrorMsg()+"["+msg.getErrorCode()+"]", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
