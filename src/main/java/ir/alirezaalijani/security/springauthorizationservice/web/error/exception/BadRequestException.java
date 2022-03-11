package ir.alirezaalijani.security.springauthorizationservice.web.error.exception;


import ir.alirezaalijani.security.springauthorizationservice.web.error.apierror.CustomErrorMessageGenerator;

public class BadRequestException extends RuntimeException {

    public BadRequestException(Class clazz, String message , String... searchParamsMap) {
        super(CustomErrorMessageGenerator.generateMessage(clazz.getSimpleName(),
                message,
                CustomErrorMessageGenerator.toMap(String.class, String.class, searchParamsMap)
        ));
    }

}
