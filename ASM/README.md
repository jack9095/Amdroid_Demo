# 动态代理的核心技术就是 ASM, ASM是一个非常底层的字节码操作框架，它可以创建一个全新的Java class文件，
也可以修改现有的 class 文件对象，是 Java 开发中非常核心的技术点。Spring, MyBatis等现在 Java 中的框架底层
都是用动态代理实现的核心功能，而动态代理的核心技术点就是 ASM，所以说没有 ASM 就没有这些框架。

