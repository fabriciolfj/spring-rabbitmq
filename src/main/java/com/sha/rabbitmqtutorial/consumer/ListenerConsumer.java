package com.sha.rabbitmqtutorial.consumer;

import com.sha.rabbitmqtutorial.model.QueueObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ListenerConsumer {

    //It expects QueueObject but we published String so we got error. To handle it, we've created generic object as
    // expected parameter.
    @RabbitListener(queues = {"${rabbitmq.direct.queue.1}","${rabbitmq.direct.queue.2}"}, containerFactory = "createListener")
    public void receiveMessages(QueueObject object) {
        System.out.println(object);
    }

    @RabbitListener(queues = {"${rabbitmq.fanout.queue.1}"}, containerFactory = "createListener")
    public void receiveMessagesFanout(QueueObject object) {
        System.out.println("fanout1 " + object);
    }

    @RabbitListener(queues = {"${rabbitmq.fanout.queue.1}"}, containerFactory = "createListener")
    public void receiveMessagesFanoutV2(QueueObject object) {
        System.out.println("fanout2 " + object);
    }

    @RabbitListener(queues = {"${rabbitmq.topic.queue.1}"}, containerFactory = "createListener")
    public void receiveMessagesTopic(QueueObject object) {
        System.out.println("topic " + object);
    }

    @RabbitListener(queues = {"${rabbitmq.topic.queue.2}"}, containerFactory = "createListener")
    public void receiveMessagesTopicV2(QueueObject object) {
        System.out.println("topic2 " + object);
    }

    @RabbitListener(queues = {"${rabbitmq.topic.queue.3}"}, containerFactory = "createListener")
    public void receiveMessagesTopicV3(QueueObject object) {
        System.out.println("topic3 " + object);
    }
}
