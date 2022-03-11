package ir.alirezaalijani.security.springauthorizationservice.security.service.encryption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component("springJsonEncryptor")
public class SpringJsonEncryptor implements DataEncryptor{

    private final TextEncryptor encryptor;
    private final ObjectMapper objectMapper;

    public SpringJsonEncryptor(@Value("${application.security.encryption.token.secret-key:defKey}") String encKey,
                               @Value("${application.security.encryption.token.salt:5c0744940b5c369b}") String encSalt,
                               ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
       this.encryptor = Encryptors.text(encKey,encSalt);
    }

    @Override
    public String encryptDataToToken(Object data) {
        if (Objects.nonNull(data)){
            try {
                return encryptor.encrypt(objectMapper.writeValueAsString(data));
            } catch (JsonProcessingException e) {
                log.error("Encrypt Object To Token Failed");
            }
        }
        return null;
    }

    @Override
    public <T> T decryptTokenToData(String token, Class<T> dataClass) {
        if (token!=null){
            try {
                return objectMapper.readValue(encryptor.decrypt(token),dataClass);
            }catch (JsonProcessingException | EncryptionOperationNotPossibleException e){
                log.info("Decrypt Token To Json Failed");
            }
        }
        return null;
    }
}
