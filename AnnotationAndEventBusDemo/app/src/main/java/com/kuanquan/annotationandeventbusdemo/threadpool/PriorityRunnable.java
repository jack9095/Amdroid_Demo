package com.kuanquan.annotationandeventbusdemo.threadpool;

/**
 * 使用：
 val threadPoolManager = ThreadPoolManager()

 // 前三个是由线程池设置的核心数量执行的，后面的是根据我们的优先级进行排序的
 for (c in 1..10) {
     val priority = c
     threadPoolManager.executorService.execute(object : PriorityRunnable(priority) {
         override fun doSth() {
             val name = Thread.currentThread().name
             Log.e("OneActivity", "线程 $name 正在执行优先级为 $priority 的任务")

             try {
                Thread.sleep(200)
             } catch (e: InterruptedException) {
                e.printStackTrace()
             }
        }
     })
  }
 */
public abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {

    private int priority;

    public PriorityRunnable(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException();
        }
        this.priority = priority;
    }

    // 任务线程优先级比较
    @Override
    public int compareTo(PriorityRunnable another) {
        int my = this.getPriority();
        int other = another.getPriority();
        return Integer.compare(other, my);
    }

    @Override
    public void run() {
        doSth();
    }

    public abstract void doSth();

    public int getPriority() {
        return priority;
    }
}
