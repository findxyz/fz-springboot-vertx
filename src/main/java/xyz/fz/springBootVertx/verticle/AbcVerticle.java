package xyz.fz.springBootVertx.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.fz.springBootVertx.service.AbcService;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class AbcVerticle extends AbstractVerticle {

    private static final String ID = UUID.randomUUID().toString();

    @Resource
    private AbcService abcService;

    @Value("${vertx.name}")
    private String vertxName;

    @Override
    public void start() {

        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("abcAddress", msg -> {
            try {
                msg.reply(abcService.hello("from vertxName: " + vertxName + ", " + msg.body().toString()));
            } catch (Exception e) {
                msg.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "error: " + e.getMessage());
            }
        });

        eventBus.consumer("abcJson", msg -> {
            try {
                JsonObject requestMap = (JsonObject) msg.body();
                msg.reply(requestMap);
            } catch (Exception e) {
                msg.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "error: " + e.getMessage());
            }
        });
    }
}
