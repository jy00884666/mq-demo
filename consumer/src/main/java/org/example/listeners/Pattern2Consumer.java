package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue1,queue2队列的消息,绑定的交换机Exchange名称是fanoutExchange1
 * 广播式交换机:fanout会将消息路由到每个绑定的队列中,这里的queue1,queue2会接收到所有消息队列里的消息,重复处理同一条消息
 */
@Slf4j
@Component
public class Pattern2Consumer {
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName1}")
    private String QUEUE_NAME1;
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName2}")
    private String QUEUE_NAME2;
    
    /**
     * 处理queue1队列的消息
     * @param msg
     */
    @RabbitListener(queues = "${rabbitmq.queueName1}")
    public void listenerQueue1(String msg) {
        log.info("queue1队列:消费者2收到了消息:{}", msg);
    }
    
    /**
     * 处理queue2队列的消息
     * @param msg
     */
    @RabbitListener(queues = "${rabbitmq.queueName2}")
    public void listenerQueue2(String msg) {
        log.info("queue2队列:消费者1收到了消息:{}", msg);
    }
}
