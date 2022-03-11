package ir.alirezaalijani.security.springauthorizationservice.security.service;

import ir.alirezaalijani.security.springauthorizationservice.security.service.encryption.DataEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataEncryptorTest {

    @Autowired
    private DataEncryptor dataEncryptor;

    @Test
    void encryptDataToTokenTest(){
        record UserDataRecord(String username,String email){}
        var token = dataEncryptor.encryptDataToToken(new UserDataRecord("alireza","alirezaalijani.ir@gmail.com"));
        System.out.println(token);
    }
}
