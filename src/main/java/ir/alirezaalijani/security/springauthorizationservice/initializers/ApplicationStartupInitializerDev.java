package ir.alirezaalijani.security.springauthorizationservice.initializers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationStartupInitializerDev implements AppStartupInitializer{

    @Override
    public void init() {
        log.info("application startup at Dev mode");
    }
}
