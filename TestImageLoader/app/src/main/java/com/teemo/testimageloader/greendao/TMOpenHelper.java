package com.teemo.testimageloader.greendao;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Asus on 2017/3/23.
 */

public class TMOpenHelper extends DaoMaster.OpenHelper {
    public TMOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion == 1) {
            //操作数据库的更新
            Log.e("update", "更新数据库User,增加列address--oldVersion:" + oldVersion + ",--newVersion:" + newVersion);
            MigrationHelper.migrate(db, UserDao.class);
        } else if (oldVersion == 2) {
            //操作数据库的更新
            Log.e("update", "更新数据库User,删除列address--oldVersion:" + oldVersion + ",--newVersion:" + newVersion);
            MigrationHelper.migrate(db, UserDao.class);
        }else if (oldVersion == 3) {
            //操作数据库的更新
            Log.e("update", "更新数据库User,删除列name--oldVersion:" + oldVersion + ",--newVersion:" + newVersion);
            MigrationHelper.migrate(db, UserDao.class);
        }
    }

}
