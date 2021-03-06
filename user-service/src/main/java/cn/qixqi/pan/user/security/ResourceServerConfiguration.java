package cn.qixqi.pan.user.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1/user").permitAll()           // 注册，允许所有人
                .antMatchers(HttpMethod.POST, "/v1/user/login").permitAll()     // 登录，允许所有人
                .antMatchers(HttpMethod.GET, "/v1/user/test").permitAll()       // 测试，允许所有人
                .anyRequest().authenticated();          // 其余任何请求，都需要权限
    }
}
