package com.kuanquan.algorithm_library;

import com.kuanquan.algorithm_library.classVisitor.TestClassVisitor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

public class Demo {

    public static void main(String[] args) {
        try {
            ClassReader cr = new ClassReader(Hello.class.getName());
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = new TestClassVisitor(cw);
            cr.accept(cv, Opcodes.ASM5);
            // 获取生成的class文件对应的二进制流
            byte[] code = cw.toByteArray();
            //将二进制流写到Hello.class所在的目录下
            FileOutputStream fos = new FileOutputStream("./algorithm_library/build/classes/java/main/com/kuanquan/algorithm_library/Hello.class");
            fos.write(code);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常" + e.getMessage());
        }
    }

}
