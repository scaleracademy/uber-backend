package com.uber.uberapi.services.messagequeue;

public interface MessageQueue {
    void sendMessage(String topic, MQMessage message);

    MQMessage consumeMessage(String topic);
}
