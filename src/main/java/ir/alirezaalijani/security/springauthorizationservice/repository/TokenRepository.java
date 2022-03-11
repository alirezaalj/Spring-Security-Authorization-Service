package ir.alirezaalijani.security.springauthorizationservice.repository;

import ir.alirezaalijani.security.springauthorizationservice.repository.model.ApplicationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<ApplicationToken,String> {
    Boolean existsByIdAndExpired(String id,Boolean expired);
}
