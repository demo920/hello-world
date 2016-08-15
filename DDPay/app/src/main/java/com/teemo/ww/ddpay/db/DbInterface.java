package com.teemo.ww.ddpay.db;

import android.app.Application;

import java.util.List;

/**
 * Created by admin on 2016/8/12.
 */

public interface DbInterface {

    void init(Application application);

    /**
     * 插入数据
     *
     * @param t  操作实体
     */
    void saveDb(Class t);

    /**
     * 删除记录
     * @param t
     * @param id
     */
    void deleteObj(Class t, int id);

    /**
     * 更新数据状态
     * @param t  操作实体
     * @param where 查找项
     * @param obj 查找项的值
     * @param updateRaw 更新项
     * @param newObj 更新项的新值
     */
    void updataDb(Class t, String where, Object obj, String updateRaw, Object newObj);

    /**
     * 读取数据库所有
     *
     * @param t  操作实体
     * @return 返回所有实体集合
     */
    List<Class> readDbList(Class t);
}
