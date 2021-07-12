package com.aliencat.javabase.api.lock;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class MyAQSLock {


    //获取到Unsafe对象
    private static final Unsafe unsafe = getUnsafe();
    //在内存的偏移量值，因为CAS种需要此参数
    private static long stateOffset;

    static {
        try {
            //找到state对象在内存中的偏移量
            stateOffset = unsafe.objectFieldOffset(MyAQSLock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    //用来记录当前加锁状态，记录加锁次数，
    //值为0/1，为1时表示已经有一个线程持有了锁
    private volatile int state = 0;
    //表示当前持有锁的线程对象
    private transient Thread exclusiveOwnerThread;
    //定义一个线程安全(底层是使用CAS算法保证线程安全的)的队列，用于保存此时没有获取到锁的线程
    //注意AQS是使用的CHL队列，为了好理解我这里用ConcurrentLinkedQueue替代了,当然性能要低不少了
    private transient ConcurrentLinkedQueue<Thread> waiters = new ConcurrentLinkedQueue<>();

    //处理当前线程中断情况
    static void selfInterrupt() {

        //interrupt()方法其作用是中断此线程（此线程不一定是当前线程，而是指调用该方法的Thread实例所代表的线程），
        // 但实际上只是给线程设置一个中断标志，线程仍会继续运行。
        Thread.currentThread().interrupt();
    }

    //通过反射机制获取到Unsafe类
    public static Unsafe getUnsafe() {
        Field field = null;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state < 0)
            throw new Error("Maximum lock count exceeded");
        this.state = state;
    }

    public Thread getLocalHolder() {
        return exclusiveOwnerThread;
    }

    public void setLocalHolder(Thread exclusiveOwnerThread) {
        this.exclusiveOwnerThread = exclusiveOwnerThread;
    }


    private boolean hasQueuedPredecessors() {
        return waiters != null && waiters.size() > 0;
    }

    //加锁
    public void lock() {
        Thread p = Thread.currentThread();
        log.debug(p.getName() + " 开始加锁--" + state);
        //首先尝试去获取锁
        acquire(1);
        log.debug(p.getName() + " 加锁成功--" + state);
    }

    public final void acquire(int arg) {
        if (!tryAcquire(arg) && acquireQueued())
            selfInterrupt();
    }

    //尝试进行加锁,次数为num
    public boolean tryAcquire(int num) {
        //CAS比较与交换算法，保证任意时刻只有一个线程可以拿到
        //当前线程
        Thread current = Thread.currentThread();
        //获取到当前state初始值
        int state = getState();
        if (current == getLocalHolder()) {
            setState(state + num);  //当前线程为锁的持有者可以直接操作
            return true;
        } else if (state == 0 && !hasQueuedPredecessors() && compareAndSwapState(0, num)) {
            //目前锁还没有被持有，在当前线程前面也没有其它线程，而且当前线程获取成功
            // 公平锁情况下判断 hasQueuedPredecessors,否则就是非公平了
            setLocalHolder(current);
            return true;
        } else {
            //如果等待队列中没有进程（实现公平锁）或者当前线程是等待队列中第一个线程，并且此线程修改成功了(加锁成功)，则设置持有锁的线程为本线程;
            if ((waiters.size() == 0 || current == waiters.peek()) && compareAndSwapState(0, 1)) {
                setLocalHolder(current);
                return true;
            }
            return false;
        }
    }

    private boolean acquireQueued() {
        Thread p = Thread.currentThread(); //当前线程
        waiters.add(p); //当前线程入队
        try {
            //如果当前线程为队列头节点且没有加锁成功，则使此线程一直自旋在本方法
            while (true) {
                if (getLocalHolder() == null && p == waiters.peek() && compareAndSwapState(0, 1)) {
                    break;
                }
                if (checkInterruptAndPark(p)) {
                    return true;
                }
            }
            setLocalHolder(p);  //设置当前线程持有锁
            waiters.poll(); //移除头节点
            return p.isInterrupted();
        } finally {
            return true; //返回true则中断当前线程
        }
    }

    private boolean checkInterruptAndPark(Thread p) {
        //让步出线程
        //1：Thread.yield();但是循环之后还是在占用cpu，不推荐
        //2：Thread.sleep(1000);不推荐，原因如下
        //（1）：设置时常大之后，其他线程已经释放锁，本线程还在睡眠，浪费时间
        //（2）：设置时常小之后，导致不停的睡眠启动线程，系统开销大
        //3：Thread.wait();不推荐，因为在唤醒线程的时候，无法准确指定唤醒那一个线程；
        //4：使用Unsafe类中的park()和unpark()方法，进行手动的释放和开启线程（此两种方法已经重写在了jdk的LockSupport类中）
        /*
            //jdk中的方法体
            public static void park(Object blocker) {
                Thread t = Thread.currentThread();
                setBlocker(t, blocker);
                U.park(false, 0L);
                setBlocker(t, (Object)null);
            }
         */
        if (p.isInterrupted())
            LockSupport.park(p);   //阻塞该线程
        //interrupted()方法 作用是测试当前线程是否被中断（检查中断标志），返回一个boolean并清除中断状态，
        // 第二次再调用时中断状态已经被清除，将返回一个false。
        return Thread.interrupted();
    }

    //解锁方法
    public void unLock() {
        Thread p = Thread.currentThread();
        log.debug(p.getName() + " 开始解锁--" + state);
        //判断当前对象是不是持有锁的对象
        if (p != exclusiveOwnerThread) {
            throw new RuntimeException("LocalHolder is not current thread");
        }
        release(1); //一层一层的减
        log.debug(p.getName() + " 解锁成功--" + state);
    }

    public final void release(int arg) {
        tryRelease(1);
/*        if (tryRelease(arg)) {
            //当前锁空闲后，如果等待队列中有线程，则唤醒此线程
            Thread head = waiters.poll();
            if (head != null) {
                LockSupport.unpark(head); //唤醒head线程
            }
        }*/
    }


    protected final boolean tryRelease(int releases) {
        int c = getState() - releases;
        if (Thread.currentThread() != getLocalHolder())
            throw new IllegalMonitorStateException();
        setState(c);
        if (c == 0) {
            setLocalHolder(null); //持有锁的线程置空
            return true;
        }
        return false;
    }

    /*
     * 原子操作。
     * @param except:目前值
     * @param update:要更新后的值
     */
    public final boolean compareAndSwapState(int except, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, except, update);
    }

    //序列化时释放锁
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        setState(0); // 重置锁，防止死锁发生
    }
}
