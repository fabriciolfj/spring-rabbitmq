package com.sha.rabbitmqtutorial.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqRetryDlqConfig {

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange("deadLetterExchange");
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("javainuseExchange");
    }

    @Bean
    Queue dlq() {
        return QueueBuilder.durable("deadLetter.queue").build();
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable("javainuse.queue")
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetter")
                .build();
    }

    @Bean
    Binding DLQbinding() {
        return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with("deadLetter");
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("javainuse");
    }

    @Bean
    public AmqpTemplate dlqTemplate(final ConnectionFactory connectionFactory, final MessageConverter messageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
