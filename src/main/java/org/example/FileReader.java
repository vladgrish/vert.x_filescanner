package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class FileReader extends AbstractVerticle {

    public void start() {
        FileSystem fs = vertx.fileSystem();
        EventBus eventBus = vertx.eventBus();
        MessageConsumer<String> consumer = eventBus.consumer("schema.json.read");
        consumer.handler(message -> {
            System.out.println(
                    "FileReader received a message: " + message.body() + " " + message.headers()
            );
            // This handler will be called for every request
            fs.readDir(message.headers().get("path"), readDir -> {
                System.out.println(readDir.result());
                JsonArray jsonArr = new JsonArray();

                readDir.result().forEach(file -> {
                 /*   fs.readFile(file, readFile -> {
                        if (readFile.succeeded()) {
                            JsonObject jsonObj = new JsonObject(readFile.result());
                            System.out.println(jsonObj);
                            //jsonArr.add(jsonObj);
                            eventBus.publish("schema.json.conf", jsonObj.toString());
                        }
                    });*/
                    jsonArr.add(file);
                });

                message.reply(jsonArr);
            });
        });
    }
}