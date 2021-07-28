package com.aliencat.communication.nio.tcp;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioDemo {


    /**
     * 客户端
     *
     * @throws IOException
     */
    @Test
    public void client() throws IOException {
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9988));
        //在向服务端发送数据时把通道转换为非阻塞模式
        sChannel.configureBlocking(false);
        ByteBuffer data = ByteBuffer.wrap(("你好:" + System.currentTimeMillis()).getBytes("UTF-8"));
        sChannel.write(data);
        data.clear();
        sChannel.close();
    }


    @Test
    public void server() throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //把服务端通道转换为非阻塞式
        ssChannel.configureBlocking(false);
        ssChannel.bind(new InetSocketAddress(9988));

        //新开一个选择器，并把服务端通道注册到选择器上，
        // 到服务器通道持有选择器的引用后就知道把事件通知给谁
        Selector selector = Selector.open();
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);


        //轮询监控选择器，selector.select() >0 代表有事件准备就绪
        while (selector.select() > 0) {
            //把就绪的时间列表取出来遍历
            Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
            while (itr.hasNext()) {
                //遍历时间列表并判断是具体的那种时间并做相应的处理
                SelectionKey key = itr.next();

                //可接收事件,表示接收就绪，
                // 此时再把客户端通道转换为非阻塞式并注册到选择器上
                if (key.isAcceptable()) {
                    SocketChannel sChannel = ssChannel.accept();
                    sChannel.configureBlocking(false);
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    //读就绪 此时可以放心的读取数据，当然最好是把读操作交给线程池里面的一个线程
                    //非阻塞式
                    SocketChannel sc = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while (sc.read(buffer) != -1) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, buffer.limit()));
                        buffer.clear();
                    }
                } else if (key.isWritable()) {

                }
                itr.remove();
            }
        }
    }


}
