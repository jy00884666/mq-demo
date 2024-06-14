package org.example.listeners;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 处理queue1队列的消息
 */
@Slf4j
@Component
public class Pattern1Consumer {
    
    /**
     * 模式1,无交换机,直接发送消息到队列queue
     * @param msg
     */
    @RabbitListener(queues = "queue1")
    public void listenerSimpleQueue1(String msg) {
        log.info("消费者收到了消息:{}",msg);
    }
}
