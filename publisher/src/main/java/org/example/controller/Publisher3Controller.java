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
@RequestMapping("/publisher3Controller")
public class Publisher3Controller {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange2}")
    private String EXCHANGE_NAME;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 指定式交换机:Direct会将消息路由到指定的Routing key绑定的队列中
     * 通过访问发送消息
     * http://localhost:8081/publisher3Controller/send1?msg=red
     * http://localhost:8081/publisher3Controller/send1?msg=blue
     * http://localhost:8081/publisher3Controller/send1?msg=yellow
     * fanoutExchange2交换机绑定了queue3{RoutingKey[red,blue]},queue4{RoutingKey[red,yellow]}
     */
    @RequestMapping("/send1")
    public String send1(@RequestParam("msg") String msg) {
        try {
            // 发送消息入参(交换机名称,RoutingKey[暂时为空],消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, msg, msg + "来了");
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
        return "it is msg:" + msg;
    }
    
}
