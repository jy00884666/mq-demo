package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue3,queue4队列的消息,绑定的交换机Exchange名称是fanoutExchange2
 * 指定式交换机:Direct会将消息路由到指定的Routing key绑定的队列中,这里的queue3绑定的RoutingKey[red,blue],queue4绑定的RoutinKey[red,yellow]
 * 当Routing key=red时两个queue都会处理,Routing key=blue时queue3消息队列中有消息处理,Routing key=yellow时queue4消息队列中有消息处理
 *
 * Spring提供了两种方式来申明交换机Exchange与队列queue,RoutingKey
 * 一.通过配置类@Configuration @Bean new Queue,缺点当配置多个交换机与消息队列时过于繁琐,尤其是绑定多个RoutingKey的时候需要复制很多代码
 * 二.通过@RabbitListener注解申明,推荐使用
 */
@Slf4j
@Component
public class Pattern3Consumer {
    
    /**
     * 处理queue3{RoutingKey[red,blue]}队列的消息
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName3}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange2}", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenerQueue3(String msg) {
        log.info("queue3队列:消息内容:{}", msg);
    }
    
    /**
     * 处理queue4{RoutinKey[red,yellow]}队列的消息
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName4}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange2}", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenerQueue4(String msg) {
        log.info("queue4队列:消息内容:{}", msg);
    }
}
