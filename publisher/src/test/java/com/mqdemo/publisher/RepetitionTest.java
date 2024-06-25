package com.mqdemo.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.PublisherApplication;
import org.example.dto.CustInfo;
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
 * 由于测试类不在主配置类的直接或间接子包中,使用 @ContextConfiguration 注解显式指定配置类
 * 如果test下的报名是org.example与main下的一致则无需指定配置类,否则 @Autowired会无法注入
 */
@ContextConfiguration(classes = PublisherApplication.class)
public class RepetitionTest {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange6}")
    private String EXCHANGE_NAME;
    
    /**
     * 路由器RoutingKey
     */
    @Value("${rabbitmq.queue.routingkey9}")
    private String ROUTING_KEY;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 测试消费者消息处理异常重试机制,会固定抛出异常到达消费者本地重试上线然后发送到 fanoutExchange7:error.direct 交换机统一处理消息
     */
    @Test
    public void testSend1() throws InterruptedException {
        CustInfo custInfo = new CustInfo();
        custInfo.setId("2");
        custInfo.setName("aaa");
        custInfo.setAge("0");
        custInfo.setMobile("310021");
        try {
            // 发送消息入参(交换机名称,RoutingKey,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, custInfo);
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
    }
    
}
