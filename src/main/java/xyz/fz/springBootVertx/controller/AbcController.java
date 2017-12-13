package xyz.fz.springBootVertx.controller;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import org.springframework.stereotype.Controller;
import xyz.fz.springBootVertx.model.Result;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import java.util.Map;

import static xyz.fz.springBootVertx.util.EventBusUtil.eventBusSend;
import static xyz.fz.springBootVertx.verticle.AbcVerticle.ABC_BUS_ADDRESS;
import static xyz.fz.springBootVertx.verticle.AbcVerticle.ABC_BUS_JSON;
import static xyz.fz.springBootVertx.verticle.AbcVerticle.ABC_BUS_RECORD;

@Controller
@Path("/abc")
public class AbcController {

    @GET
    @Path("/")
    public void abc(@Context Vertx vertx,
                    @Context HttpServerRequest req,
                    @Suspended final AsyncResponse asyncResponse,
                    @QueryParam("name") String name) {
        try {
            eventBusSend(vertx, ABC_BUS_ADDRESS, name, asyncResponse);
        } catch (Exception e) {
            asyncResponse.resume(Result.ofMessage(e.getMessage()));
        }
    }

    @GET
    @Path("/record")
    public void record(@Context Vertx vertx,
                       @Context HttpServerRequest req,
                       @Suspended final AsyncResponse asyncResponse,
                       @QueryParam("no") String no) {
        try {
            eventBusSend(vertx, ABC_BUS_RECORD, no, asyncResponse);
        } catch (Exception e) {
            asyncResponse.resume(Result.ofMessage(e.getMessage()));
        }
    }

    @POST
    @Path("/json")
    public void json(@Context Vertx vertx,
                     @Context HttpServerRequest req,
                     @Suspended final AsyncResponse asyncResponse,
                     Map<String, Object> bodyMap) {
        try {
            eventBusSend(vertx, ABC_BUS_JSON, bodyMap, asyncResponse);
        } catch (Exception e) {
            asyncResponse.resume(Result.ofMessage(e.getMessage()));
        }
    }
}
