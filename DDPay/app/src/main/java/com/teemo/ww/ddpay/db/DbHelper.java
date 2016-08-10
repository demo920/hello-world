package com.teemo.ww.ddpay.db;

import com.teemo.ww.ddpay.utils.LogUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2016/8/10.
 */

public class DbHelper<T> {
    private static final String TAG = DbHelper.class.getSimpleName();
    private static DbHelper dbHelper;

    private List<T> all;

    /**放入application入口*/
//    private static final String DB_NAME = "diandian.db";
//    private static final int DB_VERSION = 1;
//    private static final String DB_PATH = "/sdcard";
//
//    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
//            .setDbName(DB_NAME)
//            // 不设置dbDir时, 默认存储在app的私有目录.
//            .setDbDir(new File("/sdcard"))
//            .setDbVersion(DB_VERSION)
//            .setDbOpenListener(new DbManager.DbOpenListener() {
//                @Override
//                public void onDbOpened(DbManager db) {
//                    // 开启WAL, 对写入加速提升巨大
//                    db.getDatabase().enableWriteAheadLogging();
//                }
//            })
//            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
//                @Override
//                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
//                    // TODO: ...
//                    // db.addColumn(...);
//                    // db.dropTable(...);
//                    // ...
//                    // or
//                    // db.dropDb();
//                }
//            });

    public static DbHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DbHelper();
            return dbHelper;
        }
        return dbHelper;
    }

    /**
     * 插入数据
     *
     * @param db 数据库操作对象
     * @param t  操作实体
     */
    public void saveDb(DbManager db, T t) {
        // 一对多: (本示例的代码)
        // 自己在多的一方(child)保存另一方的(parentId), 查找的时候用parentId查parent或child.
        // 一对一:
        // 在任何一边保存另一边的Id并加上唯一属性: @Column(name = "parentId", property = "UNIQUE")
        // 多对多:
        // 再建一个关联表, 保存两边的id. 查询分两步: 先查关联表得到id, 再查对应表的属性.

        try {
//            DbManager db = x.getDb(daoConfig);
            db.saveOrUpdate(t);
            LogUtils.i(TAG, t + "save success");

        } catch (Throwable e) {

            String temp = "error :" + e.getMessage() + "\n";
            LogUtils.e(TAG, temp);
        }
    }

    /**
     * 删除记录
     * @param db
     * @param t
     * @param id
     */
    public void deleteObj(DbManager db,T t,int id){
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
     * 读取数据库所有
     *
     * @param db 数据库操作对象
     * @param t  操作实体
     * @return 返回所有实体集合
     */
    public List<T> readDbList(DbManager db, T t) {

        if (all == null) {
            all = new ArrayList<>();
        } else {
            all.clear();
        }

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
