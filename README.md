# Spring Boot+Testcontainers testing demo

These are five Spring Boot projects to demonstrate the usage of [TestContainers](https://www.testcontainers.org/) testing library.

## mongo-docker-file-dsl-demo
If docker hub doesn't have the image for your needs you can create own. For this situation, Testcontainers has a feature which creates images on-the-fly.  The application using a `DSL format` for running your custom images, but exist other ways which you can find out more [here](https://www.testcontainers.org/features/creating_images/)

## mongo-singleton-container-demo
The singleton container is started only once when the base class is loaded. The container can then be used by all inheriting test classes. This application gives you an example of how to use it.

These two mongo applications use the following technologies: `Java 8`, `Spring Boot`, `MongoDB`, `Gradle`. 

## mysql-redis-docker-compose-demo
This application demonstrates how to run tests with real Redis and MySQL resources using a docker-compose file with Testcontainers.

Technologies used: `Java 8`, `Spring Boot`, `MySql`, `Redis cache`, `Maven`.

## postgresql-container-demo
This application demonstrates a simple way how to run PostgreSQL container using a specific module for PostgreSQL in Testcontainers.

## postgresql-jdbc-url-demo
Testcontainers can launch database containers via JDBC URL scheme. If you use Spring boot in your application it is enough to add only these two properties - `spring.datasource.url` and `spring.datasource.driver-class-name` for starting your database container. More information you can find out [here](https://www.testcontainers.org/modules/databases/)

These two postgresql applications use the following technologies: `Java 8`, `Spring Boot`, `PostgreSQL`, `Maven`.

## Getting Started
First, make sure you have [Docker installed](https://docs.docker.com/install/).

The project uses [Lombok](https://projectlombok.org), make sure that your IDE supports it. For instance, plugin for correct work in IDE [Lombok Plugin](https://www.baeldung.com/lombok-ide).

## Running the tests
Just run test classes as a standard JUnit test case.
