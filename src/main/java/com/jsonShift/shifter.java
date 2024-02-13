package com.jsonShift;

import java.io.IOException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class shifter extends AbstractVerticle {

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

    vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private void handleRoot(RoutingContext routingContext) {
    routingContext.response()
        .putHeader("content-type", "text/plain")
        .end("Simple Password Manager");
  }
}
