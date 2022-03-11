package ir.alirezaalijani.security.springauthorizationservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {
    @NotEmpty
    @NotNull
    @Size(min = 8,max = 30)
    private String lastPassword;
    @NotEmpty
    @NotNull
    @Size(min = 8,max = 30)
    private String newPassword;
    @NotEmpty
    @NotNull
    @Size(min = 8,max = 30)
    private String reNewPassword;
}
