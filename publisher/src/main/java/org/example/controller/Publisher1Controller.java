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
@RequestMapping("/publisher1Controller")
public class Publisher1Controller {
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName1}")
    private String QUEUE_NAME1;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 无交换机,直接发送消息到队列queue
     * 通过访问 http://localhost:8081/publisher1Controller/send1?msg=hello 发送消息
     * @param msg
     * @return
     */
    @RequestMapping("/send1")
    public String send1(@RequestParam("msg") String msg) {
        try {
            rabbitTemplate.convertAndSend(QUEUE_NAME1, msg);
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
        return "it is msg:" + msg;
    }
    
}
