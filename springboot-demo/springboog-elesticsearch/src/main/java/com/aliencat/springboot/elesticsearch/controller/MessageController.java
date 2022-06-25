package com.aliencat.springboot.elesticsearch.controller;

import com.aliencat.springboot.elesticsearch.pojo.Message;
import com.aliencat.springboot.elesticsearch.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    ArticleRepository articleRepository;

    @PostMapping("save")
    public void save(@Validated @RequestBody Message req){
        //es原生保存接口
        articleRepository.save(req);
        return ;
    }



    @PostMapping("testContactAccount")
    public void testSearchTitle(@Validated @RequestBody Message req){
        List<Message> searchResult = articleRepository.findByContactAccount(req.getContactAccount());
        Iterator<Message> iterator = searchResult.iterator();
        while(iterator.hasNext()){
            System.out.println("成功查询:"+iterator.next());
        }
        return;
    }
}
