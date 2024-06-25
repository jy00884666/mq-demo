package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 测试消费者消息处理异常重试机制,会固定抛出异常到达消费者本地重试上线然后发送到 fanoutExchange7:error.direct 交换机统一处理消息
 *
 * Spring提供了两种方式来申明交换机Exchange与队列queue,RoutingKey
 * 一.通过配置类@Configuration @Bean new Queue,缺点当配置多个交换机与消息队列时过于繁琐,尤其是绑定多个RoutingKey的时候需要复制很多代码
 * 二.通过@RabbitListener注解申明,推荐使用
 */
@Slf4j
@Component
public class Pattern7Consumer {
    
    /**
     * 测试消费者消息处理异常重试机制,会固定抛出异常到达消费者本地重试上线然后发送到 fanoutExchange7:error.direct 交换机统一处理消息
     *
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName9}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange6}", type = ExchangeTypes.DIRECT),
            key = {"${rabbitmq.queue.routingkey9}"}
    ))
    public void listenerQueue9(Object msg) {
        log.info("queue9队列:消息内容:{}", msg);
        // 消息主体内容
        String body = new String(((Message) msg).getBody());
        log.info("body:{}", body);
        // 消息属性,包含异常信息
        MessageProperties messageProperties = ((Message) msg).getMessageProperties();
        log.info("messageProperties:{}", messageProperties);
        throw new RuntimeException("故意的");
    }
    
}
