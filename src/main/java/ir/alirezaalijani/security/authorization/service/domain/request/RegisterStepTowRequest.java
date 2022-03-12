package ir.alirezaalijani.security.authorization.service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStepTowRequest {
    @NotNull
    private Integer smsCode;
    @NotNull
    @NotEmpty
    private String token;
}
