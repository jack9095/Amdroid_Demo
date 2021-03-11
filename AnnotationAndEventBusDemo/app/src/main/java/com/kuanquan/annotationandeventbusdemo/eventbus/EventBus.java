package com.kuanquan.annotationandeventbusdemo.eventbus;

import android.os.Handler;
import android.os.Looper;

import com.kuanquan.annotationandeventbusdemo.eventbus.annotation.Subscribe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 自己写一个 EventBus 的思路
 * 1.创建一个全局单例
 * 2.对外提供订阅方法，注销方法
 * 3.把订阅者内部的订阅方法全部存储起来，方法要单独封装成个对象，里面涉及方法参数，线程模型等等，便于发送消息后遍历订阅者集合找到对应的订阅者
 * 4.通过反射执行方法 invoke
 * <p>
 * ConcurrentHashMap 的数据结构，底层采用分段的数组+链表实现，线程安全
 * 通过把整个Map分为N个 Segment，可以提供相同的线程安全，但是效率提升N倍，默认提升16倍。
 * (读操作不加锁，由于HashEntry的value变量是 volatile的，也能保证读取到最新的值。直接到主内存中拿数据)
 */
class EventBus {

    private Handler handler;
    // key 是订阅的组件（activity、fragment、service），value 是组件中的订阅方法
    private ConcurrentHashMap<Object, List<MethodManager>> concurrentHashMap;
    // 创建线程池
    private ExecutorService executorService;
    private volatile Object subscribe; // 订阅者对象

    private EventBus() {
//        copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        stickConcurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap = new ConcurrentHashMap<>();
        // 设置主线程的 Looper
        handler = new Handler(Looper.getMainLooper());

        // 设置线程池的核心数量根据 CPU 的数量来设置的 四核 八核 等等
        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
        // 创建出线程池
        this.executorService = Executors.newFixedThreadPool(CORE_POOL_SIZE);
    }

    private static class InnerInstance {
        final static EventBus INSTANCE = new EventBus();
    }

    public static EventBus getInstance() {
        return InnerInstance.INSTANCE;
    }

    // 订阅的同时，通过反射把订阅者对象中的订阅方法全部存储起来
    public void register(Object subscriber) {
        this.subscribe = subscriber;
        Class nameKey = subscriber.getClass();
        Method[] methods = nameKey.getMethods();

        List<MethodManager> lists = concurrentHashMap.get(subscriber);
        if (lists == null) {
            lists = new ArrayList<>();
        }
        MethodManager methodManager;
        for (Method method : methods) {
            boolean annotationPresent = method.isAnnotationPresent(Subscribe.class);
            if (annotationPresent) {
                Subscribe annotation = method.getAnnotation(Subscribe.class);
                ThreadMode threadMode = annotation.value();
                boolean isStick = annotation.stick();
                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes != null && method.getParameterTypes().length > 0) {
                    methodManager = new MethodManager(parameterTypes[0], method, threadMode, isStick);
                    // 处理粘性事件的消息发送
                    if (isStick) {
                        // 粘性事件的参数对象
                        Set<Object> objects = stickConcurrentHashMap.keySet();
                        for (Object parameter : objects) {
                            dataParser(methodManager,subscriber,parameter);
                        }
                    }
                    lists.add(methodManager);
                    // TODO concurrentHashMap 使用 put() 方法的时候要使用锁
                    concurrentHashMap.put(subscriber, lists);
                }
            }
        }
    }

    // 在注销的时候把需要注销的对象从map中移除就好了
    public void unregister(Object subscriber) {
        String nameKey = subscriber.getClass().getName();
        // TODO concurrentHashMap 使用 remove() 方法的时候要使用锁
        concurrentHashMap.remove(nameKey);
    }

    // 发送消息
    public void post(Object parameter) {
        // 获取到订阅方法的集合
        Set<Object> keys = concurrentHashMap.keySet();
        for (final Object key : keys) {

            // 不是当前页面注册的对象，其实这一步不用判断
            if (!key.equals(subscribe)) {
                // TODO concurrentHashMap 使用 get() 方法的时候没有使用锁
                List<MethodManager> methodManagers = concurrentHashMap.get(key); // 获取到被注解标记的方法清单集合
                if (methodManagers != null && methodManagers.size() > 0) {
                    // 遍历所有订阅的方法
                    for (final MethodManager methodManager : methodManagers) {
                        dataParser(methodManager, key, parameter);
                    }
                }
            }
        }
    }

    /**
     * 通过反射去执行方法
     *
     * @param methodManager 封装的方法对象
     * @param object        组件（Activity、Service等）对象
     * @param parameter           订阅方法的参数对象
     */
    private void dataParser(final MethodManager methodManager, final Object object, final Object parameter) {

        // 通过 isAssignableFrom 方法可以判断参数类型是否相同
        if (methodManager.getType().isAssignableFrom(parameter.getClass())) {
            // 在这里进行线程切换
            if (methodManager.getThreadMode() == ThreadMode.MAIN) {
                // 判断当前线程是否是主线程
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    invoke(methodManager.getMethod(), object, parameter);
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invoke(methodManager.getMethod(), object, parameter);
                        }
                    });
                }

            } else if (methodManager.getThreadMode() == ThreadMode.BACKGROUND) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            invoke(methodManager.getMethod(), object, parameter);
                        }
                    });
                } else {
                    invoke(methodManager.getMethod(), object, parameter);
                }
            } else if (methodManager.getThreadMode() == ThreadMode.POSTING) {
                invoke(methodManager.getMethod(), object, parameter);
            }
        }
    }

    /**
     * 通过反射去执行方法
     *
     * @param method        执行的方法
     * @param object        组件（Activity、Service等）对象
     * @param parameters    订阅方法的参数对象
     */
    private void invoke(Method method, Object object, Object parameters){
        // invoke 方法，参数1 调用 method 方法的对象，参数2 调用 method 这个方法的传参
        try {
            method.invoke(object, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private CopyOnWriteArrayList<Object> copyOnWriteArrayList;
    private ConcurrentHashMap<Object, Object> stickConcurrentHashMap;

    /**
     * 发送粘性事件
     * @param parameter 参数对象
     */
    public void postStick(Object parameter) {
        stickConcurrentHashMap.put(parameter,parameter);
    }

}
