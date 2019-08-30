# jmeter-samples

Spring Boot demo project with in-memory H2 database serving as a backend for sample JMeter tests. This projects
touches few different aspects like [contract-last SOAP Spring WS](https://418.netlify.com/posts/contract-last-spring-ws) 
and REST services implementation together with JMeter functional and load tests.

To run the tests from `jmeter` directory download [JMeter](https://jmeter.apache.org/download_jmeter.cgi) 5.1.1 or any other compatible version.
[H2 driver jar](https://mvnrepository.com/artifact/com.h2database/h2) is required for the tests to connect to the database. Place it in `jmeter-installation-dir/lib`.
Start the JMeter, drag and drop file and finally execute the test plan. If using IntelliJ, you could try [this plugin](https://plugins.jetbrains.com/plugin/7013-jmeter-plugin). 

## Running

###### Generating SOAP specification

To run this application jaxb2:schemagen needs to be run in generate-sources phase.
You can run `mvnw jaxb2:schemagen` and then start in debug mode from JMeterSamplesApplicationTests.main() in your favorite IDE.
Or use spring boot maven plugin with clean install goals by executing `mvnw clean install spring-boot:run`.
Also please keep in mind that the TCP server for H2 database is configured to listen on 9090 port in the code. 

###### Tests

Tests are placed in the `./jmeter` directory, open them with the JMeter. You can check sample implementation for functional REST tests, 
which have been described in-depth [here](https://418.netlify.com/posts/jmeter-rest-tests). In the same directory,
there are also sample load tests. This overview can be found [here](https://418.netlify.com/posts/load-testing-with-jmeter).
You can toggle the specific elements using CTR+T combination or enable/disable them from the popup menu when
right-clicking them. I suggest to go with them (Thread Groups) one at a time.