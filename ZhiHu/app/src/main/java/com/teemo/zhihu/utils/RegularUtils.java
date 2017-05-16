package com.teemo.zhihu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Asus on 2017/4/24.
 */

public class RegularUtils {

    private static final String EMAIL = "\\w{3,20}@\\w+\\.(com|cn.net|org|gov)";
    private static final String WORD = "\\w";
    private static final String PHONE = "^0\\d{2,3}[- ]?\\d{7,8}$";
    private static final String MOBILE = "1(3|5|8|7)\\d{9}";
    private static final String URL = "http(s)?://([\\w-]+\\.)+[\\w-]+()?";
    /**
     * 验证汉字
     * 开头和几位都必须是汉字，+号表示可以连续（一到多）汉字
     */
    private static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    private static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 以字母、汉字开头
     */
    private static final String NAME = "^[\\u4e00-\\u9fa5a-zA-Z]";

    public static boolean isEmail(String s) {
        return isMatch(EMAIL, s);
    }

    public static boolean isWord(String s) {
        return isMatch(WORD, s);
    }

    public static boolean isPhone(String s) {
        return isMatch(PHONE, s);
    }

    public static boolean isMobile(String s) {
        return isMatch(MOBILE, s);
    }

    public static boolean isUrl(String s) {
        return isMatch(URL, s);
    }

    public static boolean isHan(String s) {
        return isMatch(REGEX_CHZ, s);
    }

    private static boolean isMatch(String regex, String s) {

        try {
            if (isEmpty(regex) || isEmpty(s)) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Pattern.matches(regex, s);
    }

    private static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static void main(String[] args) {

        String email = "789@qq.com";
        boolean match = isEmail(email);
        System.out.print("match: " + match + "\n");

        String word = "_";
        boolean matchWord = isWord(word);
        System.out.print("matchWord: " + matchWord + "\n");

        String phone = "027-44712241";
        boolean matchPhone = isPhone(phone);
        System.out.print("matchPhone: " + matchPhone + "\n");

        String mobile = "我的电话13512345678，他的电话15864354989，她的电话17789562458";
        Matcher matcher = Pattern.compile(MOBILE).matcher(mobile);
        while (matcher.find()) {
            System.out.println("找到电话: " + matcher.group());
        }

        String url = "http://w.a";
        System.out.println("网址？: " + isUrl(url));

        System.out.println("汉字？: " + isHan(" 好哒"));

        a();
        b();

        String hanzi = "好人b坏人";
        Matcher matcherHan = Pattern.compile(REGEX_CHZ).matcher(hanzi);
        while(matcherHan.find()){
            System.out.println("找到汉字: " + matcherHan.group());
        }
    }

    /**
     * 捕获组
     * 捕获组是通过从左至右计算其开括号来编号
     * 在表达式（（A）（B（C））），有四个这样的组：
     * ((A)(B(C)))
     * (A)
     * (B(C))
     * (C)
     */
    public static void a() {

        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! OK?";
//        String pattern = "(\\D*)(.*)(\\d+)";
            //Found value: This order was placed for QT3000
            //Found value: This order was placed for QT
            //Found value: 300
            //Found value: 0
        String pattern = "(\\D*)(\\d+)(.*)";
            //Found value: This order was placed for QT3000! OK?
            //Found value: This order was placed for QT
            //Found value: 3000
            //Found value: ! OK?

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            System.out.println("Found value: " + m.group(3));
        } else {
            System.out.println("NO MATCH");
        }
    }

    /**
     * 获取标签中间部分内容
     */
    private static void b() {
        String text = "<textarea rows=\"20\" cols=\"70\">nexus maven repository index properties updating index central</textarea>";
//        String reg = "<textarea.*?>.*?</textarea>";
        String reg = "(<textarea.*?>)(.*?)(</textarea>)";//捕获组
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(text);
        while (m.find()) {
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println(m.group(3));
            System.out.println("----");
        }

        String regNot = "(?:<textarea.*?>)(.*?)(?:</textarea>)";//非捕获组。共有二个捕获组：(.*?)和整个匹配到的内容，两个非捕获组:(?:</textarea>)和(?:<textarea.*?>)
        Matcher mNot = Pattern.compile(regNot).matcher(text);
        while (mNot.find()){
            System.out.println(mNot.group(0));
            System.out.println(mNot.group(1));
            System.out.println("--------------");
        }
    }


}
