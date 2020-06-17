package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class ConfReader extends AbstractVerticle {

    public void start() {
        EventBus eventBus = vertx.eventBus();
        MessageConsumer<String> consumer = eventBus.consumer("schema.json.conf");
        consumer.handler(message -> {
            System.out.println("FileReader published conf: " + message.body());
        });
    }
}