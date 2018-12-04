package xyz.fz.springBootVertx.verticle;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xyz.fz.springBootVertx.annotation.SpringWorkerVerticle;
import xyz.fz.springBootVertx.service.AbcService;

import java.util.UUID;

@SpringWorkerVerticle
public class AbcVerticle extends AbstractVerticle {

    private final String ID = UUID.randomUUID().toString();

    private final AbcService abcService;

    @Value("${vertx.name}")
    private String vertxName;

    private static final String ADDRESS_PREFIX = AbcVerticle.class.getName() + ".";

    public static final String ABC_BUS_ADDRESS = ADDRESS_PREFIX + "abcAddress";

    public static final String ABC_BUS_RECORD = ADDRESS_PREFIX + "abcRecord";

    public static final String ABC_BUS_JSON = ADDRESS_PREFIX + "abcJson";

    @Autowired
    public AbcVerticle(AbcService abcService) {
        this.abcService = abcService;
    }

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
