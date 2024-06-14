package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/publisherController")
public class PublisherController {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.exchangeName}")
    private String EXCHANGE_NAME;
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName}")
    private String QUEUE_NAME;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 无交换机,直接发送消息到队列queue
     * @param msg
     * @return
     */
    @RequestMapping("/send1")
    public String send1(@RequestParam("msg") String msg) {
        try {
            rabbitTemplate.convertAndSend(QUEUE_NAME, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "it is msg:" + msg;
    }
    
}
