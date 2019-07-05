package io.github.t3rmian.jmetersamples.controller.ws.dto;

import io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Operation finished successfully
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "success", namespace = WSEndpoint.NAMESPACE_URI)
public class SuccessResponse {
}
