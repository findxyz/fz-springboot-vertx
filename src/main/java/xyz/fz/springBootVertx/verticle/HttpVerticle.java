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
import io.vertx.core.http.HttpServer;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import xyz.fz.springBootVertx.filter.BaseFilter;
import xyz.fz.springBootVertx.provider.RestExceptionMapper;
import xyz.fz.springBootVertx.util.SpringContextHelper;

import java.util.Map;

/**
 * @author Thomas Segismont
 */
@Component
public class HttpVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);

    @Value("${vertx.server.port}")
    private Integer serverPort;

    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();

        // vertx resteasy deployment
        VertxResteasyDeployment deployment = new VertxResteasyDeployment();
        deployment.start();

        // Resource and Provider
        Map<String, Object> controllerMap = SpringContextHelper.getBeansWithAnnotation(Controller.class);
        for (Map.Entry<String, Object> entry : controllerMap.entrySet()) {
            deployment.getRegistry().addSingletonResource(entry.getValue());
        }
        deployment.getProviderFactory().getContainerRequestFilterRegistry().registerClass(BaseFilter.class);
        deployment.getProviderFactory().registerProvider(RestExceptionMapper.class);

        // Set an handler calling RestEasy
        httpServer.requestHandler(new VertxRequestHandler(vertx, deployment));

        // Start the server
        httpServer.listen(serverPort);

        logger.warn("vertx httpServer started at port:{}", serverPort);
    }
}
