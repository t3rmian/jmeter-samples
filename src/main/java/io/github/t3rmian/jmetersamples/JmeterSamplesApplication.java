package io.github.t3rmian.jmetersamples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
public class JmeterSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmeterSamplesApplication.class, args);
    }

}
