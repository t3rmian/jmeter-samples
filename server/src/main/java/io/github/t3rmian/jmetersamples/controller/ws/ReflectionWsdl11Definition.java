package io.github.t3rmian.jmetersamples.controller.ws;

import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.ProviderBasedWsdl4jDefinition;

import javax.wsdl.*;
import java.lang.reflect.Field;

public class ReflectionWsdl11Definition extends DefaultWsdl11Definition {
    private String commonFaultSuffix;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Field field = this.getClass().getSuperclass().getDeclaredField("delegate");
        field.setAccessible(true);
        ProviderBasedWsdl4jDefinition delegate = (ProviderBasedWsdl4jDefinition) field.get(this);
        addCommonFaults(delegate.getDefinition());
    }

    private void addCommonFaults(Definition definition) {
        for (Object portType : definition.getPortTypes().values()) {
            for (Object operation : ((PortType) portType).getOperations()) {
                addCommonFault(definition, ((Operation) operation));
            }
        }
    }

    private void addCommonFault(Definition definition, Operation operation) {
        for (Object message : definition.getMessages().values()) {
            Message msg = (Message) message;
            if (isFaultMessage(msg)) {
                operation.addFault(createCommonFault(definition, msg));
            }
        }
    }

    @Override
    public void setFaultSuffix(String faultSuffix) {
        super.setFaultSuffix(faultSuffix);
        this.commonFaultSuffix = faultSuffix;
    }

    private boolean isFaultMessage(Message message) {
        String messageName = message.getQName().getLocalPart();
        return messageName != null && messageName.contains(commonFaultSuffix);
    }

    private Fault createCommonFault(Definition definition, Message message) {
        Fault fault = definition.createFault();
        fault.setMessage(message);
        fault.setName(fault.getMessage().getQName().getLocalPart());
        return fault;
    }
}
