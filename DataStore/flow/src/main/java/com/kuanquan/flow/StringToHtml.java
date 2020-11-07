package com.kuanquan.flow;

import java.io.*;
import java.util.*;

public class StringToHtml {

    public static void main(String[] args) {

        HashMap<String, String> hashMap = dependentMap("/Users/fei/Downloads/searchDocument/bixin/class-map.txt");

        try {

            // 生成 html 文件的文件夹路径
            String path = "/Users/fei/Downloads/html";
            File outFile = new File(path, "scan.html");
            outFile.getParentFile().mkdirs();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            String[] array = new String[]{
                    "/Users/fei/Downloads/searchDocument/bixin/installed.txt",
                    "/Users/fei/Downloads/searchDocument/bixin/imei.txt",
                    "/Users/fei/Downloads/searchDocument/bixin/mac.txt",
                    "/Users/fei/Downloads/searchDocument/bixin/location.txt",
                    "/Users/fei/Downloads/searchDocument/bixin/sms.txt",
                    "/Users/fei/Downloads/searchDocument/bixin/calls.txt",
                    "/Users/fei/Downloads/searchDocument/bixin/contract.txt"
            };

            for (String str : array) {
                string2Html(str, bw, hashMap);
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void string2Html(String textFile, BufferedWriter bw, HashMap<String, String> hashMap) {
        List<String> classNames = new ArrayList<>();
        File file = new File(textFile);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String[] strs = sb.toString().split("\\.//");

            bw.write("<br/>");
            bw.write("<h2>");
            bw.write(getTitle(textFile));
            bw.write("</h2>");

            bw.write(searchCommand(textFile));

            bw.write("<table border=\"1\">");

            bw.write("<tr>");

            bw.write("<td width=\"400px\">");
            bw.write("<b>");
            bw.write("依赖");
            bw.write("</b>");
            bw.write("</td>");

            bw.write("<td width=\"800px\">");
            bw.write("<b>");
            bw.write("代码位置");
            bw.write("</b>");
            bw.write("</td>");

            bw.write("</tr>");

            for (String str : strs) {
                if (str.isEmpty()) {
                    continue;
                }

                int fromIndex = str.indexOf("/com/");
                int toIndex = str.indexOf(".smali:");

                if (fromIndex != -1 && toIndex != -1) {
                    String substring = str.substring(fromIndex, toIndex).substring(1);

                    String className = substring.replace("/", ".");

                    if (textFile.contains("location.txt")) {
                        boolean isContinue = className.contains("com.loc.") || className.contains("com.amap.") || className.contains("com.autonavi.amap.mapcore.");
                        if (isContinue) {
                            continue;
                        }
                    }

                    if (classNames.contains(className)) {
                        continue;
                    }
                    classNames.add(className);

                    bw.write("<tr>");

                    bw.write("<td>");
                    if (hashMap.get(className) != null) {
                        bw.write(hashMap.get(className));
                    }

                    bw.write("</td>");

                    bw.write("<td>");
                    bw.write(className);
                    bw.write("</td>");

                    bw.write("</tr>");
                }
            }
            bw.write("</table>");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 查找类所在的依赖包
    public static HashMap<String, String> dependentMap(String textFile) {
        HashMap<String, String> hashMap = new HashMap<>();

        File file = new File(textFile);
        try {
            if (file.isFile() && file.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

                List<String> lists = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    lists.add(line);
                }
                for (String str : lists) {
                    if (str.isEmpty()) {
                        continue;
                    }
                    String[] split = str.split("->");
                    if (split.length >= 2) {
                        hashMap.put(split[0].trim(), split[1].trim());
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return hashMap;
    }

    public static String getTitle(String fileName){

        if (fileName.contains("installed.txt")) {
            return "获取用户的安装列表";
        } else if (fileName.contains("imei.txt")) {
            return "IMEI";
        } else if (fileName.contains("mac.txt")) {
            return "mac地址";
        } else if (fileName.contains("location.txt")) {
            return "定位信息";
        } else if (fileName.contains("sms.txt")) {
            return "短信";
        } else if (fileName.contains("calls.txt")) {
            return "通讯记录";
        } else if (fileName.contains("contract.txt")) {
            return "通讯录";
        } else {
            return "";
        }
    }

    public static String searchCommand(String fileName){

        if (fileName.contains("installed.txt")) {
            return "搜索条件：grep -rn -E --color=auto 'getInstalledPackages|getInstalledApplications' ./ --include='*.smali'";
        } else if (fileName.contains("imei.txt")) {
            return "搜索条件：grep -rn --color=auto getImei ./ --include='*.smali'";
        } else if (fileName.contains("mac.txt")) {
            return "搜索条件：grep -rn --color=auto getMac ./ --include='*.smali'";
        } else if (fileName.contains("location.txt")) {
            return "搜索条件：grep -rn -E --color=auto \"getLastKnownLocation|LocationListener\" ./ --include='*.smali'";
        } else if (fileName.contains("sms.txt")) {
            return "搜索条件：grep -rn --color=auto 'Telephony$Sms;->CONTENT_URI' ./ --include='*.smali'";
        } else if (fileName.contains("calls.txt")) {
            return "搜索条件：grep -rn --color=auto 'CallLog$Calls;->CONTENT_URI' ./ --include='*.smali'";
        } else if (fileName.contains("contract.txt")) {
            return "搜索条件：grep -rn --color=auto 'ContactsContract$Contacts;->CONTENT_URI' ./ --include='*.smali'";
        } else {
            return "";
        }
    }
}
