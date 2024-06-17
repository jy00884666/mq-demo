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
public class FanoutTest {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange1}")
    private String EXCHANGE_NAME;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 广播式交换机:fanout会将消息路由到每个绑定的队列中
     */
    @Test
    public void testSend2() {
        try {
            // 发送消息入参(交换机名称,RoutingKey[暂时为空],消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, null, "广播式交换机:fanout会将消息路由到每个绑定的队列中");
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
    }
}
