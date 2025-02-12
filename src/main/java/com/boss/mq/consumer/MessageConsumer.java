package com.boss.mq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.boss.config.RabbitMQConfig.QUEUE_NAME;

@Slf4j
@Component
public class MessageConsumer {

    @RabbitListener(queues = QUEUE_NAME)
    public void handleMessage(Message message, Channel channel) throws Exception {
        try {
            // 获取消息内容
            String msg = new String(message.getBody());
            log.info("收到消息：{}", msg);
            
            // 处理业务逻辑
            // ...

            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 消息处理失败，拒绝消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            log.error("消息处理失败：", e);
            throw e;
        }
    }
} 