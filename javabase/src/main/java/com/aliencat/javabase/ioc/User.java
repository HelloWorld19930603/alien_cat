package com.aliencat.javabase.ioc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class User {
    private String userId;
    private String userName;
}
