package com.sha.rabbitmqtutorial.config;

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



@Configuration
public class DefaultExchangeConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.default.queue}")
    private String queueName;

    @Bean
    Queue createQueue() {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public AmqpTemplate defaultQueue(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        //In default queue, routing key = queue name
        rabbitTemplate.setRoutingKey(queueName);
        return rabbitTemplate;
    }

    //In a managed bean, @PostConstruct is called only once after the regular Java object constructor.
    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(createQueue());
    }
}
