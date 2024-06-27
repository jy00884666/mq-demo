package com.mqdemo.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.PublisherApplication;
import org.example.dto.CustInfo;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
public class DelayTest {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange8}")
    private String EXCHANGE_NAME;
    
    /**
     * 路由器RoutingKey
     */
    @Value("${rabbitmq.queue.routingkey11}")
    private String ROUTING_KEY;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 延迟消息
     * 1.利用死信队列实现消息延迟处理:例如一条消息发送给Exchange7==>queue10消息队列,但是queue10并无消费者,等超时时间到了变成死信消息转入到Exchange8==>由queue11队列消费
     * 由于需要在RabbitMQ控制台配置消息队列属性 dead-letter-exchange指定一个死信交换机,这里不做演示
     *
     * ☆死信交换机不建议用来当延迟消息来处理
     * 死信交换机的定义:
     * 当一个队列中的消息满足下列情况之一时，就会成为死信(dead letter) :
     * 一.消费者使用basic.reject或basic.nack声明消费失败,并且消息的requeue参数设置为false
     * 二.消息是一个过期消息(达到了队列或消息本身设置的过期时间),超时无人消费
     * 三.要投递的队列消息堆积满了，最早的消息可能成为死信
     * 如果队列通过dead-letter-exchange属性指定了一个交换机，那么该队列中的死信就会投递到这个交换机中。这个交换机称为死信交换机（Dead Letter Exchange，简称DLX）。
     *
     * 演示发送消息设置超时时间
     */
    @Test
    public void testSend1() throws InterruptedException {
        CustInfo custInfo = new CustInfo();
        custInfo.setId("3");
        custInfo.setName("死信消息");
        custInfo.setAge("0");
        custInfo.setMobile("设置消息过期时间");
        /**
         * 1.设置消息发送超时时间使用Message发送消息,消息内容不会走消息转换器,不推荐使用
         * 由于配置了消息转换器消费者接收参数为Object,会抛出异常回执nack
         * 消费者需要使用Message参数接收
         */
        /*try {
            Message message = MessageBuilder
                    // 消息主体
                    .withBody(custInfo.toString().getBytes(StandardCharsets.UTF_8))
                    // 3秒超时
                    .setExpiration("3000")
                    .build();
            // 发送消息入参(交换机名称,RoutingKey,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
            log.info("Message消息发送成功!");
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }*/
        
        // 2.利用后置处理器设置消息发送超时时间发送消息,这样会走消息转换器,推荐使用
        try {
            // 发送消息入参(交换机名称,RoutingKey,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, custInfo, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    // 3秒超时
                    message.getMessageProperties().setExpiration("3000");
                    return message;
                }
            });
            log.info("Message后置处理器消息发送成功!");
        } catch (Exception e) {
            log.error("Message后置处理器消息发送异常:" + e.getMessage(), e);
        }
    }
    
    /**
     * RabbitMQ的官方也推出了一个插件,原生支持延迟消息功能。该插件的原理是设计了一种支持延迟消息功能的交换机，当消息投递到交换机后可以暂存一定时间，到期后再投递到队列。
     *
     * RabbitMQ延迟插件不支持mandatory=true参数，如果启用会同时收到延迟消息和路由失败消息
     * 会调用spring提供的setApplicationContext(ApplicationContext applicationContext)方法
     * 不想接收路由失败消息时设置发送者yml
     * spring:rabbitmq:publisher-returns: false
     * spring:rabbitmq:template:mandatory: false
     */
    @Test
    public void testSend2() throws InterruptedException {
        CustInfo custInfo = new CustInfo();
        custInfo.setId("4");
        custInfo.setName("延迟消息");
        custInfo.setAge("0");
        custInfo.setMobile("设置延迟处理时间");
        // 发送消息,利用消息后置处理器添加消息头
        try {
            // 发送消息入参(交换机名称,RoutingKey,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, custInfo, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    // 3秒延迟消息属性
                    message.getMessageProperties().setDelay(3000);
                    return message;
                }
            });
            log.info("Message后置处理器消息发送成功!");
        } catch (Exception e) {
            log.error("Message后置处理器消息发送异常:" + e.getMessage(), e);
        }
    }
}
