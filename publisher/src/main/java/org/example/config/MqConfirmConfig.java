package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 配置MQ消息回调方法ReturnCallBack:如果消息没有成功到达队列,会触发回调方法
 * 一个RabbitTemplate只能配置一个ReturnCallBack,因此只能放在项目启动过程中配置
 */
@Slf4j
@Configuration
public class MqConfirmConfig implements ApplicationContextAware {
    
    /**
     * 如何处理生产者的确认消息?
     * 生产者确认需要额外的网络和系统资源开销,尽量不要使用
     * 如果一定要使用，无需开启Publisher-Return机制，因为一般路由失败是自己业务问题
     * 对于nack消息可以有限次数重试，依然失败则记录异常消息
     */
    
    /**
     * Spring容器提供的方法,会将容器传入
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate对象
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 配置回调ReturnCallBack
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("消息发送失败,应答码:{},原因:{},交换机{},路由器{},消息内容{}",
                    replyCode, replyText, exchange, routingKey, message.toString());
        });
    }
}
