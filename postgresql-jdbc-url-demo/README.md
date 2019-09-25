# postgresql-jdbc-url-demo

This application demonstrates how to launch database containers via JDBC URL scheme. It's necessary to add only two properties: ```spring.datasource.url``` and ```spring.datasource.driver-class-name``` to start your database container.

If you have some problem with running tests, you need to set the value of `checks.disable` in `false`. Testcontainers will perform a set of startup check to ensure that your environment is configured correctly.

For check that your tests will be run in the CI system, you can run these commands in the terminal ```sh start.sh / sh stop.sh```. It will run docker tests and application inside a docker container.

## Running the tests

Just run `EventControllerTest` and `EventRepositoryTest` as a standard JUnit test case.
