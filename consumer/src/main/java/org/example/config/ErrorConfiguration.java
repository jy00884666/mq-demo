package org.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置错误处理交换机,当异常消息消费重试次数到达上线,指定这个交换机统一处理
 *
 * Spring提供了两种方式来申明交换机Exchange与队列queue,RoutingKey
 * 一.通过配置类@Configuration @Bean new Queue,缺点当配置多个交换机与消息队列时过于繁琐,尤其是绑定多个RoutingKey的时候需要复制很多代码
 * 二.通过@RabbitListener注解申明,推荐使用
 * 这里使用@Bean的方式申明交换机,消息队列与RoutingKey
 *
 * 配置MessageRecoverer接口
 * 在开启消费者消息失败重试机制后,当重试次数耗尽,消息依然失败,则需要有MessageRecoverer接口来处理
 */
@Configuration
/**
 * 当某个属性满足条件时生效:prefix设置属性前缀,name设置属性名称,havingValue设置属性值
 * */
//@ConditionalOnProperty(prefix = "spring.rabbitmq.listener.simple.retry", name = "enabled", havingValue = "true")
public class ErrorConfiguration {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange7}")
    private String EXCHANGE_NAME;
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName10}")
    private String QUEUE_NAME;
    
    /**
     * 绑定的RoutingKey
     */
    @Value("${rabbitmq.queue.routingkey10}")
    private String ROUTING_KEY;
    
    /**
     * 配置交换机,名称error.direct
     * @return
     */
    @Bean
    public DirectExchange errorExchange() {
        // 两种写法都可以,不喜欢用new的可以用...Builder
        // 具体用哪种交换机方式参考Pattern(2,3,4)Consumer例子
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).build();
        //return new DirectExchange(EXCHANGE_NAME);//指定式
        //return new TopicExchange(EXCHANGE_NAME);//通配符式
        //return new FanoutExchange(EXCHANGE_NAME);//广播式
    }
    
    /**
     * 配置消息队列,Queue
     * @return
     */
    @Bean
    public Queue errorQueue() {
        // 两种写法都可以
        return QueueBuilder.durable(QUEUE_NAME).build();
        //return new Queue(QUEUE_NAME);
    }
    
    /**
     * 绑定交换机与消息队列并设置RoutingKey
     * @param errorQueue
     * @param errorExchange
     * @return
     */
    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorExchange) {
        // 绑定Exchange与Queue并设置RoutingKey="error"
        return BindingBuilder.bind(errorQueue).to(errorExchange).with(ROUTING_KEY);
    }
    
    /**
     * 在开启消费者消息失败重试机制后,当重试次数耗尽,消息依然失败,则需要有MessageRecoverer接口来处理,有三种不同实现
     * 1.RejectAndDontRequeueRecoverer：重试耗尽后，直接拒绝reject，丢弃消息。默认就是这种方式
     * 2.ImmediateRequeueMessageRecoverer:重试耗尽后,返回nack,消息重新入队
     * 3.RepublishMessageRecoverer：重试耗尽后，将失败消息投递到指定的交换机
     * 这里配置第三种RepublishMessageRecoverer方式
     * -
     * @param rabbitTemplate
     * @return
     */
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        /** 入参
         * 1.rabbitTemplate实现了amqpTemplate接口所以一样可以用
         * 2.交换机名称(不是对象)
         * 3.RoutingKey
         */
        return new RepublishMessageRecoverer(rabbitTemplate, EXCHANGE_NAME, ROUTING_KEY);
    }
}
