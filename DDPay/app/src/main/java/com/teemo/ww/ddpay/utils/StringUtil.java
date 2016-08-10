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
 * StringUtil.java ,Created by: wangxiunian ,2015-06-10 19:17:11 ,lastModified:2015-06-10 09:31:03
 */


import android.text.TextUtils;
import android.util.Log;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {
    public static final String PAY_AMOUNT = "pay_amount";
    public static final String GOODS_NAME = "goods_name";
    public static final String APP_CONFIG_KEY = "app_config";
    public static final String SHARED_PREFERENCE_NAME = "shared_preference_name";
    public static final String ORDER_DETAIL = "order_detail";
    public static final String PAY_TYPE_SWIP_CARD = "刷卡支付";
    public static final String PAY_TYPE_WECHAT_SCAN = "微信扫码支付";
    public static final String PAY_TYPE_WECHAT_CODE = "微信二维码支付";
    public static final String PAY_TYPE_ZHIFUBAO_CODE = "支付宝二维码支付";
    public static final String TRADE_STATUS_SUCCESS = "交易成功";
    public static final String TRADE_STATUS_FAIL = "交易失败";



    public static boolean checkString(String str) {
        return (null != str && !TextUtils.isEmpty(str) && !str
            .equalsIgnoreCase("null"));
    }

    public static String toYuanByFen(String moneyStr) {
        if (moneyStr == null) {
            return "0";
        }

        moneyStr = moneyStr.replaceAll(",", "");
        if (StringUtil.checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                double money = new BigDecimal(moneyStr)
                    .divide(new BigDecimal("100"))
                    .setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();

                DecimalFormat df = new DecimalFormat("#,###,##0.00");
                String st = df.format(money);
                return st;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return ((Integer.parseInt(moneyStr)) / 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    public static String toFenByYuan(String moneyStr) {
        moneyStr = moneyStr.replaceAll(",", "");
        if (moneyStr.endsWith(".")) {
            moneyStr = moneyStr.substring(0, moneyStr.length() - 1);
        }
        if (StringUtil.checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                return (new BigDecimal(moneyStr)
                    .multiply(new BigDecimal("100")).setScale(0,
                        BigDecimal.ROUND_FLOOR) + "");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return ((Integer.parseInt(moneyStr)) * 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    public static String getMoneyUnit(float yuan) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        if (yuan >= 10000.f) {
            return fnum.format(yuan / 10000) + "万元";
        }
        return yuan + "元";
    }

    public static String replaceAllBlank(String s) {
        if (checkString(s)) {
            s = s.replaceAll(" ", "");
        }
        return s;
    }

    public static boolean isZeroOnTopOfSrting(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.substring(0, 1).equals("0")
                || str.substring(0, 1).equals(".");
        } else {
            return true;
        }
    }

    public static String getDateTime(String timeFormat) {
        String result = "NaN";
        if (!checkString(timeFormat)) {
            timeFormat = "yyyy-MM-dd";
        }
        try {
            Date curDate = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
            result = sdf.format(curDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String separatorCredit(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return value;
            }
            String clearValue = clearSeparator(value);
            StringBuffer result = new StringBuffer(clearValue);
            for (int i = 1; i <= (clearValue.length() - 1) / 4; i++) {
                result.insert(i * 4 + i - 1, "-");
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String clearSeparator(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return value;
            }
            value = value.replace("-", "");
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    /**
     * check if String is alphanumeric without punctuation
     *
     * @param s String input
     * @return true if s matches the pattern
     */
    public static boolean isAlphaNumeric(String s) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]+");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static boolean checkMobile(String mobile) {
        try {
            if (mobile == null || mobile.length() != 11) {
                return false;
            } else {
                Pattern p = Pattern.compile("^[1][0-9]{10}$");
                Matcher m = p.matcher(mobile);
                return (m.matches());
            }
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static boolean checkMoneyValid(String money) {
        if (StringUtil.checkString(money)) {
            Pattern p = Pattern.compile("^((\\d+)|0|)(\\.(\\d+)$)?");
            Matcher m = p.matcher(money);
            return (m.matches());
        }
        return false;
    }

    public static boolean checkCreditNum(String num) {
        boolean result = false;
        try {
            if (StringUtil.checkString(num) && !StringUtil.isZeroOnTopOfSrting(num)) {
                if (num.length() >= 15 && num.length() <= 19
                    && !StringUtil.isZeroOnTopOfSrting(num)) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean checkPWD(String pwd) {
        try {
            return !(pwd.length() < 6 || pwd.length() > 20);
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static boolean checkEmail(String email) {
        try {
            if (email.length() < 1 || email.length() > 30) {
                return false;
            } else {
                Pattern p = Pattern
                    .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
                Matcher m = p.matcher(email);
                return (m.matches());
            }
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static boolean checkLoginAccount(String account) {
        if (checkString(account) && account.length() <= 30) {
            int index = account.indexOf("#");
            if (index > 0) {
                account = account.substring(0, index);
            }
            return checkMobile(account) || checkEmail(account);
        } else {
            return false;
        }
    }

    public static String replaceUserNameWithStar(String userName) {
        try {
            if (!checkString(userName) || userName.length() < 2) {
                return userName;
            }
            String star = "";
            int userNameLen = (userName.length() % 2 == 0) ? (userName.length())
                : (userName.length() - 1);
            int starLen = userNameLen / 2;
            for (int i = 0; i < starLen; ++i) {
                star += "*";
            }
            return (star + userName.substring(starLen));
        } catch (Exception e) {
            e.printStackTrace();
            return userName;
        }
    }

    public static boolean checkPassword(String password) {
        return checkString(password) && password.length() >= 6;
    }



    public static String appZero(int num) {

        if (num == -1) {
            return "00";
        }

        if (num < 10) {
            return "0" + num;
        }

        return num + "";
    }

    public static boolean checkURL(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();
        return isMatch;
    }

    public static void print(String text) {
        Log.d("MiniCashBoxLog", text);
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }
}
