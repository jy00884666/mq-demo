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
public class TopicTest {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange3}")
    private String EXCHANGE_NAME;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 通配符交换机:TopicExchange与Direct类似,区别在于routingKey可以是多个单词列表用.分割,Queue与Exchange绑定BingKey时可以使用通配符,#表示0个或多个单词,*表示一个单词
     * fanoutExchange2交换机绑定了queue3{RoutingKey[red,blue]},queue4{RoutingKey[red,yellow]}
     */
    @Test
    public void testSend1() {
        try {
            // 发送消息入参(交换机名称,RoutingKey,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "china.news", "中国新闻来了");
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "USA.news", "美国新闻来了");
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "china.games", "中国游戏来了");
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
    }
}
