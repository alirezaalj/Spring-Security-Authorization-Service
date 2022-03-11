package ir.alirezaalijani.security.springauthorizationservice.security.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String username;
    private String email;
    private List<String> roles;
}
