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
@RequestMapping("/publisher2Controller")
public class Publisher2Controller {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange1}")
    private String EXCHANGE_NAME;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 广播式交换机:fanout会将消息路由到每个绑定的队列中
     * 通过访问发送消息
     * http://localhost:8081/publisher2Controller/send2?msg=广播式交换机:fanout会将消息路由到每个绑定的队列中
     */
    @RequestMapping("/send2")
    public String send2(@RequestParam("msg") String msg) {
        try {
            // 发送消息入参(交换机名称,RoutingKey[暂时为空],消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, null, msg);
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
        return "it is msg:" + msg;
    }
    
}
