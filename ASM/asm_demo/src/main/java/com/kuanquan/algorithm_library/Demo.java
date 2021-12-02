package com.kuanquan.algorithm_library;

public class Demo {

    public static void main(String[] args) {
//        try {
//            ClassReader cr = new ClassReader(Hello.class.getName());
//            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
//            ClassVisitor cv = new TestClassVisitor(cw);
//            cr.accept(cv, Opcodes.ASM5);
//            // 获取生成的class文件对应的二进制流
//            byte[] code = cw.toByteArray();
//            //将二进制流写到Hello.class所在的目录下
//            FileOutputStream fos = new FileOutputStream("./algorithm_library/build/classes/java/main/com/kuanquan/algorithm_library/Hello.class");
//            fos.write(code);
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("异常" + e.getMessage());
//        }
        System.out.println(result(3));
    }

   private static int result(int a){
        try {
            return a;
        }finally{
            a = a / 0;
        }
    }

}
