package com.example.javahybridsample.util;



import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashMap;


/**
 * StringUtil String 이 Null 인지 아닌지 체크한다.
 */
@SuppressWarnings("unused, WeakerAccess")
public class StringUtil {
    private static Logger logger = LoggerFactory.getLogger(StringUtil.class.getName());

    /**
     * 문자를 null로 리턴되지 않도록 한다.
     */
    public static String notNullString(String str) {
        if (str == null || "null".equals(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 문자를 null로 리턴되지 않도록 한다.
     */
    public static String notNullString(Object str) {
        if (str == null) {
            return "";
        } else {
            return str.toString();
        }
    }

    public static Object notNullObject(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj;
        }
    }

    /**
     * 문자를 null로 리턴하지 않는다.
     */
    public static String notNullString(Object obj, String defString) {
        String retString = notNullString(obj);
        if (retString.equals("")) {
            return defString;
        } else {
            return retString;
        }
    }

    /**
     * 문자를 null로 리턴하지 않는다.
     */
    public static String notNullString(String str, String defString) {
        String retString = notNullString(str);
        if (retString.equals("")) {
            return defString;
        } else {
            return retString;
        }
    }

    /**
     * 문자열이 널인지를 판단한다.
     */
    public static boolean isEmptyString(String str) {
        return notNullString(str).equals("") || notNullString(str).equals("null") || str.trim().length() == 0;
    }

    public static boolean isEmptyString(CharSequence str) {
        return isEmptyString(str.toString());
    }

    public static boolean isEmptyObject(Object obj) {
        return notNullObject(obj).equals("");
    }

    public static String repeat(String s, int times) {
        if (times <= 0)
            return "";
        else if (times % 2 == 0)
            return repeat(s + s, times / 2);
        else
            return s + repeat(s + s, times / 2);
    }

    public static String delChr(String str, char delChr) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) != delChr) {
                buffer.append(str.charAt(i));
            }
        }
        return buffer.toString();
    }

    public static String moneyForm(String data) {
        if (isEmptyString(data))
            return "0";
        try {
            String prefix = "";
            if (data.startsWith("-")) {
                prefix = "-";
            }
            return prefix + moneyForm(Long.parseLong(data.replaceAll("[^0-9]", "")));
        } catch (NumberFormatException e) {
            logger.error("", e);
        }
        return "0";
    }

    public static String moneyAddMinusForm(String data) {
        if (isEmptyString(data))
            return "0";
        try {
            return moneyForm(Long.parseLong(data.replaceAll("[^0-9\\-]", "")));
        } catch (NumberFormatException e) {
            logger.error("", e);
        }
        return "0";
    }

    public static String moneyForm(long data) {
        DecimalFormat format = new DecimalFormat(",###");
        return format.format(data);
    }

    public static String getOnlyDigit(String data) {
        if (isEmptyString(data)) {
            return "0";
        }
        return data.replaceAll("[^0-9]", "");
    }

    public static String moneyFormAddUnit(String data) {
        if (isEmptyString(data))
            return "0";
        try {
            long money = Long.parseLong(data.replaceAll("[^0-9]", ""));
            String unit = "";
            if (money >= 10000L && money < 100000000L) {
                money = money/10000L;
                unit = "만";
            } else if (money >= 100000000L){
                money = money/100000000L;
                unit = "억";
            }
            return moneyForm(money) + unit;

        } catch (NumberFormatException e) {
            logger.error("", e);
        }
        return "0";
    }

    public static String moneyToHangul(String money) {
        if (isEmpty(money)) {
            return "";
        }

        String[] han1 = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
        String[] han2 = {"", "십", "백", "천"};
        String[] han3 = {"", "만", "억", "조", "경"};
        String changeText = String.valueOf(Long.parseLong(getOnlyDigit(money)));

        StringBuilder builder = new StringBuilder();

        int len = changeText.length();
        for (int i = len - 1; i >= 0; i--) {
            int beginIndex = len - i - 1;
            int endIndex = len - i;
            builder.append(han1[Integer.parseInt(changeText.substring(beginIndex, endIndex))]);
            if (Integer.parseInt(changeText.substring(beginIndex, endIndex)) > 0) {
                builder.append(han2[i % 4]);
            }
            if (i % 4 == 0) {
                builder.append(han3[i / 4]);
            }
        }

        if (isEmpty(builder.toString())) {
            return "";
        }
        return builder.toString() + "원";
    }

    public static String accountForm(@NonNull String bankCode, @NonNull String data) {
        String str = data.replaceAll("[^0-9]", "");
        if (str.length() < 6) {
            return data;
        }

        if (str.length() == 10) {
            return String.format("%s-%s-", str.substring(0,2), str.substring(2,4)) + str.substring(4);
        } else if (str.length() == 11) {
            return String.format("%s-%s-", str.substring(0,3), str.substring(3,6)) + str.substring(6);
        } else if (str.length() == 12) {
            return String.format("%s-%s-", str.substring(0,3), str.substring(3,6)) + str.substring(6);
        } else if (str.length() == 13) {
            return String.format("%s-%s-", str.substring(0,5), str.substring(5,7)) + str.substring(7);
        } else if (str.length() == 14) {
            return String.format("%s-%s-", str.substring(0,5), str.substring(5,7)) + str.substring(7);
        } else if (str.length() == 16) {
            return String.format("%s-%s-%s-", str.substring(0,5), str.substring(5,12), str.substring(12,14)) + str.substring(14);
        } else {
            return str;
        }

    }

    public static String phoneNumberForm(@NonNull String data) {
        String str = data.replaceAll("[^0-9]", "");
        if (str.length() == 11) {
            return String.format("%s-%s-", str.substring(0,3), str.substring(3,7)) + str.substring(7);
        } else {
            return String.format("%s-%s-", str.substring(0,3), str.substring(3,6)) + str.substring(6);
        }

    }

    /**
     * replaceLast
     *
     * @param text        String
     * @param regex       찾을 문자
     * @param replacement 바꿀 문자
     */
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * 지정한 자릿수 이상 되면 짜르고 ... 넣기
     */
    public static String tailStr(String str, int length) {

        String result = str;
        if (str.length() > length) {

            result = str.substring(0, length);
            result += "...";
        }
        return result;
    }


    public static String getString(HashMap<String, String> item, String key, String defaultValue) {
        if (item == null)
            return defaultValue;
        if (StringUtil.isEmptyString(item.get(key))) {
            return defaultValue;
        }
        return item.get(key);
    }

    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            return ((String) obj).trim().isEmpty() || "null".equals(obj);
        } else {
            return obj == null;
        }
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static String getString(String value){
        return getString(value, "");
    }

    public static String getString(String value, String defaultValue) {
        if(isEmpty(value)){
            return defaultValue;
        }else{
            return value;
        }
    }

    public static String getStringUrl(String str) {
        int strLen = str.indexOf("?");
        if (strLen > 0) {
            str = str.substring(0, strLen);
        }
        return str;
    }

    public static String getStringJsonObject(JSONObject jsonObject) {
        return (""+jsonObject).replace("\"[", "[")
                .replace("]\"", "]").replace("\\", "");
    }

    public static String getStringJsonArray(JSONArray jsonArray) {
        return (""+jsonArray).replace("[]", "");
    }

    public static String convertString(String str) {
        str = str.replace(" ", "").replace("\\p{Z}", "");
        return str;
    }

    /**
     * lpad
     * @param str 문자
     * @param len 자릿수
     * @param addStr 왼쪽에 추가할 문자
     * @return String
     */
    public static String lpad(String str, int len, String addStr) {
        StringBuilder result = new StringBuilder();
        int tempLen = len - str.length();

        for (int i = 0; i < tempLen; i++) {
            result.append(addStr);
        }
        result.append(str);
        return result.toString();
    }


    /**
     * barcode 형식 - 4자리마다
     */
    public static String barcodeForm(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return String.format("%s-%s-%s-%s-%s-%s", str.substring(0,4), str.substring(4,8)
                , str.substring(8,12), str.substring(12,16), str.substring(16,20), str.substring(20));
    }

    /**
     * Y/N 또는 1/2 체크
     * @return Y/1 true
     */
    public static boolean checkYn(String str) {
        return "Y".equals(str.toUpperCase()) || "1".equals(str);
    }

    /**
     * 특수문자 체크
     */
    public static boolean hasSpecialChar(String str) {
        return !str.matches("[0-9|a-z|A-Z]*");
    }
}
