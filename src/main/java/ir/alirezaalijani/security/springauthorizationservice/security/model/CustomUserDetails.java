package ir.alirezaalijani.security.springauthorizationservice.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.alirezaalijani.security.springauthorizationservice.repository.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Integer id;
    private final String username;
    private final String email;
    @JsonIgnore
    private final String password;
    private final Boolean userEnable;
    private final Boolean emailVerified;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Integer id, String username,
                             String email, String password,Boolean enable,Boolean emailVerified,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.userEnable=enable;
        this.emailVerified=emailVerified;
    }

    public static CustomUserDetails build(User user) {
        if (user==null) return null;
        List<GrantedAuthority> grantedAuthorities =user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getEnable(),
                user.getEmailVerification(),
                grantedAuthorities);
    }

    public static CustomUserDetails build(UserData userData){
        List<GrantedAuthority> grantedAuthorities =userData.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new CustomUserDetails(
                0,
                userData.getUsername(),
                userData.getEmail(),
                null,
                null,
                null,
                grantedAuthorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.userEnable;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.emailVerified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomUserDetails user = (CustomUserDetails) o;
        return Objects.equals(username, user.username) && Objects.equals(email,user.email);

    }
}
