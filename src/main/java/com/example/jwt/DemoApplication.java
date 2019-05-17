package com.example.jwt;

import com.example.jwt.service.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.*;

import javax.sql.DataSource;

@SpringBootApplication
@EnableResourceServer
public class DemoApplication {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Configuration
    @EnableAuthorizationServer
    protected class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {


        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private JwtAccessTokenConverter converter;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserServiceDetail userServiceDetail;


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("browser")
                    .authorizedGrantTypes("refresh_token","password")
                    .scopes("ui")
                    .and()
                    .withClient("service-hi")
                    .secret(new BCryptPasswordEncoder().encode("123456"))
                    .authorizedGrantTypes("client_credentials","refresh_token","password")
                    .scopes("server");
        }



        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

            endpoints.tokenStore(tokenStore)
                      .authenticationManager(authenticationManager)
                      .userDetailsService(userServiceDetail)
                      .accessTokenConverter(converter);


        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//            security.tokenKeyAccess("permitAll()")
//                    .checkTokenAccess("isAuthenticated()");
            super.configure(security);
        }
    }


}
