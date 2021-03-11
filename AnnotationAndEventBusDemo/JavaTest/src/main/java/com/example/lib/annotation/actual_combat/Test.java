package com.example.lib.annotation.actual_combat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 测试
 */
public class Test {

    public static void main(String[] args) {

        Filter f1 = new Filter();
        f1.setId(10);

        Filter f2 = new Filter();
        f2.setUserName("lucy");

        Filter f3 = new Filter();
        f3.setEmail("fei@sina.com,zh@163.com,1235@qq.com");


        Filter f4 = new Filter();
        f4.setEmail("fei@sina.com");

        Filter f5 = new Filter();
        f5.setUserName("Tom");
        f5.setAge(20);

        Filter f6 = new Filter();
        f6.setCity("北京");

        Filter f7 = new Filter();
        f7.setMobile("18072850706");

        String sq1 = query(f1);
        String sq2 = query(f2);
        String sq3 = query(f3);
        String sq4 = query(f4);
        String sq5 = query(f5);
        String sq6 = query(f6);
        String sq7 = query(f7);
        System.out.println(sq1);
        System.out.println(sq2);
        System.out.println(sq3);
        System.out.println(sq4);
        System.out.println(sq5);
        System.out.println(sq6);
        System.out.println(sq7);

    }

    public static String query(Filter filter) {

        StringBuilder sb = new StringBuilder();
        //1.获取到Class
        Class c = filter.getClass();
        //判断是否使用了类注解
        boolean exist = c.isAnnotationPresent(Table.class);
        if (!exist) {
            return null;
        }

        //获取到Table的名字
        Table table = (Table) c.getAnnotation(Table.class);
        String tbName = table.value(); // 获取表名
        sb.append("SELECT * FROM ").append(tbName).append(" WHERE 1=1 ");
        //遍历所有字段
        Field[] fArray = c.getDeclaredFields();
        for (Field field : fArray) {
            boolean fExists = field.isAnnotationPresent(Column.class);  //判断是否使用了字段注解
            if (!fExists) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);
            String columnName = column.value();
            // 字段的名称
            String fieldName = field.getName();
            System.out.println("fieldName = " + fieldName);
            //拿到字段的值
            Object fieldValue = null;

            // 获取到git方法的名称
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            System.out.println("getMethodName = " + getMethodName);
            try {
                Method getMethod = c.getMethod(getMethodName);  // 根据方法名称获取到方法
                fieldValue = getMethod.invoke(filter, null); // 通过方法名称获取到方法对应返回的值
            } catch (Exception e) {
                e.printStackTrace();
            }

            //拼装SQL

            if (fieldValue == null || (fieldValue instanceof Integer && (Integer) fieldValue == 0)) {
                continue;
            }

            if (fieldValue instanceof String) {

                if (((String) fieldValue).contains(",")) {
                    String[] values = ((String) fieldValue).split(",");
                    sb.append("in(");
                    for (String v : values) {
                        sb.append("\'").append(v).append("\'").append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(")");
                } else {
                    fieldValue = "\'" + fieldValue + "\'";
                    sb.append(" and ").append(fieldName).append(" = ").append(fieldValue);
                }

            }else{
                sb.append(" and ").append(fieldName).append(" = ").append(fieldValue);
            }

        }
        return sb.toString();
    }
}