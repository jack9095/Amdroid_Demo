package com.example.lib;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sf {

    public static void main(String[] args) {

        int[] array = {1,3,7,2,7,5,9};

        // 冒泡排序
        for (int i = 0; i < array.length; i++) {

            // 冒泡排序，每循环一次就会把最大的数据放到结尾，所以可以
            //
            for (int j = 0; j < array.length; j++) {

            }
        }

//        HashMap<String,Integer> map = new HashMap<>();
//        for(int i=0;i < array.length;i++){
//            Integer value = map.get("" + array[i]);
//            if (value != null) {
//                map.put("" +array[i], ++value);
//            } else {
//                map.put("" +array[i],0);
//            }
//        }
//
//        Set<Map.Entry<String, Integer>> entries = map.entrySet();
//        for (Map.Entry<String, Integer> entry : entries) {
//            if (entry.getValue() >= 1) {
//                System.out.println(entry.getKey());
//            }
//        }
    }

    private static boolean isMumber(String str) {
// Pattern pattern = Pattern.compile("^-?[0-9]+"); //这个也行
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$"); //这个也行
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
