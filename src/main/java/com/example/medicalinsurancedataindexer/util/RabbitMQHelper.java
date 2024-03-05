package com.example.medicalinsurancedataindexer.util;

import com.rabbitmq.client.*;
import com.rabbitmq.client.Connection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitMQHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQHelper.class);

    public void publish(String message) {
        LOGGER.trace("Publishing message to RabbitMQ: " + message);
        String EXCHANGE_NAME = "PlansExchange";
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent message");
        } catch (Exception e) {
            LOGGER.error("Error while publishing message to RabbitMQ: " + e.getMessage());
            throw new RabbitException("Error while publishing message to RabbitMQ: " + e.getMessage());
        }
    }

    public void receive() {
        LOGGER.trace("Receiving message from RabbitMQ");
        String EXCHANGE_NAME = "PlansExchange";
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("PlansQueue", false, false, false, null);
            channel.queueBind("PlansQueue", EXCHANGE_NAME, "");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C: ");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume("PlansQueue", true, deliverCallback, consumerTag -> { });
        } catch (Exception e) {
            LOGGER.error("Error while receiving message from RabbitMQ: " + e.getMessage());
            throw new RabbitException("Error while receiving message from RabbitMQ: " + e.getMessage());
        }
    }
}
