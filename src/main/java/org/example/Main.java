package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;


public class Main extends AbstractVerticle {

    public void start() {
        Verticle fileReader = new FileReader();
        Verticle confReader = new ConfReader();
        vertx.deployVerticle(fileReader);
        vertx.deployVerticle(confReader);
        vertx.deployVerticle(FSVertical.class, new DeploymentOptions(),f->{

        });

        HttpServer server = vertx.createHttpServer();
        EventBus eventBus = vertx.eventBus();


        Router router = Router.router(vertx);
        router.route().path("/read_json").handler(routingContext -> {

            vertx.eventBus().request(FSVertical.readdir,new JsonObject(),rep->{
                System.out.println(((JsonObject)rep.result().body()).encodePrettily());

            });

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            DeliveryOptions options = new DeliveryOptions();
            options.addHeader("path", "/tmp/jsons");
            eventBus.request("schema.json.read", "read files!", options, ar -> {
                if (ar.succeeded()) {
                    System.out.println("Received reply: " + ar.result().body());
                }
            });

            // Write to the response and end it
            response.end("reading json files...");
        });

        server.requestHandler(router).listen(8080);
/*
        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            System.out.println("Enter");
            FileSystem fs = vertx.fileSystem();
            fs.readDir("/tmp/jsons", readDir -> {
                System.out.println(readDir.result());
                readDir.result().forEach(file -> {
                    fs.readFile(file, readFile -> {
                        if (readFile.succeeded()) {
                            JsonObject jsonObj = new JsonObject(readFile.result());
                            System.out.println(jsonObj);
                        }
                    });
                });
            });
            req.response().putHeader("content-type", "text/plain").end("reading json files!\n");
        });

        // Now bind the server:
        server.listen(8080, res -> {

        });

 */
    }
}