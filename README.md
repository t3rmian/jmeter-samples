# jmeter-samples

Spring Boot demo project with in-memory H2 database serving as a backend for sample JMeter tests.

To run the tests from `jmeter` directory download [JMeter](https://jmeter.apache.org/download_jmeter.cgi) 5.1.1 or any other compatible version.
[H2 driver jar](https://mvnrepository.com/artifact/com.h2database/h2) is required for the tests to connect to the database. Place it in `jmeter-installation-dir/lib`.
Start the JMeter, drag and drop file and finally execute the test plan. If using IntelliJ, you could try [this plugin](https://plugins.jetbrains.com/plugin/7013-jmeter-plugin). 

## Running

To run this application jaxb2:schemagen needs to be run in generate-sources phase.
You can run `mvnw jaxb2:schemagen` and then start in debug mode from JMeterSamplesApplicationTests.main() in your favorite IDE.
Or use spring boot maven plugin with clean install goals by executing `mvnw clean install spring-boot:run`.
Also please keep in mind that the TCP server for H2 database is configured to listen on 9090 port in the code. 