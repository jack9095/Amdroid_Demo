package com.kuanquan.annotationandeventbusdemo.event;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ViewUtil {

    // onClick
    public static void injectEvent(Activity activity){
        Class<? extends Activity> aClass = activity.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method: declaredMethods) {
            boolean annotationPresent = method.isAnnotationPresent(OnClick.class);
            if (annotationPresent) {
                OnClick annotation = method.getAnnotation(OnClick.class);

                // 获取 View 的 id
                int[] value = annotation.value();

                // 获取 EventType 注解
                EventType eventType = annotation.annotationType().getAnnotation(EventType.class);

                Class listenerType = eventType.listenerType();
                String listenerSetter = eventType.listenerSetter();
                String methodName = eventType.methodName();

                // 动态代理 动态代理最后执行的还是被代理的方法
                ProxyHandler proxyHandler = new ProxyHandler(activity);
                Object obj = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, proxyHandler);
                proxyHandler.mapMethod(methodName,method);

                try {
                    Method findViewById = aClass.getMethod("findViewById", int.class);
                    findViewById.setAccessible(true);
                    // 遍历 View id 的数组
                    for (int in: value) {
                        // 获取到View
                        View view = (View) findViewById.invoke(activity, in);

                        Method listenerSetMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        listenerSetMethod.setAccessible(true);

                        // invoke 方法，参数1 调用 listenerSetMethod 方法的对象，参数2 调用listenerSetMethod这个方法的传参
                        listenerSetMethod.invoke(view,obj);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // findViewById（）
    public static void inJectView(Activity activity){
        if (null == activity) return;

        Class<? extends Activity> activityClass = activity.getClass();

        Field[] declaredFields = activityClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(InjectView.class)) {
                //找到InjectView注解的field
                InjectView annotation = declaredField.getAnnotation(InjectView.class);
                // 找到View 的 ID
                int id = annotation.value();

                try {
                    //找到findViewById方法
                    Method findViewByIdMethod = activityClass.getMethod("findViewById", int.class);
                    findViewByIdMethod.setAccessible(true);

                    // https://blog.csdn.net/vikeyyyy/article/details/79083097
                    // https://blog.csdn.net/wenyuan65/article/details/81145900
                    findViewByIdMethod.invoke(activity, id);

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
