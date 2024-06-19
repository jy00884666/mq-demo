package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.CustInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/publisher1Controller")
public class Publisher1Controller {
    
    /**
     * 消息队列名称
     */
    @Value("${rabbitmq.queueName1}")
    private String QUEUE_NAME1;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 无交换机,直接发送消息到队列queue
     * 通过访问 http://localhost:8081/publisher1Controller/send1?msg=hello 发送消息
     * @param msg
     * @return
     */
    @RequestMapping("/send1")
    public String send1(@RequestParam("msg") String msg) {
        try {
            rabbitTemplate.convertAndSend(QUEUE_NAME1, msg);
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
        return "it is msg:" + msg;
    }
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange4}")
    private String EXCHANGE_NAME4;
    
    /**
     * 绑定queue7:RoutingKey[object.pojo]
     */
    @Value("${rabbitmq.queue.routingkey7}")
    private String ROUTING_KEY;
    
    /**
     * 测试消息转换器:发送消息到交换机4:绑定queue7:RoutingKey[object.pojo]
     * 息内容为map或者pojo对象
     * 使用postman发送请求http://localhost:8081/publisher1Controller/send2
     *
     * @param request
     * @return
     */
    @RequestMapping("/send2")
    public CustInfo send2(@RequestBody CustInfo request) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME4, ROUTING_KEY, request);
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
        return request;
    }
    
}
