package com.yj.bj.util;


import java.util.*;

/**
 * Created by bin on 2017/12/12.
 */
public class SignUtil {
    public static void main(String[] args) {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("institutionId","T00000003");
        hashMap.put("merchantId","M43689789546431692810012");
        hashMap.put("agentId","T00000003");
        hashMap.put("appId","0000");
        hashMap.put("orderNo","P440903180771917825");
        hashMap.put("payType","2");
        hashMap.put("name","宾剑锋");
        hashMap.put("phone","15210339424");
        hashMap.put("planId","10");
        hashMap.put("trade_state","SUCCESS");
        hashMap.put("total_fee","17940");
        String mySign= SignUtil.createYJSign(hashMap);
        System.out.println("97a271f335bfa134f132630a905385f7");
        System.out.println(mySign);
    }
    public static String createYJSign(HashMap<String,Object> hashMap){
        return createYJSign(hashMap, "SGNB");
    }
    public static String createYJSign(HashMap<String,Object> hashMap,String key){
        return MD5Util.getMD5String(convert(hashMap)+"&key="+key);
    }
    public static String convert(HashMap<String, Object> hashMap) {
        if (!hashMap.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            ArrayList<String> rest = new ArrayList<String>();
            Iterator<Map.Entry<String, Object>> iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                rest.add(key);
            }
            Object[] array = rest.toArray();
            Arrays.sort(array, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
            for (int i = 0; i < array.length; i++) {
                builder.append(array[i] + "=" + hashMap.get(array[i]));
                //builder.append(hashMap.get(array[i]));
                if (i != array.length - 1) {
                    builder.append("&");
                }
            }
            return builder.toString();
        } else {
            return null;
        }
    }
}
