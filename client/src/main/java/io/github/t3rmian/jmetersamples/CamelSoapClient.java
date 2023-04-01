package io.github.t3rmian.jmetersamples;

import https.github_com.t3rmian.jmeter_samples.ObjectFactory;
import https.github_com.t3rmian.jmeter_samples.User;
import https.github_com.t3rmian.jmeter_samples.UserPayload;
import https.github_com.t3rmian.jmeter_samples.Users;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.main.BaseMainSupport;
import org.apache.camel.main.Main;
import org.apache.camel.main.MainListenerSupport;

import java.io.IOException;

public class CamelSoapClient {
    static final long EXISTING_USER_ID = 4L;

    static {
        System.setProperty("wsEndpointAddress", "http://localhost:8080/ws/users");
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main(CamelSoapClient.class);
        main.addMainListener(new MainListenerSupport() {
            @Override
            public void afterStart(BaseMainSupport main) {
                UserPayload userPayload = new ObjectFactory().createUserPayload();
                userPayload.setId(EXISTING_USER_ID);
                System.out.println("Requesting user " + userPayload.getId());
                try (ProducerTemplate producerTemplate = main.getCamelContext().createProducerTemplate()) {
                    User user = producerTemplate.requestBody("direct:getUser", userPayload, User.class);
                    System.out.println("Response contains " + user.getName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Camel is running and waiting for SIGINT...");
            }
        });
        System.exit(main.run(args));
    }

    @SuppressWarnings("unused") // auto discovered by org.apache.camel.main.Main
    public static class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() {
            from("direct:getUser")
                    .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                    // An alternative to cxfConfigurer URI parameter:
//                     .setHeader(Client.REQUEST_CONTEXT, () -> new HashMap<String, Object>() {{
//                          this.put(HTTPClientPolicy.class.getName(), CxfTimeoutConfigurer.getHttpClientPolicy());
//                      }})
                    .to(getCxfUri(DataFormat.POJO) + "&loggingFeatureEnabled=true")
                    .process(exchange -> exchange.getIn().setBody(exchange.getIn().getBody()));
        }
    }
    static String getCxfUri(DataFormat dataFormat) {
        return "cxf://{{wsEndpointAddress}}"
                + "?wsdlURL=classpath:users.wsdl"
                + "&serviceClass=" + Users.class.getName()
                + "&dataFormat=" + dataFormat;
    }

}
