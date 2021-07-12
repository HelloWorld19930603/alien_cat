package com.aliencat.communication.nio.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class SelectorDemo {

    int port = 8888;
    private ServerSocketChannel server = null;
    private Selector selector = null;
    private ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocateDirect(1024);


    public static void main(String[] args) {
        new SelectorDemo().start();
    }

    public void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);         //设置非阻塞
            server.bind(new InetSocketAddress(port)); //绑定端口
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT); //注册多路复用器，绑定监听事件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        initServer();
        log.debug("服务器已启动");
        try {
            while (true) {
                //调用select阻塞,直到selector上注册的事件发生
                while (selector.select() > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys(); //获取就绪事件
                    Iterator<SelectionKey> iter = keys.iterator();

                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove(); //先移除该事件,避免重复通知
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHandler(SelectionKey key) throws IOException {
        log.debug("读取事件：" + key.toString());
        SocketChannel socketChannel = (SocketChannel) key.channel();
        readBuffer.clear();
        socketChannel.read(readBuffer);
        readBuffer.flip();
        String receiveData = Charset.forName("UTF-8").decode(readBuffer).toString();
        log.debug("收到来自客户端的消息:" + receiveData);

        //这里将收到的数据发回给客户端
        writeBuffer.clear();
        writeBuffer.put(receiveData.getBytes());
        writeBuffer.flip();
        while (writeBuffer.hasRemaining()) {
            //防止写缓冲区满，需要检测是否完全写入
            log.debug("写入数据字节数:" + socketChannel.write(writeBuffer));
        }

    }

    public void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = ssc.accept(); //接收客户端
            socketChannel.configureBlocking(false);//非阻塞模式
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel.register(selector, SelectionKey.OP_READ, buffer); ////注册读事件

            //向客户端发生一条消息
            buffer.put("你好！这里是服务端。".getBytes());
            buffer.flip();
            socketChannel.write(buffer);

            log.debug("新客户端接入成功：" + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
