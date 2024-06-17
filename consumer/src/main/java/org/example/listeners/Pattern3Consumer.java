package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue3,queue4队列的消息,绑定的交换机Exchange名称是fanoutExchange2
 * 指定式交换机:Direct会将消息路由到指定的Routing key绑定的队列中,这里的queue3绑定的RoutingKey[red,blue],queue4绑定的RoutinKey[red,yellow]
 * 当Routing key=red时两个queue都会处理,Routing key=blue时queue3消息队列中有消息处理,Routing key=yellow时queue4消息队列中有消息处理
 */
@Slf4j
@Component
public class Pattern3Consumer {
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName3")
    private String QUEUE_NAME3;
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName4")
    private String QUEUE_NAME4;
    
    /**
     * 处理queue1队列的消息
     * @param msg
     */
    @RabbitListener(queues = "${rabbitmq.queueName3}")
    public void listenerQueue3(String msg) {
        log.info("queue3队列:消费者3收到了消息:{}", msg);
    }
    
    /**
     * 处理queue2队列的消息
     * @param msg
     */
    @RabbitListener(queues = "${rabbitmq.queueName4}")
    public void listenerQueue4(String msg) {
        log.info("queue4队列:消费者4收到了消息:{}", msg);
    }
}
