package com.lgy.classics_socket.util;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池辅助类，整个应用程序就只有一个线程池去管理线程。 可以设置核心线程数、最大线程数、额外线程空状态生存时间，阻塞队列长度来优化线程池。
 */
public class ThreadPoolUtil {


    private static final int CORE_POOL_SIZE = 1;
    private static Map<Runnable, Future<?>> map = new HashMap<Runnable, Future<?>>();

    // 线程池最大线程数
    private static int MAX_POOL_SIZE = 20;

    // 额外线程空状态生存时间
    private static int KEEP_ALIVE_TIME = 10000;

    // 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
//    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
//            10);
    private static BlockingQueue<Runnable> workQueue = new SynchronousQueue();

    private ThreadPoolUtil() {
    }

    // 线程工厂
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "threadPool thread:" + integer.getAndIncrement());
        }
    };

    // 线程池
    private static ThreadPoolExecutor threadPool;

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    }

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        Future<?> submit = threadPool.submit(runnable);
        map.put(runnable, submit);
    }

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     *
     * @param runnable
     */
    public static boolean execute(String runnableName, Runnable runnable) {
        Set<Runnable> set = map.keySet();
        Iterator iterator = set.iterator();
        Future<?> submit = null;
        if (!TextUtils.isEmpty(runnableName)) {
            submit = threadPool.submit(new LRunnable(runnableName, runnable));
            map.put(runnable, submit);
            return false;
        }
        while (iterator.hasNext()) {
            if (runnableName.equals(((LRunnable) iterator.next()).getName())) {
                return true;
            }
        }
        submit = threadPool.submit(runnable);
        map.put(runnable, submit);
        return false;
    }

    public static boolean remove(String runnableName) {
        Set<Runnable> set = map.keySet();
        Iterator iterator = set.iterator();
        Future<?> submit = null;
        if (!TextUtils.isEmpty(runnableName)) {
            if (iterator.hasNext()) {
                LRunnable runnable = (LRunnable) iterator.next();
                if (runnableName.equals(runnable.getName())) {
                    submit = map.get(runnableName);
                    if (submit != null) {
                        submit.cancel(true);
                    }
                    //移除
                    map.remove(runnableName);
                    return true;
                }
            }
        }
        return false;
    }

    public static void remove(Runnable runnable) {
        Future<?> submit = (Future<?>) map.get(runnable);
        if (submit != null) {
            submit.cancel(true);
        }
        //移除
        map.remove(runnable);
    }


    public static class LRunnable implements Runnable {

        private String name;
        private Runnable runnable;

        public LRunnable(String name, Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }

        public LRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }

}