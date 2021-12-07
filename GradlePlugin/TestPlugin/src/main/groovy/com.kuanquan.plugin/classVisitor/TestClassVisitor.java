package com.kuanquan.plugin.classVisitor;

import com.kuanquan.algorithm_library.methodVisitor.TestMethodVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 解析类的监听器,解析Class字节码时会触发内部的方法
 */
public class TestClassVisitor extends ClassVisitor {

    public TestClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        if (cv != null) {
            cv.visit(version, access, name, signature, superName, interfaces);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        //如果methodName是show，则返回我们自定义的TestMethodVisitor
        if ("show".equals(name)) {
            MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
            return new TestMethodVisitor(mv);
        }
        if (cv != null) {
            return cv.visitMethod(access, name, descriptor, signature, exceptions);
        }
        return null;
//        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
