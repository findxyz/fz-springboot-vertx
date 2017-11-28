/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package xyz.fz.springBootVertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.fz.springBootVertx.model.Result;
import xyz.fz.springBootVertx.util.BaseUtil;

/**
 * @author Thomas Segismont
 */
@Component
public class HttpVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);

    @Value("${vertx.server.port}")
    private Integer serverPort;

    @Override
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route().failureHandler(routingContext -> {
            Throwable failure = routingContext.failure();
            logger.error(BaseUtil.getExceptionStackTrace(failure));
            routingContext.response().end(Result.ofMessage(failure.getMessage()));
        });

        router.route("/*").handler(routingContext -> {
            logger.debug("/* filter");
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            routingContext.next();
        });

        router.route("/abc/*").handler(routingContext -> {
            logger.debug("/abc/* filter");
            routingContext.next();
        });

        router.route("/abc").handler(routingContext -> {
            String name = routingContext.request().getParam("name");
            HttpServerResponse response = routingContext.response();
            vertx.eventBus().send("abcAddress", name, ar -> {
                if (ar.succeeded()) {
                    response.end(Result.ofData(ar.result().body()));
                } else {
                    response.end(Result.ofMessage(ar.cause().getMessage()));
                }
            });
        });

        router.route("/abc/record").handler(routingContext -> {
            String no = routingContext.request().getParam("no");
            HttpServerResponse response = routingContext.response();
            vertx.eventBus().send("abcRecord", no, ar -> {
                if (ar.succeeded()) {
                    response.end(Result.ofData(ar.result().body()));
                } else {
                    response.end(Result.ofMessage(ar.cause().getMessage()));
                }
            });
        });

        router.route("/abc/json").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            routingContext.request().bodyHandler(event -> {
                vertx.eventBus().send("abcJson", convert2JsonObject(event, response), ar -> {
                    if (ar.succeeded()) {
                        JsonObject result = (JsonObject) ar.result().body();
                        response.end(Result.ofData(result.getMap()));
                    } else {
                        response.end(Result.ofMessage(ar.cause().getMessage()));
                    }
                });
            });
        });

        httpServer.requestHandler(router::accept).listen(serverPort);

        logger.info("vertx httpServer started at port:{}", serverPort);
    }

    private JsonObject convert2JsonObject(Buffer event, HttpServerResponse response) {
        try {
            String requestJson = event.toString();
            return new JsonObject(requestJson);
        } catch (Exception e) {
            response.end(Result.ofMessage(e.getMessage()));
            throw e;
        }
    }
}
