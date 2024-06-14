package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 消息发送服务,通过访问 http://localhost:8081/publisherController/send1?msg=hello 发送消息
 */
@SpringBootApplication
public class PublisherApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }
    
}
