package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue1,queue2队列的消息,绑定的交换机Exchange名称是fanoutExchange1
 * 广播式交换机:Fanout会将消息路由到每个绑定的队列中,这里的queue1,queue2会接收到所有消息队列里的消息,重复处理同一条消息
 *
 * Spring提供了两种方式来申明交换机Exchange与队列queue,RoutingKey
 * 一.通过配置类@Configuration @Bean new Queue,缺点当配置多个交换机与消息队列时过于繁琐,尤其是绑定多个RoutingKey的时候需要复制很多代码
 * 二.通过@RabbitListener注解申明,推荐使用
 */
@Configuration
@Slf4j
@Component
public class Pattern2Consumer {
    
    /**
     * 处理queue1队列的消息
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName1}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange1}", type = ExchangeTypes.FANOUT)
    ))
    public void listenerQueue1(String msg) {
        log.info("queue1队列:消费者1收到了消息:{}", msg);
    }
    
    /**
     * 处理queue2队列的消息
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName2}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange1}", type = ExchangeTypes.FANOUT)
    ))
    public void listenerQueue2(String msg) {
        log.info("queue2队列:消费者2收到了消息:{}", msg);
    }
}
