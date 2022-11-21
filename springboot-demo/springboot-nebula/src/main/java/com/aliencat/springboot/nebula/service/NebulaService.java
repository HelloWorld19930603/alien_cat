package com.aliencat.springboot.nebula.service;


import com.aliencat.springboot.nebula.pojo.PersonVo;

import java.util.List;

public interface NebulaService {
    Boolean addOrUpdatePersonInfo(List<PersonVo> list);

    List<PersonVo> queryPersonInfo(String name);
}
