package ir.alirezaalijani.security.springauthorizationservice.security.token;

import ir.alirezaalijani.security.springauthorizationservice.domain.request.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpStepTowToken {
    private String id;
    private Integer smsCod;
    private Date expiration;
    private Date resendAfter;
    private RegisterRequest request;
}
