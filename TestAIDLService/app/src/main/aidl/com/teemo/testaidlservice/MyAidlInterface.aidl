// MyAidlInterface.aidl
package com.teemo.testaidlservice;

// Declare any non-default types here with import statements

interface MyAidlInterface {
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    int plus(int a, int b);
    String toUpperCase(String str);
}
