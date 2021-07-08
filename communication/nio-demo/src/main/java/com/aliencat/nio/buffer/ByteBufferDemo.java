package com.aliencat.nio.buffer;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;

@Slf4j
public class ByteBufferDemo {

    ByteBuffer byteBuffer = null;

    @Test
    public void allocatTest() {
        byteBuffer = ByteBuffer.allocate(20);
        log.debug("------------after allocate------------------");
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
    }

    @Test
    public void putTest() {
        allocatTest();
        for (byte i = 0; i < 5; i++) {
            byteBuffer.put(i);

        }
        log.debug("------------after putTest------------------");
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
    }

    @Test
    public void flipTest() {
        putTest();
        byteBuffer.flip();
        log.debug("------------after flipTest ------------------");
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
    }

    @Test
    public void getTest() {
        flipTest();
        for (byte i = 0; i < 2; i++) {
            byte j = byteBuffer.get();
            log.debug("j = " + j);
        }
        log.debug("------------after get 2 byte ------------------");
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
        for (byte i = 0; i < 3; i++) {
            byte j = byteBuffer.get();
            log.debug("j = " + j);
        }
        log.debug("------------after get 3 byte ------------------");
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
    }

    @Test
    public void rewindTest() {
        getTest();
        byteBuffer.rewind();
        log.debug("------------after rewind ------------------");
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
    }


    /**
     * rewind之后，重复读
     * 并且演示 mark 标记方法
     */
    @Test
    public void reRead() {

        rewindTest();
        for (byte i = 0; i < 5; i++) {
            if (i == 2) {
                byteBuffer.mark();
            }
            byte j = byteBuffer.get();
            log.debug("j = " + j);

        }
        log.debug("------------after reRead------------------");
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());

    }

    @Test
    public void afterReset() {
        reRead();
        log.debug("------------after reset------------------");
        byteBuffer.reset();
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
        for (byte i = 2; i < 5; i++) {
            byte j = byteBuffer.get();
            log.debug("j = " + j);

        }

    }

    @Test
    public void clearDemo() {
        afterReset();
        log.debug("------------after clear------------------");
        byteBuffer.clear();
        log.debug("position=" + byteBuffer.position());
        log.debug("limit=" + byteBuffer.limit());
        log.debug("capacity=" + byteBuffer.capacity());
    }

}


