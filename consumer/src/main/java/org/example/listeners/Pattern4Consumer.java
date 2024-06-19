package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue5,queue6队列的消息,绑定的交换机Exchange名称是fanoutExchange3
 * 通配符式交换机:TopicExchange与Direct类似,区别在于RoutingKey可以是多个单词列表用.分割,Queue与Exchange绑定BingKey时可以使用通配符,#表示0个或多个单词,*表示一个单词
 * 消息队列绑定情况queue5{RoutingKey[china.#]},queue6{RoutingKey[#.news]}
 * 这里queue5会处理china开头的RoutingKey消息,queue6会处理news结尾的RoutingKey消息
 *
 * Spring提供了两种方式来申明交换机Exchange与队列queue,RoutingKey
 * 一.通过配置类@Configuration @Bean new Queue,缺点当配置多个交换机与消息队列时过于繁琐,尤其是绑定多个RoutingKey的时候需要复制很多代码
 * 二.通过@RabbitListener注解申明,推荐使用
 */
@Slf4j
@Component
public class Pattern4Consumer {
    
    /**
     * 处理queue5{RoutingKey[china.#]}队列的消息
     * durable = "true"表示创建持久化的消息队列
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName5}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange3}", type = ExchangeTypes.TOPIC),
            key = {"${rabbitmq.queue.routingkey5}"}
    ))
    public void listenerQueue5(String msg) {
        log.info("queue5队列:消费者5收到了消息:{}", msg);
    }
    
    /**
     * 处理queue6{RoutingKey[#.news]}队列的消息
     * durable = "true"表示创建持久化的消息队列
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName6}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange3}", type = ExchangeTypes.TOPIC),
            key = {"${rabbitmq.queue.routingkey6}"}
    ))
    public void listenerQueue6(String msg) {
        log.info("queue6队列:消费者6收到了消息:{}", msg);
    }
}
