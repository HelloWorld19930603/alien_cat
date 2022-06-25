package com.aliencat.springboot.elesticsearch.repository;

import com.aliencat.springboot.elesticsearch.pojo.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleRepository extends ElasticsearchRepository<Message, String> {

    List<Message> findByBusinessActionId(Long businessActionId);

    List<Message> findByContactAccount(String contactAccount);

    List<Message> findByMessageContent(String messageContent);

    List<Message> findBySenderAccountAndRecipientAccount(String senderAccount, String recipientAccount);


}