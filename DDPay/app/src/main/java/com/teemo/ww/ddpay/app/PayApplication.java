package com.teemo.ww.ddpay.app;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.callback.IAuthCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.PrintPreference;
import com.teemo.ww.ddpay.utils.LogUtils;
import com.teemo.ww.ddpay.utils.ToastUtils;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * Created by admin on 2016/8/8.
 */

public class PayApplication extends Application {

    private static String TAG = "PayApplication";
    ////TODO 请找盒子申请配置信息
    private String mAppCode = "2001740";
    private String mMerchantNo = "001440196451453";
    public static String mMD5Key = "x24aq6QZT/c=";

    /**数据库配置*/
    private DbManager.DaoConfig daoConfig;
    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    /**数据库操作对象*/
    private DbManager db;
    public DbManager getDb(){
        db = x.getDb(daoConfig);
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //xUtils任务控制中心,需要在application的onCreate中初始化
        x.Ext.init(this);
        initDb();
        initBox();
    }

    /**初始化xUtil数据库配置信息*/
    private void initDb() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("diandian.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(new File("/sdcard"))
                .setDbVersion(2)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                });
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
