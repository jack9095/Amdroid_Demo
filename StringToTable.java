/*
     * txt文档转html
       filePath:txt原文件路径
       htmlPosition:转化后生成的html路径
public static void txtToHtml(String filePath, String htmlPosition) {
        try {
            //String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
                // 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                // 写文件
                FileOutputStream fos = new FileOutputStream(new File(htmlPosition,System.currentTimeMillis()+".html"));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                BufferedWriter bw = new BufferedWriter(osw);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    bw.write("&nbsp&nbsp&nbsp"+lineTxt + "</br>");
                }
                bw.close();
                osw.close();
                fos.close();
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        }
 */
package com.kuanquan.flow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StringToTable {

    public static void main(String[] args) {

        HashMap<String, String> hashMap = dependentMap("/Users/fei/Downloads/searchDocument/bixin/class-map.txt");

        try {

            // 生成 html 文件的文件夹路径
            String path = "/Users/fei/Downloads/html";
            // File outFile = new File("/Users/fei/Downloads/html", "test.html");
            File outFile = new File("scan.html");
            // outFile.getParentFile().mkdirs();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            string2Html("/Users/fei/Downloads/searchDocument/bixin/installed.txt", "/Users/fei/Downloads/html", "获取用户的安装列表", bw, hashMap);
            string2Html("/Users/fei/Downloads/searchDocument/bixin/imei.txt", "/Users/fei/Downloads/html", "IMEI", bw, hashMap);
            string2Html("/Users/fei/Downloads/searchDocument/bixin/mac.txt", "/Users/fei/Downloads/html", "mac地址", bw, hashMap);
            string2Html("/Users/fei/Downloads/searchDocument/bixin/location.txt", "/Users/fei/Downloads/html", "定位信息", bw, hashMap);
            string2Html("/Users/fei/Downloads/searchDocument/bixin/sms.txt", "/Users/fei/Downloads/html", "短信", bw, hashMap);
            string2Html("/Users/fei/Downloads/searchDocument/bixin/calls.txt", "/Users/fei/Downloads/html", "通讯记录", bw, hashMap);
            string2Html("/Users/fei/Downloads/searchDocument/bixin/contract.txt", "/Users/fei/Downloads/html", "通讯录", bw, hashMap);

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void string2Html(String textFile, String path, String title, BufferedWriter bw, HashMap<String, String> hashMap) {
        List<String> classNames = new ArrayList<>();
        File file = new File(textFile);
        try {
//            File outFile = new File(path, System.currentTimeMillis() + ".html");
//            File outFile = new File(path,  "test.html");

//            outFile.getParentFile().mkdirs();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String[] strs = sb.toString().split("\\.//");

            bw.write("<br/>");
            bw.write("<h2>");
            bw.write(title);
            bw.write("</h2>");

            bw.write("<table border=\"1\">");

            bw.write("<tr>");

//            bw.write("<td width=\"100px\">");
//            bw.write("<b>");
//            bw.write("SDK");
//            bw.write("</b>");
//            bw.write("</td>");

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

//                    int packageIndex = className.lastIndexOf(".");

//                    String packageName = className.substring(0, packageIndex);

                    bw.write("<tr>");

//                    bw.write("<td>");
//                    bw.write("腾讯  ");
//                    bw.write("</td>");

                    bw.write("<td>");
                    System.out.println("key = " + className);
//                    System.out.println("hashMap = "+ hashMap);
                    System.out.println("依赖库 = "+ hashMap.get(className.trim()));
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
//            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 查找类所在的依赖包
    public static HashMap<String, String> dependentMap(String textFile) {
        HashMap<String, String> hashMap = new HashMap<>();

        File file = new File(textFile);
        try {
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

                List<String> lists = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    lists.add(line);
                }
                System.out.println("集合大小 = " + lists.size());
                for (String str : lists) {
                    if (str.isEmpty()) {
                        continue;
                    }
                    String[] split = str.split("->");
                    System.out.println("数组长度 = " + split.length);
                    if (split.length >= 2) {
                        System.out.println("key = " + split[0].trim());
                        System.out.println("value = " + split[1].trim());
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
}
