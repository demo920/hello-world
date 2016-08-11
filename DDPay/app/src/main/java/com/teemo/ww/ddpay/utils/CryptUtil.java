package com.teemo.ww.ddpay.utils;/*
 * Copyright (c) 2011-2015. ShenZhen iBOXPAY Information Technology Co.,Ltd.
 *
 * All right reserved.
 *
 * This software is the confidential and proprietary
 * information of iBoxPay Company of China.
 * ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only
 * in accordance with the terms of the contract agreement
 * you entered into with iBoxpay inc.
 * CryptUtil.java ,Created by: wangxiunian ,2015-07-28 16:25:52 ,lastModified:2015-07-28 16:25:52
 */


import android.text.TextUtils;
import android.util.Log;
import com.iboxpay.cashbox.minisdk.model.Config;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author kingmanager
 * @version V1.0
 * @title CryptUtil.java
 * @package com.iboxpay.common.util
 * @description 提供各种加密解密的算法工具
 * @com.iboxpay.iboxpay 2011-10-25 下午06:18:51
 */
public class CryptUtil {

    public static final String TRADE_APP_CODE_KEY = "appCode";
    public static final String TRADE_AMOUNT_KEY = "transAmount";
    public static final String OUT_TRADE_NO_KEY = "outTradeNo";
    public static final String TRADE_NO_KEY = "tradeNo";

    public static final String PARTNER_ID = "partnerId";
    public static final String PARTNER_USER_ID = "partnerUserId";
    public static final String IBOXPAY_MERCHANT_NO = "iboxMchtNo";

    /**
     * MD5 加密
     *
     * @param info 需要MD5加密的字符穿
     * @return String result MD5加密后返回的结果
     */
    public static String encryptToMD5(String info) {
        // MessageDegist计算摘要后 得到的是Byte数组
        byte[] digesta = null;
        try {
            // 获取消息摘要MessageDigest抽象类的实例
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            // 添加需要进行计算摘要的对象（字节数组）
            mDigest.update(info.getBytes("UTF-8"));
            // 计算摘要
            digesta = mDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 将字节数组转换为String并返回

        return bytes2Hex(digesta);
    }

    /**
     * SHA-1 加密
     *
     * @param info 需要SHA加密的字符穿
     * @return String result SHA加密后返回的结果
     */
    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            // 获取消息摘要MessageDigest抽象类的实例
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            // 添加需要进行计算摘要的对象（字节数组）
            mDigest.update(info.getBytes());
            // 计算摘要
            digesta = mDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 将字节数组转换为String并返回
        return bytes2Hex(digesta);
    }

    /**
     * 将2进制字节数组转换为16进制字符串
     *
     * @param bytes 字节数组
     * @return String hex 返回16进制内容的字符串，比较类似UDB的密钥
     */
    public static String bytes2Hex(byte[] bytes) {
        // 16进制结果
        String hex = "";
        // 存放byte字节对象的临时变量
        String temp = "";

        // 对字节数组元素进行处理
        for (int i = 0; i < bytes.length; i++) {
            // byte的取值范围是从-127-128，需要& 0xFF (11111111) 使得byte原来的负值变成正的
            temp = Integer.toHexString(bytes[i] & 0xFF);
            // 长度为1，那么需要补充一位 0
            if (temp.length() == 1) {
                hex = hex + "0" + temp;
            } else {
                // 长度为2，直接拼接即可
                hex = hex + temp;
            }
        }
        // 返回大写的字符串
        return hex.toUpperCase();
    }

    /**
     * 16进制内容的字符串转换为字节数组 对于已加密的内容(String类型)，需要使用此方法将其转换为字节数组，不能直接使用getBytes()
     *
     * @param hex 已加密的内容
     * @return byte[] result 已加密内容转换为字节数组
     */
    public static byte[] hex2byte(String hex) {
        byte[] result = new byte[8];

        byte[] temp = hex.getBytes();

        for (int i = 0; i < 8; i++) {
            result[i] = uniteBytes(temp[i * 2], temp[i * 2 + 1]);
        }

        return result;
    }

    /**
     * 将两个ASCII字符拼接成一个字节，比如 “EF” ---> "0xEF"
     *
     * @param src1 ASCII字符1
     * @param src2 ASCII字符2
     * @return byte result 拼接后的结果
     */
    public static byte uniteBytes(byte src1, byte src2) {
        byte temp1 = Byte.decode("0x" + new String(new byte[] {
            src1
        })).byteValue();
        byte temp2 = Byte.decode("0x" + new String(new byte[] {
            src2
        })).byteValue();

        byte result = (byte) (temp1 ^ temp2);
        return result;
    }

    /**
     * todo 此方法不要放在客户端。。 获取默认的签名 tradeNo 在撤销时传入
     * @param config
     * @param amount 交易金额
     * @param outTradeNo 商户自定义订单号
     * @param cbTradeNo 盒子支付订单号，没有不用加入验签参数列表（撤销订单时传入）
     * @param md5key
     * @param attachMap
     * @return
     */
    public static String getDefaultSign(Config config, String amount, String outTradeNo,
        String cbTradeNo,String md5key, Map<String, String> attachMap) {
        TreeMap<String, String> treeMap = new TreeMap<>();


        treeMap.put(TRADE_APP_CODE_KEY, config.getAppCode());
        treeMap.put(TRADE_AMOUNT_KEY, amount);
        treeMap.put(OUT_TRADE_NO_KEY, outTradeNo);
        treeMap.put(TRADE_NO_KEY, cbTradeNo);//盒子支付订单号，没有不用加入验签参数列表
        treeMap.put(PARTNER_USER_ID, config.getPartnerUserId());
        treeMap.put(PARTNER_ID, config.getPartnerId());
        treeMap.put(IBOXPAY_MERCHANT_NO, config.getIboxMchtNo());
        treeMap.putAll(attachMap);
        //String md5key = "yLyFgLWSGik=";
        //String md5key = "lJ3Ls0y8bpQ=";
        StringBuilder sb = new StringBuilder();
        for (String key : treeMap.keySet()) {
            if (!TextUtils.isEmpty(treeMap.get(key))) {
                sb.append(key).append("=").append(treeMap.get(key)).append("&");
            }
        }
        String result = "";
        if (sb.length() > 0) {
            result = sb.substring(0, sb.length() - 1);
        }
        Log.d("CryptUtil", result + md5key);
        Log.d("MiniCashBoxLog", result + md5key);
        String md5Result = CryptUtil.encryptToMD5(result + md5key);
        Log.d("CryptUtilResult", md5Result, new Throwable(md5Result));
        return md5Result;
    }
}
