# jmeter-samples

Spring Boot demo project with in-memory H2 database serving as a backend for sample JMeter tests. This projects
presents few different aspects like:
- [contract-last SOAP Spring WS](https://blog.termian.dev/posts/contract-last-spring-ws) (`server` module);
- REST services implementation (`server` module);
- JMeter functional and load tests (`jmeter` directory);
- SOAP Web Service client implementation using Camel/cxf (`client` directory);
- a way to start a dependency server without polluting the classpath (`client/pom.xml` plugins configuration).

To run the tests from `jmeter` directory download [JMeter](https://jmeter.apache.org/download_jmeter.cgi) 5.1.1 or any other compatible version.
[H2 driver jar](https://mvnrepository.com/artifact/com.h2database/h2) is required for the tests to connect to the database. Place it in `jmeter-installation-dir/lib`.
Start the JMeter, drag and drop file and finally execute the test plan. If using IntelliJ, you could try [this plugin](https://plugins.jetbrains.com/plugin/7013-jmeter-plugin). 

## Running

###### Server

To run this application `jaxb2:schemagen` needs to be run in `generate-sources` phase.
You can run `./mvnw install` and then:
- start in debug mode from `JMeterSamplesApplicationTests.main()` in your favorite IDE;
- or use spring boot maven plugin after install by executing `./mvnw -pl server org.springframework.boot:spring-boot-maven-plugin:run`.

Also, please keep in mind that by default the server starts at 8080 with additional TCP server for H2 database listening on 9090. 

###### Client

In the `client` module you will find integration tests that require the `server` to be up. Run them with maven `./mvnw -pl client verify`.
Maven will bind to the lifecycle and start an exec jar from the `server` module (must be installed first).
If you prefer the IDE way, remember to bind the startup of the server to your run configuration or start it manually. 

###### Tests

Tests are placed in the `./jmeter` directory, open them with the JMeter. You can check sample implementation for functional REST tests, 
which have been described in-depth [here](https://blog.termian.dev/posts/jmeter-rest-tests). In the same directory,
there are also sample load tests. This overview can be found [here](https://blog.termian.dev/posts/load-testing-with-jmeter).
You can toggle the specific elements using CTR+T combination or enable/disable them from the popup menu when
right-clicking them. I suggest to go with them (Thread Groups) one at a time.