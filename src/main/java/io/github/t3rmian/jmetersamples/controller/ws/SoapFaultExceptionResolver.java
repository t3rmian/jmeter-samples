package io.github.t3rmian.jmetersamples.controller.ws;

import io.github.t3rmian.jmetersamples.controller.RestErrorHandler;
import io.github.t3rmian.jmetersamples.controller.dto.ErrorResponse;
import io.github.t3rmian.jmetersamples.controller.ws.dto.ObjectFactory;
import io.github.t3rmian.jmetersamples.service.exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.Date;

public class SoapFaultExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(SoapFaultExceptionResolver.class);

    private final RestErrorHandler errorHandler = new RestErrorHandler();
    private final JAXBContext jaxbContext = JAXBContext.newInstance(ErrorResponse.class);
    private final Marshaller marshaller = jaxbContext.createMarshaller();
    private final ObjectFactory objectFactory = new ObjectFactory();

    public SoapFaultExceptionResolver() throws JAXBException {
    }

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        ErrorResponse errorResponse;
        if (ex instanceof ClientException) {
            errorResponse = errorHandler.processClientException((ClientException) ex);
        } else if (ex instanceof DataIntegrityViolationException) {
            errorResponse = errorHandler.processDataIntegrityViolationException((DataIntegrityViolationException) ex);
        } else {
            logger.error("Unmapped SOAP exception", ex);
            errorResponse = new ErrorResponse();
            errorResponse.setTime(new Date());
        }

        try {
            marshaller.marshal(objectFactory.createCommonFault(errorResponse), fault.addFaultDetail().getResult());
        } catch (JAXBException e) {
            logger.warn("Exception thrown while marshalling fault response", e);
        }
    }
}