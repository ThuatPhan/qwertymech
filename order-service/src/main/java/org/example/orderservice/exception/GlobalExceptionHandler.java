package org.example.orderservice.exception;


import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlobalExceptionHandler implements CommonErrorHandler {
    @Override
    public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        log.error(thrownException.getMessage(), thrownException);
    }
}
