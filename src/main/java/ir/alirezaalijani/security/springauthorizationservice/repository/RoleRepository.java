package ir.alirezaalijani.security.springauthorizationservice.repository;

import ir.alirezaalijani.security.springauthorizationservice.repository.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String name);
}
