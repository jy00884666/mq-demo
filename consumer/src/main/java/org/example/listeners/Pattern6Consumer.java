package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消息消费者:处理queue8队列的消息,绑定的交换机Exchange名称是fanoutExchange5
 * 消费者消息确认机制
 *
 */
@Slf4j
@Component
public class Pattern6Consumer {
    
    /**
     * 处理queue8队列的消息
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     *
     *
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName8}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange5}", type = ExchangeTypes.DIRECT),
            key = {"${rabbitmq.queue.routingkey8}"}
    ))
    public void listenerQueue5(String msg) {
        log.info("queue8队列:消息内容:{}", msg);
    }
    
}
