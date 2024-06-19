package org.example;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 消息发送服务,启动后通过访问 http://localhost:8081/publisherController/send1?msg=hello 发送消息
 */
@SpringBootApplication
public class PublisherApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }
    
    /**
     * MQ消息转换器,可以将消息内容用json序列化,消息内容可以是pojo对象传递,或者map等对象,没有这个就会使用jdk自带的字节码序列化流转换,字节码序列化会有安全漏洞
     * @return
     */
    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
}
