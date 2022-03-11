package ir.alirezaalijani.security.springauthorizationservice.web.error.exception;

import ir.alirezaalijani.security.springauthorizationservice.web.error.apierror.CustomErrorMessageGenerator;

public class RequestUserOwnerException extends RuntimeException{

    public RequestUserOwnerException(Class clazz, String message , String... searchParamsMap) {
        super(CustomErrorMessageGenerator.generateMessage(clazz.getSimpleName(),
                message,
                CustomErrorMessageGenerator.toMap(String.class, String.class, searchParamsMap)
        ));
    }
}
