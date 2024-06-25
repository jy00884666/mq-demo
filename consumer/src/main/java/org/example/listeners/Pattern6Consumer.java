package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者:处理queue8队列的消息,绑定的交换机Exchange名称是fanoutExchange5
 * 消费者消息确认机制
 */
@Slf4j
@Component
public class Pattern6Consumer {
    
    /**
     * 处理queue8队列的消息
     * durable = "true"表示创建持久化的消息队列,非持久化消息队列queue不会将消息记录到磁盘
     *
     * arguments = @Argument(name = "x-queue-mode", value = "lazy")配置惰性队列,消息会直接放在磁盘,默认内存中只会放最新的2048条消息,
     * 虽然时效性能会有所下降但是消息上线高,可防止消息堆积问题,性能比较稳定
     *
     * 当消息堆积问题出现时，RabbitMQ 的内存占用会逐渐增加，直到触发内存预警上限。此时，RabbitMQ
     * 将开始将内存中的消息刷写到磁盘上，这个过程称为“PageOut”。“PageOut”过程会耗费一定的时间，并且会阻塞队列进程。因此，在这个过程中，RabbitMQ 将无法处理新的消息，导致生产者的所有请求都被阻塞。
     *
     * 经过测试惰性队列创建后会影响后续应用再次启动
     * Channel shutdown: channel error; protocol method: #method<channel.close>(reply-code=406,
     * reply-text=PRECONDITION_FAILED - inequivalent arg 'x-queue-mode' for queue 'queue8' in vhost '/': received the
     * value 'lazy' of type 'longstr' but current is none, class-id=50, method-id=10)
     * 原因是惰性队列会保存在磁盘所以应用第二次启动项目的时候无法将第一次创建的队列删除重新创建
     * 暴力一点的解决办法就是将后台RabbitMQ里的惰性队列通过控制管理台删除,重新启动应用即可
     *
     * 为了不影响后面例子这里先将 arguments = @Argument(name = "x-queue-mode", value = "lazy") 注释
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${rabbitmq.queueName8}", durable = "true"/*,
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")*/),
            exchange = @Exchange(name = "${rabbitmq.fanoutExchange5}", type = ExchangeTypes.DIRECT),
            key = {"${rabbitmq.queue.routingkey8}"}
    ))
    public void listenerQueue8(String msg) {
        log.info("queue8队列:消息内容:{}", msg);
    }
    
}
