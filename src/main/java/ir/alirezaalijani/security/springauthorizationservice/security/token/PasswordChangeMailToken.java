package ir.alirezaalijani.security.springauthorizationservice.security.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeMailToken {
    private String id;
    private String username;
    private String email;
    private Date expiration;
}
