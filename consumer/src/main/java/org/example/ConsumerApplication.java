package org.example;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 消息消费者服务
 */
@SpringBootApplication
public class ConsumerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    
    /**
     * MQ消息转换器,可以将消息内容用json序列化,消息内容可以是pojo对象传递,或者map等对象,没有这个就会使用jdk自带的字节码序列化流转换,字节码序列化会有安全漏洞
     * 1.不考虑幂等的情况可以直接 new 返回
     * 2.若考虑幂等性则大体有如下方案
     * 幂等方案一: 在消息发送方配置自动创建消息id setCreateMessageIds(true) ,在消息消费者接收参数 Properties:{message_id:xxx} 存入数据库,
     *            在下一次处理业务逻辑时先判断数据库中是否已经存在这个id,存在就不处理
     *            这个方案有个缺点,入库,验证都不属于原始业务逻辑,属于业务入侵
     * 提示:消息发送者如果没有配置,则发送的消息属性中不会有 Properties:{message_id:xxx}
     * 幂等方案二: 基于业务逻辑,在业务本身做判断,例如根据支付单号修改支付状态,若支付状态已经修改成功则不做处理
     * 提示:有些操作天生幂等,如查询,根据id删除
     * @return
     */
    @Bean
    public MessageConverter jacksonMessageConverter() {
        Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();
        // 配置自动创建消息id,用户识别不同消息,也可以在业务中基于id判断消息是否重复
        jjmc.setCreateMessageIds(true);
        return jjmc;
        //return new Jackson2JsonMessageConverter();
    }
    
}
