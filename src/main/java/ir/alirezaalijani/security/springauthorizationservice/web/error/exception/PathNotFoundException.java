package ir.alirezaalijani.security.springauthorizationservice.web.error.exception;

import ir.alirezaalijani.security.springauthorizationservice.web.error.apierror.CustomErrorMessageGenerator;

public class PathNotFoundException extends RuntimeException{

    public PathNotFoundException(Class clazz, String message , String... searchParamsMap) {
        super(CustomErrorMessageGenerator.generateMessage(clazz.getSimpleName(),
                message,
                CustomErrorMessageGenerator.toMap(String.class, String.class,  searchParamsMap)
        ));
    }
}
