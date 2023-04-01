package io.github.t3rmian.jmetersamples;

import https.github_com.t3rmian.jmeter_samples.CommonFault;
import https.github_com.t3rmian.jmeter_samples.ObjectFactory;
import https.github_com.t3rmian.jmeter_samples.User;
import https.github_com.t3rmian.jmeter_samples.UserPayload;
import https.github_com.t3rmian.jmeter_samples.Users;
import https.github_com.t3rmian.jmeter_samples.UsersService;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.CxfPayload;
import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class CamelSoapClientIT {
    static final String NS = "https://github.com/t3rmian/jmeter-samples";
    private static final long EXISTING_USER_ID = 4L;
    private static final String EXISTING_USER_NAME = "smith";

    @BeforeClass
    public static void setUp() throws JAXBException, IOException {
        printSchema();
        System.setProperty("wsEndpointAddress", "http://localhost:8080/ws/users");
    }

    private static void printSchema() throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(UserPayload.class);
        jaxbContext.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) {
                StreamResult streamResult = new StreamResult(new PrintWriter(System.out) {
                    @Override
                    public void close() {
                    }
                });
                streamResult.setSystemId(suggestedFileName);
                return streamResult;
            }
        });
    }


    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingSAAJ_Then_returnSmithName() throws SOAPException, MalformedURLException {
        SOAPMessage soapMessage = createGetUserSAAJMessage(EXISTING_USER_ID);
        URL endpointUrl = new URL(System.getProperty("wsEndpointAddress"));
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        SOAPMessage response = soapConnection.call(soapMessage, endpointUrl);

        SOAPBody soapBody = response.getSOAPBody();
        assertEquals(soapBody.getElementsByTagNameNS(NS, "id").item(0).getTextContent(), String.valueOf(EXISTING_USER_ID));
        assertEquals(soapBody.getElementsByTagNameNS(NS, "name").item(0).getTextContent(), EXISTING_USER_NAME);
    }

    @SuppressWarnings("SameParameterValue")
    static SOAPMessage createGetUserSAAJMessage(long userId) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        SOAPBodyElement getUserRequest = body.addBodyElement(new QName(NS, "getUserRequest"));
        SOAPElement id = getUserRequest.addChildElement(new QName(NS, "id"));
        id.setTextContent(String.valueOf(userId));
        return soapMessage;
    }

    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingCxf_Then_returnSmithName() throws CommonFault {
        UserPayload userPayload = new UserPayload();
        userPayload.setId(EXISTING_USER_ID);

        Users users = new UsersService().getUsersSoap11(getCxfLoggingFeature());
        ((HTTPConduit) ClientProxy.getClient(users).getConduit()).getClient().setReceiveTimeout(1000L);
        BindingProvider bindingProvider = (BindingProvider) users;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, System.getProperty("wsEndpointAddress"));
        User user = users.getUser(userPayload);

        assertEquals(EXISTING_USER_NAME, user.getName());
        assertEquals(EXISTING_USER_ID, user.getId());
    }

    static WebServiceFeature getCxfLoggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        loggingFeature.setVerbose(true);
        loggingFeature.setLogMultipart(true);
        return loggingFeature;
    }

    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingCamelCxfPOJOFormat_Then_returnSmithName() throws Exception {
        UserPayload userPayload = new ObjectFactory().createUserPayload();
        userPayload.setId(EXISTING_USER_ID);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .to(getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.POJO))
                            .log("Body after cxf route: ${body[0]}"); // body is of MessageContentsList
                }
            });
            camelContext.start();
            User user = camelContext.createProducerTemplate().requestBody("direct:getUser", userPayload, User.class);

            assertEquals(EXISTING_USER_NAME, user.getName());
            assertEquals(EXISTING_USER_ID, user.getId());
        }
    }


    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingCamelCxfRawFormat_Then_returnSmithName() throws Exception {
        String message = getTestResourceContent("getUser_4_smith_request.xml");

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .to(getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.RAW))
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            String response = camelContext.createProducerTemplate().requestBody("direct:getUser", message, String.class);

            assertThat(response, containsString(String.valueOf(EXISTING_USER_ID)));
            assertThat(response, containsString(EXISTING_USER_NAME));
        }
    }

    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingCamelCxfRawFormatGenericDispatchMode_Then_returnSmithName() throws Exception {
        String message = getTestResourceContent("getUser_4_smith_request.xml");

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .to("cxf://{{wsEndpointAddress}}/UsersSoap11?dataFormat=" + DataFormat.RAW)
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            String response = camelContext.createProducerTemplate().requestBody("direct:getUser", message, String.class);

            assertThat(response, containsString(String.valueOf(EXISTING_USER_ID)));
            assertThat(response, containsString(EXISTING_USER_NAME));
        }
    }

    static String getTestResourceContent(String testResourceName) throws IOException {
        try (InputStream requestStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(testResourceName);
             InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(requestStream, "Test resource not found"), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(streamReader)
        ) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }


    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingCamelCxfCxfMessageFormat_Then_returnSmithName() throws Exception {
        SOAPMessage soapMessage = createGetUserSOAPMessage(EXISTING_USER_ID);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .to(getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.CXF_MESSAGE))
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            SOAPMessage response = camelContext.createProducerTemplate().requestBody("direct:getUser", soapMessage, SOAPMessage.class);

            SOAPBody soapBody = response.getSOAPBody();
            assertEquals(soapBody.getElementsByTagNameNS(NS, "id").item(0).getTextContent(), String.valueOf(EXISTING_USER_ID));
            assertEquals(soapBody.getElementsByTagNameNS(NS, "name").item(0).getTextContent(), EXISTING_USER_NAME);
        }
    }

    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingCamelCxfPayloadFormatGenericDispatchMode_Then_returnSmithName() throws Exception {
        List<Source> outElements = new ArrayList<>();
        Document outDocument = createGetUserXmlDocument(EXISTING_USER_ID);
        outElements.add(new DOMSource(outDocument.getDocumentElement()));
        CxfPayload<SoapHeader> payload = new CxfPayload<>(null, outElements, null);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .to("cxf://{{wsEndpointAddress}}/UsersSoap11?dataFormat=" + DataFormat.PAYLOAD)
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            //noinspection unchecked
            CxfPayload<Element> response = camelContext.createProducerTemplate().requestBody("direct:getUser", payload, CxfPayload.class);
            Element getUserResponse = response.getBody().get(0);

            assertEquals(getUserResponse.getElementsByTagNameNS(NS, "id").item(0).getTextContent(), String.valueOf(EXISTING_USER_ID));
            assertEquals(getUserResponse.getElementsByTagNameNS(NS, "name").item(0).getTextContent(), EXISTING_USER_NAME);
        }
    }

    static SOAPMessage createGetUserSOAPMessage(long userId) throws SOAPException, JAXBException {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        ObjectFactory objectFactory = new ObjectFactory();
        UserPayload userPayload = objectFactory.createUserPayload();
        userPayload.setId(userId);
        JAXBContext.newInstance(UserPayload.class).createMarshaller()
                .marshal(objectFactory.createGetUserRequest(userPayload), envelope.getBody());
        // or handcraft: envelope.getBody().addBodyElement().addChildElement()...
        return soapMessage;
    }

    @Test
    public void given_existingUserSmith_When_getUserBySmithId_usingCamelCxfPayloadFormat_Then_returnSmithName() throws Exception {
        List<Source> outElements = new ArrayList<>();
        Document outDocument = createGetUserXmlDocument(EXISTING_USER_ID);
        outElements.add(new DOMSource(outDocument.getDocumentElement()));
        CxfPayload<SoapHeader> payload = new CxfPayload<>(null, outElements, null);

        try (CamelContext camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:getUser")
                            .log("Body before cxf route: ${body}")
                            .setHeader(CxfConstants.OPERATION_NAME, constant("getUser"))
                            .to(getCxfUriWithVerboseLoggingOfDataFormat(DataFormat.PAYLOAD))
                            .log("Body after cxf route: ${body}");
                }
            });
            camelContext.start();
            //noinspection unchecked
            CxfPayload<Element> response = camelContext.createProducerTemplate().requestBody("direct:getUser", payload, CxfPayload.class);
            Element getUserResponse = response.getBody().get(0);

            assertEquals(getUserResponse.getElementsByTagNameNS(NS, "id").item(0).getTextContent(), String.valueOf(EXISTING_USER_ID));
            assertEquals(getUserResponse.getElementsByTagNameNS(NS, "name").item(0).getTextContent(), EXISTING_USER_NAME);
        }
    }

    static Document createGetUserXmlDocument(long existingUserId) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        document.setXmlVersion("1.1");

        Element rootElement = document.createElementNS(NS, "getUserRequest");
        document.appendChild(rootElement);

        Element childElement = document.createElementNS(NS, "id");
        childElement.appendChild(document.createTextNode(String.valueOf(existingUserId)));
        rootElement.appendChild(childElement);

        return document;
    }

    static String getCxfUriWithVerboseLoggingOfDataFormat(DataFormat dataFormat) {
        return CamelSoapClient.getCxfUri(dataFormat) + "&cxfConfigurer=#class:io.github.t3rmian.jmetersamples.CxfTimeoutConfigurer";
    }
}
