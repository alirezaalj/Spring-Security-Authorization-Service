package ir.alirezaalijani.security.authorization.service.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "application_token")
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationToken {
    @Id
    private String id;
    private Date useTime;
    private String tokenTowFactor;
    private String type;
    private Boolean expired;
    private String username;
}
