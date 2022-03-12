package ir.alirezaalijani.security.authorization.service.repository;

import ir.alirezaalijani.security.authorization.service.repository.model.ApplicationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<ApplicationToken,String> {
    Boolean existsByIdAndExpired(String id,Boolean expired);
}
