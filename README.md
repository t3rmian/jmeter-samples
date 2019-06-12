# jmeter-samples

Spring Boot demo project with in-memory H2 database serving as a backend for sample JMeter tests.

To run the tests from `jmeter` directory download [JMeter](https://jmeter.apache.org/download_jmeter.cgi) 5.1.1 or any other compatible version.
[H2 driver jar](https://mvnrepository.com/artifact/com.h2database/h2) is required for the tests to connect to the database. Place it in `jmeter-installation-dir/lib`.
Start the JMeter, drag and drop file and finally execute the test plan. If using IntelliJ, you could try [this plugin](https://plugins.jetbrains.com/plugin/7013-jmeter-plugin). 
