package org.example.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue10队列的消息
 */
@Slf4j
@Component
public class Pattern8Consumer {
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName10}")
    private String QUEUE_NAME;
    
    /**
     * 处理消费者本地异常消息重试次数耗尽的error.queue
     *
     * 使用@RabbitListener(queues = "queue1")监听queue1消息队列
     * @param msg
     */
    @RabbitListener(queues = "${rabbitmq.queueName10}")
    public void listenerSimpleQueue1(Object msg) {
        log.info("queue10队列:消费者收到了error.queue消息:{}", msg);
        // 消息主体内容
        String body = new String(((Message) msg).getBody());
        log.info("body:{}", body);
        // 消息属性,包含异常信息
        MessageProperties messageProperties = ((Message) msg).getMessageProperties();
        log.info("messageProperties:{}", messageProperties);
    }
    
}
