package io.github.t3rmian.jmetersamples.controller.ws.dto;

import io.github.t3rmian.jmetersamples.controller.dto.ErrorResponse;
import io.github.t3rmian.jmetersamples.controller.dto.UserRegistrationRequest;
import io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint;
import io.github.t3rmian.jmetersamples.data.User;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private interface QNames {
        QName getUserRequest = new QName(WSEndpoint.NAMESPACE_URI, "getUserRequest");
        QName getUserResponse = new QName(WSEndpoint.NAMESPACE_URI, "getUserResponse");
        QName commonFault = new QName(WSEndpoint.NAMESPACE_URI, "commonFault");
        QName getUserIncorrectRequest = new QName(WSEndpoint.NAMESPACE_URI, "getUserIncorrectRequest");
        QName getUserIncorrectResponse = new QName(WSEndpoint.NAMESPACE_URI, "getUserIncorrectResponse");
        QName deleteUserRequest = new QName(WSEndpoint.NAMESPACE_URI, "deleteUserRequest");
        QName deleteUserResponse = new QName(WSEndpoint.NAMESPACE_URI, "deleteUserResponse");
        QName registerUserRequest = new QName(WSEndpoint.NAMESPACE_URI, "deleteUserRequest");
        QName registerUserResponse = new QName(WSEndpoint.NAMESPACE_URI, "deleteUserResponse");
        QName updateUserRequest = new QName(WSEndpoint.NAMESPACE_URI, "updateUserRequest");
        QName updateUserResponse = new QName(WSEndpoint.NAMESPACE_URI, "updateUserResponse");
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "getUserRequest")
    public JAXBElement<UserPayload> createGetUserRequest(UserPayload value) {
        return new JAXBElement<>(QNames.getUserRequest, UserPayload.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "getUserResponse")
    public JAXBElement<User> createGetUserResponse(User value) {
        return new JAXBElement<>(QNames.getUserResponse, User.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "commonFault")
    public JAXBElement<ErrorResponse> createCommonFault(ErrorResponse value) {
        return new JAXBElement<>(QNames.commonFault, ErrorResponse.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "getUserIncorrectRequest")
    public JAXBElement<UserPayload> createGetUserIncorrectRequest(UserPayload value) {
        return new JAXBElement<>(QNames.getUserIncorrectRequest, UserPayload.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "getUserIncorrectResponse")
    public JAXBElement<User> createGetUserIncorrectResponse(User value) {
        return new JAXBElement<>(QNames.getUserIncorrectResponse, User.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "deleteUserRequest")
    public JAXBElement<UserPayload> createDeleteUserRequest(UserPayload value) {
        return new JAXBElement<>(QNames.deleteUserRequest, UserPayload.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "deleteUserResponse")
    public JAXBElement<SuccessResponse> createDeleteUserResponse(SuccessResponse value) {
        return new JAXBElement<>(QNames.deleteUserResponse, SuccessResponse.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "registerUserRequest")
    public JAXBElement<UserRegistrationRequest> createUserRegistrationRequest(UserRegistrationRequest value) {
        return new JAXBElement<>(QNames.registerUserRequest, UserRegistrationRequest.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "registerUserResponse")
    public JAXBElement<UserPayload> createUserRegistrationResponse(UserPayload value) {
        return new JAXBElement<>(QNames.registerUserResponse, UserPayload.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "updateUserRequest")
    public JAXBElement<UserUpdatePayload> createUpdateUserRequest(UserUpdatePayload value) {
        return new JAXBElement<>(QNames.updateUserRequest, UserUpdatePayload.class, null, value);
    }

    @XmlElementDecl(namespace = WSEndpoint.NAMESPACE_URI, name = "updateUserResponse")
    public JAXBElement<SuccessResponse> createUpdateUserResponse(SuccessResponse value) {
        return new JAXBElement<>(QNames.updateUserResponse, SuccessResponse.class, null, value);
    }
}
