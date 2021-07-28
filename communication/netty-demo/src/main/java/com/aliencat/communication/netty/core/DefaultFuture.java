package com.aliencat.communication.netty.core;

import com.aliencat.communication.netty.param.Request;
import com.aliencat.communication.netty.param.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class DefaultFuture {

    public final static Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>();

    static {
        FutureTimeOutThread timeOutThread = new FutureTimeOutThread();
        timeOutThread.setDaemon(true);
        timeOutThread.start();

    }

    private final long start = System.currentTimeMillis();
    private long id;
    private volatile Response response;
    private long timeout;
    private volatile Lock lock = new ReentrantLock();
    private volatile Condition condition = lock.newCondition();

    public DefaultFuture() {
    }


    public DefaultFuture(Request request) {
        id = request.getId();
        FUTURES.put(id, this);
    }

    //收到服务器响应
    public static void recive(Response res) {
        //找到res相对应的DefaultFuture
        DefaultFuture future = FUTURES.remove(res.getId());
        if (future == null) {
            return;
        }
        Lock lock = future.getLock();
        lock.lock();
        try {
            future.setResponse(res);
            Condition condition = future.getCondition();
            if (condition != null) {
                condition.signal();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Response get() {
        lock.lock();
        while (!hasDone()) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


        return response;
    }

    public Response get(long timeout) {
        long start = System.currentTimeMillis();
        lock.lock();
        while (!hasDone()) {
            try {
                condition.await(timeout, TimeUnit.SECONDS);
                if (System.currentTimeMillis() - start >= timeout) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


        return response;
    }

    private boolean hasDone() {
        return response != null ? true : false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStart() {
        return start;
    }

    static class FutureTimeOutThread extends Thread {

        @Override
        public void run() {

            while (true) {
                for (long futureId : FUTURES.keySet()) {
                    DefaultFuture f = FUTURES.get(futureId);
                    if (f == null) {
                        FUTURES.remove(futureId);
                        continue;
                    }
                    if (f.getTimeout() > 0) {
                        if ((System.currentTimeMillis() - f.getStart()) > f.getTimeout()) {
                            Response res = new Response();
                            res.setContent(null);
                            res.setMsg("请求超时！");
                            res.setStatus(1);//响应异常处理
                            res.setId(f.getId());
                            DefaultFuture.recive(res);
                        }
                    }


                }


            }


        }


    }


}
