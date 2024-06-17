package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue1队列的消息
 */
@Slf4j
@Component
public class Pattern1Consumer {
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName1}")
    private String QUEUE_NAME1;
    
    /**
     * 处理queue1队列的消息
     * 模式1,无交换机,发送者Publisher直接发送消息到队列queue1
     * @param msg
     */
    @RabbitListener(queues = "${rabbitmq.queueName1}")
    public void listenerSimpleQueue1(String msg) {
        log.info("queue1队列:消费者1收到了消息:{}", msg);
    }
    
}
