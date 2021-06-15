package com.aliencat.javabase.database;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by cc on 2017/9/21.
 */
public class MyConnectionPool {

    //包装真实的Connetion对象
    private Connection connection = null;

    //这里采用的是BlockingQueue阻塞队列作为连接池
    private static BlockingQueue<MyConnectionPool> pool;
    private static String PASSWORD;
    private static String URL;
    private static String DRIVER;
    private static String USER;
    //数据库连接池的容量
    private static int POOLSIZE;

    static {
        Properties prop = new Properties();
        /**
         * 加载jdbc配置文件
         * 资源文件在resource目录下：/conf/jdbc.properties
         */
        InputStream in = MyConnectionPool.class.getResourceAsStream("/conf/jdbc.properties");
        try {
            prop.load(in);
            URL = prop.getProperty("URL").trim();
            DRIVER = prop.getProperty("DRIVER").trim();
            USER = prop.getProperty("USER").trim();
            PASSWORD = prop.getProperty("PASSWORD").trim();
            POOLSIZE = Integer.valueOf(prop.getProperty("POOLSIZE").trim());
            Class.forName(DRIVER);
            //创建线程池
            pool = new LinkedBlockingDeque<>(POOLSIZE);
        } catch (IOException e) {
            System.out.println("jdbc.properties加载失败");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MyConnectionPool() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public MyConnectionPool(Connection conn) {
        connection = conn;
    }

    /**
     * 向线程池添加MyConnection对象
     * 返回true代表成功
     *
     * @return
     */
    public boolean addConnection() {
        return pool.offer(this);
    }

    /**
     * 这里的关闭并不是真正意义上的关闭，而是将MyConnection对象返送给连接池中
     * offer的用法在于在队列中添加对象，若是队列满了则返回false
     * 所以这里有个判断，若是为null，则真正关闭connetion
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (!pool.offer(this)) {
            connection.close();
        }
    }

    /**
     * 这里是真正意义上关闭数据库连接
     *
     * @throws SQLException
     */
    public void closeConnetion() throws SQLException {
        connection.close();
    }

    /**
     * 从连接池中获取MyConnetion对象
     * poll的作用在于若是连接池空了则返回null
     * 所以下面有个判断若是myConnetion为空则新建连接
     * 但是由于数据库有最大连接限制，大并发下显然不能无限创建connection
     * 所以通过take方法进行阻塞，使得池中返还对象后立即返回
     *
     * @return
     */
    public static MyConnectionPool getConnection() {
        MyConnectionPool myConnection = pool.poll();
        if (myConnection != null) {
            return myConnection;
        } else {
            try {
                myConnection = new MyConnectionPool();
            } catch (SQLException e) {
                try {
                    myConnection = pool.take();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            return myConnection;
        }
    }

    /**
     * 阻塞式的从池中获取MyConnetion，不会新建新的对象
     * 这样能保证最大连接数不变
     *
     * @return
     * @throws InterruptedException
     */
    public static MyConnectionPool takeConnection() throws InterruptedException {
        return pool.take();
    }

    //获取PreparedStatement对象
    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * 这个方法是用来解决mysql数据库8小时超时的问题
     * 默认一个Connection超过8小时未被使用就会被mysql挂掉
     * 我的解决方案是若出现异常后我会进行捕获然后调用此方法验证是否是由于Connetion失效所致
     * 若true，则新建Connection对象
     *
     * @param time
     * @return
     * @throws SQLException
     */
    public boolean isValid(int time) throws SQLException {
        return connection.isValid(time);
    }
}


