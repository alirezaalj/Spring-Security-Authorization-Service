package ir.alirezaalijani.security.springauthorizationservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordRequest {
    @NotNull(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @Size(min = 5 ,max = 30)
    private String username;
}
