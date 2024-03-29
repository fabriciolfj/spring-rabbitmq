package com.sha.rabbitmqtutorial.queue;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MaxSizeConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.max-size.queue}")
    private String queueName;

    @Bean
    Queue createMaxSizeQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length-bytes", 15);
        return new Queue(queueName, true, false, false, args);
    }

    //Important: Each bean name should be unique in application.
    @Bean
    public AmqpTemplate defaultMaxSizeQueue(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        //In default queue, routing key = queue name
        rabbitTemplate.setRoutingKey(queueName);
        return rabbitTemplate;
    }

    //In a managed bean, @PostConstruct is called only once after the regular Java object constructor.
    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(createMaxSizeQueue());
    }
}
