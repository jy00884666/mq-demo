package com.mqdemo.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.PublisherApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * 消息发送测试
 */
@SpringBootTest
@Slf4j
/**
 * 由于测试类不在主配置类的直接或间接子包中，使用 @ContextConfiguration 注解显式指定配置类
 * 如果test下的报名是org.example与main下的一致则无需指定配置类,否则 @Autowired会无法注入
 */
@ContextConfiguration(classes = PublisherApplication.class)
public class DirectTest {
    
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
     * http://localhost:8081/publisherController2/send1?msg=red
     * http://localhost:8081/publisherController2/send1?msg=blue
     * http://localhost:8081/publisherController2/send1?msg=yellow
     * fanoutExchange2交换机绑定了queue3{RoutingKey[red,blue]},queue4{RoutingKey[red,yellow]}
     */
    @Test
    public void testSend1() {
        try {
            // 发送消息入参(交换机名称,RoutingKey,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "red", "red来了");
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "blue", "blue来了");
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "yellow", "yellow来了");
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
    }
}
