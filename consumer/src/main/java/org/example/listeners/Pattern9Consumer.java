package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 延迟消息消费者:需要先安装RabbitMQ提供的延迟交换机插件。该插件的原理是设计了一种支持延迟消息功能的交换机，当消息投递到交换机后可以暂存一定时间，到期后再投递到队列。
 *
 * 一.通过配置类@Configuration @Bean new Queue,缺点当配置多个交换机与消息队列时过于繁琐,尤其是绑定多个RoutingKey的时候需要复制很多代码
 * 二.通过@RabbitListener注解申明,推荐使用
 */
@Slf4j
@Component
public class Pattern9Consumer {
    
    /**
     * RabbitMQ延迟插件不支持mandatory=true参数，如果启用会同时收到延迟消息和路由失败消息
     * 会调用spring提供的setApplicationContext(ApplicationContext applicationContext)方法
     * 不想接收路由失败消息时设置发送者yml
     * spring:rabbitmq:publisher-returns: false
     * spring:rabbitmq:template:mandatory: false
     *
     * delayed = "true"表示创建的交换机是一个延迟交换机
     *
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName11}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange8}", type = ExchangeTypes.DIRECT, delayed = "true"),
            key = {"${rabbitmq.queue.routingkey11}"}
    ))
    public void listenerQueue11(Object msg) throws Exception {
        log.info("queue11队列:消息内容:{}", msg);
        // 消息主体内容
        String body = new String(((Message) msg).getBody());
        log.info("body:{}", body);
        // 消息属性,包含异常信息
        MessageProperties messageProperties = ((Message) msg).getMessageProperties();
        log.info("messageProperties:{}", messageProperties);
    }
    
    /**
     * 申明式方法
     */
    /*@Bean
    public DirectExchange delayExchange(){
        return ExchangeBuilder
                .directExchange("${rabbitmq.fanoutExchange8}")
                .delayed() // 设置delayed的属性为true
                .durable(true) // 持久化
                .build();
    }*/
}
