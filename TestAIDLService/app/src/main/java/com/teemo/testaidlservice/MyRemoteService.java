package com.teemo.testaidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * 实现AIDL接口，并返回IBinder对象
 */
public class MyRemoteService extends Service {

    public MyRemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //要return一个IBinder,否则客户端调用的时候会报空指针
        return mBinder;
    }

    /**
     * // 要使用AIDL，Service需要以aidl文件的方式提供服务接口，AIDL工具将生成一个相应的java接口，
     * // 并且在生成的服务接口中包含一个功能调用的stub服务桩类。
     * // Service的实现类需要去继承这个stub服务桩类。
     * // Service的onBind方法会返回实现类的对象，之后你就可以使用它了。
     */
    private IBinder mBinder = new MyAidlInterface.Stub() {
        @Override
        public int plus(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public String toUpperCase(String str) throws RemoteException {
            if (str != null) {
                return str.toUpperCase();
            }
            return null;
        }
    };
}
