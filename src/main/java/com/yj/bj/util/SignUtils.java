package com.yj.bj.util;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Stony Created Date : 2016/4/21  11:49
 */
public abstract class SignUtils {

    public static String sign(String signBefore, String merchantSescrt) throws Exception {
        return MD5Utils.md5UpperCase(signBefore, merchantSescrt);
    }



    public static String signBefore(Map<String, Object> body) {
        StringBuffer sign = new StringBuffer();
        Set<String> keySet = new TreeSet<String>();
        for (String key : body.keySet()) {
            keySet.add(key);
        }
        for (String key : keySet) {
            if (!"sign".equals(key)&&body.get(key)!=null) {
                sign.append("&" + key + "=" + body.get(key));
            }
        }
        if (sign != null && !("".equals(sign))) {
            sign.delete(0, 1);
        }
        return sign.toString();
    }
}