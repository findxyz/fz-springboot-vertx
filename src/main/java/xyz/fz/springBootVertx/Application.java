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

package xyz.fz.springBootVertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import xyz.fz.springBootVertx.annotation.MyWorkerVerticle;
import xyz.fz.springBootVertx.util.SpringContextHelper;
import xyz.fz.springBootVertx.verticle.HttpVerticle;

import java.util.Map;

/**
 * @author Thomas Segismont
 */
@SpringBootApplication
public class Application {

    @Value("${vertx.server.port}")
    private Integer serverPort;

    private final Vertx vertx;

    @Autowired
    public Application(Vertx vertx) {
        this.vertx = vertx;
    }

    @Autowired
    private HttpVerticle httpVerticle;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Deploy verticles when the Spring application is ready.
     */
    @EventListener
    void deployVerticles(ApplicationReadyEvent event) {
        if (serverPort > 0) {
            vertx.deployVerticle(httpVerticle);
        }
        Map<String, Object> myWorkerVerticleMap = SpringContextHelper.getBeansWithAnnotation(MyWorkerVerticle.class);
        for (Map.Entry entry : myWorkerVerticleMap.entrySet()) {
            for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
                DeploymentOptions workerDeploymentOptions = new DeploymentOptions();
                workerDeploymentOptions.setWorker(true);
                vertx.deployVerticle((Verticle) entry.getValue(), workerDeploymentOptions);
            }
        }
    }
}