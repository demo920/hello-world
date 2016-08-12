package com.teemo.ww.ddpay.db;

import android.app.Application;

import com.teemo.ww.ddpay.utils.LogUtils;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2016/8/10.
 */

public class DbHelper<T> {
    private static final String TAG = DbHelper.class.getSimpleName();
    private static final String DB_NAME = "diandian.db";
    private static final int DB_VERSION = 1;
    private static final String DB_PATH = "/sdcard";

    /** 实际的数据库访问接口 */
    private DbManager db;

    private static DbHelper dbHelper;
    private File dbDir = new File(DB_PATH);

    /**
     * 获取实例
     * @return 返回当前类实例
     */
    public static DbHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DbHelper();
            return dbHelper;
        }
        return dbHelper;
    }

    /** xUtils任务控制中心,需要在application的onCreate中初始化 */
    public void init(Application application) {
        x.Ext.init(application);
        initDb(DB_NAME, dbDir, DB_VERSION);
    }

    /** 初始化xUtil数据库配置信息
     * @param Name
     * @param path
     * @param version*/
    private void initDb(String Name, File path, int version) {
        /**数据库配置*/
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(Name)
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(path)
                .setDbVersion(version)
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
        db = x.getDb(daoConfig);
    }

    /**
     * 插入数据
     *
     * @param t  操作实体
     */
    public void saveDb(T t) {
        // 一对多: (本示例的代码)
        // 自己在多的一方(child)保存另一方的(parentId), 查找的时候用parentId查parent或child.
        // 一对一:
        // 在任何一边保存另一边的Id并加上唯一属性: @Column(name = "parentId", property = "UNIQUE")
        // 多对多:
        // 再建一个关联表, 保存两边的id. 查询分两步: 先查关联表得到id, 再查对应表的属性.

        try {
            db.saveOrUpdate(t);
            LogUtils.i(TAG, t + "save success");

        } catch (Throwable e) {

            String temp = "error :" + e.getMessage() + "\n";
            LogUtils.e(TAG, temp);
        }
    }

    /**
     * 删除记录
     * @param t
     * @param id
     */
    public void deleteObj(T t, int id){
        try {
            if (id >= 1){
                db.deleteById((Class<T>) t,id);
                return;
            }
            db.delete((Class<T>) t);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据状态
     * @param t  操作实体
     * @param where 查找项
     * @param obj 查找项的值
     * @param updateRaw 更新项
     * @param newObj 更新项的新值
     */
    public void updataDb(Class t, String where, Object obj, String updateRaw, Object newObj) {
        try {

            WhereBuilder whereBuilder = WhereBuilder.b(where,"=",obj);
            KeyValue values = new KeyValue(updateRaw,newObj);
            int result = db.update((Class<T>) t,whereBuilder,values);
            LogUtils.d(TAG,"更新项："+result);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public T readDb(DbManager db, T t,String where,Object obj){

        return t;
    }

    /**
     * 读取数据库所有
     *
     * @param t  操作实体
     * @return 返回所有实体集合
     */
    public List<T> readDbList(T t) {
        List<T> all = new ArrayList<>();

        try {

//            DbManager db = x.getDb(daoConfig);
            all = db.selector((Class<T>) t).orderBy("id", true).findAll();
            for (T order : all) {
                LogUtils.e(TAG, order.toString());
            }

        } catch (Throwable e) {

        }
        return all;
    }
}
