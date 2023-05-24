package com.sha.rabbitmqtutorial.config.ttl;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public DirectExchange directExchangeTest() {
        return new DirectExchange("meu-exchange");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("minha-dlq").build();
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange("deadLetterExchange");
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("minha-dlq");
    }

    @Bean
    public Queue myQueue() {
        return QueueBuilder
                .durable("minha-fila")
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "minha-dlq")
                .withArgument("x-message-ttl", 10000)
                .withArgument("x-max-retries", 3)
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(myQueue())
                .to(directExchangeTest())
                .with("minha-rota");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRetryTemplate(retryTemplate());
        return rabbitTemplate;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // Defina o número máximo de retentativas desejado
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    // Configuração do SimpleRabbitListenerContainerFactory
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(messageConverter);
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(retryInterceptor());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }



    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000) // Configuração de backoff (atraso entre retentativas)
                .build();
    }


    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(deadLetterQueue());
        amqpAdmin.declareQueue(myQueue());
        amqpAdmin.declareExchange(directExchangeTest());
        amqpAdmin.declareExchange(deadLetterExchange());
        amqpAdmin.declareBinding(dlqBinding());
        amqpAdmin.declareBinding(binding());
    }
}
