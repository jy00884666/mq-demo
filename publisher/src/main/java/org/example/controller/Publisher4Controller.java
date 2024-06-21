package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 如何处理生产者的确认消息?
 * 生产者确认需要额外的网络和系统资源开销,尽量不要使用
 * 如果一定要使用，无需开启Publisher-Return机制，因为一般路由失败是自己业务问题
 * 对于nack消息可以有限次数重试，依然失败则记录异常消息
 */
@Slf4j
@RestController
@RequestMapping("/publisher4Controller")
public class Publisher4Controller {
    
    /**
     * 交换机名称
     */
    @Value("${rabbitmq.fanoutExchange5}")
    private String EXCHANGE_NAME;
    
    /**
     * 路由器RoutingKey
     */
    @Value("${rabbitmq.queue.routingkey8}")
    private String ROUTING_KEY;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * http://localhost:8081/publisher4Controller/send1?msg=hello
     * 消费者单个发送消息确认回调,配置ConfirmCallback
     * 每个消息都有id,通过CorrelationData给每一个消息提供回调方法
     *
     * SpringAMQP中生产者消息确认的几种返回值情况:
     * 1.消息投递到了MQ,但是路由失败。会return路由异常原因,返回ACK
     * 2.临时消息投递到了MQ(queue durable = "false"),并且入队成功,返回ACK
     * 3.持久消息投递到了MQ(queue durable = "true"),并且入队完成持久化,返回ACK
     * 4.其它情况都会返回NACK,告知投递失败
     */
    @RequestMapping("/send1")
    public String send1(@RequestParam("msg") String msg) {
        // 创建CorrelationDate,当前消息标识,入参指定一个唯一id
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        // 给Futrue添加ConfirmCallback,使用的是jdk的Futrue回调机制
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            /**
             * Spring内部处理失败,一般不会调用
             * @param throwable
             */
            @Override
            public void onFailure(Throwable throwable) {
                log.error("handle message ack fail,Spirng内部失败基本不会调用", throwable);
            }
            
            /**
             * MQ的回调成功
             * @param confirm
             */
            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                // true表示ack回执,false表示nack回执
                if (confirm.isAck()) {
                    log.info("发送消息成功,收到ack");
                } else {
                    // confirm.getReason(),返回String,nack时的异常描述
                    log.error("发送消息失败,收到nack,reason:{}", confirm.getReason());
                    // 重发消息逻辑写在这里...
                }
            }
        });
        try {
            // 发送消息入参(交换机名称,RoutingKey,消息内容)
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, msg + "[confirmCallback]来了", cd);
        } catch (Exception e) {
            log.error("消息发送异常:" + e.getMessage(), e);
        }
        return "it is msg:" + msg;
    }
    
}
