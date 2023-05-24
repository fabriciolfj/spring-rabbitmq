package com.sha.rabbitmqtutorial.controller;

import com.sha.rabbitmqtutorial.model.QueueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/retry")
public class RetryController {

    private final Logger LOGGER = LoggerFactory.getLogger(RetryController.class);

    private static final String RETRY_COUNT_HEADER = "x-retry-count";


    @Autowired
    private RabbitTemplate template;

    private AtomicLong count = new AtomicLong(0L);

    @GetMapping
    public void send() {
        var queue = new QueueObject("retry", LocalDateTime.now());
        template.convertAndSend("meu-exchange", "minha-rota", queue.toString());
    }

    @RabbitListener(queues = {"minha-fila"})
    public void received(final Message message) {
        LOGGER.info("tentativa {}", count.incrementAndGet());
        int retryCount = 0;
        var result = message.getMessageProperties().getHeaders().get(RETRY_COUNT_HEADER);
        if (result != null) {
            retryCount = (int) result + 1;
        }

        LOGGER.info("retry : {}", retryCount);
        if (retryCount == 2) {
            LOGGER.info("consume success message {}", message.getBody());
            return;
        }



        message.getMessageProperties().getHeaders().put(RETRY_COUNT_HEADER, retryCount);
        throw new RuntimeException("teste exception");
    }
}
