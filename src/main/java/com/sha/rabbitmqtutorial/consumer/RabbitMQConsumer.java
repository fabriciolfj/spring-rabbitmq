package com.sha.rabbitmqtutorial.consumer;

import com.sha.rabbitmqtutorial.exception.InvalidSalaryException;
import com.sha.rabbitmqtutorial.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQConsumer {

    @RabbitListener(queues = "javainuse.queue")
    public void receivedMessage(final Employee employee) throws InvalidSalaryException {
        log.info("receive message from rabbitmq {}", employee);

        if (employee.getSalary() < 0) {
            throw new InvalidSalaryException();
        }
    }
}
