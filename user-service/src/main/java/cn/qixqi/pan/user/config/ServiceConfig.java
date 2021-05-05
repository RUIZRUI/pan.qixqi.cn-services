package cn.qixqi.pan.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServiceConfig {
    @Value("${example.property}")
    private String exampleProperty;

    @Value("${jwt.signing.key}")
    private String jwtSigningKey;

    public String getExampleProperty(){
        return this.exampleProperty;
    }

    public String getJwtSigningKey(){
        return this.jwtSigningKey;
    }
}
