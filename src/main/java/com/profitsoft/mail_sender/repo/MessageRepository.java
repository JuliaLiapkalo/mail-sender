package com.profitsoft.mail_sender.repo;

import com.profitsoft.mail_sender.entity.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MessageRepository extends ElasticsearchRepository<Message, String> {

    List<Message> findByStatus(String status);

}
