package io.github.t3rmian.jmetersamples;

import https.github_com.t3rmian.jmeter_samples.CommonFault;
import https.github_com.t3rmian.jmeter_samples.ObjectFactory;
import https.github_com.t3rmian.jmeter_samples.User;
import https.github_com.t3rmian.jmeter_samples.UserPayload;
import https.github_com.t3rmian.jmeter_samples.Users;
import https.github_com.t3rmian.jmeter_samples.UsersService;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.CxfPayload;
import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CamelSoapClientErrorsIT {
    private static final long NON_EXISTING_USER_ID = -1L;
    private static final String NON_EXISTING_USER_ERROR_MESSAGE = String.format("User with id %d not found", NON_EXISTING_USER_ID);

    @BeforeClass
    public static void init() {
        System.setProperty("wsEndpointAddress", "http://localhost:8080/ws/users");
    }

    @Test
    public void given_nonExistingUser_When_getUser_byNegativeId_usingCxf_Then_throwCommonFault() {
        UserPayload userPayload = new UserPayload();
        userPayload.setId(NON_EXISTING_USER_ID);

        Users users = new UsersService().getUsersSoap11(CamelSoapClientIT.getCxfLoggingFeature());
        ((HTTPConduit) ClientProxy.getClient(users).getConduit()).getClient().setReceiveTimeout(1000L);
        BindingProvider bindingProvider = (BindingProvider) users;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, System.getProperty("wsEndpointAddress"));

        assertThrows(NON_EXISTING_USER_ERROR_MESSAGE, CommonFault.class, () -> users.getUser(userPayload));
    }

    @Test
    public void given_nonExistingUser_When_getUser_byNegativeId_usingCamelCxfPOJOFormat_Then_throwCamelExecutionException() throws Exception {
        UserPayload userPayload = new ObjectFactory().createUserPayload();
        userPayload.setId(NON_EXISTING_USER_ID);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .to(CamelSoapClientIT.getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.POJO))
                            .end()
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            assertThrows(NON_EXISTING_USER_ERROR_MESSAGE, CamelExecutionException.class, () -> camelContext.createProducerTemplate().requestBody("direct:getUser", userPayload, User.class));
        }
    }

    @Test
    public void given_nonExistingUser_When_getUser_byNegativeId_usingCamelCxfPOJOFormat_catchExceptionAndReturn_Then_returnCommonFault() throws Exception {
        UserPayload userPayload = new ObjectFactory().createUserPayload();
        userPayload.setId(NON_EXISTING_USER_ID);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .doTry()
                            .to(CamelSoapClientIT.getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.POJO))
                            .doCatch(Exception.class)
                            .process(exchange -> exchange.getIn().setBody(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class)))
                            .end()
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            CommonFault fault = camelContext.createProducerTemplate().requestBody("direct:getUser", userPayload, CommonFault.class);

            assertEquals(NON_EXISTING_USER_ERROR_MESSAGE, fault.getFaultInfo().getError());
        }
    }


    @Test
    public void given_nonExistingUser_When_getUser_byNegativeId_usingCamelCxfRawFormat_Then_returnCommonFault() throws Exception {
        String request = CamelSoapClientIT.getTestResourceContent("getUser_-1_nonExisting_request.xml");
        String expectedResponse = CamelSoapClientIT.getTestResourceContent("getUser_-1_nonExisting_response.xml");
        // not the best idea to compare text responses - namespace prefixes can be autogenerated as well as application elements like time
        // formatting is yet another case

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .to(CamelSoapClientIT.getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.RAW))
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();

            String response = camelContext.createProducerTemplate().requestBody("direct:getUser", request, String.class);
            String[] responseParts = expectedResponse.split("time>");
            assertThat(response, containsString(responseParts[0]));
            assertThat(response, containsString(responseParts[2]));
        }
    }

    @Test
    public void given_nonExistingUser_When_getUser_byNegativeId_usingCamelCxfCxfMessageFormat_catchExceptionAndReturn_Then_returnCommonFault() throws Exception {
        SOAPMessage soapMessage = CamelSoapClientIT.createGetUserSOAPMessage(NON_EXISTING_USER_ID);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .doTry()
                            .to(CamelSoapClientIT.getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.CXF_MESSAGE))
                            .doCatch(Exception.class)
                            .process(exchange -> exchange.getIn().setBody(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class)))
                            .end()
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            SoapFault response = camelContext.createProducerTemplate().requestBody("direct:getUser", soapMessage, SoapFault.class);

            assertEquals(NON_EXISTING_USER_ERROR_MESSAGE, response.getMessage());
            assertEquals(1, response.getDetail().getElementsByTagNameNS(CamelSoapClientIT.NS, "time").getLength());
        }
    }

    @Test
    public void given_nonExistingUser_When_getUser_byNegativeId_usingCamelCxfPayloadFormat_catchExceptionAndReturn_Then_returnSoapFault() throws Exception {
        List<Source> outElements = new ArrayList<>();
        Document outDocument = CamelSoapClientIT.createGetUserXmlDocument(NON_EXISTING_USER_ID);
        outElements.add(new DOMSource(outDocument.getDocumentElement()));
        CxfPayload<SoapHeader> payload = new CxfPayload<>(null, outElements, null);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .doTry()
                            .to(CamelSoapClientIT.getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.PAYLOAD))
                            .doCatch(Exception.class)
                            .process(exchange -> exchange.getIn().setBody(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class)))
                            .end()
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            SoapFault response = camelContext.createProducerTemplate().requestBody("direct:getUser", payload, SoapFault.class);

            assertEquals(NON_EXISTING_USER_ERROR_MESSAGE, response.getMessage());
            assertEquals(1, response.getDetail().getElementsByTagNameNS(CamelSoapClientIT.NS, "time").getLength());
        }
    }

}
