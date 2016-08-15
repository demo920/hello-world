package com.teemo.ww.ddpay.app;

import android.app.Application;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.callback.IAuthCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.PrintPreference;
import com.teemo.ww.ddpay.db.DbHelper;
import com.teemo.ww.ddpay.manager.IBoxPayManager;
import com.teemo.ww.ddpay.utils.LogUtils;
import com.teemo.ww.ddpay.utils.ToastUtils;
import com.uuch.adlibrary.utils.DisplayUtil;

/**
 * Created by admin on 2016/8/8.
 */

public class PayApplication extends Application {

    private static String TAG = "PayApplication";
    ////TODO 请找盒子申请配置信息
    private String mAppCode = "2001740";
    private String mMerchantNo = "001440196451453";
    public static String mMD5Key = "x24aq6QZT/c=";

    @Override
    public void onCreate() {
        super.onCreate();
        //xUtils任务控制中心,需要在application的onCreate中初始化
        DbHelper.getInstance().init(this);
        IBoxPayManager.getInstance(this).initAppInfo();
//        initAppInfo();

        initDisplayOpinion();

        Fresco.initialize(this);
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }

    /**初始化盒子支付配置信息*/
    private void initBox() {
        //初始化配置
        PrintPreference printPreference = new PrintPreference();
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
                    LogUtils.d(TAG, "authSuccess");
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(getApplicationContext(),"签到成功");
                        }
                    });
                }

                @Override
                public void onAuthFail(final ErrorMsg msg) {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(getApplicationContext(),"签到失败");
                        }
                    });
                }
            });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }
}
