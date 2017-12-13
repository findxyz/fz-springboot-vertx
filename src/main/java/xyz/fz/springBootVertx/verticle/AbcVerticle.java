package xyz.fz.springBootVertx.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.fz.springBootVertx.annotation.MyWorkerVerticle;
import xyz.fz.springBootVertx.service.AbcService;

import java.util.UUID;

@Component
@MyWorkerVerticle
public class AbcVerticle extends AbstractVerticle {

    private static final String ID = UUID.randomUUID().toString();

    @Autowired
    private AbcService abcService;

    @Value("${vertx.name}")
    private String vertxName;

    private static final String ADDRESS_PREFIX = AbcVerticle.class.getName() + ".";

    public static final String ABC_BUS_ADDRESS = ADDRESS_PREFIX + "abcAddress";

    public static final String ABC_BUS_RECORD = ADDRESS_PREFIX + "abcRecord";

    public static final String ABC_BUS_JSON = ADDRESS_PREFIX + "abcJson";

    @Override
    public void start() {

        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(ABC_BUS_ADDRESS, msg -> {
            try {
                msg.reply(abcService.hello("from vertxName: " + vertxName + ID + ", " + msg.body().toString()));
            } catch (Exception e) {
                msg.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "error: " + e.getMessage());
            }
        });

        eventBus.consumer(ABC_BUS_RECORD, msg -> {
            try {
                msg.reply(abcService.record(msg.body().toString()));
            } catch (Exception e) {
                msg.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "error: " + e.getMessage());
            }
        });

        eventBus.consumer(ABC_BUS_JSON, msg -> {
            try {
                JsonObject requestJsonObject = (JsonObject) msg.body();
                msg.reply(requestJsonObject);
            } catch (Exception e) {
                msg.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "error: " + e.getMessage());
            }
        });
    }
}
