package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class SchemaReaderVerticale extends AbstractVerticle {
    public static final String readschema = "schemareader.readschema";
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.eventBus().<JsonObject>consumer(readschema, this::readSchema);
        startPromise.complete();
    }

    private void readSchema(Message<JsonObject> msg){

    }
}
