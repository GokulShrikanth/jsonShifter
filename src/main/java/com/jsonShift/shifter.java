package com.jsonShift;

import java.io.IOException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * This class represents a shifter that extends AbstractVerticle.
 * It provides functionality to handle HTTP requests and perform data shifting operations.
 */
public class shifter extends AbstractVerticle {

  /**
   * Starts the shifter verticle and sets up the HTTP server.
   *
   * @param startPromise a Promise that indicates the completion or failure of the start operation
   * @throws Exception if an error occurs during the start operation
   */
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.route("/").handler(this::handleRoot);
    router.route("/jsontojava").handler(jsonToJava::shift);
    router.route("/csvtojava").handler(event -> {
      try {
        csvToJava.shift(event);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    //TODO Define the route
    //return this.getSerializer().toXml(new csvToJava().read(stream));

    vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  /**
   * Handles the root request and sends a plain text response.
   *
   * @param routingContext the routing context for the request
   */
  private void handleRoot(RoutingContext routingContext) {
    routingContext.response()
        .putHeader("content-type", "text/plain")
        .end("Simple Password Manager");
  }
}
