package com.griddynamics.testcontainers.demo;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.images.builder.dockerfile.DockerfileBuilder;

public abstract class AbstractContainerTest {

    public static final GenericContainer MONGO_CONTAINER = new GenericContainer(new ImageFromDockerfile()
            .withDockerfileFromBuilder(AbstractContainerTest::buildDockerFile))
            .withExposedPorts(27017);

    static {
        MONGO_CONTAINER.start();

        System.setProperty("spring.data.mongodb.uri", String.format("mongodb://%s:%s/test", MONGO_CONTAINER.getContainerIpAddress(),
                MONGO_CONTAINER.getMappedPort(27017)));
    }

    private static void buildDockerFile(DockerfileBuilder builder) {
        builder.from("mongo:4.2.0").build();
    }
}
