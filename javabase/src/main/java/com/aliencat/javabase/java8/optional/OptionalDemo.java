package com.aliencat.javabase.java8.optional;

import com.aliencat.javabase.ioc.User;
import com.pattern.proxy.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@Slf4j
public class OptionalDemo {


    /**
     * 两个 Optional  对象都包含非空值，两个方法都会返回对应的非空值。
     * 不过，orElse() 方法仍然创建了 User 对象。
     * 与之相反，orElseGet() 方法不创建 User 对象。
     */
    @Test
    public void givenPresentValue_whenCompare_thenOk() {
        User user = new User("111", "1234");
        log.info("Using orElse");
        User result = Optional.ofNullable(user).orElse(createNewUser());
        log.info("Using orElseGet");
        User result2 = Optional.ofNullable(user).orElseGet(() -> createNewUser());
    }


    private User createNewUser() {
        log.debug("Creating New User");
        return new User("1", "zhangshan");
    }


    @Test
    public void whenMap_thenOk() {
        User user = new User("lishi@gmail.com", "lishi");
        String name = Optional.ofNullable(user)
                .map(u -> u.getUserName()).orElse("default@gmail.com");

        assertEquals(name, user.getUserName());
    }

}

