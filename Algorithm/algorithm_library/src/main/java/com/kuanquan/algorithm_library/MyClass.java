package com.kuanquan.algorithm_library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyClass {

    public static void main(String[] args) {
//        mergeDeDuplication();
//        selectedSort();
        List<Integer> lists = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            lists.add(i);
        }

        for (int i = 0; i < lists.size(); i++) {
            System.out.println(i%4);
        }
    }

    // 选择排序
    private static void selectedSort() {
        int[] array = new int[]{9, 4, 6, 2, 7, 8, 1, 3, 5};

        // 剖析：最外层的遍历是因为要从数组中取出一个元素和其他的元素进行比较，
        // 这里数组的长度减1是因为，取到到处第二个数据比较完后，其实已经排好序列了，所以减1是为了减少遍历

        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                minIndex = array[j] > array[minIndex] ? minIndex : j;
            }

            int temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }

        for (int i : array) {
            System.out.println(i);
        }
    }

    // 快速获取一个字符串中重复的字符
    private static void getDuplicationStr() {
        String str = "abadchfgdba";
        HashMap<Object, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (hashMap.get(charAt) != null) {
                Integer integer = hashMap.get(charAt);
                hashMap.put(charAt, ++integer);
            } else {
                hashMap.put(charAt, 1);
            }
        }

        Set<Map.Entry<Object, Integer>> entries = hashMap.entrySet();
        for (Map.Entry<Object, Integer> entry : entries) {
            System.out.println("key" + entry.getKey());
            System.out.println("value" + entry.getValue());
        }
    }

    // 两个集合和并去重
    // 将两个List添加到一个Set中，因为Set的内容不可重复，所以会自动去重，然后再由Set转为List
    // 温馨提示：如果要合并的是对象请注意重写equals和hashcode方法
    private static void mergeDeDuplication() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("2");
        list1.add("3");
        list1.add("4");
        list1.add("5");

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("5");
        list2.add("6");
        list2.add("7");
        list2.add("4");
        list2.add("8");
        list2.add("9");

        Set<String> set = new HashSet<>(list1);
        set.addAll(list2);

        List<String> list = new ArrayList<>(set);
//        System.out.println(list);

        // java8 以后的写法
        List<String> collect = Stream.of(list1, list2)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(collect);
    }
}