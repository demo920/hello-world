package com.teemo.testimageloader.database;

import android.util.Log;

import com.teemo.testimageloader.MyApplication;
import com.teemo.testimageloader.bean.User;
import com.teemo.testimageloader.greendao.UserDao;

/**
 * Created by Asus on 2017/3/22.
 */

public class Test {

    private static final String TAG = "Test";

    public static void test() {

        String key = "teacher";
        Object object = "京津冀";

        String result = JsonTool.createJson(key, object);
        Log.e(TAG, result);

        String gson = JsonTool.createJsonObject(object);
        Log.e(TAG, gson);


        char c = 'a';
        System.out.print(c);
        System.out.print(c++);
        System.out.print(++c);
        System.out.print(c+1);

    }

    public static void main(String []args) {

        char c = 'a';
        System.out.println(c);
        c = 'a';
        System.out.println(c++);
        c = 'a';
        System.out.println(++c);
        c = 'a';
        System.out.println(c+1);

        char ch = '人';
        System.out.println(ch);

        int i = 99_55;
        System.out.println(i);
        reverse();
    }

    public static void reverse(){
        // 原始字符串
        String s = "A quick brown fox jumps over the lazy dog.";
        System.out.println("原始的字符串：" + s);

        System.out.print("反转后字符串：");
        for (int i = s.length(); i > 0; i--) {
            System.out.print(s.charAt(i - 1));
        }

        // 也可以转换成数组后再反转，不过有点多此一举
        char[] data = s.toCharArray();
        System.out.println();
        System.out.print("反转后字符串：");
        for (int i = data.length; i > 0; i--) {
            System.out.print(data[i - 1]);
        }

        System.out.println();
        System.out.print("反转后字符串：");
        StringBuffer buff = new StringBuffer(s);
        // java.lang.StringBuffer类的reverse()方法可以将字符串反转
        System.out.println(buff.reverse().toString());
    }

    public static void databaseTest(){
        UserDao userDao = MyApplication.mInstance.getDaoSession().getUserDao();

        User insertEntity = new User();
        insertEntity.setName("删除罗列");
//        insertEntity.setAddress("北京");
        userDao.insert(insertEntity);
    }
}
