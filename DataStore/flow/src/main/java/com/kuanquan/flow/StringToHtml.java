package com.kuanquan.flow;

import java.io.*;
import java.util.*;

public class StringToHtml {

    public static void main(String[] args) {

        String customStr = null;
        String hasDefault = "0";
        String projectName = "";
        String[] split = null;
        if (args.length >= 3) {
            System.out.println("是否加载默认数据=" + args[0]);
            projectName = args[0];
            hasDefault = args[1];
            customStr = args[2];
            System.out.println("加载自定义数据=" + args[1]);
        } else if (args.length >= 2) {
            projectName = args[0];
            hasDefault = args[1];
        } else if (args.length >= 1) {
            projectName = args[0];
        }

        String localPath = "/Users/fei/Downloads/searchDocument/test/";
//        String localPath = "";

        HashMap<String, String> hashMap = dependentMap(localPath + "class-map.txt");
//        HashMap<String, String> hashMap = dependentMap(projectName + "/class-map.txt");

        try {

            if (customStr != null && !"".equals(customStr)) {
                split = customStr.split(",");
            }


            // 生成 html 文件的文件夹路径
            String path = "/Users/fei/Downloads/html";
            File outFile = new File(path,"scan.html");
//            File outFile = new File("scan.html");
            outFile.getParentFile().mkdirs();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            bw.write("<br/>");
            bw.write("<h1 align=\"center\" width=\"50%\">");
            bw.write(projectName);
            bw.write("</h1>");

            ArrayList<String> lists = new ArrayList<>();
            if ("0".equals(hasDefault)) {
                lists.add(localPath + "installed.txt");
                lists.add(localPath + "imei.txt");
                lists.add(localPath + "mac.txt");
                lists.add(localPath + "location.txt");
                lists.add(localPath + "sms.txt");
                lists.add(localPath + "calls.txt");
                lists.add(localPath + "contract.txt");
            }

            if (split != null && split.length > 0) {
                for (String str: split) {
                    if (!"".equals(str.trim())) {
                        lists.add(str+".txt");
                    }
                }
            }

            for (String str : lists) {
                string2Html(str, bw, hashMap, hasDefault);
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void string2Html(String textFile, BufferedWriter bw, HashMap<String, String> hashMap, String hasDefault) {
        List<String> classNames = new ArrayList<>();
        File file = new File(textFile);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

//            StringBuilder sb = new StringBuilder();
            List<String> rowLists = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("line的大小 = "+ line.length());
//                sb.append(line);
                rowLists.add(line);
            }
//            String sbStr = sb.toString();
//            String[] strs = sbStr.split("\\.//");
//            sb.trimToSize();
            String title;
            String subtitle;
            String replace = textFile.replace(".txt", "");
            System.out.println("replace" + textFile);
            if ("0".equals(hasDefault)) {
                System.out.println("getTitle(replace)" + getTitle(textFile));
                if ("".equals(getTitle(textFile))) {
                    title = replace;
                } else {
                    title = getTitle(textFile);
                }

                System.out.println("searchCommand(replace)" + searchCommand(textFile));
                if ("".equals(searchCommand(textFile))) {
                    subtitle = "搜索条件：grep -rn --color=auto " + replace + " ./ --include='*.smali'";
                } else {
                    subtitle = searchCommand(textFile);
                }
            } else {
                title = replace;
                subtitle = "搜索条件：grep -rn --color=auto " + replace + " ./ --include='*.smali'";
            }

            bw.write("<br/>");
            bw.write("<h2>");
            bw.write(title);
            bw.write("</h2>");

            bw.write(subtitle);

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

            for (String str : rowLists) {
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
            rowLists.clear();
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
            return "搜索条件：grep -rn -E --color=auto 'getLastKnownLocation|LocationListener' ./ --include='*.smali'";
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
