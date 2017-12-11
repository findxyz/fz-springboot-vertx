package xyz.fz.springBootVertx.util;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import xyz.fz.springBootVertx.model.Result;

import javax.ws.rs.container.AsyncResponse;
import java.util.Map;

public class EventBusUtil {

    public static void eventBusSend(Vertx vertx, String address, String message, final AsyncResponse asyncResponse) {
        vertx.eventBus().send(address, message, ar -> {
            if (ar.succeeded()) {
                asyncResponse.resume(Result.ofData(ar.result().body()));
            } else {
                asyncResponse.resume(Result.ofMessage(ar.cause().getMessage()));
            }
        });
    }

    public static void eventBusSend(Vertx vertx, String address, Map<String, Object> message, final AsyncResponse asyncResponse) {
        vertx.eventBus().send(address, new JsonObject(message), ar -> {
            if (ar.succeeded()) {
                JsonObject responseJsonObject = (JsonObject) ar.result().body();
                asyncResponse.resume(Result.ofData(responseJsonObject.getMap()));
            } else {
                asyncResponse.resume(Result.ofMessage(ar.cause().getMessage()));
            }
        });
    }
}
