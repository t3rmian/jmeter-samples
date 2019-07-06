package io.github.t3rmian.jmetersamples;

import io.github.t3rmian.jmetersamples.controller.ws.ReflectionWsdl11Definition;
import io.github.t3rmian.jmetersamples.controller.ws.SoapFaultExceptionResolver;
import io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint;
import io.github.t3rmian.jmetersamples.service.exception.ClientException;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import javax.xml.bind.JAXBException;
import java.sql.SQLException;
import java.util.Properties;

@SpringBootApplication
@Configuration
@EnableWs
public class JMeterSamplesApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(JMeterSamplesApplication.class, args);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server createInMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "users")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema usersSchema) {
        DefaultWsdl11Definition wsdl11Definition = new ReflectionWsdl11Definition();
        wsdl11Definition.setPortTypeName("Users");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(WSEndpoint.NAMESPACE_URI);
        wsdl11Definition.setSchema(usersSchema);
        wsdl11Definition.setRequestSuffix("Request");
        wsdl11Definition.setResponseSuffix("Response");
        wsdl11Definition.setFaultSuffix("commonFault");
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema usersSchema() {
        return new SimpleXsdSchema(new ClassPathResource("users.xsd"));
    }
}
