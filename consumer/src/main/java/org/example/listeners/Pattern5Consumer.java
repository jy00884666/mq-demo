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
 * 消息消费者:处理queue7队列的消息,绑定的交换机Exchange名称是fanoutExchange4
 * 消息内容使用消息转换器,传递map对象,也可以是pojo对象
 *
 * Spring提供了两种方式来申明交换机Exchange与队列queue,RoutingKey
 * 一.通过配置类@Configuration @Bean new Queue,缺点当配置多个交换机与消息队列时过于繁琐,尤其是绑定多个RoutingKey的时候需要复制很多代码
 * 二.通过@RabbitListener注解申明,推荐使用
 */
@Slf4j
@Component
public class Pattern5Consumer {
    
    /**
     * 处理queue7队列的消息
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     *
     * 经过测试发现消息类型不管事map还是pojo对象,接收方都能通过Map<String, String>的方式接收并处理
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName7}", durable = "true"),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange4}", type = ExchangeTypes.DIRECT),
            key = {"${rabbitmq.queue.routingkey7}"}
    ))
    public void listenerQueue5(Map<String, String> msg) {
        log.info("queue7队列:消息内容:{}", msg);
    }
    
}
