package com.mqdemo.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.PublisherApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

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
public class SpringAmqpTest {
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName1}")
    private String QUEUE_NAME1;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 无交换机模式,直接发送消息到队列queue
     */
    @Test
    public void testSend1() {
        try {
            // 发送消息入参(queue队列名称,消息内容)
            rabbitTemplate.convertAndSend(QUEUE_NAME1, "hello 无交换机模式,直接发送消息到队列queue");
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
    }
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange4}")
    private String EXCHANGE_NAME4;
    
    /**
     * 绑定queue7:RoutingKey[object.pojo]
     */
    @Value("${rabbitmq.queue.routingkey7}")
    private String ROUTING_KEY;
    
    /**
     * 测试消息转换器:发送消息到交换机4:绑定queue7:RoutingKey[object.pojo]
     * 消息内容为map或者pojo对象
     */
    @Test
    public void testSend2() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("name", "沙士杰");
            map.put("age", "18");
            map.put("mobile", "13816421047");
            // 发送消息入参(queue队列名称,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME4, ROUTING_KEY, map);
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
    }
    
}
