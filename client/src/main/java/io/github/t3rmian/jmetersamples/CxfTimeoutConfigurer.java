package io.github.t3rmian.jmetersamples;

import org.apache.camel.component.cxf.jaxws.CxfConfigurer;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.ext.logging.event.PrettyLoggingFilter;
import org.apache.cxf.ext.logging.slf4j.Slf4jVerboseEventSender;
import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

@SuppressWarnings("unused") // used in tests
public class CxfTimeoutConfigurer implements CxfConfigurer {

    @Override
    public void configure(AbstractWSDLBasedEndpointFactory factoryBean) {

    }

    @Override
    public void configureClient(Client client) {
        HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        httpConduit.setClient(getHttpClientPolicy());
        LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor(new PrettyLoggingFilter(new Slf4jVerboseEventSender()));
        client.getInInterceptors().add(loggingInInterceptor);
        LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor(new PrettyLoggingFilter(new Slf4jVerboseEventSender()));
        client.getOutInterceptors().add(loggingOutInterceptor);
    }

    static HTTPClientPolicy getHttpClientPolicy() {
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(1000);
        httpClientPolicy.setConnectionRequestTimeout(1000);
        httpClientPolicy.setReceiveTimeout(1000);
        return httpClientPolicy;
    }

    @Override
    public void configureServer(Server server) {

    }
}
