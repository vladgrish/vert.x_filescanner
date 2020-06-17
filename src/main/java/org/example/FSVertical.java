package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class FSVertical extends AbstractVerticle {
    public static final String readdir = "schemareader.readdir";
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.eventBus().<JsonObject>consumer(readdir, this::readdir);
        startPromise.complete();
    }

    private void readdir(Message<JsonObject> msg){
        vertx.fileSystem().readDir("/tmp",res->{
            JsonArray arr = new JsonArray();

            res.result().forEach(f->{
                arr.add(f);
            });

            msg.reply(new JsonObject().put("data",arr));
        });
    }
}
