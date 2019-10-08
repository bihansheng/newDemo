/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈字符串常用处理方法〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class StringUtil {

    //手机正则表达式
    private final static String phone = "[1][1-9]{1}[0-9]{9}";
    //邮箱正则表达式
    private final static String email = "[a-zA-Z0-9_]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z_-]+)+";

    private final static String num = "[0-9]+";


    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return true
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String regX = "[0-9]{17}X";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex) || text.matches(regX);
    }


    /**
     * 判断是否是正确的手机号码
     *
     * @param number 手机号码
     * @return 返回boolean
     */
    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern pattern = Pattern.compile(phone);
        return pattern.matcher(number).matches();
    }

    /**
     * 判断是不是一个合法的邮箱
     *
     * @param mail 邮箱
     * @return 返回boolean
     */
    public static boolean isEmail(String mail) {
        if (TextUtils.isEmpty(mail)) {
            return false;
        }
        Pattern pattern = Pattern.compile(email);
        return pattern.matcher(mail).matches();
    }



    /**
     * 修改身份证号码为前三后四，其余用"****"代替
     *
     * @param idCard
     * @return 返回替换后的身份证号
     */
    public static String changeIdCard(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return "";
        }
        return idCard.substring(0, 3) + "***********" + idCard.substring(idCard.length() - 4, idCard.length());
    }

    /**
     * 修改手机号码为前三后四，其余用"****"代替
     *
     * @param phone 手机号码
     * @return 返回修改后的手机号码
     */
    public static String changePhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
    }


    /**
     * 判断字符串是否存在该子串
     *
     * @param oldString 旧的字符串
     * @param subString 子串
     * @return 返回boolean
     */
    public static boolean indexOfSubString(String oldString, String subString) {
        if (TextUtils.isEmpty(oldString) || TextUtils.isEmpty(subString)) {
            return false;
        }
        return (oldString.indexOf(subString) != -1) ? true : false;
    }



    /**
     * 字节转字符串
     *
     * @param bytes 字节数组
     * @return 返回字符串
     */
    public static String byteToString(byte[] bytes) {
        String result = "";
        try {
            if (null != bytes && bytes.length > 0) {
                result = new String(bytes, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param oldString 旧的字符串
     * @return 返回去除后的字符串
     */
    public static String replaceBlank(String oldString) {
        String newString = "";
        if (oldString != null && oldString.length() > 0) {
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(oldString);
            newString = matcher.replaceAll("");
        }
        return newString;
    }

    /**
     * 删除制定字符
     *
     * @param editable Editable
     * @param ch       需要删除的字符
     */
    public static void deleteChar(Editable editable, char ch) {
        if (editable.length() > 0) {
            int pos = editable.length() - 1;
            char c = editable.charAt(pos);
            if (c == ch) {
                editable.delete(pos, pos + 1);
            }
        }
    }

    /**
     * 删除多个字符
     *
     * @param editable Editable
     * @param ch       字符数组
     */
    public static void deleteChar(Editable editable, char[] ch) {
        if (editable.length() > 0) {
            int pos = editable.length() - 1;
            char c = editable.charAt(pos);
            int len = ch.length;
            for (int i = 0; i < len; i++) {
                if (c == ch[i]) {
                    editable.delete(pos, pos + 1);
                }
            }
        }
    }

    /**
     * 删除字符串里面的子串
     *
     * @param oldString    老的字符串
     * @param deleteString 需要删除的字符串
     * @return 返回删除过后的字符串
     */
    public static String deleteChar(String oldString, String deleteString) {
        if (TextUtils.isEmpty(oldString) || TextUtils.isEmpty(deleteString)) {
            return oldString;
        }
        if (oldString.contains(deleteString)) {
            oldString = oldString.replaceAll(deleteString, " ");
            oldString = StringUtil.replaceBlank(oldString);
        }
        return oldString;
    }

    /**
     * 判断单个字符是不是中文
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(c);
        return null != unicodeBlock && (unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || unicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || unicodeBlock == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || unicodeBlock == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || unicodeBlock == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
    }

    /**
     * 判断字符串是不是中文
     *
     * @param name
     * @return
     */
    public static boolean isChinese(String name) {
        char[] ch = name.toCharArray();
        if (null != ch && ch.length > 0) {
            for (int i = 0, len = ch.length; i < len; i++) {
                char c = ch[i];
                if (isChinese(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断给定字符串是否是空白串；其中空白串是指由空格、制表符、回车符、换行符组成的字符串
     *
     * @param input 字符串
     * @return boolean 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmptyOrNull(String input) {
        boolean flag = false;
        if (input == null || "".equals(input) || "null".equalsIgnoreCase(input)) {
            flag = true;
        } else {
            int length = input.length();
            for (int i = 0; i < length; i++) {
                char c = input.charAt(i);
                if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }



    /**
     * 将double类型的数据转成 36125.0——》36,125.00的格式,固定保留两位小数 切割方式
     *
     * @param number 数据
     * @return 返回格式化结果
     */
    public static String formatNumToString(double number) {
        String tempStr = formatNumToChina(number);
        String[] tempSplits = tempStr.split("\\.");
        if (tempSplits != null && tempSplits.length > 0) {
            if (tempSplits.length < 2) {
                return tempStr + ".00";
            } else {
                String point = tempSplits[1];
                if (point.length() == 0) {
                    return tempSplits[0] + ".00";
                }
                if (point.length() == 1) {
                    return tempSplits[0] + "." + tempSplits[1] + "0";
                }
                if (point.length() > 2) {
                    return tempSplits[0] + "." + point.substring(0, 2);
                }
                return tempStr;
            }
        }
        return "";
    }

    /**
     * 将double类型的数据转成1,236.5格式，eg:36125.0->36,125
     *
     * @param number 数据
     * @return 返回格式化数据
     */
    public static String formatNumToChina(double number) {
        NumberFormat nf = NumberFormat.getInstance(Locale.CHINA);
        return nf.format(number);
    }

    /**
     * 格式化Double，保留2位小数点
     *
     * @param number
     * @return
     */
    public static String formatDouble(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }




    /**
     * 在数字型字符串千分位加逗号
     *
     * @param str
     * @return
     */
    public static String numDddComma(String str) {
        boolean neg = false;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        //处理负数
        if (str.startsWith("-")) {
            str = str.substring(1);
            neg = true;
        }
        String tail = null;
        //处理小数点
        if (str.indexOf('.') != -1) {
            tail = str.substring(str.indexOf('.'));
            str = str.substring(0, str.indexOf('.'));
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4) {
            sb.insert(i, ',');
        }
        sb.reverse();
        if (neg) {
            sb.insert(0, '-');
        }
        if (tail != null) {
            sb.append(tail);
        }
        return sb.toString();
    }

    /**
     * 数字每隔4位加空格
     */
    public static String addSpace(String checkCode) {
        if (TextUtils.isEmpty(checkCode)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(checkCode);
        for (int i = 4; i < sb.length(); i += 5) {
            sb.insert(i, ' ');
        }
        return sb.toString();
    }


    /**
     * 给TextView添加删除线
     *
     * @param content 内容
     * @return
     */
    public static SpannableString getSpannableString(String content) {
        SpannableString sp = null;
        if (content != null && !TextUtils.isEmpty(content)) {
            sp = new SpannableString(content);
            sp.setSpan(new StrikethroughSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }


    /**
     * 对指定文本进行颜色和大小的改变处理
     *
     * @param first       第一段文本
     * @param second      第二段文本
     * @param firstColor  第一段文本的颜色
     * @param secondColor 第二段文本的颜色
     * @param firstSize   第一段文本的大小 单位px
     * @param secondSize  第二段文本的大小 单位px
     * @return 处理过的String
     */
    public static SpannableString spanString(String first, String second, int firstColor, int secondColor, int firstSize, int secondSize, boolean firstIsBold, boolean secondIsBold) {
        SpannableString spannableString = null;
        if (!TextUtils.isEmpty(first)) {
            spannableString = new SpannableString(String.format("%s%s", first, second));
            int start = first.length();
            int end = spannableString.length();
            if (firstSize != -1) {
                AbsoluteSizeSpan mFirstSize = new AbsoluteSizeSpan(firstSize);
                spannableString.setSpan(mFirstSize, 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (firstColor != -1) {
                ForegroundColorSpan mFirstColor = new ForegroundColorSpan(firstColor);
                spannableString.setSpan(mFirstColor, 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (secondSize != -1) {
                AbsoluteSizeSpan mSecondSize = new AbsoluteSizeSpan(secondSize);
                spannableString.setSpan(mSecondSize, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (secondColor != -1) {
                ForegroundColorSpan mSecondColor = new ForegroundColorSpan(secondColor);
                spannableString.setSpan(mSecondColor, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (firstIsBold) {
                //加粗
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(span, 0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (secondIsBold) {
                //加粗
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        return spannableString;
    }

    /**
     * 对指定文本进行大小的改变处理
     *
     * @param first      第一段文本
     * @param second     第二段文本
     * @param firstSize  第一段文本的大小 单位px
     * @param secondSize 第二段文本的大小 单位px
     * @return 处理过的String
     */
    public static SpannableString spanStringSize(String first, String second, int firstSize, int secondSize, boolean isBold) {
        return spanString(first, second, -1, -1, firstSize, secondSize, false, false);
    }

    /**
     * 对指定文本进行颜色的改变处理
     *
     * @param first       第一段文本
     * @param second      第二段文本
     * @param firstColor  第一段文本的颜色
     * @param secondColor 第二段文本的颜色
     * @return 处理过的SpannableString
     */
    public static SpannableString spanString(String first, String second, int firstColor, int secondColor) {
        return spanString(first, second, firstColor, secondColor, -1, -1, false, false);
    }



    /**
     * 判断是否是正确的数字
     *
     * @param number 数字
     * @return 返回boolean
     */
    public static boolean isNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern pattern = Pattern.compile(num);
        return pattern.matcher(number).matches();
    }



    /**
     * 检查是否包含关键词
     *
     * @param content 关键词
     */
    public static boolean checkWords(Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            StringBuffer sb = new StringBuffer();
            InputStream is = null;
            try {
                Properties pro = new Properties();
                is = context.getAssets().open("words.properties");
                pro.load(is);
                Enumeration enu = pro.propertyNames();
                while (enu.hasMoreElements()) {
                    sb.append(new String(enu.nextElement().toString().getBytes("ISO-8859-1"), "GBK"));
                    sb.append(",");
                }
                if (!TextUtils.isEmpty(sb)) {
                    sb.deleteCharAt(sb.length() - 1);
                    for (String word : sb.toString().split(",")) {
                        if (content.contains(word)) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}