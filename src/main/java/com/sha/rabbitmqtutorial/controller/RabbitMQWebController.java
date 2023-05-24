package com.sha.rabbitmqtutorial.controller;

import com.sha.rabbitmqtutorial.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/javainuse-rabbitmq")
@RequiredArgsConstructor
public class RabbitMQWebController {

    private final AmqpTemplate dlqTemplate;

    @PostMapping
    public String produtor(@RequestBody final Employee employee) {
        dlqTemplate.convertAndSend("javainuseExchange", "javainuse", employee);
        return "message sent to the rabbitmq";
    }
}
